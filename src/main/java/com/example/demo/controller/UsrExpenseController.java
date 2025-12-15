package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.Expense;
import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Req;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.GeminiService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/expense")
public class UsrExpenseController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final GeminiService geminiService;
    private final Req req;

    /* =========================
       공통 유틸
    ========================= */
    private boolean needLogin() {
        return !req.isLogined();
    }

    private Expense loadUserExpenseOrNull(int id) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) return null;
        if (expense.getMemberId() != req.getLoginedMemberId()) return null;
        return expense;
    }

    /* =========================
       등록
    ========================= */

    @GetMapping("/write")
    public String showWrite() {
        if (needLogin())
            return "redirect:/usr/member/login?msg=login-required";
        return "usr/expense/write";
    }

    @PostMapping("/doWrite")
    @ResponseBody
    public String doWrite(
            @RequestParam int amount,
            @RequestParam(required = false) Integer subCategoryId,
            @RequestParam(required = false) String memo,
            @RequestParam String expenseDate
    ) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        Integer ruleResult = categoryService.findSubCategoryIdByKeyword(memo);

        if (ruleResult != null) {
            subCategoryId = ruleResult;
        } else if (subCategoryId == null || subCategoryId == 0) {
            subCategoryId = geminiService.subCategoryIdFromMemo(memo);
        }

        expenseService.write(
                req.getLoginedMemberId(),
                subCategoryId,
                amount,
                memo,
                expenseDate
        );

        return req.jsReplace("지출이 등록되었습니다.", "/usr/expense/list");
    }

    /* =========================
       목록
    ========================= */

    @GetMapping("/list")
    public String showList(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session
    ) {
        LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
        if (user == null)
            return "redirect:/usr/member/login";

        int memberId = user.getId();
        List<Expense> expenses;

        if (date != null && !date.isBlank() && keyword != null && !keyword.isBlank()) {
            expenses = expenseService.getExpensesByDateAndKeyword(memberId, date, keyword);
        } else if (date != null && !date.isBlank()) {
            expenses = expenseService.getExpensesByDate(memberId, date);
        } else if (keyword != null && !keyword.isBlank()) {
            expenses = expenseService.getExpensesByKeyword(memberId, keyword);
        } else {
            expenses = expenseService.getExpenses(memberId);
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("selectedDate", date);
        model.addAttribute("keyword", keyword);

        return "usr/expense/list";
    }

    /* =========================
       상세
    ========================= */

    @GetMapping("/detail")
    public String showDetail(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String date,
            HttpSession session,
            Model model
    ) {
        LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
        if (user == null)
            return "redirect:/usr/member/login";

        int memberId = user.getId();

        if (date != null) {
            model.addAttribute("expenses", expenseService.getExpensesByDate(memberId, date));
            model.addAttribute("selectedDate", date);
            return "usr/expense/list";
        }

        if (id != null) {
            Expense expense = expenseService.getExpenseById(id);
            model.addAttribute("expense", expense);
            return "usr/expense/detail";
        }

        return "redirect:/usr/expense/list";
    }

    /* =========================
       수정
    ========================= */

    @GetMapping("/modify")
    public String showModify(@RequestParam int id, Model model) {
        if (needLogin())
            return "redirect:/usr/member/login?msg=login-required";

        Expense expense = loadUserExpenseOrNull(id);
        if (expense == null)
            return req.jsHistoryBack("존재하지 않는 지출 또는 수정 권한이 없습니다.");

        model.addAttribute("expense", expense);
        return "usr/expense/modify";
    }

    @PostMapping("/doModify")
    @ResponseBody
    public String doModify(
            @RequestParam int id,
            @RequestParam(required = false) Integer subCategoryId,
            @RequestParam int amount,
            @RequestParam(required = false) String memo,
            @RequestParam String expenseDate
    ) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        Expense expense = loadUserExpenseOrNull(id);
        if (expense == null)
            return req.jsHistoryBack("존재하지 않는 지출 또는 수정 권한이 없습니다.");

        Integer ruleResult = categoryService.findSubCategoryIdByKeyword(memo);

        if (ruleResult != null) {
            subCategoryId = ruleResult;
        } else if (subCategoryId == null || subCategoryId == 0) {
            subCategoryId = geminiService.subCategoryIdFromMemo(memo);
        }

        expenseService.update(
                id,
                subCategoryId,
                amount,
                memo,
                expenseDate
        );

        return req.jsReplace("수정되었습니다.", "/usr/expense/list");
    }

    /* =========================
       삭제
    ========================= */

    @PostMapping("/doDelete")
    @ResponseBody
    public String doDelete(@RequestParam int id) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        Expense expense = loadUserExpenseOrNull(id);
        if (expense == null)
            return req.jsHistoryBack("존재하지 않는 지출 또는 삭제 권한이 없습니다.");

        expenseService.delete(id);
        return req.jsReplace("삭제되었습니다.", "/usr/expense/list");
    }
}
