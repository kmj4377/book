package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.CategoryExpense;
import com.example.demo.dto.Expense;

@Mapper
public interface ExpenseDao {

    @Insert("""
        INSERT INTO expense
        SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            amount = #{amount},
            category = #{category},
            memo = #{memo},
            expenseDate = #{expenseDate}
    """)
    void write(
        @Param("memberId") int memberId,
        @Param("amount") int amount,
        @Param("category") String category,
        @Param("memo") String memo,
        @Param("expenseDate") String expenseDate
    );

    @Select("""
        SELECT *
        FROM expense
        WHERE memberId = #{memberId}
        ORDER BY id DESC
    """)
    List<Expense> getListByMemberId(@Param("memberId") int memberId);

    @Select("""
        SELECT *
        FROM expense
        WHERE id = #{id}
    """)
    Expense getExpenseById(@Param("id") int id);

    @Update("""
        UPDATE expense
        SET updateDate = NOW(),
            amount = #{amount},
            category = #{category},
            memo = #{memo},
            expenseDate = #{expenseDate}
        WHERE id = #{id}
    """)
    void update(
        @Param("id") int id,
        @Param("amount") int amount,
        @Param("category") String category,
        @Param("memo") String memo,
        @Param("expenseDate") String expenseDate
    );

    @Delete("DELETE FROM expense WHERE id = #{id}")
    void delete(@Param("id") int id);


    // ✔ 월간 총합
    @Select("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
    """)
    long getMonthlyExpense(
        @Param("year") int year,
        @Param("month") int month,
        @Param("memberId") int memberId
    );

    // ✔ 월간 카테고리 합계
    @Select("""
        SELECT category AS category,
               COALESCE(SUM(amount), 0) AS totalAmount
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
        GROUP BY category
    """)
    List<CategoryExpense> getCategorySummary(
        @Param("year") int year,
        @Param("month") int month,
        @Param("memberId") int memberId
    );


    // ✔ 월간 수입
    @Select("""
        SELECT IFNULL(SUM(amount), 0)
        FROM income
        WHERE YEAR(incomeDate) = #{year}
          AND MONTH(incomeDate) = #{month}
    """)
    int getMonthlyIncome(
        @Param("year") int year,
        @Param("month") int month
    );


    // ✔ regDate 기준 월 총 소비
    @Select("""
        SELECT IFNULL(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND DATE_FORMAT(regDate, '%Y-%m') = #{yearMonth}
    """)
    int getMonthlyTotalExpense(
        @Param("memberId") int memberId,
        @Param("yearMonth") String yearMonth
    );

    // ✔ date 컬럼 오타 존재하던 메서드는 그대로 둠 (필요 없으면 삭제 가능)
    @Select("""
        SELECT IFNULL(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND DATE_FORMAT(expenseDate, '%Y-%m') = #{month}
    """)
    int getTotalByMonth(
        @Param("memberId") int memberId,
        @Param("month") String month
    );


    @Select("""
        SELECT 
            DAY(expenseDate) AS day,
            SUM(amount) AS amount
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
        GROUP BY DAY(expenseDate)
    """)
    List<Map<String, Object>> getDailyExpenseByMonth(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month
    );


    @Select("""
        SELECT IFNULL(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND expenseDate = #{date}
    """)
    int getDailyExpenseByDate(
        @Param("memberId") int memberId,
        @Param("date") String date
    );

    @Select("""
        SELECT IFNULL(SUM(amount), 0)
        FROM expense
        WHERE memberId = #{memberId}
          AND YEAR(expenseDate) = #{year}
          AND MONTH(expenseDate) = #{month}
          AND DAY(expenseDate) = #{day}
    """)
    int getDailyExpense(
        @Param("memberId") int memberId,
        @Param("year") int year,
        @Param("month") int month,
        @Param("day") int day
    );
    
    @Select("""
    	    SELECT *
    	    FROM expense
    	    WHERE memberId = #{memberId}
    	      AND DATE(regDate) = #{date}
    	    ORDER BY regDate DESC
    	""")
    	List<Expense> getExpensesByDate(int memberId, String date);
    
    @Select("""
    	    SELECT *
    	    FROM expense
    	    WHERE memberId = #{memberId}
    	    ORDER BY regDate DESC
    	""")
    	List<Expense> getExpenses(int memberId);

}
