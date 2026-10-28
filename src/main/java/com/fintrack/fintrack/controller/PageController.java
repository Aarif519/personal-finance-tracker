package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.dto.CategoryTotal;
import com.fintrack.fintrack.dto.SummaryResponse;
import com.fintrack.fintrack.entity.User;
import com.fintrack.fintrack.repository.UserRepository;
import com.fintrack.fintrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ReportService reportService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        if (userRepo.existsByEmail(email)) {
            return "redirect:/register?error";
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/dashboard")
public String dashboard(@RequestParam(required = false) String month,
                        Principal principal, Model model) {
    User user = userRepo.findByEmail(principal.getName()).orElseThrow();

    if (month == null) {
        month = YearMonth.now().toString();
    }

    SummaryResponse summary = reportService.getSummary(user.getId(), month);
    List<CategoryTotal> breakdown = reportService.getCategoryBreakdown(user.getId(), month);

    // Savings rate: income ka kitna % bacha
    int savingsRate = 0;
    if (summary.getTotalIncome().signum() > 0) {
        savingsRate = summary.getBalance().multiply(BigDecimal.valueOf(100))
                .divide(summary.getTotalIncome(), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    // Sabse zyada kharch wali category
    if (!breakdown.isEmpty()) {
        CategoryTotal top = breakdown.get(0);
        int topPercent = top.getTotal().multiply(BigDecimal.valueOf(100))
                .divide(summary.getTotalExpense(), 0, RoundingMode.HALF_UP)
                .intValue();
        model.addAttribute("topCategory", top);
        model.addAttribute("topPercent", topPercent);
    }

    model.addAttribute("name", user.getName());
    model.addAttribute("month", month);
    model.addAttribute("summary", summary);
    model.addAttribute("savingsRate", savingsRate);
    model.addAttribute("labels", breakdown.stream().map(CategoryTotal::getCategory).toList());
    model.addAttribute("values", breakdown.stream().map(CategoryTotal::getTotal).toList());
    return "dashboard";
}
}