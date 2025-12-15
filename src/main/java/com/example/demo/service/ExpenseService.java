package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ExpenseDao;
import com.example.demo.dto.Expense;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseDao expenseDao;

    /* =========================
       기본 조회
    ========================= */
    public List<Expense> getExpenses(int memberId) {
        return expenseDao.getExpenses(memberId);
    }

    public List<Expense> getExpensesByDate(int memberId, String date) {
        return expenseDao.getExpensesByDate(memberId, date);
    }

    /* =========================
       🔍 검색
    ========================= */
    public List<Expense> getExpensesByKeyword(int memberId, String keyword) {
        return expenseDao.getExpensesByKeyword(memberId, keyword);
    }

    public List<Expense> getExpensesByDateAndKeyword(
        int memberId,
        String date,
        String keyword
    ) {
        return expenseDao.getExpensesByDateAndKeyword(memberId, date, keyword);
    }

    /* =========================
       CRUD
    ========================= */
    public void write(
        int memberId,
        int subCategoryId,
        int amount,
        String memo,
        String expenseDate
    ) {
        expenseDao.write(
            subCategoryId,
            memberId,
            amount,
            memo,
            expenseDate
        );
    }

    public Expense getExpenseById(int id) {
        return expenseDao.getExpenseById(id);
    }

    public void update(
        int id,
        int subCategoryId,
        int amount,
        String memo,
        String expenseDate
    ) {
        expenseDao.update(
            id,
            subCategoryId,
            amount,
            memo,
            expenseDate
        );
    }

    public void delete(int id) {
        expenseDao.delete(id);
    }

    /* =========================
       📊 통계
    ========================= */

    // 월 총 지출 (YYYY-MM)
    public int getMonthlyTotalExpense(int memberId, String yearMonth) {
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        return expenseDao.getMonthlyTotalExpense(memberId, year, month);
    }

    // ✅ 특정 일 지출 합계 (Controller에서 사용 중)
    public int getDailyExpense(
        int memberId,
        int year,
        int month,
        int day
    ) {
        // YYYY-MM-DD 형태로 변환
        String date = String.format(
            "%04d-%02d-%02d",
            year, month, day
        );

        // 날짜별 지출 목록을 가져와서 합계 계산
        return expenseDao.getExpensesByDate(memberId, date)
                         .stream()
                         .mapToInt(Expense::getAmount)
                         .sum();
    }
}
