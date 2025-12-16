package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.dto.CategorySub;

@Mapper
public interface CategorySubDao {

	@Select("""
			    SELECT id, aiKeyword
			    FROM category_sub
			    WHERE aiKeyword IS NOT NULL
			""")
	List<CategorySub> getAll();
}
