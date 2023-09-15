package com.example.petree.domain.breeder.service;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.domain.ResidentialEnvImgFile;
import com.example.petree.domain.adopter.dto.AdopterDetailDto;
import com.example.petree.domain.adopter.dto.ResidentialEnvDto;
import com.example.petree.domain.breeder.domain.*;
import com.example.petree.domain.breeder.dto.BreederDetailDto;
import com.example.petree.domain.breeder.dto.BreederDto;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.main_breed.repository.MainBreedRepository;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.repository.ProfileImgFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BreederService {

    private final BreederRepository breederRepository;
    private final ProfileImgFileRepository profileImgFileRepository;
    private final MemberRepository memberRepository;
    private final MainBreedRepository mainBreedRepository;

    /**
     * @author 박수현
     * @date 2023-07-14
     * @description : BreederSearch객체 생성
     * @return BreederSearch
     */
    public BreederSearch getBreederSearch(String verification, String keyword){
        BreederSearch breederSearch = new BreederSearch();
        if (verification != null) {
            if (verification.equals("yes")) {
                breederSearch.setVerification(Verification.yes);
            } else if (verification.equals("no")) {
                breederSearch.setVerification(Verification.no);
            }
        }
        if(keyword != null){
            breederSearch.setKeyword(keyword);
        }
        return breederSearch;
    }

    /**
     * @author 박수현
     * @date 2023-08-04
     * @description : 브리더 조회시, 프로필 이미지 조회
     * @return
     */

    public String getBreederProfileImage(Long breederId) {
        Member member = memberRepository.findById(breederId).orElse(null);

        ProfileImgFile profileImgFile = profileImgFileRepository.findByMember(member).orElse(null);

        if (profileImgFile != null) {
            return profileImgFile.getFileUrl();
        } else {
            return null;
        }
    }


    /**
     * @author 박수현
     * @date 2023-07-13
     * @description : 사용자가 로그인하지 않았다면, 단순 브리더 목록 조회
     *
     * @return Page<BreederDto>
     */

    @Transactional(readOnly = true)
    public Page<BreederDto> list(Pageable pageable, String searchType, String searchWord) {

        BreederSearch breederSearch = this.getBreederSearch(searchType, searchWord);

        log.info(breederSearch + "로그인하지 않은 사용자 - 브리더 검색 정보");

        Page<BreederProjectionImpl> breedersPage = breederRepository.findBreeders(breederSearch, pageable);
        Map<Long, List<String>> map = new HashMap<>();
        List<BreederDto> breederDtos = new ArrayList<>();

        this.groupByList(breedersPage, map);


        for (BreederProjectionImpl breederProjection : breedersPage.getContent()) {
            Long id = breederProjection.getId();
            if (map.containsKey(id)) {
                List<String> types = map.get(id);
                map.remove(id);

                BreederDto breederDto = BreederDto.createBreederDtoNotVerificated(breederProjection, getBreederProfileImage(id));
                breederDto.setTypes(types);
                breederDtos.add(breederDto);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), breederDtos.size());

        List<BreederDto> pageContent = breederDtos.subList(start, end);
        log.info("pageContent : " + pageContent);

        return new PageImpl<>(pageContent, pageable, breederDtos.size());
    }

    /**
     * @author 박수현
     * @date 2023-07-13
     * @description : 사용자가 로그인하였다면, 해당 사용자를 기준으로 브리더를 거리순으로 조회 및 거리 반환.
     *                거리는 소수점 첫째짜리까지만 반환한다.
     * @return Page<BreederDto>
     */


    @Transactional
    public Page<BreederDto> listByDistance(Member member, Pageable pageable, String verification, String keyword) {
        Double memberLatitude = member.getLatitude();
        Double memberLongitude = member.getLongitude();

        BreederSearch breederSearch = this.getBreederSearch(verification, keyword);

        log.info("BREEDER 검색 : " + breederSearch);

        Page<BreederProjectionImpl> breedersPage = breederRepository.findNearbyBreedersOrderByDistance(memberLatitude, memberLongitude, breederSearch, pageable);

        log.info("breedersPage : " + breedersPage);

        Map<Long, List<String>> map = new HashMap<>();
        List<BreederDto> breederDtos = new ArrayList<>();

        log.info("groupByList메소드가 사용되기 전 : " + map);

        this.groupByList(breedersPage, map);

        log.info("groupByList메소드가 사용된 후 : " + map);


        for (BreederProjectionImpl breederProjection : breedersPage.getContent()) {
            Long id = breederProjection.getId();
            if (map.containsKey(id)) {
                List<String> types = map.get(id);
                map.remove(id);

                BreederDto breederDto = BreederDto.createBreederDtoVerificated(breederProjection, getBreederProfileImage(id));
                log.info("브리더 조회 DTO : " + breederDto);
                breederDto.setDistance(Double.valueOf(String.format("%.1f", breederDto.getDistance())));

                breederDto.setTypes(types);
                breederDtos.add(breederDto);
            }
        }

        log.info("브리더 리스트 : " + breederDtos);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), breederDtos.size());

        List<BreederDto> pageContent = breederDtos.subList(start, end);
        log.info("pageContent : " + pageContent);

        return new PageImpl<>(pageContent, pageable, breederDtos.size());

    }

    /**
     * @author 박수현
     * @date 2023-07-14
     * @description : 주력견종정보의 타입이 String으로 반환되기에 List<String>으로 변환
     * @return void
     */

    private void groupByList(Page<BreederProjectionImpl> list, Map<Long, List<String>> map){

        for (BreederProjectionImpl breederProjection : list.getContent()) {
            Long id = breederProjection.getId();
            List<String> types = convertStringToList(breederProjection.getTypes());
            if (types == null) {
                types = new ArrayList<>();
            }

            //map의 id가 없는 경우에만 types추가! (id가 이미 있다면 중복발생)
            if (!map.containsKey(id)) {
                map.put(id, types);
            } else {
                List<String> existingTypes = map.get(id);
                existingTypes.addAll(types);
            }
        }
        log.info("map 정보 : " + map);
    }

    /**
     * @author 박수현
     * @date 2023-07-16
     * @description : String을 List<String>으로 변환하는 유틸리티 메서드
     * @return List<String>
     */

    private List<String> convertStringToList(String types) {
        List<String> list = new ArrayList<>();
        if (types != null && !types.isEmpty()) {
            String[] arr = types.replaceAll("\\[|\\]", "").split(", ");
            list.addAll(Arrays.asList(arr));
        }
        return list;
    }

    /**
     * @author 박수현
     * @date 2023-08-27
     * @description : 제3자가 보는 브리더 프로필 조회
     * @return
     */

    public BreederDetailDto getProfileById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);

        String profileImgUrl = getBreederProfileImage(id);

        if (member instanceof Breeder) {
            Breeder breeder = (Breeder) member;
            BreederDetailDto breederDetailDto = BreederDetailDto.builder()
                    .id(breeder.getId())
                    .nickname(breeder.getNickname())
                    .address1(breeder.getAddress1())
                    .verified(breeder.getIsVerified())
                    .selfIntroduction(breeder.getSelfIntroduction())
                    .profileImgUrl(profileImgUrl)
                    .build();

            List<MainBreedDto.MainBreedDtoResponse> mainBreedDtoResponses = new ArrayList<>();
            List<MainBreed> mainBreeds = mainBreedRepository.findByBreederId(id);

            for (MainBreed mainBreed : mainBreeds) {
                DogType dogType = mainBreed.getDogType();
                if (dogType != null) {
                    MainBreedDto.MainBreedDtoResponse mainBreedDtoResponse =
                            new MainBreedDto.MainBreedDtoResponse(dogType);
                    mainBreedDtoResponses.add(mainBreedDtoResponse);
                }
            }
            breederDetailDto.setMainBreedDtoResponseList(mainBreedDtoResponses);

            List<SimpleDogDto> simpleDogDtos = getSimpleDogDtos(member);
            breederDetailDto.setSimpleDogDtos(simpleDogDtos);

            return breederDetailDto;
        }

        return null;
    }

    /**
     * @author 박수현
     * @date 2023-08-15
     * @description : 브리더 id값을 토대로, 보유견종 리스트 조회
     * @return
     */

    private List<SimpleDogDto> getSimpleDogDtos(Member member) {
        if (member instanceof Breeder) {
            Breeder breeder = (Breeder) member;
            List<Dog> dogs = breeder.getDogs();

            List<SimpleDogDto> simpleDogDtos = new ArrayList<>();
            for (Dog dog : dogs) {
                SimpleDogDto dto = SimpleDogDto.builder()
                        .id(dog.getId())
                        .type(dog.getDogType().getName())
                        .gender(dog.getGender())
                        .birthDate(dog.getBirthDate())
                        .name(dog.getName())
                        .status(dog.getStatus())
                        .imgUrl(dog.getDogImgFiles().get(0).getFileUrl())
                        .breederNickName(dog.getBreeder().getNickname())
                        .isBreederVerified(dog.getBreeder().getIsVerified())
                        .build();
                simpleDogDtos.add(dto);
            }
            return simpleDogDtos;
        }
        return Collections.emptyList();
    }
}
