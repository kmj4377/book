package com.example.demo.service;

import java.util.*;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Budget;
import com.example.demo.dto.ResultData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetStatService {

    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    /**
     * ============================================================
     * 1) 특정 year-month 예산 대비 지출 상태 조회
     * ============================================================
     */
    public Map<String, Object> getBudgetStatus(int memberId, String yearMonth) {

        Map<String, Object> result = new HashMap<>();

        Budget budget = budgetService.getBudget(memberId, yearMonth);
        int budgetAmount = (budget != null) ? budget.getAmount() : 0;

        int totalExpense = expenseService.getMonthlyTotalExpense(memberId, yearMonth);

        double percent = (budgetAmount == 0)
                ? 0
                : (totalExpense * 100.0 / budgetAmount);

        result.put("yearMonth", yearMonth);
        result.put("budgetAmount", budgetAmount);
        result.put("totalExpense", totalExpense);
        result.put("percent", percent);

        return result;
    }


    /**
     * ============================================================
     * 2) 특정 연도 전체 예산 대비 지출 상태 조회
     * ============================================================
     */
    public List<Map<String, Object>> getYearlyBudgetStatus(int memberId, int year) {

        List<Map<String, Object>> list = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            String ym = String.format("%04d-%02d", year, month);

            Budget budget = budgetService.getBudget(memberId, ym);
            int budgetAmount = (budget != null) ? budget.getAmount() : 0;

            int totalExpense = expenseService.getMonthlyTotalExpense(memberId, ym);

            double percent = (budgetAmount == 0)
                    ? 0
                    : (totalExpense * 100.0 / budgetAmount);

            Map<String, Object> data = new HashMap<>();
            data.put("month", month);
            data.put("yearMonth", ym);
            data.put("budgetAmount", budgetAmount);
            data.put("totalExpense", totalExpense);
            data.put("percent", percent);

            list.add(data);
        }

        return list;
    }


    /**
     * ============================================================
     * (컨트롤러용) 특정 월 예산 대비 지출 비교 API
     * UsrBudgetStatController.compare() 에서 사용
     * ============================================================
     */
    public ResultData<Map<String, Object>> getBudgetCompareData(int memberId, String yearMonth) {

        Map<String, Object> data = getBudgetStatus(memberId, yearMonth);

        return ResultData.from("S-1", "예산 대비 지출 통계", data);
    }


    /**
     * ============================================================
     * (컨트롤러용) 특정 연도 월별 예산 비교 API
     * UsrBudgetStatController.monthly() 에서 사용
     * ============================================================
     */
    public ResultData<List<Map<String, Object>>> getMonthlyBudgetVsExpense(int memberId, int year) {

        List<Map<String, Object>> data = getYearlyBudgetStatus(memberId, year);

        return ResultData.from("S-1", "월별 예산 대비 지출 통계", data);
    }
}
