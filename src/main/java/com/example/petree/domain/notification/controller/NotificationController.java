package com.example.petree.domain.notification.controller;

import com.example.petree.domain.matching.schema.PostResultSchema;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.repository.MemberRepository;
import com.example.petree.domain.notification.domain.Notification;
import com.example.petree.domain.notification.dto.NotificationDto;
import com.example.petree.domain.notification.schema.NotificationsSchema;
import com.example.petree.domain.notification.schema.PutResultSchema;
import com.example.petree.domain.notification.service.NotificationService;
import com.example.petree.global.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.notification.controller
 * fileName       : NotificationController
 * author         : 정세창
 * date           : 2023/07/17
 * description    : 알림 관련 컨트롤러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/17         정세창              init
 */
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/notifications")
//@Tag(name = "강아지 분양하기", description = "강아지 분양 관련 API")
//public class NotificationController {
//
//    private final Response response;
//    private final MemberRepository memberRepository;
//    private final NotificationService notificationService;
//
//    @GetMapping
//    @Operation(        // swagger annotation
//            summary = "알림 리스트 조회",
//            description = "현재 회원의 숨김 처리하지 않은 알림 리스트를 조회한다."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "알림 리스트 조회 성공",
//                    content = @Content(schema = @Schema(implementation = NotificationsSchema.NotificationSchema200.class)))
//    })
//    public ResponseEntity<?> getNotifications(Principal principal) {
//        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
//        List<NotificationDto> notifications = notificationService.getNotifications(member);
//
//        return response.success(HttpStatus.OK, notifications);
//    }
//
//    @PutMapping("/{id}")
//    @Operation(        // swagger annotation
//            summary = "특정 알림 숨김 처리",
//            description = "해당 알림을 숨김 처리한다."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "알림 숨김 처리 성공",
//                    content = @Content(schema = @Schema(implementation = PutResultSchema.class))),
//            @ApiResponse(responseCode = "500", description = "엔티티 조회 실패",
//                    content = @Content(schema = @Schema(implementation = PutResultSchema.class)))
//    })
//    public ResponseEntity<?> changeNotificationStatus(@PathVariable("id") Long id) {
//
//        notificationService.changeNotificationStatus(id);
//
//        return response.success(HttpStatus.OK);
//    }
//}
