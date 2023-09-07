package com.example.petree.domain.main_breed.service;

import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.dto.DogTypeDto;
import com.example.petree.domain.main_breed.repository.DogTypeRepository;
import com.example.petree.global.error.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.main_breed.service
 * fileName       : DogTypeService
 * author         : 박수현
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/14        박수현              최초생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DogTypeService {

    private final DogTypeRepository dogTypeRepository;

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 견종 정보 리스트 조회
     * @return
     */

    public List<DogTypeDto> getAllDogTypes() {
        List<DogType> dogTypes = dogTypeRepository.findAll();
        return dogTypes.stream()
                .map(DogTypeDto::createDogTypeDto)
                .collect(Collectors.toList());
    }

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 견종 정보 검색어 이용
     * @return
     */
    @Cacheable(cacheNames = "dogTypes")
    public List<DogTypeDto> searchDogTypesByKeyword(String keyword) {
        List<DogType> dogTypes = dogTypeRepository.findByNameContainingIgnoreCase(keyword);
        return dogTypes.stream()
                .map(DogTypeDto::createDogTypeDto)
                .collect(Collectors.toList());
    }

    /**
     * @author 박수현
     * @date 2023-08-21
     * @description : 견종 정보 추가
     * @return
     */

    public DogTypeDto addDogType(String name) {

        if (dogTypeRepository.findByName(name).isPresent()) {
            throw new DuplicateException("해당 견종이 이미 존재합니다.");
        }

        DogType newDogType = new DogType();
        newDogType.setName(name);
        DogType savedDogType = dogTypeRepository.save(newDogType);

        return DogTypeDto.createDogTypeDto(savedDogType);
    }

}
