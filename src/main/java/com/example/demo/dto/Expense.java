package com.example.demo.dto;

import lombok.Data;

@Data
public class Expense {

	private int id;
	private String regDate;
	private String updateDate;
	private int memberId;
	private int subCategoryId;
	private int amount;
	private String memo;
	private String expenseDate;

	private String mainCategoryName;
	private String subCategoryName;
	private String mainCategoryColor;
}
