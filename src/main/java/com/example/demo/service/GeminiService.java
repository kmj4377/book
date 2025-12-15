package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.url}")
    private String geminiApiUrl;

    /**
     * 🔥 메모 → subCategoryId 자동 추천
     */
    public int subCategoryIdFromMemo(String memoText) {

        RestTemplate rest = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text",
                                """
                                다음 지출 메모를 보고 가장 적절한 하위 카테고리를 하나만 골라라.
                                아래 목록 중에서 정확히 하나만 답해라.

                                [택시, 지하철, 버스, 외식, 카페, 마트, 쇼핑, 통신비, 공과금, 기타]

                                메모: %s
                                """.formatted(memoText)
                        )
                ))
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = rest.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map candidate =
                    (Map) ((List) response.getBody().get("candidates")).get(0);
            Map content =
                    (Map) candidate.get("content");
            List<Map> parts =
                    (List<Map>) content.get("parts");

            String result = parts.get(0).get("text").toString().trim();

            return mapSubCategoryNameToId(result);

        } catch (HttpClientErrorException e) {
            return getDefaultSubCategoryId(); // 기타
        }
    }

    /* =========================
       🔥 하위 카테고리 매핑
    ========================= */

    private int mapSubCategoryNameToId(String name) {

        return switch (name) {
            case "택시" -> 5;
            case "지하철" -> 4;
            case "버스" -> 3;
            case "외식" -> 1;
            case "카페" -> 2;
            case "마트" -> 6;
            case "쇼핑" -> 7;
            case "통신비" -> 8;
            case "공과금" -> 9;
            default -> getDefaultSubCategoryId(); // 기타
        };
    }

    private int getDefaultSubCategoryId() {
        return 10; // 기타
    }
}
