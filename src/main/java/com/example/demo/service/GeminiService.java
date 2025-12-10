package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.url}")
    private String geminiApiUrl;

    public String categoryFromMemo(String memoText) {

        RestTemplate rest = new RestTemplate();

        // ---------------------
        // 요청 JSON 만들기
        // ---------------------
        Map<String, Object> content = new HashMap<>();
        content.put("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text",
                                "다음 메모를 보고 지출 카테고리를 하나만 추천해줘. " +
                                "[식비, 교통비, 쇼핑, 생활비, 기타] 중에서 하나만 답해. 메모: " + memoText
                        )
                )))
        );

        // ---------------------
        // 헤더 설정
        // ---------------------
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Authorization 헤더 추가 (선택이지만 대부분의 환경에서 안전)
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(content, headers);

        try {
            ResponseEntity<Map> response = rest.exchange(
                    geminiApiUrl,  // key 포함된 URL
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // ---------------------
            // 응답 파싱
            // ---------------------
            Map candidate = (Map) ((List) response.getBody().get("candidates")).get(0);
            Map contentObj = (Map) candidate.get("content");
            List<Map> parts = (List<Map>) contentObj.get("parts");

            return parts.get(0).get("text").toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "기타";
        }
    }
}
