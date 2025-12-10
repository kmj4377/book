package com.example.demo.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    // ------------------ 로그인 아이디로 회원 조회 ------------------
    public Member getMemberByLoginId(String loginId) {
        return memberDao.getMemberByLoginId(loginId);
    }

    // ------------------ 닉네임으로 회원 조회 ------------------
    public Member getMemberByNickname(String nickname) {
        return memberDao.getMemberByNickname(nickname);
    }

    // ------------------ 일반 회원가입 ------------------
    @Transactional
    public void joinMember(String loginId, String loginPw, String name, String nickname) {

        if (memberDao.getMemberByLoginId(loginId) != null)
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");

        if (memberDao.getMemberByNickname(nickname) != null)
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");

        // 이메일 관련 로직 완전 제거
        memberDao.joinMember(loginId, loginPw, name, nickname);
    }

    // ------------------ 카카오 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinKakao(Map<String, Object> kakaoUser) {

        if (kakaoUser == null || !kakaoUser.containsKey("id")) {
            throw new IllegalArgumentException("카카오 사용자 정보가 올바르지 않습니다.");
        }

        Long kakaoId = ((Number) kakaoUser.get("id")).longValue();
        Map<String, Object> account = (Map<String, Object>) kakaoUser.get("kakao_account");
        Map<String, Object> profile = account != null ? (Map<String, Object>) account.get("profile") : null;

        String nickname = profile != null ? (String) profile.get("nickname") : ("kakao_" + kakaoId);

        Member member = memberDao.getMemberByKakaoId(kakaoId);

        if (member == null) {
            nickname = makeUniqueNickname(nickname);
            String loginId = "kakao_" + kakaoId;
            String name = nickname;

            memberDao.joinKakaoMember(loginId, name, nickname, kakaoId);
            member = memberDao.getMemberByKakaoId(kakaoId);
        }

        return new LoginedMember(member);
    }

    // ------------------ 네이버 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinNaver(Map<String, Object> naverUser) {

        if (naverUser == null || !naverUser.containsKey("id"))
            throw new IllegalArgumentException("네이버 사용자 정보가 올바르지 않습니다.");

        String naverId = (String) naverUser.get("id");
        String name = (String) naverUser.get("name");

        Member member = memberDao.getMemberByNaverId(naverId);

        if (member == null) {

            String loginId = "naver_" + naverId;
            String loginPw = "naver-login";

            String nickname = name != null ? name : ("네이버" + naverId.substring(0, 5));
            nickname = makeUniqueNickname(nickname);

            
            memberDao.insertNaverMember(loginId, loginPw, name, nickname, naverId);

            member = memberDao.getMemberByNaverId(naverId);
        }

        return new LoginedMember(member);
    }

    // ------------------ 구글 로그인 ------------------
    @Transactional
    public LoginedMember loginOrJoinGoogle(Map<String, Object> googleUser) {

        if (googleUser == null || !googleUser.containsKey("sub"))
            throw new IllegalArgumentException("구글 사용자 정보가 올바르지 않습니다.");

        String googleId = (String) googleUser.get("sub");
        String name = (String) googleUser.get("name");

        Member member = memberDao.getMemberByGoogleId(googleId);

        if (member == null) {

            String loginId = "google_" + googleId;
            String nickname = name != null ? name : ("GoogleUser_" + googleId.substring(0, 5));
            nickname = makeUniqueNickname(nickname);

            String loginPw = "google-login";

            // ⭐ email 제거
            memberDao.insertGoogleMember(loginId, loginPw, name, nickname, googleId);

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
}
