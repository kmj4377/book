package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.Budget;
import com.example.demo.dto.CalendarResult;
import com.example.demo.dto.Expense;
import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Req;
import com.example.demo.service.BudgetService;
import com.example.demo.service.CalendarService;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.ExpenseStatService;
import com.example.demo.util.Util;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UsrHomeController {

    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final ExpenseStatService expenseStatService;
    private final CalendarService calendarService;
    private final Req req;

    @GetMapping("/usr/welcome/index")
    public String showWelcome() {
        return "usr/welcome/index";
    }

    @GetMapping("/usr/home/main")
    public String showMain(Model model) {

        LoginedMember loginedMember = req.getLoginedMember();
        if (loginedMember == null) {
            return "redirect:/usr/welcome/index";
        }

        int memberId = loginedMember.getId();
        String yearMonth = Util.getYearMonth();

        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(5, 7));
        int day = Util.getDay();

        /* =========================
           예산 / 지출 기본 정보
        ========================= */
        Budget budget = budgetService.getBudget(memberId, yearMonth);
        int monthlyExpense = expenseService.getMonthlyTotalExpense(memberId, yearMonth);
        int saving = (budget != null) ? budget.getAmount() - monthlyExpense : 0;

        int todayExpense = expenseService.getDailyExpense(memberId, year, month, day);
        CalendarResult calendar = calendarService.getCalendar(memberId, year, month);

        /* =========================
           🔥 오늘 지출 목록 (NEW)
        ========================= */
        String todayDate = LocalDate.now().toString(); // yyyy-MM-dd
        List<Expense> todayExpenseList =
                expenseService.getExpensesByDate(memberId, todayDate);

        /* =========================
           월간 비교
        ========================= */
        LocalDate now = LocalDate.now();
        String thisMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String lastMonth = now.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        int thisTotal = expenseService.getMonthlyTotalExpense(memberId, thisMonth);
        int lastTotal = expenseService.getMonthlyTotalExpense(memberId, lastMonth);

        int diff = thisTotal - lastTotal;

        double increaseRate = 0;
        if (lastTotal > 0) {
            increaseRate = ((double) diff / lastTotal) * 100.0;
        }

        /* =========================
           이번 달 최다 지출 카테고리
        ========================= */
        String topCategory = "기타";

        List<Map<String, Object>> categoryStats =
                expenseStatService.getCategoryStatsByMonth(memberId, year, month);

        if (categoryStats != null && !categoryStats.isEmpty()) {
            Object category = categoryStats.get(0).get("category");
            if (category != null) {
                topCategory = category.toString();
            }
        }

        /* =========================
           Model 전달
        ========================= */
        model.addAttribute("loginedMember", loginedMember);

        model.addAttribute("budget", budget);
        model.addAttribute("monthlyExpense", monthlyExpense);
        model.addAttribute("saving", saving);

        model.addAttribute("todayCount", todayExpense);
        model.addAttribute("todayExpenseList", todayExpenseList); // ⭐ 핵심
        model.addAttribute("calendar", calendar);

        model.addAttribute("thisMonthTotal", thisTotal);
        model.addAttribute("lastMonthTotal", lastTotal);
        model.addAttribute("increaseRate", increaseRate);
        model.addAttribute("diff", diff);

        model.addAttribute("topCategory", topCategory);

        return "usr/home/main";
    }
}
