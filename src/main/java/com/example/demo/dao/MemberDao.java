package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

    /* =========================
       기본 조회
    ========================= */

    // 로그인 아이디로 회원 조회
    @Select("""
        SELECT *
        FROM member
        WHERE loginId = #{loginId}
    """)
    Member getMemberByLoginId(String loginId);

    // 닉네임으로 회원 조회
    @Select("""
        SELECT *
        FROM member
        WHERE nickname = #{nickname}
    """)
    Member getMemberByNickname(String nickname);

    // ID로 회원 조회
    @Select("""
        SELECT *
        FROM member
        WHERE id = #{id}
    """)
    Member getMemberById(int id);

    // 이메일로 회원 수 조회
    @Select("""
        SELECT COUNT(*)
        FROM member
        WHERE email = #{email}
    """)
    int getCountByEmail(String email);

    /* =========================
       회원가입
    ========================= */

    // 일반 회원 가입
    @Insert("""
        INSERT INTO member
        SET regDate = NOW(),
            updateDate = NOW(),
            loginId = #{loginId},
            loginPw = #{loginPw},
            name = #{name},
            nickname = #{nickname},
            email = #{email},
            authLevel = 1,
            email_verified = 0
    """)
    void joinMember(
        @Param("loginId") String loginId,
        @Param("loginPw") String loginPw,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("email") String email
    );

    /* =========================
       이메일 인증 (핵심)
    ========================= */

    // ⭐ 이메일 인증 완료 처리
    @Update("""
        UPDATE member
        SET email_verified = 1,
            email_verified_at = NOW(),
            updateDate = NOW()
        WHERE email = #{email}
    """)
    void setEmailVerifiedByEmail(@Param("email") String email);

    /* =========================
       SNS 로그인
    ========================= */

    // 카카오
    @Select("""
        SELECT *
        FROM member
        WHERE kakao_id = #{kakaoId}
    """)
    Member getMemberByKakaoId(Long kakaoId);

    @Insert("""
        INSERT INTO member
        SET regDate = NOW(),
            updateDate = NOW(),
            loginId = #{loginId},
            loginPw = NULL,
            name = #{name},
            nickname = #{nickname},
            email = #{email},
            kakao_id = #{kakaoId},
            authLevel = 1,
            email_verified = 1,
            email_verified_at = NOW()
    """)
    void joinKakaoMember(
        @Param("loginId") String loginId,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("email") String email,
        @Param("kakaoId") Long kakaoId
    );

    // 네이버
    @Select("""
        SELECT *
        FROM member
        WHERE naver_id = #{naverId}
    """)
    Member getMemberByNaverId(String naverId);

    @Insert("""
        INSERT INTO member
        SET regDate = NOW(),
            updateDate = NOW(),
            loginId = #{loginId},
            loginPw = NULL,
            name = #{name},
            nickname = #{nickname},
            email = #{email},
            naver_id = #{naverId},
            authLevel = 1,
            email_verified = 1,
            email_verified_at = NOW()
    """)
    void insertNaverMember(
        @Param("loginId") String loginId,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("email") String email,
        @Param("naverId") String naverId
    );

    // 구글
    @Select("""
        SELECT *
        FROM member
        WHERE google_id = #{googleId}
    """)
    Member getMemberByGoogleId(String googleId);

    @Insert("""
        INSERT INTO member
        SET regDate = NOW(),
            updateDate = NOW(),
            loginId = #{loginId},
            loginPw = NULL,
            name = #{name},
            nickname = #{nickname},
            email = #{email},
            google_id = #{googleId},
            authLevel = 1,
            email_verified = 1,
            email_verified_at = NOW()
    """)
    void insertGoogleMember(
        @Param("loginId") String loginId,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("email") String email,
        @Param("googleId") String googleId
    );

    /* =========================
       마이페이지
    ========================= */

    // 내 정보 수정
    @Update("""
        UPDATE member
        SET name = #{name},
            nickname = #{nickname},
            email = #{email},
            email_verified = 0,
            email_verified_at = NULL,
            updateDate = NOW()
        WHERE id = #{id}
    """)
    void updateMemberInfo(
        @Param("id") int id,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("email") String email
    );

    // 비밀번호 변경
    @Update("""
        UPDATE member
        SET loginPw = #{loginPw},
            updateDate = NOW()
        WHERE id = #{id}
    """)
    void updatePassword(
        @Param("id") int id,
        @Param("loginPw") String loginPw
    );

    // 최근 로그인 시간 업데이트
    @Update("""
        UPDATE member
        SET last_login_at = NOW()
        WHERE id = #{memberId}
    """)
    void updateLastLoginAt(@Param("memberId") int memberId);
}
