package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Budget;
import com.example.demo.dto.LoginedMember;
import com.example.demo.service.BudgetService;
import com.example.demo.util.Util;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/usr/budget")
public class UsrBudgetController {

    @Autowired
    private BudgetService budgetService;

    @RequestMapping("/set")
    public String showSetBudget(HttpServletRequest req, Model model, String month) {

        LoginedMember loginedMember =
                (LoginedMember) req.getSession().getAttribute("loginedMember");

        if (loginedMember == null) {
            return Util.jsHistoryBack("로그인 후 이용해주세요.");
        }

        int memberId = loginedMember.getId();

        if (month == null || month.isEmpty()) {
            month = Util.getYearMonth();  // "yyyy-MM"
        }

        Budget budget = budgetService.getBudget(memberId, month);

        model.addAttribute("budget", budget);
        model.addAttribute("loginedMember", loginedMember);
        model.addAttribute("month", month);

        return "usr/budget/set";
    }

    @RequestMapping("/doSet")
    @ResponseBody
    public String doSet(HttpServletRequest req, String month, int amount) {

        LoginedMember loginedMember =
                (LoginedMember) req.getSession().getAttribute("loginedMember");

        if (loginedMember == null) {
            return Util.jsHistoryBack("로그인 후 이용해주세요.");
        }

        int memberId = loginedMember.getId();

        // 사용자가 선택한 month 그대로 저장!
        budgetService.setBudget(memberId, month, amount);

        return Util.jsReplace("예산이 설정되었습니다.", "/usr/budget/set?month=" + month);
    }
}
