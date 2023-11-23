package com.example.petree.domain.member.service;

import com.example.petree.domain.member.dto.MessageDto;
import com.example.petree.domain.member.dto.SmsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * packageName    : com.example.petree.domain.member.service
 * fileName       : SmsService
 * author         : 박수현
 * date           : 2023-08-09
 * description    : 문자인증
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-09        박수현              최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    @Value("${sms.naver.cloud.accessKey}")
    private String accessKey;

    @Value("${sms.naver.cloud.secretKey}")
    private String secretKey;

    @Value("${sms.naver.cloud.serviceId}")
    private String serviceId;

    @Value("${sms.naver.cloud.senderPhone}")
    private String phone;

    private final RedisService redisService;

    /**
     * @author 박수현
     * @date 2023-08-10
     * @description : 레디스에 저장된 인증코드와의 일치 유무 확인
     * @return
     */

    public boolean verifySmsCode(String phoneNumber, String code) {
        String formattedPhoneNumber = phoneNumber.replaceAll("-", "");
        String redisKey = formattedPhoneNumber;
        String storedCode = redisService.getVerificationCode(redisKey);
        return storedCode != null && storedCode.equals(code);
    }

    /**
     * @author 박수현
     * @date 2023-08-10
     * @description : 암호화가 필요한 헤더 구성
     * @return
     */

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    /**
     * @author 박수현
     * @date 2023-08-10
     * @description : 메시지 발송
     * @return
     */

    public SmsDto.SmsResponseDto sendSms(MessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        //-제거
        String formattedPhoneNumber = messageDto.getTo().replaceAll("-", "");
        messageDto.setTo(formattedPhoneNumber);

        // 랜덤한 인증코드 생성
        String smsConfirmNum = createSmsKey();

        // 유효 기간을 현재 시간에서 3분 추가하여 설정
        Instant expirationTime = Instant.now().plusSeconds(3 * 60); // 현재 시간 + 3분


        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        SmsDto.SmsRequestDto request = SmsDto.SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                //.content(messageDto.getContent())
                .content("[Petree] 인증번호 [" + smsConfirmNum + "]를 입력해주세요")
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsDto.SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsDto.SmsResponseDto.class);
        response.setSmsConfirmNum(smsConfirmNum);
        String redisKey = formattedPhoneNumber;
        log.info("redisKey : " + redisKey);
        redisService.setVerificationCode(redisKey, smsConfirmNum, expirationTime);
        return response;
    }

    /**
     * @author 박수현
     * @date 2023-08-23
     * @description : 인증코드 만들기
     * @return
     */

    public static String createSmsKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 5; i++) { // 인증코드 5자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}
