package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.entity.Category;
import com.fintrack.fintrack.entity.Transaction;
import com.fintrack.fintrack.entity.User;
import com.fintrack.fintrack.repository.CategoryRepository;
import com.fintrack.fintrack.repository.TransactionRepository;
import com.fintrack.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionPageController {

    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    @GetMapping
    public String page(@RequestParam(required = false) String month,
                       Principal principal, Model model) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();

        if (month == null) {
            month = YearMonth.now().toString();
        }
        YearMonth ym = YearMonth.parse(month);

        List<Transaction> list = new ArrayList<>(
                transactionRepo.findByUserIdAndTransactionDateBetween(
                        user.getId(), ym.atDay(1), ym.atEndOfMonth()));

        // naye se purane: pehle date, phir time
        Comparator<Transaction> byDateTime = Comparator
                .comparing(Transaction::getTransactionDate)
                .thenComparing(t -> t.getTransactionTime() == null
                        ? LocalTime.MIN : t.getTransactionTime());
        list.sort(byDateTime.reversed());

        model.addAttribute("transactions", list);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("month", month);
        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("nowTime", LocalTime.now().withSecond(0).withNano(0).toString());
        return "transactions";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long categoryId,
                      @RequestParam BigDecimal amount,
                      @RequestParam String description,
                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime transactionTime,
                      Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        Category category = categoryRepo.findById(categoryId).orElseThrow();

        Transaction t = new Transaction();
        t.setUser(user);
        t.setCategory(category);
        t.setAmount(amount);
        t.setType(category.getType());
        t.setDescription(description);
        t.setTransactionDate(transactionDate);
        t.setTransactionTime(transactionTime);
        transactionRepo.save(t);

        return "redirect:/transactions?month=" + YearMonth.from(transactionDate);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam String month,
                         Principal principal) {
        Transaction t = transactionRepo.findById(id).orElseThrow();
        if (t.getUser().getEmail().equals(principal.getName())) {
            transactionRepo.delete(t);
        }
        return "redirect:/transactions?month=" + month;
    }
}