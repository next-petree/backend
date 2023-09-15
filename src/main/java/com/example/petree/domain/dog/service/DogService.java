package com.example.petree.domain.dog.service;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.dog.controller.PossessionController;
import com.example.petree.domain.dog.domain.*;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.dog.dto.DetailDogDto;
import com.example.petree.domain.dog.dto.SimpleDogDto;
import com.example.petree.domain.dog.repository.DogImgFileRepository;
import com.example.petree.domain.dog.repository.DogRepository;
import com.example.petree.domain.dog.repository.DogSpecification;
import com.example.petree.domain.main_breed.domain.Size;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.main_breed.service.MainBreedService;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.Response;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.example.petree.domain.dog.cotroller
 * fileName       : DogService
 * author         : jsc
 * date           : 2023/07/05
 * description    : 강아지 관련 서비스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/04        정세창               getDogs() 메서드 추가
 * 2023/07/06        정세창               getDog() 메서드 추가
 * 2023-07-08        이지수               파일 업로드 제외 CRUD 작성
 * 2023-07-11        이지수               파일업로드 완료
 * 2023-07-16        이지수               생성, 삭제, 수정 모두 가능
 * (s3 버킷에는 삭제 반영 안됨)
 * 삭제, 수정은 추후에 보강
 * 2023-07-17        이지수               최종 수정
 * 2023-07-30        이지수               Swagger작성, 전반적인 수정
 * 2023-08-02        이지수               s3버킷 삭제 안되는 것 해결
 * 2023-08-29        이지수               create, update 분리
 */
@Service
@RequiredArgsConstructor
public class DogService {

    private final MainBreedService mainBreedService;
    private final DogRepository dogRepository;
    private final DogImgFileRepository dogImgFileRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;
    private final Response response;


    /**
     * @author 박수현
     * @date 2023-08-28
     * @description : id에 따른 dog조회
     * @return
     */
    public Dog getDogById(Long dogId) {
        return dogRepository.findById(dogId).orElse(null);
    }


    /**
     *
     * @param pageable Pageable 객체
     * @param dogTypeId 견종 식별자 id
     * @param verification 인증 브리더 필터링 조건. yes/no
     * @param isAvailable 입양가능 견종 필터링 조건
     * @param gender 성별 필터링 조건
     * @param size 크기 필터링 조건
     * @return
     */
    @Transactional(readOnly = true)
    public ResponseEntity<?> getDogs(Pageable pageable, Long dogTypeId, String verification,
                                     Boolean isAvailable, Gender gender, Size size) {

        Specification<Dog> spec = Specification
                .where(DogSpecification.isVerified(verification))
                .and(DogSpecification.hasDogType(dogTypeId))
                .and(DogSpecification.isVerified(verification))
                .and(DogSpecification.filterAvailableDog(isAvailable))
                .and(DogSpecification.filterByGender(gender))
                .and(DogSpecification.filterBySize(size));

        Page<Dog> dogs = dogRepository.findAll(spec, pageable);

        return response.success(HttpStatus.OK, dogs.map(SimpleDogDto::new));
    }

    /**
     *
     * @param id 강아지 id
     * @return Dog 엔티티를 가공한 DetailDogDto 객체
     */
    @Transactional(readOnly = true)
    public ResponseEntity<?> getDog(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(null);
        if(dog == null) {
            return response.error("해당 id를 식별자로 가지는 강아지 엔티티가 조회되지 않습니다.");
        }

        return response.success(HttpStatus.OK, new DetailDogDto(dog));
    }
    /**
     * @author 이지수
     * @date 2023-08-29
     * @description : 검색 조건 추가 후 견종으로 검색하기 위해 수정
     */
    @Transactional(readOnly = true)
    public Page<SimpleDogDto> getDogsOfBreeder(Pageable pageable, Breeder breeder, String searchType, String keyword) {

        Specification<Dog> spec = Specification.where(DogSpecification.filterByBreeder(breeder));

        if (SearchType.NAME.getSearchType().equals(searchType)) {
            spec = spec.and(DogSpecification.hasDogNameKeyword(keyword));
        } else if (SearchType.TYPE.getSearchType().equals(searchType)) {
            spec = spec.and(DogSpecification.hasDogTypeKeyword(keyword));
        } else if (SearchType.WHOLE.getSearchType().equals(searchType)){
            spec = spec.and(DogSpecification.latestCreate());
        } else{
            spec = spec.and(DogSpecification.latestCreate());
        }

        Page<Dog> dogs = dogRepository.findAll(spec, pageable);

        return dogs.map(SimpleDogDto::new);
    }


    @Transactional(readOnly = true)
    public PossessionDogDto getDogToUpdate(Long id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        return new PossessionDogDto(dog);
    }
    /**
     * @author 이지수
     * @date 2023-08-23
     * @description : DogType에 대한 내용 추가 (해당 브리더의 주력견종으로 등록된 dogType을 받아서 dog를 생성하고, dog_type_id값 저장
     */

    @Transactional
    public PossessionDogDto.CreateDogDto create(Breeder breeder , Long dogTypeId, PossessionDogDto.CreateDogDto possessionDogDto) {
        // 해당 브리더의 주력견종들을 가져와 리스트 형태로 저장
        List<MainBreed> mainBreeds = breeder.getMainBreeds();

        // mainBreeds가 null이거나 비어있는 경우 예외 처리
        if (mainBreeds == null || mainBreeds.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 값입니다.");
        }

        // 요청된 dogTypeId가 mainBreeds에 속하는지 확인
        boolean validDogType = mainBreeds.stream()
                .anyMatch(mainBreed -> mainBreed.getDogType().getId().equals(dogTypeId));

        // 저장되어 있지 않는 dogType 입력 시 예외 처리
        if (!validDogType) {
            throw new IllegalArgumentException("유효하지 않은 값입니다.");
        }

        // 브리더는 possessionDogDto를 통해 새로운 dog 생성
        Dog dog = new Dog(breeder, possessionDogDto, LocalDate.now());
        // ModelAttribute를 사용했기 떄문에 set을 통해 dog 정보를 저장
        dog.createFromDto(possessionDogDto);
        dog.setStatus(Status.AVAILABLE);
        // 브리더의 주력 견종 중 해당 보유견 dog를 생성할 떄 dog에 dogType을 저장해줌
        dog.setDogType(mainBreeds.stream()
                .filter(mainBreed -> mainBreed.getDogType().getId().equals(dogTypeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 값입니다."))
                .getDogType()); // mainBreed의 dogType 설정

        // dog에 이미지 업로드를 추가함
        uploadDogImgFiles(dog, possessionDogDto);
        dogRepository.save(dog);

        return new PossessionDogDto.CreateDogDto(dog);
    }
    private void uploadDogImgFiles(Dog dog, PossessionDogDto.CreateDogDto possessionDogDto) {
        List<MultipartFile> imgFiles = possessionDogDto.getDogImgFiles();

        if (imgFiles.size() > 4) {
            throw new IllegalArgumentException("이미지는 최대 4개까지만 등록할 수 있습니다.");
        }

        for (MultipartFile multipartFile : imgFiles) {
            String originalFileName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFileName);
            String fileUrl = s3Util.upload(multipartFile, "dog-img", fileName);
            DogImgFile dogImgFile = new DogImgFile(originalFileName, fileName, fileUrl, dog);
            dog.addDogImgFile(dogImgFile);
        }
    }


    /**
     * @author 이지수
     * @date 2023-08-15
     * @description : 삭제 시 생성한 브리더만 삭제 권한 있게 수정함
     */

    @Transactional
    public PossessionDogDto delete(Breeder breeder, Long id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        Long breederId = breeder.getId();
        if(!dog.getBreeder().getId().equals(breederId)){
            throw new IllegalStateException("해당 글 삭제에 대한 권한이 없습니다.");
        }
        // 해당 dog의 모든 이미지를 삭제한다.
        deleteDogImgFiles(dog);
        // 해당 강아지에 대한 정보도 모두 삭제한다.
        dogRepository.delete(dog);

        return new PossessionDogDto(dog);
    }

    private void deleteDogImgFiles(Dog dog) {
        List<DogImgFile> dogImgFiles = dogImgFileRepository.findByDogId(dog.getId());
//        if (dogImgFiles.isEmpty()) {
//            throw new IllegalArgumentException("해당 강아지에 대한 이미지 파일이 존재하지 않습니다.");
//        }

        for (DogImgFile dogImgFile : dogImgFiles) {
            String fileUrl= "dog-img/" +dogImgFile.getFileName();
            s3Util.delete(fileUrl);
            dogImgFileRepository.delete(dogImgFile);
        }
    }

    @Transactional
    public PossessionDogDto.UpdateDogDto update(Breeder breeder, Long id, PossessionDogDto.UpdateDogDto possessionDogDto) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        Long breederId = breeder.getId();
        if(!dog.getBreeder().getId().equals(breederId)){
            throw new IllegalStateException("해당 글 수정에 대한 권한이 없습니다.");
        }
        // possessionDogDto의 patch 메소드를 사용하여 dog에 저장되어 있는 정보를 수정한다.
        possessionDogDto.patch(dog);
        // 수정한 정보를 저장한다.
        dog.updateFromDto(possessionDogDto);
        dogRepository.save(dog);

        return new PossessionDogDto.UpdateDogDto(dog);
    }

    @Transactional
    public PossessionDogDto.UpdateDogDto deleteAndAddImages(Breeder breeder, Long id, PossessionDogDto.UpdateDogDto possessionDogDto) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        // 이미지 삭제 처리
        deleteDogImgFileSelect(dog, possessionDogDto);

        int imgFilesCount = dog.getDogImgFiles().size();

        if(imgFilesCount + possessionDogDto.getDogImgFiles().size()>4){
            throw new IllegalArgumentException("이미지는 최대 4개까지만 등록할 수 있습니다.");
        }

        addImg(breeder,id,possessionDogDto);
        return new PossessionDogDto.UpdateDogDto(dog);
    }

    public void addImg(Breeder breeder, Long id, PossessionDogDto.UpdateDogDto possessionDogDto) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        if (possessionDogDto.isUploadImage()) {
            if (!possessionDogDto.getDogImgFiles().isEmpty()) {
                // 이미지 업로드 메소드를 사용하여 이미지 업로드 수행
                possessionDogDto.getDogImgFiles().stream().forEach(multipartFile -> {
                    String originalFileName = multipartFile.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFileName);
                    String fileUrl = s3Util.upload(multipartFile, "dog-img", fileName);
                    DogImgFile dogImgFile = new DogImgFile(originalFileName, fileName, fileUrl, dog);
                    dog.addDogImgFile(dogImgFile);
                });
            }
        }
        dogRepository.save(dog);
    }


    /**
     * @author 이지수
     * @date 2023-08-13
     * @description : 수정 시 이미지 무조건 삭제되는 것을 선택적으로 삭제할 수 있게 수정
     * 사용자에게 삭제할 filename을 입력받아서 삭제가 이루어짐
     */

    // 해당 dog의 이미지를 선택적으로 삭제함. 사용자에게 originalFileName 입력받아 해당 이미지 삭제
    public void deleteDogImgFileSelect(Dog dog,PossessionDogDto.UpdateDogDto possessionDogDto) {
        List<DogImgFile> dogImgFiles = dogImgFileRepository.findByDogId(dog.getId());
        if (dogImgFiles.isEmpty()) {
            throw new IllegalArgumentException("해당 강아지에 대한 이미지 파일이 존재하지 않습니다.");
        }
        List<String> fileNamesToDelete = possessionDogDto.getImgNameToDelete();
        if (fileNamesToDelete == null) {
            return;
        }
        // 이미지 파일을 삭제한 후 남아있는 이미지 파일의 갯수 계산
        int remainingImageCount = (int) dogImgFiles.stream()
                .filter(imgFile -> !fileNamesToDelete.contains(imgFile.getOriginalFileName()))
                .count();

        if (remainingImageCount <= 0) {
            throw new IllegalArgumentException("최소한 한 개의 이미지 파일이 남아 있어야 합니다.");
        }

        for (String originalFileName : fileNamesToDelete) {

                DogImgFile dogImgFile=dogImgFileRepository.findByOriginalFileNameAndDogId(originalFileName, dog.getId());
                String fileName=dogImgFile.getFileName();
                String s3path = "dog-img/" + fileName;
                s3Util.delete(s3path); // S3 이미지 삭제 로직 호출
                if(dogImgFile == null) {
                    throw new IllegalArgumentException("삭제 요청하신 파일이 존재하지 않습니다.");
                }
                dogImgFileRepository.deleteByOriginalFileNameAndDogId(originalFileName, dog.getId());
            }
        dog.getDogImgFiles().removeIf(imgFile -> {
            String originalFileName = imgFile.getOriginalFileName();
            Long dogId = imgFile.getDog().getId();

            // 이미지 파일 객체를 삭제할 조건 설정
            boolean shouldRemove = fileNamesToDelete.contains(originalFileName) && dogId.equals(dog.getId());

            // 삭제 조건이 충족되면 true를 반환하여 이미지 파일 객체 삭제
            return shouldRemove;
        });
    }
}
