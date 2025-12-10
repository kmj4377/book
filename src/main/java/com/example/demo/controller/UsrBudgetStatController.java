package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.ResultData;
import com.example.demo.service.BudgetStatService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsrBudgetStatController {

    @Autowired
    private BudgetStatService budgetStatService;

    // 월별 예산 / 지출 비교 gauge 용
    @RequestMapping("/usr/expenseStat/monthly")
    @ResponseBody
    public ResultData compareBudget(
            HttpSession session,
            Integer year,
            Integer month) {

        LoginedMember loginedMember = (LoginedMember) session.getAttribute("loginedMember");

        if (loginedMember == null) {
            return ResultData.from("F-A", "로그인 후 이용해주세요.");
        }

        if (year == null || month == null) {
            return ResultData.from("F-1", "year, month 값을 입력해주세요.");
        }

        int memberId = loginedMember.getId();
        String ym = String.format("%04d-%02d", year, month);

        return budgetStatService.getBudgetCompareData(memberId, ym);
    }
}

