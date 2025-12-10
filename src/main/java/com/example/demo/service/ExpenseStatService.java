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

    // 월별 총합 리스트 (전체 기간) - 기존
    public List<Map<String, Object>> getMonthlyStats(int memberId) {
        return expenseStatDao.getMonthlyTotalByMemberId(memberId);
    }

    // ⭐ 월간 추이 (월별 총 지출 그래프용)
    // DAO에서 month(total) 리스트 가져와 그대로 사용
    public List<Map<String, Object>> getMonthlyTrend(int memberId) {
        return expenseStatDao.getMonthlyTotalByMemberId(memberId);
    }

    // 전체 카테고리 통계
    public List<Map<String, Object>> getCategoryStats(int memberId) {
        return expenseStatDao.getCategoryStats(memberId);
    }

    // 요일 통계
    public List<Map<String, Object>> getWeekdayStats(int memberId) {
        return expenseStatDao.getWeekdayStats(memberId);
    }

    // 특정 월 카테고리 통계
    public List<Map<String, Object>> getCategoryStatsByMonth(int memberId, int year, int month) {
        return expenseStatDao.getCategoryStatsByMonth(memberId, year, month);
    }

    // 특정 월 일별 통계
    public List<Map<String, Object>> getDailyStatsByMonth(int memberId, int year, int month) {
        return expenseStatDao.getDailyStatsByMonth(memberId, year, month);
    }

    // 이번 달 총 지출
    public int getThisMonthTotal(int memberId) {
        return expenseStatDao.getThisMonthTotal(memberId);
    }

    // 지난 달 총 지출
    public int getPrevMonthTotal(int memberId) {
        return expenseStatDao.getPrevMonthTotal(memberId);
    }

    // 특정 년/월 총합
    public int getMonthlyTotal(int memberId, int year, int month) {
        return expenseStatDao.getMonthlyTotal(memberId, year, month);
    }

    // TOP N 카테고리
    public List<Map<String, Object>> getTopCategories(int memberId, int year, int month, int limit) {
        return expenseStatDao.getTopCategoriesByMonth(memberId, year, month, limit);
    }

    // 특정 월 카테고리 total map
    public List<Map<String, Object>> getCategoryStatsForMonth(int memberId, int year, int month) {
        return expenseStatDao.getCategoryStatsForMonth(memberId, year, month);
    }

    // 전월 대비 증감률 계산
    public double getMonthlyChangePercent(int memberId) {
        int thisTotal = getThisMonthTotal(memberId);
        int prevTotal = getPrevMonthTotal(memberId);

        if (prevTotal == 0) {
            return thisTotal == 0 ? 0 : 100;
        }

        return ((double) (thisTotal - prevTotal) / prevTotal) * 100;
    }
}
