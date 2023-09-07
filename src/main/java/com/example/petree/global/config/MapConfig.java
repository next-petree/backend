package com.example.petree.global.config;

import com.example.petree.domain.member.dto.MemberDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * packageName    : com.example.petree.global.config
 * fileName       : MapConfig
 * author         : 박수현
 * date           : 2023-07-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-18        박수현              최초 생성
 */

@Component
@Getter
public class MapConfig {

    private static String clientId;

    private static String clientSecret;

    private static String url;

    @Value("${map.naver.clientId}")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Value("${map.naver.clientSecret}")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Value("${map.naver.url}")
    public void setUrl(String url){
        this.url = url;
    }

    public static String getClientSecret(){
        return clientSecret;
    }

    public static String getUrl(){
        return url;
    }

    public static String getClientId(){
        return clientId;
    }

}
