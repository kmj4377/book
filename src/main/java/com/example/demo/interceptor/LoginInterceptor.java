package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.util.Util;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();

        if (uri.startsWith("/usr/member/login") ||
            uri.startsWith("/usr/member/doLogin") ||
            uri.startsWith("/usr/member/join") ||
            uri.startsWith("/usr/member/doJoin") ||
            uri.equals("/")
        ) {
            return true;
        }

        HttpSession session = request.getSession();
        boolean isLogined = session.getAttribute("loginedMemberId") != null;

        if (!isLogined) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().append(
                Util.jsReplace("로그인이 필요합니다.", "/usr/member/login")
            );
            return false;
        }

        return true;
    }
}


