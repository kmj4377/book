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
            DATE_FORMAT(STR_TO_DATE(expenseDate, '%Y-%m-%d'), '%Y-%m') AS month,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
        GROUP BY DATE_FORMAT(STR_TO_DATE(expenseDate, '%Y-%m-%d'), '%Y-%m')
        ORDER BY month ASC
    """)
    List<Map<String, Object>> getMonthlyTotalByMemberId(@Param("memberId") int memberId);


    /* =========================================================================
     *  2) 전체 카테고리 통계
     * ========================================================================= */
    @Select("""
        SELECT 
            category,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
        GROUP BY category
        ORDER BY total DESC
    """)
    List<Map<String, Object>> getCategoryStats(@Param("memberId") int memberId);


    /* =========================================================================
     *  3) 요일별 통계
     * ========================================================================= */
    @Select("""
        SELECT 
            DAYOFWEEK(STR_TO_DATE(expenseDate, '%Y-%m-%d')) AS weekday,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
        GROUP BY weekday
        ORDER BY weekday
    """)
    List<Map<String, Object>> getWeekdayStats(@Param("memberId") int memberId);


    /* =========================================================================
     *  4) 특정 월 카테고리 통계
     * ========================================================================= */
    @Select("""
        SELECT 
            category,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(#{year}, '-', LPAD(#{month}, 2, '0'), '%')
        GROUP BY category
        ORDER BY total DESC
    """)
    List<Map<String, Object>> getCategoryStatsByMonth(
            @Param("memberId") int memberId,
            @Param("year") int year,
            @Param("month") int month
    );


    /* =========================================================================
     *  4-1) 특정 월 Top N 카테고리 (월별 비교 페이지에서 사용)
     * ========================================================================= */
    @Select("""
        SELECT 
            category,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(#{year}, '-', LPAD(#{month}, 2, '0'), '%')
        GROUP BY category
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
            category,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(#{year}, '-', LPAD(#{month}, 2, '0'), '%')
        GROUP BY category
        ORDER BY category ASC
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
            expenseDate AS date,
            SUBSTRING(expenseDate, 9, 2) AS day,
            SUM(amount) AS total
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(#{year}, '-', LPAD(#{month}, 2, '0'), '%')
        GROUP BY expenseDate
        ORDER BY expenseDate
    """)
    List<Map<String, Object>> getDailyStatsByMonth(
            @Param("memberId") int memberId,
            @Param("year") int year,
            @Param("month") int month
    );


     //*  6) 이번 달 총 지출
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(DATE_FORMAT(NOW(), '%Y-%m'), '%')
    """)
    int getThisMonthTotal(@Param("memberId") int memberId);


     //  7) 지난 달 총 지출
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 MONTH), '%Y-%m'), '%')
    """)
    int getPrevMonthTotal(@Param("memberId") int memberId);


    /* =========================================================================
     *  8) 특정 년·월 총 지출
     * ========================================================================= */
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate LIKE CONCAT(#{year}, '-', LPAD(#{month}, 2, '0'), '%')
    """)
    int getMonthlyTotal(
            @Param("memberId") int memberId,
            @Param("year") int year,
            @Param("month") int month
    );
}
