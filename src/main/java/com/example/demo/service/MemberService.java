package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final JavaMailSender mailSender;

    private final Map<String, EmailAuthInfo> emailAuthStore = new ConcurrentHashMap<>();


    // ------------------ 로그인 아이디로 회원 조회 ------------------
    public Member getMemberByLoginId(String loginId) {
        return memberDao.getMemberByLoginId(loginId);
    }


    // ------------------ 이메일 인증코드 발송 (🔥 여기서부터 제한) ------------------
    public ResultData sendEmailAuthCode(String email) {

        // 🔥 이메일 3개 제한 체크
        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            return ResultData.from("F-EMAIL", "해당 이메일로는 최대 3개의 계정만 생성 가능합니다.");
        }

        String authCode = UUID.randomUUID().toString().substring(0, 8);

        emailAuthStore.put(email, new EmailAuthInfo(authCode, LocalDateTime.now().plusMinutes(5)));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[가계부] 이메일 인증 코드 안내");
            helper.setText("<h3>인증코드</h3><p><b>" + authCode + "</b></p><p>5분 내 입력해주세요.</p>", true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.from("F-1", "이메일 발송 중 오류가 발생했습니다.");
        }

        return ResultData.from("S-1", "인증코드가 이메일로 발송되었습니다.");
    }


    // ------------------ 인증코드 확인 (🔥 인증도 차단) ------------------
    public ResultData checkEmailAuthCode(String email, String code) {

        // 🔥 이메일 3개 제한 체크
        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            return ResultData.from("F-EMAIL", "이미 해당 이메일로 가입된 계정이 3개입니다.");
        }

        EmailAuthInfo info = emailAuthStore.get(email);

        if (info == null)
            return ResultData.from("F-1", "인증코드를 요청한 기록이 없습니다.");

        if (LocalDateTime.now().isAfter(info.expireTime))
            return ResultData.from("F-2", "인증코드가 만료되었습니다.");

        if (!info.code.equals(code))
            return ResultData.from("F-3", "인증코드가 일치하지 않습니다.");

        info.authed = true;
        return ResultData.from("S-1", "이메일 인증이 완료되었습니다.");
    }


    // ------------------ 일반 회원가입 (🔥 최종 안전장치) ------------------
    @Transactional
    public void joinMember(String loginId, String loginPw, String name, String nickname, String email) {

        // 🔥 같은 이메일 3개 제한
        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            throw new IllegalArgumentException("해당 이메일로는 더 이상 계정을 생성할 수 없습니다. (최대 3개)");
        }

        EmailAuthInfo info = emailAuthStore.get(email);

        if (info == null)
            throw new IllegalArgumentException("이메일 인증을 요청해주세요.");

        if (LocalDateTime.now().isAfter(info.expireTime))
            throw new IllegalArgumentException("이메일 인증 시간이 만료되었습니다. 다시 인증해주세요.");

        if (!info.authed)
            throw new IllegalArgumentException("이메일 인증을 완료해주세요.");

        // 가입 처리
        memberDao.joinMember(loginId, loginPw, name, nickname, email);

        // 인증 정보 삭제
        emailAuthStore.remove(email);
    }


    // ------------------ 카카오 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinKakao(Map<String, Object> kakaoUser) {

        Long kakaoId = ((Number) kakaoUser.get("id")).longValue();
        Map<String, Object> account = (Map<String, Object>) kakaoUser.get("kakao_account");
        Map<String, Object> profile = account != null ? (Map<String, Object>) account.get("profile") : null;

        String email = account != null ? (String) account.get("email") : null;

        if (email == null) {
            email = "kakao_" + kakaoId + "@no-email.com";
        }

        String nickname = profile != null ? (String) profile.get("nickname") : ("kakao_" + kakaoId);

        Member member = memberDao.getMemberByKakaoId(kakaoId);

        if (member == null) {
            nickname = makeUniqueNickname(nickname);
            String loginId = "kakao_" + kakaoId;
            String name = nickname;

            memberDao.joinKakaoMember(loginId, name, nickname, email, kakaoId);
            member = memberDao.getMemberByKakaoId(kakaoId);
        }

        return new LoginedMember(member);
    }


    // ------------------ 네이버 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinNaver(Map<String, Object> naverUser) {

        String naverId = (String) naverUser.get("id");
        String name = (String) naverUser.get("name");
        String email = (String) naverUser.get("email");
        String nickname = (String) naverUser.get("nickname");

        if (name == null || name.isBlank()) name = "네이버사용자";
        if (nickname == null || nickname.isBlank()) nickname = name;
        if (email == null) email = "naver_" + naverId + "@no-email.com";

        Member member = memberDao.getMemberByNaverId(naverId);

        if (member == null) {
            String loginId = "naver_" + naverId;

            nickname = makeUniqueNickname(nickname);

            memberDao.insertNaverMember(loginId, name, nickname, email, naverId);
            member = memberDao.getMemberByNaverId(naverId);
        }

        return new LoginedMember(member);
    }


    // ------------------ 구글 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinGoogle(Map<String, Object> googleUser) {

        String googleId = (String) googleUser.get("sub");
        String name = (String) googleUser.get("name");
        String email = (String) googleUser.get("email");

        if (name == null) name = "googleUser";
        if (email == null) email = "google_" + googleId + "@no-email.com";

        Member member = memberDao.getMemberByGoogleId(googleId);

        if (member == null) {
            String loginId = "google_" + googleId;
            String nickname = makeUniqueNickname(name);

            memberDao.insertGoogleMember(loginId, name, nickname, email, googleId);
            member = memberDao.getMemberByGoogleId(googleId);
        }

        return new LoginedMember(member);
    }


    // ------------------ 닉네임 중복 방지 ------------------
    private String makeUniqueNickname(String baseNickname) {
        String nickname = baseNickname;
        int suffix = 1;

        while (memberDao.getMemberByNickname(nickname) != null) {
            nickname = baseNickname + "_" + suffix++;
        }

        return nickname;
    }


    private static class EmailAuthInfo {
        final String code;
        final LocalDateTime expireTime;
        boolean authed;

        EmailAuthInfo(String code, LocalDateTime expireTime) {
            this.code = code;
            this.expireTime = expireTime;
            this.authed = false;
        }
    }
}
