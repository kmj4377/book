package com.example.demo.dto;

import lombok.Data;

@Data
public class Expense {

    /* expense 테이블 */
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private int subCategoryId;
    private int amount;
    private String memo;
    private String expenseDate;

    /* JOIN 결과 */
    private String mainCategoryName;
    private String subCategoryName;
    private String mainCategoryColor;
}
