package com.example.demo.controller;

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

    public UsrMemberController(MemberService memberService,
                               Req req,
                               KakaoService kakaoService,
                               NaverService naverService,
                               GoogleService googleService) {

        this.memberService = memberService;
        this.req = req;
        this.kakaoService = kakaoService;
        this.naverService = naverService;
        this.googleService = googleService;
    }

    // ------------------ 회원가입 페이지 ------------------
    @GetMapping("/join")
    public String join() {
        return "usr/member/join";
    }

    // ------------------ 회원가입 처리 ------------------
    @PostMapping("/doJoin")
    @ResponseBody
    public String doJoin(String loginId, String loginPw, String loginPwChk,
                         String name, String nickname) {

        if (!loginPw.equals(loginPwChk)) {
            return req.jsHistoryBack("비밀번호 확인이 일치하지 않습니다.");
        }

        try {
            memberService.joinMember(loginId, loginPw, name, nickname);
        } catch (IllegalArgumentException e) {
            return req.jsHistoryBack(e.getMessage());
        }

        return req.jsReplace(loginId + "님의 가입이 완료되었습니다.", "/usr/member/login");
    }

    // ------------------ 로그인 페이지 ------------------
    @GetMapping("/login")
    public String login(Model model) {

        // 카카오 로그인 URL 데이터 전달
        String kakaoRestKey = System.getenv("KAKAO_REST_API_KEY");
        String kakaoRedirectUri = "http://localhost:8081/usr/member/kakao/callback";
        model.addAttribute("kakaoRestKey", kakaoRestKey);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);

        // 네이버 로그인 URL 전달
        String naverClientId = System.getenv("NAVER_CLIENT_ID");
        String naverRedirectUri = "http://localhost:8081/usr/member/naver/callback";
        String state = "NAVER_LOGIN_STATE";

        String naverLoginUrl =
                "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                        + "&client_id=" + naverClientId
                        + "&redirect_uri=" + naverRedirectUri
                        + "&state=" + state;

        model.addAttribute("naverLoginUrl", naverLoginUrl);

        // 구글 로그인 URL 전달
        String googleClientId = System.getenv("GOOGLE_CLIENT_ID");
        String googleRedirectUri = "http://localhost:8081/usr/member/google/callback";

        String googleLoginUrl =
                "https://accounts.google.com/o/oauth2/v2/auth"
                        + "?client_id=" + googleClientId
                        + "&redirect_uri=" + googleRedirectUri
                        + "&response_type=code"
                        + "&scope=openid%20email%20profile"
                        + "&access_type=offline"
                        + "&prompt=login";

        model.addAttribute("googleLoginUrl", googleLoginUrl);

        return "usr/member/login";
    }

    // ------------------ 일반 로그인 처리 ------------------
    @PostMapping("/doLogin")
    @ResponseBody
    public String doLogin(String loginId, String loginPw) {

        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null)
            return req.jsHistoryBack("존재하지 않는 아이디입니다.");
        if (!member.getLoginPw().equals(loginPw))
            return req.jsHistoryBack("비밀번호가 일치하지 않습니다.");

        LoginedMember loginedMember = new LoginedMember(member);
        req.login(loginedMember);

        return req.jsReplace(member.getLoginId() + "님 환영합니다!", "/usr/home/main");
    }

    // ------------------ 로그아웃 ------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        LoginedMember loginedMember = (LoginedMember) session.getAttribute("loginedMember");

        if (loginedMember == null)
            return "redirect:/usr/member/login";

        req.logout();

        // 카카오 로그아웃 처리
        if (loginedMember.getKakaoId() != null) {
            String clientId = System.getenv("KAKAO_REST_API_KEY");
            String logoutRedirectUri = "http://localhost:8081/usr/member/logoutComplete";

            return "redirect:https://kauth.kakao.com/oauth/logout"
                    + "?client_id=" + clientId
                    + "&logout_redirect_uri=" + logoutRedirectUri;
        }

        return "redirect:/usr/member/logoutComplete";
    }

    // ------------------ 로그아웃 완료 화면 ------------------
    @GetMapping("/logoutComplete")
    public String logoutComplete() {
        return "usr/welcome/index";
    }

    // ------------------ 카카오 Callback ------------------
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
            return "redirect:/usr/member/login?msg=kakao-login-failed";
        }
    }

    // ------------------ 네이버 Callback ------------------
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam String code,
                                @RequestParam String state,
                                HttpSession session) {
        try {
            Map<String, Object> naverUser = naverService.getNaverUser(code, state);

            LoginedMember loginedMember = memberService.loginOrJoinNaver(naverUser);

            req.login(loginedMember);
            session.setAttribute("loginedMember", loginedMember);

            return "redirect:/usr/home/main";

        } catch (Exception e) {
            return "redirect:/usr/member/login?msg=naver-login-failed";
        }
    }

    // ------------------ 구글 Callback ------------------
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

    // ------------------ 아이디 중복 체크 ------------------
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
}
