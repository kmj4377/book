package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.demo.dao.ExpenseDao;
import com.example.demo.dto.CalendarDay;
import com.example.demo.dto.CalendarResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final ExpenseDao expenseDao;

	private final String SERVICE_KEY = "ad44d697b230a7e5e71014a1179bddda3b8ce502d8f78f908015aa2a830878e2";

	private Map<String, String> getHolidays(int year, int month) {

		Map<String, String> holidays = new HashMap<>();

		try {
			StringBuilder urlBuilder = new StringBuilder(
					"https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo");
			urlBuilder.append("?" + "ServiceKey=" + SERVICE_KEY);
			urlBuilder.append("&solYear=" + year);
			urlBuilder.append("&solMonth=" + String.format("%02d", month));
			urlBuilder.append("&_type=json");

			URL url = new URL(urlBuilder.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(br);

			br.close();

			JsonNode items = root.path("response").path("body").path("items").path("item");

			if (!items.isArray())
				return holidays;

			for (JsonNode item : items) {

				String locdate = item.get("locdate").asText();
				String dateName = item.get("dateName").asText();

				String y = locdate.substring(0, 4);
				String m = locdate.substring(4, 6);
				String d = locdate.substring(6, 8);

				holidays.put(y + "-" + m + "-" + d, dateName);
			}

		} catch (Exception e) {
			System.out.println("공휴일 API 오류: " + e.getMessage());
		}

		return holidays;
	}

	private String getTagValue(String tag, Element e) {
		NodeList nl = e.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = nl.item(0);
		return nValue != null ? nValue.getNodeValue() : null;
	}

	public CalendarResult getCalendar(int memberId, Integer year, Integer month) {

		LocalDate now = LocalDate.now();
		if (year == null || month == null) {
			year = now.getYear();
			month = now.getMonthValue();
		}

		YearMonth ym = YearMonth.of(year, month);
		int lastDay = ym.lengthOfMonth();

		int startWeekday = LocalDate.of(year, month, 1).getDayOfWeek().getValue();
		if (startWeekday == 7)
			startWeekday = 0;

		List<Map<String, Object>> dailyExpenses = expenseDao.getDailyExpenseByMonth(memberId, year, month);

		List<List<CalendarDay>> weeks = new ArrayList<>();
		List<CalendarDay> currentWeek = new ArrayList<>();

		Map<String, String> holidayMap = getHolidays(year, month);

		for (int i = 0; i < startWeekday; i++) {
			currentWeek.add(CalendarDay.empty());
		}

		for (int day = 1; day <= lastDay; day++) {

			boolean isToday = (year == now.getYear() && month == now.getMonthValue() && day == now.getDayOfMonth());

			int expense = getExpenseForDay(dailyExpenses, day);

			CalendarDay cd = CalendarDay.of(day, isToday, expense);

			LocalDate d = LocalDate.of(year, month, day);
			String key = d.toString();

			if (holidayMap.containsKey(key)) {
				cd.setHoliday(true);
				cd.setHolidayName(holidayMap.get(key));
			}

			currentWeek.add(cd);

			if (currentWeek.size() == 7) {
				weeks.add(currentWeek);
				currentWeek = new ArrayList<>();
			}
		}

		if (!currentWeek.isEmpty()) {
			while (currentWeek.size() < 7) {
				currentWeek.add(CalendarDay.empty());
			}
			weeks.add(currentWeek);
		}

		YearMonth prev = ym.minusMonths(1);
		YearMonth next = ym.plusMonths(1);

		CalendarResult result = new CalendarResult();
		result.setYear(year);
		result.setMonth(month);
		result.setWeeks(weeks);

		result.setPrevYear(prev.getYear());
		result.setPrevMonth(prev.getMonthValue());
		result.setNextYear(next.getYear());
		result.setNextMonth(next.getMonthValue());

		return result;
	}

	private int getExpenseForDay(List<Map<String, Object>> list, int day) {
		for (Map<String, Object> row : list) {
			int d = Integer.parseInt(String.valueOf(row.get("day")));
			if (d == day) {
				Object amountObj = row.get("amount");
				return amountObj == null ? 0 : Integer.parseInt(String.valueOf(amountObj));
			}
		}
		return 0;
	}
}
