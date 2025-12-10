package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

    // -------------------- 로그인 아이디로 회원 조회 --------------------
    @Select("""
        SELECT *
        FROM member
        WHERE loginId = #{loginId}
    """)
    Member getMemberByLoginId(String loginId);

    // -------------------- 닉네임으로 회원 조회 --------------------
    @Select("""
        SELECT *
        FROM member
        WHERE nickname = #{nickname}
    """)
    Member getMemberByNickname(String nickname);

    // -------------------- ID로 회원 조회 --------------------
    @Select("""
        SELECT *
        FROM member
        WHERE id = #{id}
    """)
    Member getMemberById(int id);

 // -------------------- 일반 회원 가입 --------------------
    @Insert("""
        INSERT INTO member
        SET regDate = NOW(),
            updateDate = NOW(),
            loginId = #{loginId},
            loginPw = #{loginPw},
            name = #{name},
            nickname = #{nickname},
            authLevel = 1
    """)
    void joinMember(String loginId, String loginPw, String name, String nickname);


    // -------------------- 카카오 로그인 --------------------
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
            kakao_id = #{kakaoId},
            authLevel = 1
    """)
    void joinKakaoMember(String loginId, String name, String nickname, Long kakaoId);

    // -------------------- 네이버 로그인 --------------------
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
    	        loginPw = #{loginPw},
    	        name = #{name},
    	        nickname = #{nickname},
    	        naver_id = #{naverId},
    	        authLevel = 1
    	""")
    	void insertNaverMember(String loginId, String loginPw, String name, String nickname, String naverId);


    // -------------------- 구글 로그인 --------------------
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
    	        loginPw = #{loginPw},
    	        name = #{name},
    	        nickname = #{nickname},
    	        google_id = #{googleId},
    	        authLevel = 1
    	""")
    	void insertGoogleMember(String loginId, String loginPw, String name, String nickname, String googleId);

}
