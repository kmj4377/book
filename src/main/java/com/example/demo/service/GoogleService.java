package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleService {

    public Map<String, Object> getGoogleUser(String code) {

        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
        String redirectUri = "http://localhost:8081/usr/member/google/callback";

        // 1) Access Token 요청
        String tokenUrl = "https://oauth2.googleapis.com/token";

        RestTemplate rest = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        ResponseEntity<Map> tokenResponse =
                rest.postForEntity(tokenUrl, params, Map.class);

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2) 최신 Google People API 엔드포인트
        String userInfoUrl =
                "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> userResponse =
                rest.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        
        return userResponse.getBody();
    }
}

