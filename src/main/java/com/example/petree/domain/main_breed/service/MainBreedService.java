package com.example.petree.domain.main_breed.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.breeder.repository.BreederRepository;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.dto.MainBreedDto;
import com.example.petree.domain.main_breed.repository.DogTypeRepository;
import com.example.petree.domain.main_breed.repository.MainBreedRepository;
import com.example.petree.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.petree.domain.main_breed.dto.MainBreedDto.createMainBreedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainBreedService {

    private final BreederRepository breederRepository;
    private final MainBreedRepository mainBreedRepository;
    private final DogTypeRepository dogTypeRepository;

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 로그인한 사용자에 따른 주력견종 목록 반환
     * @return
     */

    public List<MainBreedDto.MainBreedDtoResponse> index(Member member) {
        Long breederId = member.getId();
        return mainBreedRepository.findByBreederId(breederId)
                .stream()
                .map(MainBreedDto::createMainBreedDto)
                .collect(Collectors.toList());
    }


    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 로그인한 사용자에 따른 주력견종의 게시글을 반환
     * @return MainBreedDto
     */

    /*public MainBreedDto.MainBreedDtoResponse show(Member member, Long id){
        Long breederId = member.getId();
        MainBreed mainBreed = mainBreedRepository.findByBreederIdAndId(breederId, id);
        return MainBreedDto.createMainBreedDto(mainBreed);
    }*/


    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 로그인한 사용자가 주력견종을 등록, 기존에 이미 있는 데이터라면, NULL을 반환한다.
     * @return
     */
    @Transactional
    public List<MainBreedDto.MainBreedDtoResponse> create(Breeder breeder, List<Long> dogTypeIds) {

        List<MainBreedDto.MainBreedDtoResponse> createdMainBreeds = new ArrayList<>();

        for (Long dogTypeId : dogTypeIds) {
            DogType dogType = dogTypeRepository.findById(dogTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("견종 정보를 찾을 수 없습니다."));

            if (!exists(breeder.getId(), dogTypeId)) {
                MainBreed mainBreed = MainBreed.builder()
                        .breeder(breeder)
                        .dogType(dogType)
                        .build();

                mainBreedRepository.save(mainBreed);

                MainBreedDto.MainBreedDtoResponse createdMainBreedDto = createMainBreedDto(mainBreed);
                createdMainBreeds.add(createdMainBreedDto);
            }
        }

        return createdMainBreeds;
    }

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 주력견종을 브리더id와 dogType id를 기반으로 조회
     * @return
     */


    public boolean exists(Long breederId, Long dogTypeId) {
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작성자가 존재하지 않습니다."));

        DogType dogType = dogTypeRepository.findById(dogTypeId)
                .orElseThrow(() -> new IllegalArgumentException("견종 정보를 찾을 수 없습니다."));

        return mainBreedRepository.existsByBreederAndDogType(breeder, dogType);
    }

    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 로그인한 사용자가 등록한 주력견종 데이터 삭제
     * @return MainBreedDto
     */

    /*@Transactional
    public void delete(Member member, Long dogTypeId) {

        Long breederId = member.getId();

        // 1. 해당 breederId가 있는지 조회
        breederRepository.findById(breederId)
                //해당 작성자가 존재하지 않는다면, 아래 코드를 수행하지 않음.
                .orElseThrow(() -> new IllegalArgumentException("해당 작성자가 존재하지 않습니다."));

        // 2. 주력견종을 등록하기 위해 견종 정보 조회
        DogType dogType = dogTypeRepository.findById(dogTypeId).orElse(null);

        // 해당 견종 정보가 없다면 아래 코드를 수행하지 않음.
        if (dogType == null) {
            throw new IllegalArgumentException("견종 정보를 찾을 수 없습니다.");
        }

        MainBreed mainBreed = mainBreedRepository.findByBreederIdAndId(breederId, dogTypeId);

        mainBreedRepository.delete(mainBreed);
    }*/

    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 작성자에 따른 주력견종 데이터 수정
     * @return MainBreedDto
     */
    @Transactional
    public List<MainBreedDto.MainBreedDtoResponse> update(Member member, MainBreedDto.MainBreedDtoRequest mainBreedDtoRequest) {

        Long breederId = member.getId();

        // 1. 해당 breederId가 있는지 조회
        Breeder breeder = breederRepository.findById(breederId)
                // 해당 작성자가 존재하지 않는다면, 아래 코드를 수행하지 않음.
                .orElseThrow(() -> new IllegalArgumentException("해당 작성자가 존재하지 않습니다."));

        // 2. 기존의 주력견종 정보 삭제
        mainBreedRepository.deleteByBreederId(breederId);

        List<MainBreed> mainBreeds = mainBreedRepository.findByBreederId(breederId);
        log.info("삭제 후 주력견종 정보 조회 : " + mainBreeds);

        List<MainBreedDto.MainBreedDtoResponse> updatedMainBreeds = new ArrayList<>();

        // 3. 새로운 주력견종 정보 등록
        for (Long dogTypeId : mainBreedDtoRequest.getDogTypeId()) {
            DogType dogType = dogTypeRepository.findById(dogTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("견종 정보를 찾을 수 없습니다."));

            MainBreed mainBreed = MainBreed.builder()
                    .breeder(breeder)
                    .dogType(dogType)
                    .build();

            mainBreedRepository.save(mainBreed);

            MainBreedDto.MainBreedDtoResponse mainBreedDtoResponse = MainBreedDto.createMainBreedDto(mainBreed);
            updatedMainBreeds.add(mainBreedDtoResponse);
        }
        return updatedMainBreeds;
    }
}
