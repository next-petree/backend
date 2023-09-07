package com.example.petree.domain.member.service;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.main_breed.repository.MainBreedRepository;
import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.dto.MemberDto;
import com.example.petree.domain.member.dto.ProfileDto;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.repository.ProfileImgFileRepository;
import com.example.petree.global.config.MapConfig;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.DuplicateException;
import com.example.petree.global.error.exception.IncorrectPasswordException;
import com.example.petree.global.error.exception.ServerException;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.petree.domain.member.dto.ProfileDto.createProfileImgResponseDTO;

/**
 * packageName    : com.example.petree.domain.member.service
 * fileName       : ProfileService
 * author         : 박수현
 * date           : 2023-08-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-03        박수현              최초 생성
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileImgFileRepository profileImgFileRepository;
    private final MemberRepository memberRepository;
    private final MainBreedRepository mainBreedRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapConfig mapConfig;

    private final S3Util s3Util;
    private final FileUtil fileUtil;

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 회원정보조회
     * @return
     */
    public ProfileDto.PersonalInfoResponseDto getMemberInfo(Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        ProfileDto.PersonalInfoResponseDto personalInfoResponseDto = ProfileDto.PersonalInfoResponseDto.createpersonalInfoResponseDto(member);
        return personalInfoResponseDto;
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 회원정보 변경
     * @return
     */
    @Transactional
    public void updateMemberInfo(Principal principal, ProfileDto.PersonalInfoRequestDto updateDto) throws JSONException, IOException {
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (updateDto.getNickname() != null) {
            member.setNickname(updateDto.getNickname());
        }

        if (updateDto.getAddress1() != null) {
            String oldAddress = member.getAddress1(); // 기존 주소
            member.setAddress1(updateDto.getAddress1());
            // 주소가 변경되었을 때만 위도, 경도 업데이트 수행
            if (!oldAddress.equals(updateDto.getAddress1())) {
                this.geocodingAndUpdateCoordinates(member, updateDto.getAddress1());
            }
        }

        // 주소 변경이 있는 경우에만 상세주소 업데이트
        if (updateDto.getAddress1() != null) {
            if (updateDto.getAddress2() != null) {
                member.setAddress2(updateDto.getAddress2());
            } else {
                member.setAddress2(null); // 상세주소 삭제
            }
        }

        memberRepository.save(member);
    }

    /**
     * @author 박수현
     * @date 2023-08-26
     * @description : 주소 변경시, 위도-경도 값 업데이트
     * @return
     */

    public void geocodingAndUpdateCoordinates(Member member, String newAddress) throws IOException, JSONException {

        String addr = URLEncoder.encode(newAddress, "UTF-8");

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

        member.setLatitude(latitude);
        member.setLongitude(longitude);
    }

    /**
     * @author 박수현
     * @date 2023-08-24
     * @description : 비밀번호 변경
     * @return
     */
    public void changePassword(Principal principal, String currentPassword, String newPassword) {
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IncorrectPasswordException();
        }

        String hashNewPwd = passwordEncoder.encode(newPassword);
        member.setPassword(hashNewPwd);
        memberRepository.save(member);
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 프로필 이미지 조회 - DTO로 반환
     * @return
     */
    public ProfileDto.ProfileImgResponseDto getProfileImgDto(Principal principal){
        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        ProfileImgFile existingProfileImg = profileImgFileRepository.findByMember(member).orElse(null);
        if(existingProfileImg != null){
            return createProfileImgResponseDTO(existingProfileImg);
        } else {
            return null;
        }
    }

    /**
     * @author 박수현
     * @date 2023-08-04
     * @description : 프로필 이미지 조회
     * @return String
     */

    public String getProfileImage(Long id) {
        Member member = memberRepository.findById(id).orElse(null);

        ProfileImgFile profileImgFile = profileImgFileRepository.findByMember(member).orElse(null);

        if (profileImgFile != null) {
            return profileImgFile.getFileUrl();
        } else {
            return null;
        }
    }


    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : 프로필 이미지 업로드
     *                프로필 이미지가 이미 등록되었다면, 이미시 수정.
     *                프로필 이미지가 등록되지 않았다면, 단순히 업로드
     * @return ProfileImgFile
     */

    public ProfileDto.ProfileImgResponseDto uploadProfileImage(MultipartFile multipartFile, Principal principal) {
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFileName);
        String fileUrl = s3Util.upload(multipartFile, "profile-img", fileName);

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        ProfileImgFile existingProfileImg = profileImgFileRepository.findByMember(member).orElse(null);

        ProfileImgFile profileImgFile = null;
        if (existingProfileImg != null) {
            // 기존 프로필 이미지가 있을 경우 수정
            existingProfileImg.setOriginalFileName(originalFileName);
            existingProfileImg.setFileName(fileName);
            existingProfileImg.setFileUrl(fileUrl);
            profileImgFileRepository.save(existingProfileImg);
        } else {
            // 기존 프로필 이미지가 없을 경우 새로 등록
            profileImgFile = ProfileImgFile.builder()
                    .originalFileName(originalFileName)
                    .fileName(fileName)
                    .fileUrl(fileUrl)
                    .member(member)
                    .build();

            profileImgFileRepository.save(profileImgFile);
        }

        return createProfileImgResponseDTO(existingProfileImg != null ? existingProfileImg : profileImgFile);
    }

    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : 프로필 이미지 삭제
     * @return ProfileImgFile
     */

    public void deleteProfileImage(Long profileImgId) {
        ProfileImgFile profileImgFile = profileImgFileRepository.findById(profileImgId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 이미지를 찾을 수 없습니다."));

        // S3에서 이미지 삭제
        s3Util.delete(profileImgFile.getFileName());

        // ProfileImgFile 데이터베이스 정보 삭제
        profileImgFileRepository.delete(profileImgFile);
    }

    /**
     * @author 정세창
     * @date 2023-08-28
     * @description : 자기소개 조회
     * @return
     */

    public String getMyIntroduction(Principal principal) {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ServerException(ErrorCode.NULL_EXCEPTION));
        return member.getSelfIntroduction();
    }

    /**
     * @author 정세창
     * @date 2023-08-28
     * @description : 자기소개 수정
     * @return
     */

    public void updateMyIntroduction(Principal principal, String content) {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ServerException(ErrorCode.NULL_EXCEPTION));

        member.setSelfIntroduction(content);
        memberRepository.save(member);
    }
}
