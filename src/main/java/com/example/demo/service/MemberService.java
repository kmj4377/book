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

    /**
     * 이메일 인증 임시 저장소 (메일 인증용)
     * key: email
     */
    private final Map<String, EmailAuthInfo> emailAuthStore = new ConcurrentHashMap<>();

    /* ======================
       회원 조회
    ====================== */

    public Member getMemberByLoginId(String loginId) {
        return memberDao.getMemberByLoginId(loginId);
    }

    public Member getMemberById(int id) {
        return memberDao.getMemberById(id);
    }

    /* ======================
       이메일 인증 - 코드 발송
    ====================== */

    public ResultData sendEmailAuthCode(String email) {

        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            return ResultData.from("F-EMAIL", "해당 이메일로는 최대 3개의 계정만 생성 가능합니다.");
        }

        String authCode = UUID.randomUUID().toString().substring(0, 8);

        emailAuthStore.put(
                email,
                new EmailAuthInfo(authCode, LocalDateTime.now().plusMinutes(5))
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[가계부] 이메일 인증 코드");
            helper.setText(
                    "<h3>이메일 인증 코드</h3>" +
                    "<p><b>" + authCode + "</b></p>" +
                    "<p>5분 내에 입력해주세요.</p>",
                    true
            );

            mailSender.send(message);

        } catch (Exception e) {
            return ResultData.from("F-1", "이메일 발송 중 오류가 발생했습니다.");
        }

        return ResultData.from("S-1", "인증코드가 이메일로 발송되었습니다.");
    }

    /* ======================
       이메일 인증 - 코드 확인
       ⭐ DB 인증 반영 핵심
    ====================== */

    @Transactional
    public ResultData checkEmailAuthCode(String email, String code) {

        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            return ResultData.from("F-EMAIL", "이미 해당 이메일로 가입된 계정이 3개입니다.");
        }

        EmailAuthInfo info = emailAuthStore.get(email);

        if (info == null) {
            return ResultData.from("F-1", "인증코드를 요청한 기록이 없습니다.");
        }

        if (LocalDateTime.now().isAfter(info.expireTime)) {
            emailAuthStore.remove(email);
            return ResultData.from("F-2", "인증코드가 만료되었습니다.");
        }

        if (!info.code.equals(code)) {
            return ResultData.from("F-3", "인증코드가 일치하지 않습니다.");
        }

        // ✅ DB에 이메일 인증 상태 반영
        memberDao.setEmailVerifiedByEmail(email);

        // ✅ 인증 정보 제거
        emailAuthStore.remove(email);

        return ResultData.from("S-1", "이메일 인증이 완료되었습니다.");
    }

    /* ======================
       회원가입
    ====================== */

    @Transactional
    public void joinMember(String loginId, String loginPw,
                           String name, String nickname, String email) {

        int emailCount = memberDao.getCountByEmail(email);
        if (emailCount >= 3) {
            throw new IllegalArgumentException(
                    "해당 이메일로는 더 이상 계정을 생성할 수 없습니다."
            );
        }

        EmailAuthInfo info = emailAuthStore.get(email);

        if (info == null || !info.authed) {
            throw new IllegalArgumentException("이메일 인증을 완료해주세요.");
        }

        if (LocalDateTime.now().isAfter(info.expireTime)) {
            emailAuthStore.remove(email);
            throw new IllegalArgumentException("이메일 인증 시간이 만료되었습니다.");
        }

        memberDao.joinMember(loginId, loginPw, name, nickname, email);

        // ✅ 가입과 동시에 이메일 인증 처리
        memberDao.setEmailVerifiedByEmail(email);

        emailAuthStore.remove(email);
    }

    /* ======================
       내 정보 수정
    ====================== */

    @Transactional
    public void updateMemberInfo(int memberId,
                                 String name,
                                 String nickname,
                                 String email) {

        Member exist = memberDao.getMemberByNickname(nickname);
        if (exist != null && exist.getId() != memberId) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        memberDao.updateMemberInfo(memberId, name, nickname, email);
    }

    /* ======================
       비밀번호 변경
    ====================== */

    @Transactional
    public void changePassword(int memberId,
                               String oldPw,
                               String newPw) {

        Member member = memberDao.getMemberById(memberId);

        if (!member.getLoginPw().equals(oldPw)) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        memberDao.updatePassword(memberId, newPw);
    }

    /* ======================
       최근 로그인 시간 업데이트
    ====================== */

    @Transactional
    public void updateLastLoginAt(int memberId) {
        memberDao.updateLastLoginAt(memberId);
    }

    /* ======================
       SNS 로그인
    ====================== */

    @Transactional
    public LoginedMember loginOrJoinKakao(Map<String, Object> kakaoUser) {

        Long kakaoId = ((Number) kakaoUser.get("id")).longValue();
        Map<String, Object> account =
                (Map<String, Object>) kakaoUser.get("kakao_account");
        Map<String, Object> profile =
                account != null ? (Map<String, Object>) account.get("profile") : null;

        String email =
                account != null ? (String) account.get("email") : null;
        if (email == null) {
            email = "kakao_" + kakaoId + "@no-email.com";
        }

        String nickname =
                profile != null ? (String) profile.get("nickname") : "kakao_" + kakaoId;

        Member member = memberDao.getMemberByKakaoId(kakaoId);

        if (member == null) {
            nickname = makeUniqueNickname(nickname);
            memberDao.joinKakaoMember(
                    "kakao_" + kakaoId,
                    nickname,
                    nickname,
                    email,
                    kakaoId
            );
            member = memberDao.getMemberByKakaoId(kakaoId);
        }

        return new LoginedMember(member);
    }

    @Transactional
    public LoginedMember loginOrJoinNaver(Map<String, Object> naverUser) {

        String naverId = (String) naverUser.get("id");
        String name =
                (String) naverUser.getOrDefault("name", "네이버사용자");
        String email =
                (String) naverUser.getOrDefault(
                        "email",
                        "naver_" + naverId + "@no-email.com"
                );

        String nickname = makeUniqueNickname(name);

        Member member = memberDao.getMemberByNaverId(naverId);

        if (member == null) {
            memberDao.insertNaverMember(
                    "naver_" + naverId,
                    name,
                    nickname,
                    email,
                    naverId
            );
            member = memberDao.getMemberByNaverId(naverId);
        }

        return new LoginedMember(member);
    }

    @Transactional
    public LoginedMember loginOrJoinGoogle(Map<String, Object> googleUser) {

        String googleId = (String) googleUser.get("sub");
        String name =
                (String) googleUser.getOrDefault("name", "googleUser");
        String email =
                (String) googleUser.getOrDefault(
                        "email",
                        "google_" + googleId + "@no-email.com"
                );

        Member member = memberDao.getMemberByGoogleId(googleId);

        if (member == null) {
            String nickname = makeUniqueNickname(name);
            memberDao.insertGoogleMember(
                    "google_" + googleId,
                    name,
                    nickname,
                    email,
                    googleId
            );
            member = memberDao.getMemberByGoogleId(googleId);
        }

        return new LoginedMember(member);
    }

    /* ======================
       공통 유틸
    ====================== */

    private String makeUniqueNickname(String baseNickname) {
        String nickname = baseNickname;
        int suffix = 1;

        while (memberDao.getMemberByNickname(nickname) != null) {
            nickname = baseNickname + "_" + suffix++;
        }
        return nickname;
    }

    /* ======================
       이메일 인증 내부 클래스
    ====================== */

    private static class EmailAuthInfo {
        final String code;
        final LocalDateTime expireTime;
        boolean authed = false;

        EmailAuthInfo(String code, LocalDateTime expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }
}
