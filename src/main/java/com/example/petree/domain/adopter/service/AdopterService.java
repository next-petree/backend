package com.example.petree.domain.adopter.service;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.domain.ResidentialEnvImgFile;
import com.example.petree.domain.adopter.domain.SpaceType;
import com.example.petree.domain.adopter.dto.AdopterDetailDto;
import com.example.petree.domain.adopter.dto.ResidentialEnvDto;
import com.example.petree.domain.adopter.repository.ResidentialEnvImgRepository;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.repository.ProfileImgFileRepository;
import com.example.petree.global.error.ErrorCode;
import com.example.petree.global.error.exception.BusinessException;
import com.example.petree.global.error.exception.ServerException;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.petree.domain.adopter.service
 * fileName       : AdopterService
 * author         : 정세창
 * date           : 2023/08/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/21        정세창               init
 * 2023/08/26        정세창        SpaceType에 따른 별개 처리
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdopterService {

    private final ResidentialEnvImgRepository residentialEnvFileRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;
    private final MemberRepository memberRepository;
    private final ProfileImgFileRepository profileImgFileRepository;


    /**
     * @author 박수현
     * @date 2023-08-28
     * @description : 제3자가 보는 분양희망자 프로필 상세조회
     * @return
     */

    public AdopterDetailDto getProfileById(Long id) {
        log.info("제3자가 보는 분양희망자 프로필 조회 탔음");
        Member member = memberRepository.findById(id).orElse(null);

        if (member instanceof Adopter) {
            Adopter adopter = (Adopter) member;

            List<ResidentialEnvDto.EnvResponseDto> envResponseDtos = new ArrayList<>();
            for (ResidentialEnvImgFile residentialEnvImgFile : adopter.getResidentialEnvImgFiles()) {
                ResidentialEnvDto.EnvResponseDto envResponseDto = new ResidentialEnvDto.EnvResponseDto(residentialEnvImgFile);
                envResponseDtos.add(envResponseDto);
            }

            ProfileImgFile profileImgFile = profileImgFileRepository.findByMember(member).orElse(null);
            String profileImgUrl = profileImgFile != null ? profileImgFile.getFileUrl() : null;

            AdopterDetailDto adopterDetailDto = AdopterDetailDto.builder()
                    .id(adopter.getId())
                    .nickname(adopter.getNickname())
                    .address1(adopter.getAddress1())
                    .verified(adopter.getIsVerified())
                    .selfIntroduction(adopter.getSelfIntroduction())
                    .envResponseDtos(envResponseDtos)
                    .profileImgUrl(profileImgUrl)
                    .build();

            return adopterDetailDto;
        }

        return null;
    }

    /**
     * 분양 희망자의 주거환경 이미지를 가져오는 서비스 메소드
     * @param adopter 분양 희망자 엔티티
     * @return
     */
    public List<Object> getEnvironments(Adopter adopter) {

        List<Object> result = new ArrayList<>();
        List<SpaceType> spaceTypes = Arrays.asList(SpaceType.values());

        // 현재 분양희망자가 등록한 공간 타입의 리스트 가져옴
        List<SpaceType> savedSpaceTypes = adopter.getResidentialEnvImgFiles()
                .stream().map(ResidentialEnvImgFile::getSpaceType).collect(Collectors.toList());

        for (SpaceType spaceType : spaceTypes) {
            if(savedSpaceTypes.contains(spaceType)) {
                result.add(
                        new ResidentialEnvDto.EnvResponseDto(
                                residentialEnvFileRepository.findByAdopterAndSpaceType(adopter, spaceType)
                                        .orElseThrow(() -> new BusinessException(ErrorCode.NULL_EXCEPTION))
                        )
                );
            } else {
                Map<String, Object> notSavedSpace = new HashMap<>();
                notSavedSpace.put("id", null);
                notSavedSpace.put("imgUrl", null);
                notSavedSpace.put("spaceType", spaceType);
                result.add(notSavedSpace);
            }
        }
        // 조회 결과를 dto로 가공하여 반환
        return result;
    }


    /**
     * 특정 분양 희망자의 주거 환경 리스트를 갱신하는 서비스 메소드(생성/수정/삭제를 모두 포함)
     * @param adopter 분양 희망자 엔티티
     * @param form 삭제하거나 추가해야할 주거환경 정보가 들어있는 dto
     */
    @Transactional
    public void updatePhotos(Adopter adopter, ResidentialEnvDto.EnvRequestDto form) {
        // 삭제할 이미지 ID 리스트를 가져옴
        List<Long> deletedImgsId = form.getDeletedImgsId();

        // 가져온 리스트를 순회하면서 해당하는 id의 엔티티와 s3에 저장된 파일을 삭제한다
        for (Long imgId : deletedImgsId) {
            ResidentialEnvImgFile img = residentialEnvFileRepository.findById(imgId)
                    .orElseThrow(() -> new ServerException(ErrorCode.NULL_EXCEPTION));

            s3Util.delete(img.getFileUrl());
            // 사용자와 연결된 사진 삭제
            adopter.getResidentialEnvImgFiles().remove(img);
            // 데이터베이스에서 이미지 삭제
            residentialEnvFileRepository.delete(img);
        }

        // 현재 분양희망자가 등록한 공간 타입의 리스트 가져옴
        List<SpaceType> savedSpaceTypes = adopter.getResidentialEnvImgFiles()
                .stream().map(ResidentialEnvImgFile::getSpaceType).collect(Collectors.toList());


        // 거실 이미지를 가져오기
        MultipartFile livingRoomImg = form.getLivingRoomImg();
        // 이미지가 담겨있는지와, 거실 이미지가 이미 등록되어 있는지 확인. 참이라면 중복 등록이므로 예외 발생
        log.info("거실이미지 확인 : " + livingRoomImg.getContentType());
        if (livingRoomImg.getContentType() != null && savedSpaceTypes.contains(SpaceType.LIVING_ROOM)) {
            throw new BusinessException(ErrorCode.HAS_MORE_THAN_ONE_SPACE_TYPE);
        }
        // 이미지가 담겨있는지 확인하고 참이면 해당 이미지 등록
        if (livingRoomImg.getContentType() != null) {
            String originalFilename = livingRoomImg.getOriginalFilename();

            // 주거환경 이미지 객체를 생성하고 정보를 설정
            String filename = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFilename);
            String res = s3Util.upload(livingRoomImg, "residential-env-img", filename);
            ResidentialEnvImgFile residentialEnvImgFile =
                    new ResidentialEnvImgFile(SpaceType.LIVING_ROOM, originalFilename, filename, res, adopter);

            // 사용자와 연결하고 저장
            adopter.getResidentialEnvImgFiles().add(residentialEnvImgFile);
            residentialEnvFileRepository.save(residentialEnvImgFile);
        }

        // 화장실 이미지도 거실 이미지와 동일한 로직으로 진행
        MultipartFile bathRoomImg = form.getBathRoomImg();
        if (bathRoomImg.getContentType() != null && savedSpaceTypes.contains(SpaceType.BATH_ROOM)) {
            throw new BusinessException(ErrorCode.HAS_MORE_THAN_ONE_SPACE_TYPE);
        }
        if (bathRoomImg.getContentType() != null) {
            String originalFilename = bathRoomImg.getOriginalFilename();

            // 주거환경 이미지 객체를 생성하고 정보를 설정
            String filename = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFilename);
            String res = s3Util.upload(bathRoomImg, "residential-env-img", filename);
            ResidentialEnvImgFile residentialEnvImgFile =
                    new ResidentialEnvImgFile(SpaceType.BATH_ROOM, originalFilename, filename, res, adopter);

            // 사용자와 연결하고 저장
            adopter.getResidentialEnvImgFiles().add(residentialEnvImgFile);
            residentialEnvFileRepository.save(residentialEnvImgFile);
        }

        // 화장실 이미지도 거실 이미지와 동일한 로직으로 진행
        MultipartFile yardImg = form.getYardImg();
        if (yardImg.getContentType() != null && savedSpaceTypes.contains(SpaceType.YARD)) {
            throw new BusinessException(ErrorCode.HAS_MORE_THAN_ONE_SPACE_TYPE);
        }
        if (yardImg.getContentType() != null) {
            String originalFilename = yardImg.getOriginalFilename();

            // 주거환경 이미지 객체를 생성하고 정보를 설정
            String filename = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFilename);
            String res = s3Util.upload(yardImg, "residential-env-img", filename);
            ResidentialEnvImgFile residentialEnvImgFile =
                    new ResidentialEnvImgFile(SpaceType.YARD, originalFilename, filename, res, adopter);

            // 사용자와 연결하고 저장
            adopter.getResidentialEnvImgFiles().add(residentialEnvImgFile);
            residentialEnvFileRepository.save(residentialEnvImgFile);
        }

    }

}
