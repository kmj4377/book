package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExpenseStatDao {

    /* =========================================================================
     *  1) 월별 총 지출 (전체 기간)
     * ========================================================================= */
    @Select("""
        SELECT
            DATE_FORMAT(e.expenseDate, '%Y-%m') AS month,
            SUM(e.amount) AS total
        FROM expense e
        WHERE e.memberId = #{memberId}
        GROUP BY DATE_FORMAT(e.expenseDate, '%Y-%m')
        ORDER BY month ASC
    """)
    List<Map<String, Object>> getMonthlyTotalByMemberId(
        @Param("memberId") int memberId
    );

    /* =========================================================================
     *  2) 전체 메인 카테고리 통계
     * ========================================================================= */
    @Select("""
        SELECT
            cm.name AS category,
            cm.color AS color,
            SUM(e.amount) AS total
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
        GROUP BY cm.id
        ORDER BY total DESC
    """)
    List<Map<String, Object>> getCategoryStats(
        @Param("memberId") int memberId
    );

    /* =========================================================================
     *  3) 요일별 통계
     * ========================================================================= */
    @Select("""
        SELECT
            DAYOFWEEK(e.expenseDate) AS weekday,
            SUM(e.amount) AS total
        FROM expense e
        WHERE e.memberId = #{memberId}
        GROUP BY DAYOFWEEK(e.expenseDate)
        ORDER BY weekday
    """)
    List<Map<String, Object>> getWeekdayStats(
        @Param("memberId") int memberId
    );

    /* =========================================================================
     *  4) 특정 월 메인 카테고리 통계
     * ========================================================================= */
    @Select("""
        SELECT
            cm.name AS category,
            cm.color AS color,
            SUM(e.amount) AS total
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND YEAR(e.expenseDate) = #{year}
          AND MONTH(e.expenseDate) = #{month}
        GROUP BY cm.id
        ORDER BY total DESC
    """)
    List<Map<String, Object>> getCategoryStatsByMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* =========================================================================
     *  4-1) 특정 월 Top N 메인 카테고리
     * ========================================================================= */
    @Select("""
        SELECT
            cm.name AS category,
            cm.color AS color,
            SUM(e.amount) AS total
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND YEAR(e.expenseDate) = #{year}
          AND MONTH(e.expenseDate) = #{month}
        GROUP BY cm.id
        ORDER BY total DESC
        LIMIT #{limit}
    """)
    List<Map<String, Object>> getTopCategoriesByMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month,
        @Param("limit") int limit
    );

    /* =========================================================================
     *  4-2) 도넛 차트용 카테고리 통계 (월별)
     * ========================================================================= */
    @Select("""
        SELECT
            cm.name AS category,
            cm.color AS color,
            SUM(e.amount) AS total
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND YEAR(e.expenseDate) = #{year}
          AND MONTH(e.expenseDate) = #{month}
        GROUP BY cm.id
        ORDER BY cm.sortOrder
    """)
    List<Map<String, Object>> getCategoryStatsForMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* =========================================================================
     *  5) 특정 월 일별 지출 통계
     * ========================================================================= */
    @Select("""
        SELECT
            e.expenseDate AS date,
            DAY(e.expenseDate) AS day,
            SUM(e.amount) AS total
        FROM expense e
        WHERE e.memberId = #{memberId}
          AND YEAR(e.expenseDate) = #{year}
          AND MONTH(e.expenseDate) = #{month}
        GROUP BY e.expenseDate
        ORDER BY e.expenseDate
    """)
    List<Map<String, Object>> getDailyStatsByMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* =========================================================================
     *  6) 이번 달 총 지출
     * ========================================================================= */
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = YEAR(NOW())
          AND MONTH(expenseDate) = MONTH(NOW())
    """)
    int getThisMonthTotal(
        @Param("memberId") int memberId
    );

    /* =========================================================================
     *  7) 지난 달 총 지출
     * ========================================================================= */
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = YEAR(DATE_SUB(NOW(), INTERVAL 1 MONTH))
          AND MONTH(expenseDate) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH))
    """)
    int getPrevMonthTotal(
        @Param("memberId") int memberId
    );

    /* =========================================================================
     *  8) 특정 년·월 총 지출
     * ========================================================================= */
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
    """)
    int getMonthlyTotal(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );
}
