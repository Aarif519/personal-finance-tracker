package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.dto.BudgetRequest;
import com.fintrack.fintrack.entity.TransactionType;
import com.fintrack.fintrack.entity.User;
import com.fintrack.fintrack.repository.CategoryRepository;
import com.fintrack.fintrack.repository.UserRepository;
import com.fintrack.fintrack.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.YearMonth;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetPageController {

    private final BudgetService budgetService;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    @GetMapping
    public String page(@RequestParam(required = false) String month,
                       Principal principal, Model model) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();

        if (month == null) {
            month = YearMonth.now().toString();
        }

        model.addAttribute("statuses", budgetService.getStatus(user.getId(), month));
        model.addAttribute("categories", categoryRepo.findAll().stream()
                .filter(c -> c.getType() == TransactionType.EXPENSE)
                .toList());
        model.addAttribute("month", month);
        return "budgets";
    }

    @PostMapping("/set")
    public String set(@RequestParam Long categoryId,
                      @RequestParam BigDecimal monthlyLimit,
                      @RequestParam String month,
                      Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();

        BudgetRequest req = new BudgetRequest();
        req.setCategoryId(categoryId);
        req.setMonthlyLimit(monthlyLimit);
        req.setMonth(month);
        budgetService.setBudget(user, req);

        return "redirect:/budgets?month=" + month;
    }
}
