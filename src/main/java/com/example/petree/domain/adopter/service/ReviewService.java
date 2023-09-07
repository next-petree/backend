package com.example.petree.domain.adopter.service;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.domain.Review;
import com.example.petree.domain.adopter.domain.ReviewImgFile;
import com.example.petree.domain.adopter.domain.SearchType;
import com.example.petree.domain.adopter.dto.ReviewDto;
import com.example.petree.domain.adopter.repository.ReviewImgFileRepository;
import com.example.petree.domain.adopter.repository.ReviewRepository;
import com.example.petree.domain.breeder.domain.BreederProjectionImpl;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.repository.DogRepository;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.global.util.FileUtil;
import com.example.petree.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.example.petree.domain.adopter.service
 * fileName       : ReviewService
 * author         : 박수현
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DogRepository dogRepository;
    private final ReviewImgFileRepository reviewImgFileRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;

    /**
     * @author 박수현
     * @date 2023-08-29
     * @description : 분양후기 수정
     * @return
     */
    @Transactional
    public void update(ReviewDto.ReviewUpdateRequestDto request, Review review, Adopter adopter) {
        // 작성자와 리뷰의 작성자가 일치하는지 확인
        if (!review.getAdopter().equals(adopter)) {
            throw new IllegalArgumentException("해당 작성자만 수정할 수 있습니다.");
        }

        if (request.getTitle() != null) {
            review.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            review.setContent(request.getContent());
        }

        // 이미지 리스트를 수정하려면 이전 이미지 리스트를 제거하고 새로운 이미지를 추가
        if (request.getReviewImgFiles() != null && !request.getReviewImgFiles().isEmpty()) {
            List<ReviewImgFile> newImages = new ArrayList<>();
            for (MultipartFile file : request.getReviewImgFiles()) {
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFilename);
                String fileUrl = s3Util.upload(file, "review-img", fileName);

                ReviewImgFile imgFile = ReviewImgFile.builder()
                        .originalFileName(file.getOriginalFilename())
                        .fileName(fileName)
                        .fileUrl(fileUrl)
                        .build();

                reviewImgFileRepository.save(imgFile);
                newImages.add(imgFile);
            }
            review.setImages(newImages);
        }

        reviewRepository.save(review);
    }

    /**
     * @author 박수현
     * @date 2023-08-29
     * @description : 분양후기 단건 조회
     * @return
     */
    @Transactional(readOnly = true)
    public ReviewDto.ReviewResponseDto show(Review review) {
        return ReviewDto.ReviewResponseDto.createReviewResponseDto(review);
    }

    /**
     * @author 박수현
     * @date 2023-08-28
     * @description : 분양후기 등록
     * @return
     */

    @Transactional
    public ReviewDto.ReviewResponseDto create(ReviewDto.ReviewRequestDto request, Adopter adopter) {

        Dog dog = dogRepository.findById(request.getDogId()).orElseThrow(null);

        Review review = Review.builder()
                .dog(dog)
                .title(request.getTitle())
                .content(request.getContent())
                .adopter(adopter)
                .build();

        List<ReviewImgFile> imgFiles = new ArrayList<>();

        for (MultipartFile file : request.getReviewImgFiles()) {
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "." + fileUtil.extractExt(originalFilename);
            String fileUrl = s3Util.upload(file, "review-img", fileName);

            ReviewImgFile imgFile = ReviewImgFile.builder()
                    .originalFileName(file.getOriginalFilename())
                    .fileName(fileName)
                    .fileUrl(fileUrl)
                    .review(review)
                    .build();

            reviewImgFileRepository.save(imgFile);
            imgFiles.add(imgFile);
        }

        review.setImages(imgFiles);

        Review savedReview = reviewRepository.save(review);

        return ReviewDto.ReviewResponseDto.createReviewResponseDto(savedReview);
    }

    /**
     * @author 박수현
     * @date 2023-08-29
     * @description : 분양후기 게시글 삭제
     * @return
     */

    @Transactional
    public void deleteReview(Review review, Adopter adopter) {

        if (!review.getAdopter().getId().equals(adopter.getId())) {
            throw new IllegalArgumentException("해당 작성자가 아닙니다.");
        }

        for (ReviewImgFile imgFile : review.getImages()) {
            s3Util.delete(imgFile.getFileName());
            reviewImgFileRepository.delete(imgFile);
        }

        reviewRepository.delete(review);
    }

    /**
     * @author 박수현
     * @date 2023-08-31
     * @description : 자신의 분양후기 목록 조회
     * @return
     */

    public Page<ReviewDto.ReviewListResponseDto> getMyReviews(Member member, Pageable pageable, SearchType searchType, String keyword) {
        Page<Review> reviewsPage = reviewRepository.findReviewsByMember(member, searchType, keyword, pageable);
        return reviewsPage.map(ReviewDto.ReviewListResponseDto::createReviewListResponseDto);
    }
}
