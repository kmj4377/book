package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/usr/**")
                .excludePathPatterns(
                        // 회원 인증 없이 접근 허용되는 URL들
                        "/usr/member/login",
                        "/usr/member/doLogin",
                        "/usr/member/validLoginInfo",
                        "/usr/member/join",
                        "/usr/member/doJoin",
                        "/usr/member/loginIdDupChk",

                        // 🔥 이메일 인증 관련 (반드시 필요)
                        "/usr/member/sendEmailAuthCode",
                        "/usr/member/checkEmailAuthCode",

                        // 소셜 로그인 콜백
                        "/usr/member/kakao/callback",
                        "/usr/member/naver/callback",
                        "/usr/member/google/callback",
                        "/usr/member/logoutComplete",

                        // 정적 파일
                        "/usr/welcome/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",

                        // 오류 페이지
                        "/error"
                );
    }
}

