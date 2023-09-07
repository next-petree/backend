package com.example.petree.domain.dog.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.domain.dog.repository.DogRepository;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.domain.Size;
import com.example.petree.domain.member.domain.Role;
import com.example.petree.global.util.PagingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

/**
 * packageName    : com.example.petree.domain.dog.service
 * fileName       : DogServiceTest
 * author         : jsc
 * date           : 2023/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/05        jsc
 */
//@ExtendWith(MockitoExtension.class)
//public class DogServiceTest {
//
//    @Mock
//    private DogRepository dogRepository;
//
//    @Mock
//    private PagingUtil pagingUtil;
//
//    @InjectMocks
//    private DogService dogService;
//
//    @Test
//    void getDogTest() {
//        Breeder breeder1 = new Breeder(
//                1L, "abc123", "breeder1", "1234",
//                "010-1234-1234", "경기도 수원시 영통구 월드컵로 206",
//                37.28266159769498, 127.0444739641248, Role.BREEDER, true
//        );
//        MainBreed mainBreed1 = new MainBreed(1L, "푸들", "귀엽고 복슬함", breeder1, Size.SMALL);
//
//        Dog dog1 = new Dog(
//                1L, Gender.MALE, Status.AVAILABLE, LocalDate.of(2023, 7, 1),
//                "쫑이", "착하고 순해요", mainBreed1, breeder1
//        );
//        Dog dog2 = new Dog(
//                2L, Gender.MALE, Status.AVAILABLE, LocalDate.of(2023, 7, 1),
//                "꽁이", "착하고 순해요", mainBreed1, breeder1
//        );
//        List<Dog> dogList = Arrays.asList(dog1, dog2);
//        Page<Dog> dogPage = new PageImpl<>(dogList, PageRequest.of(0, 2), dogList.size());
//
//        when(dogRepository.findAll((Pageable) any(Pageable.class))).thenReturn(dogPage);
//        Page<SimpleDogDto> result = dogService.getDogs(PageRequest.of(0, 2), "", "");
//
//        verify(dogRepository, times(1)).findAll((Pageable) any(Pageable.class));
//
//    }
//}
