package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ExpenseStatDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseStatService {

	private final ExpenseStatDao expenseStatDao;

	public List<Map<String, Object>> getMonthlyStats(int memberId) {
		return expenseStatDao.getMonthlyTotalByMemberId(memberId);
	}

	public List<Map<String, Object>> getMonthlyTrend(int memberId) {
		return expenseStatDao.getMonthlyTotalByMemberId(memberId);
	}

	public List<Map<String, Object>> getCategoryStats(int memberId) {
		return expenseStatDao.getCategoryStats(memberId);
	}

	public List<Map<String, Object>> getWeekdayStats(int memberId) {
		return expenseStatDao.getWeekdayStats(memberId);
	}

	public List<Map<String, Object>> getCategoryStatsByMonth(int memberId, int year, int month) {
		return expenseStatDao.getCategoryStatsByMonth(memberId, year, month);
	}

	public List<Map<String, Object>> getDailyStatsByMonth(int memberId, int year, int month) {
		return expenseStatDao.getDailyStatsByMonth(memberId, year, month);
	}

	public int getThisMonthTotal(int memberId) {
		return expenseStatDao.getThisMonthTotal(memberId);
	}

	public int getPrevMonthTotal(int memberId) {
		return expenseStatDao.getPrevMonthTotal(memberId);
	}

	public int getMonthlyTotal(int memberId, int year, int month) {
		return expenseStatDao.getMonthlyTotal(memberId, year, month);
	}

	public List<Map<String, Object>> getTopCategories(int memberId, int year, int month, int limit) {
		return expenseStatDao.getTopCategoriesByMonth(memberId, year, month, limit);
	}

	public List<Map<String, Object>> getCategoryStatsForMonth(int memberId, int year, int month) {
		return expenseStatDao.getCategoryStatsForMonth(memberId, year, month);
	}

	public double getMonthlyChangePercent(int memberId) {
		int thisTotal = getThisMonthTotal(memberId);
		int prevTotal = getPrevMonthTotal(memberId);

		if (prevTotal == 0) {
			return thisTotal == 0 ? 0 : 100;
		}

		return ((double) (thisTotal - prevTotal) / prevTotal) * 100;
	}
}
