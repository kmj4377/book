package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // -------------------------------
    // 🔹 1) 네이버 Access Token 요청
    // -------------------------------
    public String getAccessToken(String code, String state) {

        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("state", state);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, params, Map.class);

        if (response.getBody() == null || response.getBody().get("access_token") == null) {
            throw new RuntimeException("네이버 Access Token 요청 실패");
        }

        return (String) response.getBody().get("access_token");
    }

    // -------------------------------
    // 🔹 2) Access Token으로 사용자 정보 조회
    // -------------------------------
    public Map<String, Object> getUserInfo(String accessToken) {

        String profileUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                profileUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getBody() == null || response.getBody().get("response") == null) {
            throw new RuntimeException("네이버 사용자 정보 조회 실패");
        }

        // ⭐ response 내부가 실제 사용자 정보
        Map<String, Object> user = (Map<String, Object>) response.getBody().get("response");


        return user;
    }

    // -------------------------------
    // 🔹 3) Controller에서 직접 쓰기 위한 통합 메서드
    // -------------------------------
    public Map<String, Object> getNaverUser(String code, String state) {

        String accessToken = getAccessToken(code, state);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        // ⭐ Controller에서는 userInfo만 반환하는 것이 핵심.
        //   MemberService가 그대로 name, email 등을 get() 할 수 있게.
        return userInfo;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
