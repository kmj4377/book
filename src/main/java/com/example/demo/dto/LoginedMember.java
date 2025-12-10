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
    private Long kakaoId;  
    private String naverId;
    private String googleId;

    public LoginedMember(Member member) {
        this.id = member.getId();
        this.authLevel = member.getAuthLevel();
        this.name = member.getName();
        this.loginId = member.getLoginId();
        this.kakaoId = member.getKakaoId();
        this.naverId = member.getNaverId();
        this.googleId = member.getGoogleId();    
    }
}
