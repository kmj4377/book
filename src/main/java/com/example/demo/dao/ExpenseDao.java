package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.example.demo.dto.CategoryExpense;
import com.example.demo.dto.Expense;

@Mapper
public interface ExpenseDao {

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
	void write(@Param("subCategoryId") int subCategoryId, @Param("memberId") int memberId, @Param("amount") int amount,
			@Param("memo") String memo, @Param("expenseDate") String expenseDate);

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
			    ORDER BY e.expenseDate DESC, e.id DESC
			""")
	List<Expense> getExpenses(@Param("memberId") int memberId);

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
			    ORDER BY e.expenseDate DESC, e.id DESC
			""")
	List<Expense> getExpensesByDate(@Param("memberId") int memberId, @Param("date") String date);

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

	@Update("""
			    UPDATE expense
			    SET updateDate = NOW(),
			        subCategoryId = #{subCategoryId},
			        amount = #{amount},
			        memo = #{memo},
			        expenseDate = #{expenseDate}
			    WHERE id = #{id}
			""")
	void update(@Param("id") int id, @Param("subCategoryId") int subCategoryId, @Param("amount") int amount,
			@Param("memo") String memo, @Param("expenseDate") String expenseDate);

	@Delete("""
			    DELETE FROM expense
			    WHERE id = #{id}
			""")
	void delete(@Param("id") int id);

	@Select("""
			    SELECT COALESCE(SUM(amount), 0)
			    FROM expense
			    WHERE memberId = #{memberId}
			      AND YEAR(expenseDate) = #{year}
			      AND MONTH(expenseDate) = #{month}
			""")
	int getMonthlyTotalExpense(@Param("memberId") int memberId, @Param("year") int year, @Param("month") int month);

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
	List<CategoryExpense> getCategorySummary(@Param("memberId") int memberId, @Param("year") int year,
			@Param("month") int month);

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
	List<Map<String, Object>> getDailyExpenseByMonth(@Param("memberId") int memberId, @Param("year") int year,
			@Param("month") int month);

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
			    ORDER BY e.expenseDate DESC, e.id DESC
			""")
	List<Expense> getExpensesByKeyword(@Param("memberId") int memberId, @Param("keyword") String keyword);

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
			    ORDER BY e.expenseDate DESC, e.id DESC
			""")
	List<Expense> getExpensesByDateAndKeyword(@Param("memberId") int memberId, @Param("date") String date,
			@Param("keyword") String keyword);
}
