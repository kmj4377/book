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

	public List<Expense> getExpensesByKeyword(int memberId, String keyword) {
		return expenseDao.getExpensesByKeyword(memberId, keyword);
	}

	public List<Expense> getExpensesByDateAndKeyword(int memberId, String date, String keyword) {
		return expenseDao.getExpensesByDateAndKeyword(memberId, date, keyword);
	}

	public void write(int memberId, int subCategoryId, int amount, String memo, String expenseDate) {
		expenseDao.write(subCategoryId, memberId, amount, memo, expenseDate);
	}

	public Expense getExpenseById(int id) {
		return expenseDao.getExpenseById(id);
	}

	public void update(int id, int subCategoryId, int amount, String memo, String expenseDate) {
		expenseDao.update(id, subCategoryId, amount, memo, expenseDate);
	}

	public void delete(int id) {
		expenseDao.delete(id);
	}

	public int getMonthlyTotalExpense(int memberId, String yearMonth) {
		String[] parts = yearMonth.split("-");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);

		return expenseDao.getMonthlyTotalExpense(memberId, year, month);
	}

	public int getDailyExpense(int memberId, int year, int month, int day) {
		String date = String.format("%04d-%02d-%02d", year, month, day);

		return expenseDao.getExpensesByDate(memberId, date).stream().mapToInt(Expense::getAmount).sum();
	}
}
