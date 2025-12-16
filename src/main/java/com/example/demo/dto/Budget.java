package com.example.demo.dto;

import lombok.Data;

@Data
public class Budget {
	private int id;
	private int memberId;
	private String month;
	private int amount;
	private String regDate;
	private String updateDate;
}
