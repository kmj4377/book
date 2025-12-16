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

		registry.addInterceptor(loginInterceptor).addPathPatterns("/usr/**").excludePathPatterns("/usr/member/login",
				"/usr/member/doLogin", "/usr/member/validLoginInfo", "/usr/member/join", "/usr/member/doJoin",
				"/usr/member/loginIdDupChk", "/usr/member/emailAuth",

				"/usr/member/sendEmailAuthCode", "/usr/member/checkEmailAuthCode", "/usr/member/sendEmailAuthCode",
				"/usr/member/checkEmailAuthCode",

				"/usr/member/kakao/callback", "/usr/member/naver/callback", "/usr/member/google/callback",
				"/usr/member/logoutComplete",

				"/usr/welcome/**", "/static/**", "/css/**", "/js/**", "/img/**",

				"/error");
	}
}
