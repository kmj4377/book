package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LoginedMember;
import com.example.demo.service.ExpenseStatService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/expensestat")
public class UsrExpenseStatController {

	private final ExpenseStatService expenseStatService;

	@GetMapping("/monthly")
	public String showStats(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month,
			Model model, HttpSession session) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return "redirect:/usr/member/login";

		int memberId = user.getId();
		LocalDate now = LocalDate.now();

		if (year == null)
			year = now.getYear();
		if (month == null)
			month = now.getMonthValue();

		model.addAttribute("monthlyStats", expenseStatService.getMonthlyStats(memberId));
		model.addAttribute("categoryStatsAll", expenseStatService.getCategoryStats(memberId));
		model.addAttribute("categoryStats", expenseStatService.getCategoryStatsByMonth(memberId, year, month));
		model.addAttribute("dailyStats", expenseStatService.getDailyStatsByMonth(memberId, year, month));

		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);

		return "usr/expensestat/monthly";
	}

	@GetMapping("/category")
	public String showCategory(@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month, Model model, HttpSession session) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return "redirect:/usr/member/login";

		int memberId = user.getId();
		LocalDate now = LocalDate.now();

		if (year == null)
			year = now.getYear();
		if (month == null)
			month = now.getMonthValue();

		model.addAttribute("categoryStats", expenseStatService.getCategoryStatsByMonth(memberId, year, month));
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);

		return "usr/expensestat/graph_category";
	}

	@GetMapping("/api/categoryRatio")
	@ResponseBody
	public List<Map<String, Object>> apiCategoryRatio(@RequestParam int year, @RequestParam int month,
			HttpSession session) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return List.of();

		int memberId = user.getId();
		return expenseStatService.getCategoryStatsByMonth(memberId, year, month);
	}

	@GetMapping("/compare")
	public String showComparePage(@RequestParam(required = false) Integer year1,
			@RequestParam(required = false) Integer month1, @RequestParam(required = false) Integer year2,
			@RequestParam(required = false) Integer month2, HttpSession session, Model model) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return "redirect:/usr/member/login";

		int memberId = user.getId();
		LocalDate today = LocalDate.now();

		if (year1 == null || month1 == null) {
			year1 = today.getYear();
			month1 = today.getMonthValue();
		}

		if (year2 == null || month2 == null) {
			LocalDate prev = today.minusMonths(1);
			year2 = prev.getYear();
			month2 = prev.getMonthValue();
		}

		int total1 = expenseStatService.getMonthlyTotal(memberId, year1, month1);
		int total2 = expenseStatService.getMonthlyTotal(memberId, year2, month2);

		model.addAttribute("year1", year1);
		model.addAttribute("month1", month1);
		model.addAttribute("year2", year2);
		model.addAttribute("month2", month2);

		model.addAttribute("total1", total1);
		model.addAttribute("total2", total2);
		model.addAttribute("diff", total1 - total2);

		model.addAttribute("top3Month1", expenseStatService.getTopCategories(memberId, year1, month1, 3));
		model.addAttribute("top3Month2", expenseStatService.getTopCategories(memberId, year2, month2, 3));

		model.addAttribute("cateStat1", expenseStatService.getCategoryStatsForMonth(memberId, year1, month1));
		model.addAttribute("cateStat2", expenseStatService.getCategoryStatsForMonth(memberId, year2, month2));

		return "usr/expensestat/compare";
	}

	@GetMapping("/api/monthCompareSummary")
	@ResponseBody
	public Map<String, Object> apiMonthCompareSummary(@RequestParam int year1, @RequestParam int month1,
			@RequestParam int year2, @RequestParam int month2, HttpSession session) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return Map.of("result", "fail", "msg", "로그인 필요");

		int memberId = user.getId();

		return Map.of("result", "success", "total1", expenseStatService.getMonthlyTotal(memberId, year1, month1),
				"total2", expenseStatService.getMonthlyTotal(memberId, year2, month2));
	}

	@GetMapping("/api/dayStatsCompare")
	@ResponseBody
	public Map<String, Object> apiDayStatsCompare(@RequestParam int year1, @RequestParam int month1,
			@RequestParam int year2, @RequestParam int month2, HttpSession session) {
		LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
		if (user == null)
			return Map.of("result", "fail", "msg", "로그인 필요");

		int memberId = user.getId();

		return Map.of("result", "success", "month1", expenseStatService.getDailyStatsByMonth(memberId, year1, month1),
				"month2", expenseStatService.getDailyStatsByMonth(memberId, year2, month2));
	}
}
