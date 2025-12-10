package com.example.demo.dto;

import lombok.Data;

@Data
public class CalendarDay {

    private Integer day;
    private boolean today;
    private boolean currentMonth;
    private int expenseTotal;

    private boolean holiday;       
    private String holidayName;    

    public static CalendarDay of(int day, boolean isToday, int expenseTotal) {
        CalendarDay d = new CalendarDay();
        d.day = day;
        d.today = isToday;
        d.currentMonth = true;
        d.expenseTotal = expenseTotal;
        return d;
    }

    public static CalendarDay empty() {
        CalendarDay d = new CalendarDay();
        d.day = null;
        d.today = false;
        d.currentMonth = false;
        d.expenseTotal = 0;
        return d;
    }
}
