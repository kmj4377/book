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

		String safeMsg = msg == null ? "" : msg.replace("'", "\\'");
		String safeUri = uri == null ? "/" : uri;

		return """
				<script>
				    const _msg = '%s';
				    if (_msg && _msg.length > 0) alert(_msg);
				    location.replace('%s');
				</script>
				""".formatted(safeMsg, safeUri);
	}

	public String jsHistoryBack(String msg) {

		String safeMsg = msg == null ? "" : msg.replace("'", "\\'");

		return """
				<script>
				    const _msg = '%s';
				    if (_msg && _msg.length > 0) alert(_msg);
				    history.back();
				</script>
				""".formatted(safeMsg);
	}

	public String getParam(String name) {
		return req.getParameter(name);
	}
}
