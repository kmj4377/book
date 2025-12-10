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

    public List<Expense> getExpenses(int memberId) {
        return expenseDao.getExpenses(memberId);
    }

    public List<Expense> getExpensesByDate(int memberId, String date) {
        return expenseDao.getExpensesByDate(memberId, date);
    }


    public void write(int memberId, int amount, String category, String memo, String expenseDate) {
        expenseDao.write(memberId, amount, category, memo, expenseDate);
    }

    public List<Expense> getListByMemberId(int memberId) {
        return expenseDao.getListByMemberId(memberId);
    }

    public Expense getExpenseById(int id) {
        return expenseDao.getExpenseById(id);
    }

    public void update(int id, int amount, String category, String memo, String expenseDate) {
        expenseDao.update(id, amount, category, memo, expenseDate);
    }

    public void delete(int id) {
        expenseDao.delete(id);
    }

    public int getMonthlyTotalExpense(int memberId, String yearMonth) {
        return expenseDao.getMonthlyTotalExpense(memberId, yearMonth);
    }

    public int getDailyExpense(int memberId, int year, int month, int day) {
        return expenseDao.getDailyExpense(memberId, year, month, day);
    }

}
