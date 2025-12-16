package com.example.demo.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Member;
import com.example.demo.dto.Req;
import com.example.demo.dto.ResultData;
import com.example.demo.service.GoogleService;
import com.example.demo.service.KakaoService;
import com.example.demo.service.MemberService;
import com.example.demo.service.NaverService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usr/member")
public class UsrMemberController {

	private final MemberService memberService;
	private final Req req;
	private final KakaoService kakaoService;
	private final NaverService naverService;
	private final GoogleService googleService;

	public UsrMemberController(MemberService memberService, Req req, KakaoService kakaoService,
			NaverService naverService, GoogleService googleService) {

		this.memberService = memberService;
		this.req = req;
		this.kakaoService = kakaoService;
		this.naverService = naverService;
		this.googleService = googleService;
	}

	@GetMapping("/join")
	public String join() {
		return "usr/member/join";
	}

	@PostMapping("/sendEmailAuthCode")
	@ResponseBody
	public ResultData sendEmailAuthCode(String email) {
		return memberService.sendEmailAuthCode(email);
	}

	@PostMapping("/checkEmailAuthCode")
	@ResponseBody
	public ResultData checkEmailAuthCode(String email, String code) {
		return memberService.checkEmailAuthCode(email, code);
	}

	@PostMapping("/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String loginPwChk, String name, String nickname, String email,
			String emailAuthed) {

		if (!loginPw.equals(loginPwChk)) {
			return req.jsHistoryBack("비밀번호 확인이 일치하지 않습니다.");
		}

		if (!"1".equals(emailAuthed)) {
			return req.jsHistoryBack("이메일 인증을 완료해주세요.");
		}

		memberService.joinMember(loginId, loginPw, name, nickname, email);

		return req.jsReplace(loginId + "님의 가입이 완료되었습니다.", "/usr/member/login");
	}

	@GetMapping("/login")
	public String login(Model model, HttpSession session) {

		String kakaoRestKey = System.getenv("KAKAO_REST_API_KEY");
		String kakaoRedirectUri = "http://localhost:8081/usr/member/kakao/callback";

		String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize" + "?client_id=" + kakaoRestKey
				+ "&redirect_uri=" + kakaoRedirectUri + "&response_type=code" + "&prompt=login";

		model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);

		String naverClientId = System.getenv("NAVER_CLIENT_ID");
		String naverRedirectUri = "http://localhost:8081/usr/member/naver/callback";

		String state = "NAVER_LOGIN_" + System.currentTimeMillis();
		session.setAttribute("naverState", state);

		String encodedRedirectUri = URLEncoder.encode(naverRedirectUri, StandardCharsets.UTF_8);

		String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize" + "?response_type=code" + "&client_id="
				+ naverClientId + "&redirect_uri=" + encodedRedirectUri + "&state=" + state + "&auth_type=reprompt";

		model.addAttribute("naverLoginUrl", naverLoginUrl);

		String googleClientId = System.getenv("GOOGLE_CLIENT_ID");
		String googleRedirectUri = "http://localhost:8081/usr/member/google/callback";

		String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + googleClientId
				+ "&redirect_uri=" + googleRedirectUri + "&response_type=code" + "&scope=openid%20email%20profile"
				+ "&access_type=offline" + "&prompt=login";

		model.addAttribute("googleLoginUrl", googleLoginUrl);

		return "usr/member/login";
	}

	@PostMapping("/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw) {

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null)
			return req.jsHistoryBack("존재하지 않는 아이디입니다.");
		if (!member.getLoginPw().equals(loginPw))
			return req.jsHistoryBack("비밀번호가 일치하지 않습니다.");

		memberService.updateLastLoginAt(member.getId());

		Member updatedMember = memberService.getMemberById(member.getId());

		LoginedMember loginedMember = new LoginedMember(updatedMember);
		req.login(loginedMember);

		return req.jsReplace(updatedMember.getLoginId() + "님 환영합니다!", "/usr/home/main");
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

	    session.invalidate();

	    return "redirect:/usr/member/login";
	}


	@GetMapping("/logoutComplete")
	public String logoutComplete() {
		return "usr/welcome/index";
	}

	@GetMapping("/kakao/callback")
	public String kakaoCallback(@RequestParam String code, HttpSession session) {

		try {
			String accessToken = kakaoService.getAccessToken(code);
			Map<String, Object> kakaoUser = kakaoService.getKakaoUser(accessToken);

			LoginedMember loginedMember = memberService.loginOrJoinKakao(kakaoUser);

			req.login(loginedMember);
			session.setAttribute("loginedMember", loginedMember);

			return "redirect:/usr/home/main";

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/usr/member/login?msg=kakao-login-failed";
		}
	}

	@GetMapping("/naver/callback")
	public String naverCallback(@RequestParam String code, @RequestParam String state, HttpSession session) {

		// 1️⃣ state 검증
		String sessionState = (String) session.getAttribute("naverState");
		if (sessionState == null || !sessionState.equals(state)) {
			return "redirect:/usr/member/login?msg=naver-state-mismatch";
		}

		try {
			Map<String, Object> naverUser = naverService.getNaverUser(code, state);

			LoginedMember loginedMember = memberService.loginOrJoinNaver(naverUser);

			req.login(loginedMember);

			String redirectUri = (String) session.getAttribute("afterLoginRedirectUri");

			if (redirectUri != null) {
				session.removeAttribute("afterLoginRedirectUri");
				return "redirect:" + redirectUri;
			}

			return "redirect:/usr/home/main";

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/usr/member/login?msg=naver-login-failed";
		}
	}

	@GetMapping("/google/callback")
	public String googleCallback(@RequestParam String code, HttpSession session) {

		try {
			Map<String, Object> googleUser = googleService.getGoogleUser(code);

			LoginedMember loginedMember = memberService.loginOrJoinGoogle(googleUser);

			req.login(loginedMember);
			session.setAttribute("loginedMember", loginedMember);

			return "redirect:/usr/home/main";

		} catch (Exception e) {
			return "redirect:/usr/member/login?msg=google-login-failed";
		}
	}

	@GetMapping("/loginIdDupChk")
	@ResponseBody
	public ResultData loginIdDupChk(String loginId) {

		if (loginId == null || loginId.trim().length() == 0) {
			return ResultData.from("F-1", "아이디를 입력해주세요.");
		}

		Member member = memberService.getMemberByLoginId(loginId);

		if (member != null) {
			return ResultData.from("F-2", "이미 존재하는 아이디입니다.");
		}

		return ResultData.from("S-1", "사용 가능한 아이디입니다.");
	}

	@GetMapping("/mypage")
	public String myPage(Model model) {

		LoginedMember loginedMember = req.getLoginedMember();

		if (loginedMember == null) {
			return "redirect:/usr/member/login";
		}

		Member member = memberService.getMemberById(loginedMember.getId());

		model.addAttribute("member", member);

		return "usr/member/mypage";
	}

	@GetMapping("/edit")
	public String edit(Model model) {

		LoginedMember loginedMember = req.getLoginedMember();
		if (loginedMember == null) {
			return "redirect:/usr/member/login";
		}

		Member member = memberService.getMemberById(loginedMember.getId());
		model.addAttribute("member", member);

		return "usr/member/edit";
	}

	@PostMapping("/doEdit")
	@ResponseBody
	public String doEdit(String name, String nickname, String email) {

		LoginedMember loginedMember = req.getLoginedMember();
		if (loginedMember == null) {
			return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");
		}

		if (name == null || name.trim().isEmpty()) {
			return req.jsHistoryBack("이름은 필수 입력입니다.");
		}

		if (nickname == null || nickname.trim().isEmpty()) {
			return req.jsHistoryBack("닉네임은 필수 입력입니다.");
		}

		memberService.updateMemberInfo(loginedMember.getId(), name.trim(), nickname.trim(),
				email != null ? email.trim() : null);

		Member updatedMember = memberService.getMemberById(loginedMember.getId());

		req.login(new LoginedMember(updatedMember));

		return req.jsReplace("정보가 수정되었습니다.", "/usr/member/mypage");
	}

	@GetMapping("/password")
	public String password() {

		LoginedMember loginedMember = req.getLoginedMember();
		if (loginedMember == null) {
			return "redirect:/usr/member/login";
		}

		if (loginedMember.getKakaoId() != null || loginedMember.getNaverId() != null
				|| loginedMember.getGoogleId() != null) {

			return "redirect:/usr/member/mypage";
		}

		return "usr/member/password";
	}

	@PostMapping("/doChangePassword")
	@ResponseBody
	public String doChangePassword(String oldPw, String newPw, String newPwChk) {

		LoginedMember loginedMember = req.getLoginedMember();
		if (loginedMember == null) {
			return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");
		}

		if (oldPw == null || newPw == null || newPwChk == null || oldPw.isBlank() || newPw.isBlank()
				|| newPwChk.isBlank()) {
			return req.jsHistoryBack("비밀번호를 모두 입력해주세요.");
		}

		if (!newPw.equals(newPwChk)) {
			return req.jsHistoryBack("새 비밀번호가 일치하지 않습니다.");
		}

		try {
			memberService.changePassword(loginedMember.getId(), oldPw, newPw);
		} catch (IllegalArgumentException e) {
			return req.jsHistoryBack(e.getMessage());
		}

		return req.jsReplace("비밀번호가 변경되었습니다.", "/usr/member/mypage");
	}

	@GetMapping("/emailAuth")
	public String emailAuth(Model model) {

		LoginedMember loginedMember = req.getLoginedMember();
		if (loginedMember == null) {
			return "redirect:/usr/member/login";
		}

		Member member = memberService.getMemberById(loginedMember.getId());
		model.addAttribute("email", member.getEmail());

		return "usr/member/emailAuth";
	}

}
