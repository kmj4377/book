package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private int id;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private LocalDateTime lastLoginAt;
	private String loginId;
	private String loginPw;
	private String name;
	private int authLevel;
	private Long kakaoId;
	private String naverId;
	private String googleId;
	private String email;
	private String nickname;
	private int emailAuthed;
	private int emailVerified;
	private String emailVerifiedAt;
}