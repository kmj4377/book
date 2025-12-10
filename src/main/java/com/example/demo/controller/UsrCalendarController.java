package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.CalendarResult;
import com.example.demo.dto.Req;
import com.example.demo.service.CalendarService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/expense")
public class UsrCalendarController {

    private final CalendarService calendarService;
    private final Req req;

    @GetMapping("/calendar")
    public String showCalendar(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            HttpSession session,
            Model model
    ) {

        LocalDate now = LocalDate.now();
        if (year == null || month == null) {
            year = now.getYear();
            month = now.getMonthValue();
        }

        LoginedMember loginedMember = (LoginedMember) session.getAttribute("loginedMember");
        int memberId = (loginedMember != null) ? loginedMember.getId() : 0;

        CalendarResult calendar = calendarService.getCalendar(memberId, year, month);

        model.addAttribute("calendar", calendar);
        return "usr/expense/calendar";
    }
}
