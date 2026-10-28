package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.dto.BudgetRequest;
import com.fintrack.fintrack.dto.BudgetStatus;
import com.fintrack.fintrack.entity.Budget;
import com.fintrack.fintrack.repository.UserRepository;
import com.fintrack.fintrack.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final UserRepository userRepo;

    @PostMapping
    public Budget setBudget(@RequestBody BudgetRequest req, Principal principal) {
        var user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return budgetService.setBudget(user, req);
    }

    @GetMapping("/status")
    public List<BudgetStatus> status(@RequestParam String month, Principal principal) {
        var user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return budgetService.getStatus(user.getId(), month);
    }
}