package com.example.demo.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@SessionScope
@RequiredArgsConstructor
public class Req {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;

    // 세션에서 항상 가져오도록 변경
    public LoginedMember getLoginedMember() {
        return (LoginedMember) session.getAttribute("loginedMember");
    }

    public int getLoginedMemberId() {
        LoginedMember member = getLoginedMember();
        return member != null ? member.getId() : 0;
    }

    public int getLoginedMemberAuthLevel() {
        LoginedMember member = getLoginedMember();
        return member != null ? member.getAuthLevel() : 0;
    }

    public boolean isLogined() {
        return getLoginedMember() != null;
    }

    public void login(LoginedMember member) {
        session.setAttribute("loginedMember", member);
        session.setAttribute("loginedMemberId", member.getId());
        session.setAttribute("loginedMemberAuthLevel", member.getAuthLevel());
    }

    public void logout() {
        session.invalidate();
    }

    public String jsReplace(String msg, String uri) {
        return """
                <script>
                    alert('%s');
                    location.replace('%s');
                </script>
                """.formatted(msg, uri);
    }

    public String jsHistoryBack(String msg) {
        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
    }

    public String getParam(String name) {
        return req.getParameter(name);
    }
}
