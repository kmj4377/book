package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BudgetStatDao {

	@Select("""
			    SELECT amount
			    FROM budget
			    WHERE memberId = #{memberId}
			      AND `month` = #{month}
			""")
	Integer getBudget(@Param("memberId") int memberId, @Param("month") String month);

	@Select("""
			    SELECT COALESCE(SUM(amount), 0)
			    FROM expense
			    WHERE memberId = #{memberId}
			      AND expenseDate LIKE CONCAT(#{month}, '%')
			""")
	int getTotalExpenseOfMonth(@Param("memberId") int memberId, @Param("month") String month);

	@Select("""
			    SELECT
			        LPAD(SUBSTRING(`month`, 6, 2), 2, '0') AS month,
			        amount
			    FROM budget
			    WHERE memberId = #{memberId}
			      AND SUBSTRING(`month`, 1, 4) = #{year}
			    ORDER BY month
			""")
	List<Map<String, Object>> getBudgetByYear(@Param("memberId") int memberId, @Param("year") int year);

	@Select("""
			    SELECT
			        LPAD(SUBSTRING(expenseDate, 6, 2), 2, '0') AS month,
			        SUM(amount) AS total
			    FROM expense
			    WHERE memberId = #{memberId}
			      AND SUBSTRING(expenseDate, 1, 4) = #{year}
			    GROUP BY month
			    ORDER BY month
			""")
	List<Map<String, Object>> getExpenseByYear(@Param("memberId") int memberId, @Param("year") int year);

}
