package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.rest-api-key}")
    private String REST_API_KEY;

    @Value("${kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;

    public String getAccessToken(String code) {

        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("access_token")) {
                return (String) body.get("access_token");
            }
            throw new RuntimeException("카카오 Access Token 응답 오류: " + body);

        } catch (Exception e) {
            throw new RuntimeException("카카오 Access Token 발급 실패", e);
        }
    }

    public Map<String, Object> getKakaoUser(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("카카오 사용자 정보 없음");

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }
}

