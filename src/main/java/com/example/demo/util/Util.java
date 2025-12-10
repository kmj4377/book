package com.example.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	// 🔥 추가된 부분
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
	// 🔥 여기까지
	
    public static String getNowDateStr() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public static String getYearMonth() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
    
    public static int getDay() {
        return java.time.LocalDate.now().getDayOfMonth();
    }


}
