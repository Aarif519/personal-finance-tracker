package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.dto.CategoryTotal;
import com.fintrack.fintrack.dto.SummaryResponse;
import com.fintrack.fintrack.repository.UserRepository;
import com.fintrack.fintrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepo;

    private Long currentUserId(Principal principal) {
        return userRepo.findByEmail(principal.getName()).orElseThrow().getId();
    }

    @GetMapping("/summary")
    public SummaryResponse summary(@RequestParam String month, Principal principal) {
        return reportService.getSummary(currentUserId(principal), month);
    }

    @GetMapping("/category-breakdown")
    public List<CategoryTotal> categoryBreakdown(@RequestParam String month, Principal principal) {
        return reportService.getCategoryBreakdown(currentUserId(principal), month);
    }
}