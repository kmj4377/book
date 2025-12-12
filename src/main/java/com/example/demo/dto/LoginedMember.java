package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginedMember {

    private int id;
    private int authLevel;
    private String name;
    private String loginId;

    // 소셜 로그인 식별자
    private Long kakaoId;
    private String naverId;
    private String googleId;

    // Member → LoginedMember 변환 생성자
    public LoginedMember(Member member) {
        this.id = member.getId();
        this.authLevel = member.getAuthLevel();
        this.name = member.getName();
        this.loginId = member.getLoginId();
        this.kakaoId = member.getKakaoId();
        this.naverId = member.getNaverId();
        this.googleId = member.getGoogleId();
    }

    /**
     * ⭐ 회원 인증 유형
     * JSP에서는 ${sessionScope.loginedMember.memberAuthType} 로 접근
     */
    public String getMemberAuthType() {
        if (kakaoId != null) {
            return "KAKAO";
        }
        if (naverId != null) {
            return "NAVER";
        }
        if (googleId != null) {
            return "GOOGLE";
        }
        return "LOCAL";
    }
}
