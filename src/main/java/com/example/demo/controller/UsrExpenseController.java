package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.Expense;
import com.example.demo.dto.LoginedMember;
import com.example.demo.dto.Req;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.GeminiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/expense")
public class UsrExpenseController {

    private final ExpenseService expenseService;
    private final GeminiService geminiService;
    private final Req req;

    private boolean needLogin() {
        return !req.isLogined();
    }

    private Expense loadUserExpenseOrNull(int id) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) return null;
        if (expense.getMemberId() != req.getLoginedMemberId()) return null;
        return expense;
    }

    @GetMapping("/write")
    public String showWrite() {
        if (needLogin())
            return "redirect:/usr/member/login?msg=login-required";
        return "usr/expense/write";
    }

    @PostMapping("/doWrite")
    @ResponseBody
    public String doWrite(int amount, String category, String memo, String expenseDate) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        if (category == null || category.isBlank() || category.equals("auto"))
            category = geminiService.categoryFromMemo(memo);

        expenseService.write(req.getLoginedMemberId(), amount, category, memo, expenseDate);
        return req.jsReplace("지출이 등록되었습니다.", "/usr/expense/list");
    }

    @GetMapping("/list")
    public String showList(
            @RequestParam(required = false) String date,
            Model model,
            HttpSession session
    ) {

        LoginedMember user = (LoginedMember) session.getAttribute("loginedMember");
        if (user == null)
            return "redirect:/usr/member/login";

        int memberId = user.getId();
        List<Expense> expenses = (date != null)
                ? expenseService.getExpensesByDate(memberId, date)
                : expenseService.getExpenses(memberId);

        model.addAttribute("expenses", expenses);
        model.addAttribute("selectedDate", date);

        return "usr/expense/list";
    }

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
    public String doModify(int id, int amount, String category, String memo, String expenseDate) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        Expense expense = loadUserExpenseOrNull(id);
        if (expense == null)
            return req.jsHistoryBack("존재하지 않는 지출 또는 수정 권한이 없습니다.");

        if ("auto".equals(category))
            category = geminiService.categoryFromMemo(memo);

        expenseService.update(id, amount, category, memo, expenseDate);

        return req.jsReplace("수정되었습니다.", "/usr/expense/list");
    }

    @PostMapping("/doDelete")
    @ResponseBody
    public String doDelete(int id) {
        if (needLogin())
            return req.jsReplace("로그인 후 이용해주세요.", "/usr/member/login");

        Expense expense = loadUserExpenseOrNull(id);
        if (expense == null)
            return req.jsHistoryBack("존재하지 않는 지출 또는 삭제 권한이 없습니다.");

        expenseService.delete(id);
        return req.jsReplace("삭제되었습니다.", "/usr/expense/list");
    }
}
