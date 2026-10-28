package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.dto.TransactionRequest;
import com.fintrack.fintrack.entity.Transaction;
import com.fintrack.fintrack.entity.User;
import com.fintrack.fintrack.repository.CategoryRepository;
import com.fintrack.fintrack.repository.TransactionRepository;
import com.fintrack.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    private User currentUser(Principal principal) {
        return userRepo.findByEmail(principal.getName()).orElseThrow();
    }

    @PostMapping
    public Transaction add(@RequestBody TransactionRequest req, Principal principal) {
        Transaction t = new Transaction();
        t.setUser(currentUser(principal));
        t.setCategory(categoryRepo.findById(req.getCategoryId()).orElseThrow());
        t.setAmount(req.getAmount());
        t.setType(req.getType());
        t.setDescription(req.getDescription());
        t.setTransactionDate(req.getTransactionDate());
        return transactionRepo.save(t);
    }

    @GetMapping
    public List<Transaction> getMine(@RequestParam(required = false) String month,
                                     Principal principal) {
        Long userId = currentUser(principal).getId();
        if (month != null) {
            YearMonth ym = YearMonth.parse(month);
            return transactionRepo.findByUserIdAndTransactionDateBetween(
                    userId, ym.atDay(1), ym.atEndOfMonth());
        }
        return transactionRepo.findByUserId(userId);
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id,
                              @RequestBody TransactionRequest req,
                              Principal principal) {
        Transaction t = findOwned(id, principal);
        t.setCategory(categoryRepo.findById(req.getCategoryId()).orElseThrow());
        t.setAmount(req.getAmount());
        t.setType(req.getType());
        t.setDescription(req.getDescription());
        t.setTransactionDate(req.getTransactionDate());
        return transactionRepo.save(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Principal principal) {
        Transaction t = findOwned(id, principal);
        transactionRepo.delete(t);
    }

    // transaction dhoondo, aur check karo ki ye isi user ki hai
    private Transaction findOwned(Long id, Principal principal) {
        Transaction t = transactionRepo.findById(id).orElseThrow();
        if (!t.getUser().getId().equals(currentUser(principal).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your transaction");
        }
        return t;
    }
}