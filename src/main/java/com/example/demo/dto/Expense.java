package com.example.demo.dto;

import lombok.Data;

@Data
public class Expense {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private int amount;
    private String category;
    private String memo;
    private String expenseDate;
}
