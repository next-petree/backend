package com.example.petree.domain.adopter.controller;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.domain.Review;
import com.example.petree.domain.adopter.domain.SearchType;
import com.example.petree.domain.adopter.dto.ReviewDto;
import com.example.petree.domain.adopter.repository.AdopterRepository;
import com.example.petree.domain.adopter.repository.ReviewRepository;
import com.example.petree.domain.adopter.schema.ReviewSchema;
import com.example.petree.domain.adopter.service.ReviewService;
import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.Status;
import com.example.petree.domain.dog.service.DogService;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.member.schema.MemberSchema;
import com.example.petree.global.Response;
import com.example.petree.global.ResponseSchema;
import com.example.petree.global.error.exception.MissingPrincipalException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * packageName    : com.example.petree.domain.adopter.controller
 * fileName       : ReviewController
 * author         : 박수현
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/adopter")
@Tag(name = "분양희망자 회원, 마이페이지 분양후기 관리", description = "분양희망자 회원, 마이페이지 분양후기 관리 관련 API")
public class ReviewController {

    private final Response response;
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    private final DogService dogService;
    private final ReviewRepository reviewRepository;

    @PostMapping("/review")
    @Operation(
            summary = "분양후기 등록",
            description = "유효성 검사를 통해 분양후기 글을 등록할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양후기 등록 성공",
                    content = @Content(schema = @Schema(implementation = ReviewSchema.ReviewSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 분양후기 등록 불가능",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> create(@Valid @ModelAttribute ReviewDto.ReviewRequestDto request, Principal principal){
        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member != null) {
            if (member.getRole().getTitle().equals("ADOPTER")) {
                Dog adoptedDog = dogService.getDogById(request.getDogId());

                if (adoptedDog == null || adoptedDog.getStatus() != Status.DONE) {
                    return response.fail(HttpStatus.BAD_REQUEST, "분양 완료된 상태가 아닙니다.");
                }

                int maxImageCount = 4;
                int currentImageCount = request.getReviewImgFiles().size();
                if (currentImageCount > maxImageCount) {
                    return response.fail(HttpStatus.BAD_REQUEST, "이미지는 최대 " + maxImageCount + "개까지 업로드 가능합니다.");
                }

                ReviewDto.ReviewResponseDto responseDto = reviewService.create(request, (Adopter) member);
                return response.success(HttpStatus.OK, responseDto);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    @GetMapping("/review/{reviewId}")
    @Operation(
            summary = "분양후기 단건 조회",
            description = "분양후기 단건 조회 가능하다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양후기 조회 성공",
                    content = @Content(schema = @Schema(implementation = ReviewSchema.ReviewSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "없는 ID로 요청한 경우 404 반환",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema404.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 분양후기 단건 조회 불가능",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> show(@PathVariable Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return response.fail(HttpStatus.NOT_FOUND, "해당 ID의 분양후기를 찾을 수 없습니다.");
        }

        ReviewDto.ReviewResponseDto responseDto = reviewService.show(review);

        return response.success(HttpStatus.OK, responseDto);
    }

    @PatchMapping("/review/{reviewId}")
    @Operation(
            summary = "분양후기 수정 API",
            description = "분양후기를 수정한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양후기 수정 성공",
                    content = @Content(schema = @Schema(implementation = ReviewSchema.ReviewSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류로 모든 요청 필드들에 대해 유효성 검사를 진행한다." +
                            "요청 파라미터에 대해 형식을 지키지 않은 경우, 응답 Body data에 형식에 대한 에러메시지를 상세확인할 수 있다.",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema400M.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 분양후기를 찾을 수 없는 경우",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema404.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 분양후기 수정 불가능",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> update(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute ReviewDto.ReviewUpdateRequestDto request,
            Principal principal
    ) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return response.fail(HttpStatus.NOT_FOUND, "해당 ID의 분양후기를 찾을 수 없습니다.");
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member != null) {
            if (member.getRole().getTitle().equals("ADOPTER")) {

                int maxImageCount = 4;
                int currentImageCount = request.getReviewImgFiles().size();
                if (currentImageCount > maxImageCount) {
                    return response.fail(HttpStatus.BAD_REQUEST, "이미지는 최대 " + maxImageCount + "개까지 업로드 가능합니다.");
                }

                reviewService.update(request, review, (Adopter) member);
                return response.success(HttpStatus.OK, "수정이 성공적으로 이루어졌습니다.");
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    @DeleteMapping("/review/{reviewId}")
    @Operation(
            summary = "분양후기 삭제",
            description = "분양후기를 삭제할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양후기 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "없는 ID로 요청한 경우 404 반환",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema404.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 분양후기 삭제 불가능",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            )
    })
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Principal principal) {

        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return response.fail(HttpStatus.NOT_FOUND, "해당 ID의 분양후기를 찾을 수 없습니다.");
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member != null) {
            if (member.getRole().getTitle().equals("ADOPTER")) {

                reviewService.deleteReview(review, (Adopter) member);
                return response.success(HttpStatus.OK, "삭제가 성공적으로 이루어졌습니다.");
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }


    @GetMapping("/reviews")
    @Operation(
            summary = "로그인한 사용자의 분양후기 목록 API",
            description = "로그인한 사용자는 자신의 분양후기 목록을 확인할 수 있다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "분양후기 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ReviewSchema.ReviewsSchema200.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "분양희망자 회원이 아니거나 로그인이 필요한 경우 자신의 분양후기 목록 반환 실패",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema401.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ResponseSchema.ResponseSchema500.class))
            )
    })
    public ResponseEntity<?> getMyReviews(
            @PageableDefault(page = 0, size = 4) Pageable pageable,
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String keyword,
            Principal principal
    ) {
        if (principal == null) {
            throw new MissingPrincipalException();
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);

        if (member != null) {
            if (member.getRole().getTitle().equals("ADOPTER")) {
                Page<ReviewDto.ReviewListResponseDto> reviewsPage = reviewService.getMyReviews(member, pageable, searchType, keyword);
                return response.success(HttpStatus.OK, reviewsPage);
            } else {
                return response.fail(HttpStatus.FORBIDDEN, "분양희망자 회원이 아닙니다.");
            }
        } else {
            return response.fail(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
