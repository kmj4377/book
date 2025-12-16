package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.dto.Budget;

@Mapper
public interface BudgetDao {

	@Select("""
			SELECT *
			FROM budget
			WHERE memberId = #{memberId}
			  AND `month` = #{month}
			""")
	Budget getBudget(@Param("memberId") int memberId, @Param("month") String month);

	@Insert("""
			INSERT INTO budget
			SET memberId = #{memberId},
			    `month` = #{month},
			    amount = #{amount}
			""")
	void insertBudget(@Param("memberId") int memberId, @Param("month") String month, @Param("amount") long amount);

	@Update("""
			UPDATE budget
			SET amount = #{amount}
			WHERE memberId = #{memberId}
			  AND `month` = #{month}
			""")
	void updateBudget(@Param("memberId") int memberId, @Param("month") String month, @Param("amount") int amount);
}
