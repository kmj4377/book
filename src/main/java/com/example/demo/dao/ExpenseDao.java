package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.example.demo.dto.CategoryExpense;
import com.example.demo.dto.Expense;

@Mapper
public interface ExpenseDao {

    /* ===============================
       지출 등록
    =============================== */
    @Insert("""
        INSERT INTO expense
        SET regDate = NOW(),
            updateDate = NOW(),
            subCategoryId = #{subCategoryId},
            memberId = #{memberId},
            amount = #{amount},
            memo = #{memo},
            expenseDate = #{expenseDate}
    """)
    void write(
        @Param("subCategoryId") int subCategoryId,
        @Param("memberId") int memberId,
        @Param("amount") int amount,
        @Param("memo") String memo,
        @Param("expenseDate") String expenseDate
    );

    /* ===============================
       전체 목록 조회
    =============================== */
    @Select("""
        SELECT
            e.*,
            cs.name AS subCategoryName,
            cm.name AS mainCategoryName,
            cm.color AS mainCategoryColor
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
        ORDER BY e.regDate DESC
    """)
    List<Expense> getExpenses(@Param("memberId") int memberId);

    /* ===============================
       날짜별 목록 조회
    =============================== */
    @Select("""
        SELECT
            e.*,
            cs.name AS subCategoryName,
            cm.name AS mainCategoryName,
            cm.color AS mainCategoryColor
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND e.expenseDate = #{date}
        ORDER BY e.regDate DESC
    """)
    List<Expense> getExpensesByDate(
        @Param("memberId") int memberId,
        @Param("date") String date
    );

    /* ===============================
       단건 조회
    =============================== */
    @Select("""
        SELECT
            e.*,
            cs.name AS subCategoryName,
            cm.name AS mainCategoryName,
            cm.color AS mainCategoryColor
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.id = #{id}
    """)
    Expense getExpenseById(@Param("id") int id);

    /* ===============================
       수정
    =============================== */
    @Update("""
        UPDATE expense
        SET updateDate = NOW(),
            subCategoryId = #{subCategoryId},
            amount = #{amount},
            memo = #{memo},
            expenseDate = #{expenseDate}
        WHERE id = #{id}
    """)
    void update(
        @Param("id") int id,
        @Param("subCategoryId") int subCategoryId,
        @Param("amount") int amount,
        @Param("memo") String memo,
        @Param("expenseDate") String expenseDate
    );

    /* ===============================
       삭제
    =============================== */
    @Delete("""
        DELETE FROM expense
        WHERE id = #{id}
    """)
    void delete(@Param("id") int id);

    /* ===============================
       📊 월별 총 지출
    =============================== */
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
    """)
    int getMonthlyTotalExpense(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* ===============================
       📊 메인 카테고리별 통계
    =============================== */
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
    List<CategoryExpense> getCategorySummary(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* ===============================
       📊 월별 일 지출 합계 (차트)
    =============================== */
    @Select("""
        SELECT
            DAY(expenseDate) AS day,
            SUM(amount) AS amount
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
        GROUP BY DAY(expenseDate)
        ORDER BY day
    """)
    List<Map<String, Object>> getDailyExpenseByMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );

    /* ===============================
       🔍 키워드 검색
    =============================== */
    @Select("""
        SELECT
            e.*,
            cs.name AS subCategoryName,
            cm.name AS mainCategoryName,
            cm.color AS mainCategoryColor
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND (
                e.memo LIKE CONCAT('%', #{keyword}, '%')
             OR cs.name LIKE CONCAT('%', #{keyword}, '%')
             OR cm.name LIKE CONCAT('%', #{keyword}, '%')
          )
        ORDER BY e.regDate DESC
    """)
    List<Expense> getExpensesByKeyword(
        @Param("memberId") int memberId,
        @Param("keyword") String keyword
    );

    /* ===============================
       🔍 날짜 + 키워드 검색
    =============================== */
    @Select("""
        SELECT
            e.*,
            cs.name AS subCategoryName,
            cm.name AS mainCategoryName,
            cm.color AS mainCategoryColor
        FROM expense e
        JOIN category_sub cs ON e.subCategoryId = cs.id
        JOIN category_main cm ON cs.mainId = cm.id
        WHERE e.memberId = #{memberId}
          AND e.expenseDate = #{date}
          AND (
                e.memo LIKE CONCAT('%', #{keyword}, '%')
             OR cs.name LIKE CONCAT('%', #{keyword}, '%')
             OR cm.name LIKE CONCAT('%', #{keyword}, '%')
          )
        ORDER BY e.regDate DESC
    """)
    List<Expense> getExpensesByDateAndKeyword(
        @Param("memberId") int memberId,
        @Param("date") String date,
        @Param("keyword") String keyword
    );
}
