package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class CalendarResult {

    private int year;
    private int month;

    private int prevYear;
    private int prevMonth;

    private int nextYear;
    private int nextMonth;

    private List<List<CalendarDay>> weeks;
}
