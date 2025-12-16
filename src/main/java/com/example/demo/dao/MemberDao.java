package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

	@Select("""
			    SELECT *
			    FROM member
			    WHERE loginId = #{loginId}
			""")
	Member getMemberByLoginId(String loginId);

	@Select("""
			    SELECT *
			    FROM member
			    WHERE nickname = #{nickname}
			""")
	Member getMemberByNickname(String nickname);

	@Select("""
			    SELECT *
			    FROM member
			    WHERE id = #{id}
			""")
	Member getMemberById(int id);

	@Select("""
			    SELECT COUNT(*)
			    FROM member
			    WHERE email = #{email}
			""")
	int getCountByEmail(String email);

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
	void joinMember(@Param("loginId") String loginId, @Param("loginPw") String loginPw, @Param("name") String name,
			@Param("nickname") String nickname, @Param("email") String email);

	@Update("""
			    UPDATE member
			    SET email_verified = 1,
			        email_verified_at = NOW(),
			        updateDate = NOW()
			    WHERE email = #{email}
			""")
	void setEmailVerifiedByEmail(@Param("email") String email);

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
	void joinKakaoMember(@Param("loginId") String loginId, @Param("name") String name,
			@Param("nickname") String nickname, @Param("email") String email, @Param("kakaoId") Long kakaoId);

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
	void insertNaverMember(@Param("loginId") String loginId, @Param("name") String name,
			@Param("nickname") String nickname, @Param("email") String email, @Param("naverId") String naverId);

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
	void insertGoogleMember(@Param("loginId") String loginId, @Param("name") String name,
			@Param("nickname") String nickname, @Param("email") String email, @Param("googleId") String googleId);

	@Update("""
			    UPDATE member
			    SET name = #{name},
			        nickname = #{nickname},
			        updateDate = NOW()
			    WHERE id = #{id}
			""")
	void updateMemberInfoWithoutEmail(@Param("id") int id, @Param("name") String name,
			@Param("nickname") String nickname);

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
	void updateMemberInfoWithEmail(@Param("id") int id, @Param("name") String name, @Param("nickname") String nickname,
			@Param("email") String email);

	@Update("""
			    UPDATE member
			    SET loginPw = #{loginPw},
			        updateDate = NOW()
			    WHERE id = #{id}
			""")
	void updatePassword(@Param("id") int id, @Param("loginPw") String loginPw);

	@Update("""
			    UPDATE member
			    SET last_login_at = NOW()
			    WHERE id = #{memberId}
			""")
	void updateLastLoginAt(@Param("memberId") int memberId);
}
