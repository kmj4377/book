package com.example.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Util {

	public static String jsReplace(String msg, String uri) {

		if (msg == null) {
			msg = "";
		}

		if (uri == null || uri.length() == 0) {
			uri = "/";
		}

		return String.format("""
				<script>
				    const msg = '%s'.trim();

				    if (msg.length > 0) {
				        requestAnimationFrame(() => {
				            alert(msg);
				        })
				    }

				    const uri = '%s'.trim();

				    if (uri == 'hb') {
				        history.back();
				    }

				    setTimeout(() => {
				        location.replace(uri);
				    }, 100);

				</script>
				""", msg, uri);
	}

	public static String jsHistoryBack(String msg) {
		if (msg == null) {
			msg = "";
		}

		return String.format("""
				<script>
				    const msg = '%s'.trim();

				    if (msg.length > 0) {
				        requestAnimationFrame(() => {
				            alert(msg);
				        })
				    }

				    history.back();
				</script>
				""", msg);
	}

	public static String getNowDateStr() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String getYearMonth() {
		LocalDate now = LocalDate.now();
		return now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
	}

	public static int getDay() {
		return LocalDate.now().getDayOfMonth();
	}

	public static String sha256(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(plainText.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();

		} catch (Exception e) {
			throw new RuntimeException("비밀번호 암호화 실패", e);
		}
	}
}
