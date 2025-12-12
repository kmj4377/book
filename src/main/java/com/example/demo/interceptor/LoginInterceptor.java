package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.dto.LoginedMember;
import com.example.demo.util.Util;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 로그인 없이 허용
        if (uri.startsWith("/usr/member/login") ||
            uri.startsWith("/usr/member/doLogin") ||
            uri.startsWith("/usr/member/join") ||
            uri.startsWith("/usr/member/doJoin") ||
            uri.startsWith("/usr/member/emailAuth") ||
            uri.startsWith("/usr/member/sendEmailAuthCode") ||
            uri.startsWith("/usr/member/checkEmailAuthCode") ||
            uri.equals("/")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        Object loginedMember = (session != null)
                ? session.getAttribute("loginedMember")
                : null;

        if (loginedMember == null) {

            // ⭐ AJAX 요청이면 JSON만 내려보낸다
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                    "{\"resultCode\":\"F-LOGIN\",\"msg\":\"로그인이 필요합니다.\"}"
                );
                return false;
            }

            // ⭐ 일반 페이지 요청만 jsReplace
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(
                Util.jsReplace("로그인이 필요합니다.", "/usr/member/login")
            );
            return false;
        }

        return true;
    }
}

