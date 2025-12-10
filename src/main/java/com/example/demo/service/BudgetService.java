package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.dao.BudgetDao;
import com.example.demo.dto.Budget;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetDao budgetDao;

    public Budget getBudget(int memberId, String month) {
        return budgetDao.getBudget(memberId, month);
    }

    public void setBudget(int memberId, String month, int amount) {
        Budget budget = getBudget(memberId, month);

        if (budget == null) {
            budgetDao.insertBudget(memberId, month, amount);
        } else {
            budgetDao.updateBudget(memberId, month, amount);
        }
    }
}
