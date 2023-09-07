package com.example.petree.domain.member.service;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.main_breed.repository.DogTypeRepository;
import com.example.petree.domain.member.domain.*;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.member.repository.AdminRepository;
import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.member.dto.LoginDto;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.repository.ProfileImgFileRepository;
import com.example.petree.global.config.MapConfig;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.DuplicateException;
import com.example.petree.global.error.exception.ServerException;
import com.example.petree.global.jwt.dto.JwtTokenDto;
import com.example.petree.global.jwt.service.JwtTokenProvider;
import com.example.petree.global.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
@Component
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final AdopterRepository adopterRepository;
    private final BreederRepository breederRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MapConfig mapConfig;
    private final DogTypeRepository dogTypeRepository;
    private final ProfileImgFileRepository profileImgFileRepository;

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 이메일(아이디) 중복 검사
     * @return
     */
    public void checkEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            log.info("이메일 중복확인 체크 탐.");
            throw new DuplicateException("해당 아이디(이메일)가 이미 존재합니다.");
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 닉네임 중복 검사
     * @return
     */
    public void checkNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateException("해당 닉네임이 이미 존재합니다.");
        }
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 회원가입, 회원가입시 주소를 기반으로 위도, 경도 정보를 저장 및 해시알고리즘을 통해 비밀번호를 DB에 암호화하여 저장
     * @return Member
     */

    @Transactional
    public MemberDto.MemberDtoReponse signupMember(MemberDto.MemberDtoRequest memberDto) throws JSONException, IOException, Exception {

        //주소에 대한 위도, 경도 값 저장
        MemberDto.MemberDtoReponse response = this.geocodingByAddress(memberDto);

        Member member = MemberDto.MemberDtoReponse.toEntity(response);

        //해시알고리즘을 적용한 비밀번호를 member객체에 비밀번호로 저장.
        String hashPwd = passwordEncoder.encode(member.getPassword());
        member.setPassword(hashPwd);

        if(member.getRole().getTitle().equals("BREEDER")){
            Breeder breeder = (Breeder) Breeder.builder()
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .address1(member.getAddress1())
                    .address2(member.getAddress2())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .role(member.getRole())
                    .latitude(member.getLatitude())
                    .longitude(member.getLongitude())
                    .isVerified(false)
                    .build();

            Optional<MainBreedDto.MainBreedDtoRequest> mainBreedDtoRequest = memberDto.getMainBreedDtoRequest();

            if (mainBreedDtoRequest != null) {
                log.info("브리더 회원가입 탔음");
                List<MainBreed> mainBreeds = new ArrayList<>();
                for (Long dogTypeId : mainBreedDtoRequest.get().getDogTypeId()) {
                    DogType dogType = dogTypeRepository.findById(dogTypeId)
                            .orElseThrow(() -> new Exception("유효하지 않은 주력견종 ID입니다."));

                    MainBreed mainBreed = MainBreed.builder()
                            .dogType(dogType)
                            .breeder(breeder)
                            .build();

                    mainBreeds.add(mainBreed);
                }
                breeder.setMainBreeds(mainBreeds);
                //breederRepository.save(breeder);

                List<MainBreedDto.MainBreedDtoResponse> mainBreedDtoResponses = new ArrayList<>();
                for (MainBreed mainBreed : mainBreeds) {
                    MainBreedDto.MainBreedDtoResponse mainBreedDtoResponse = MainBreedDto.MainBreedDtoResponse.builder()
                            .id(mainBreed.getDogType().getId())
                            .name(mainBreed.getDogType().getName())
                            .imgUrl(mainBreed.getDogType().getImgUrl())
                            .build();
                    mainBreedDtoResponses.add(mainBreedDtoResponse);
                }
                response.setMainBreedDtoResponseList(mainBreedDtoResponses);
            }
            breederRepository.save(breeder);
            log.info("주력견종 정보가 없는경우");
            return response;

        } else if(member.getRole().getTitle().equals("ADOPTER")){
            Adopter adopter = (Adopter) Adopter.builder()
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .address1(member.getAddress1())
                    .address2(member.getAddress2())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .role(member.getRole())
                    .latitude(member.getLatitude())
                    .longitude(member.getLongitude())
                    .isVerified(false)
                    .build();

            adopterRepository.save(adopter);
            return response;
        }/**
         * @author 이지수
         * @date 2023-08-07
         * @description : 관리자 회원가입 임시, 관리자에 대해 상의 후 수정해야함
         * @return Member
         */
        else if(member.getRole().getTitle().equals("ADMIN")){
            Admin admin = (Admin) Admin.builder()
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .address1(member.getAddress1())
                    .address2(member.getAddress2())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .role(member.getRole())
                    .latitude(member.getLatitude())
                    .longitude(member.getLongitude())
                    .build();
            adminRepository.save(admin);
            return response;
        }
        return null;

    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 주소를 기반으로 위도, 경도 추출
     * @return MemberDto.MemberDtoReponse
     */

    public MemberDto.MemberDtoReponse geocodingByAddress(MemberDto.MemberDtoRequest memberDto) throws IOException, JSONException {

        log.info("memberDto : " + memberDto.toString());
        log.info("geocodingByAddress메소드 탔음");
        log.info("memberDto의 주소 : " + memberDto.getAddress1());

        if (isDuplicateMember(memberDto)) {
            throw new DuplicateException("이미 존재하는 회원 정보입니다.");
        }

        String addr = URLEncoder.encode(memberDto.getAddress1(), "UTF-8");

        // Geocoding 개요에 나와있는 API URL 입력.
        String apiURL = mapConfig.getUrl() + addr;    // JSON

        log.info(mapConfig.getUrl());

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Geocoding 개요에 나와있는 요청 헤더 입력.
        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", mapConfig.getClientId());
        con.setRequestProperty("X-NCP-APIGW-API-KEY", mapConfig.getClientSecret());

        // 요청 결과 확인. 정상 호출인 경우 200
        int responseCode = con.getResponseCode();

        BufferedReader br;

        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;

        StringBuffer response = new StringBuffer();

        while((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        log.info("response : " + response);

        Double latitude = null;
        Double longitude = null;


        JSONTokener tokener = new JSONTokener(response.toString());
        JSONObject object = new JSONObject(tokener);
        JSONArray arr = object.getJSONArray("addresses");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject temp = (JSONObject) arr.get(i);
            log.info("address : " + temp.get("roadAddress"));
            log.info("jibunAddress : " + temp.get("jibunAddress"));
            log.info("위도(latitude) : " + temp.get("y"));
            log.info("경도(longitude) : " + temp.get("x"));

            latitude = Double.valueOf(String.valueOf (temp.get("y")));
            longitude = Double.valueOf(String.valueOf (temp.get("x")));
            System.out.println(latitude);
            System.out.println(longitude);
        }

        MemberDto.MemberDtoReponse responseDto = MemberDto.MemberDtoReponse.builder()
                .id(null)
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .password(memberDto.getPassword())
                .phoneNumber(memberDto.getPhoneNumber())
                .address1(memberDto.getAddress1())
                .address2(memberDto.getAddress2())
                .latitude(latitude)
                .longitude(longitude)
                .role(memberDto.getRole())
                .build();
        return responseDto;
    }

    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 회원 중복 저장 방지
     * @return
     */
    private boolean isDuplicateMember(MemberDto.MemberDtoRequest memberDto) {
        boolean emailExists = memberRepository.existsByEmail(memberDto.getEmail());
        boolean nicknameExists = memberRepository.existsByNickname(memberDto.getNickname());

        return emailExists || nicknameExists;
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 로그인 시 이메일 확인하기 위한 메서드
     * @return Member
     */

    public Member findMember(LoginDto.LoginRequestDto loginRequestDto) {
        return memberRepository.findByEmail(loginRequestDto.getEmail()).orElse(null);
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 로그인 시 비밀번호 확인하기 위한 메서드
     * @return boolean
     */
    public LoginDto.LoginResponseDto checkPassword(Member member, LoginDto.LoginRequestDto loginRequestDto) {
        boolean result = passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword());
        if(result){
            boolean isVerified = false;
            if (member instanceof Breeder) {
                isVerified = ((Breeder) member).getIsVerified();
            } else if (member instanceof Adopter) {
                isVerified = ((Adopter) member).getIsVerified();
            }
            ProfileImgFile profileImgFile = profileImgFileRepository.findByMember(member).orElse(null);
            log.info("인증 정보 : " + isVerified);
            JwtTokenDto jwtTokenDto = jwtTokenProvider.createJwtTokenDto(member.getEmail(), member.getRole(), isVerified);
            log.info("AT확인 " + jwtTokenDto.getAccessToken());
            // Redis에 RT저장
            redisService.setRefreshToken(member.getEmail(), jwtTokenDto.getRefreshToken());
            return LoginDto.LoginResponseDto.of(jwtTokenDto, profileImgFile != null ? profileImgFile.getFileUrl() : null);
        }
        return null;
    }

    /**
     * @author 박수현
     * @date 2023-07-28
     * @description : 로그아웃
     * @return void
     */

    public void logout(String accessToken) {

        // 1. 토큰 검증
        //jwtTokenProvider.validateToken(accessToken);

        // 2. 토큰 타입 확인
        /*Claims tokenClaims = jwtTokenProvider.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }*/

        // 3. accessToken을 블랙리스트에 추가
        //Claims tokenClaims = jwtTokenProvider.getTokenClaims(accessToken);
        //String email = (String) tokenClaims.get("email");
        //Member member = memberRepository.findByEmail(email).orElse(null);
        redisService.addToBlacklist(accessToken);
    }


    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 아이디(이메일)찾기
     * @return
     */
    public MemberDto.IdFindResponseDto findEmail(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElse(null);

        if (member != null) {
            return MemberDto.IdFindResponseDto.builder()
                    .email(member.getEmail())
                    .build();
        }
        return null;
    }


    /**
     * @author 박수현
     * @date 2023-08-26
     * @description : 회원탈퇴
     * @return
     */


    public void withdrawMember(Principal principal) {

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        memberRepository.delete(member);
    }



//    /**
//     * @author 박수현
//     * @date 2023-07-28
//     * @description : 로그인성공이후 정보 가져오기
//     * @return Optional<Member>
//     */
//
//    public Optional<Member> getUserDetailsAfterLogin(Authentication authentication) {
//        if(authentication != null){
//            return memberRepository.findByEmail(authentication.getName());
//        } else {
//            return null;
//        }
//    }

//    /**
//     * @author 박수현
//     * @date 2023-07-13
//     * @description : 로그인한 사용자 정보 조회
//     * @return Member
//     */
//    public Member getMember(HttpServletRequest httpServletRequest) throws IOException {
//        String authorizationHeader = httpServletRequest.getHeader("Authorization");
//        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
//
//        if(authorizationHeader != null){
//            String accessToken = authorizationHeader.split(" ")[1];
//            jwtTokenProvider.validateToken(accessToken);
//            return jwtTokenProvider.getMember(accessToken);
//        } else {
//            return null;
//        }
//    }

}


