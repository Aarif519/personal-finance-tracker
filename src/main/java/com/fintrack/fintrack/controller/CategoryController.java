package com.fintrack.fintrack.controller;

import com.fintrack.fintrack.entity.Category;
import com.fintrack.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepo;

    @PostMapping
    public Category add(@RequestBody Category category) {
        return categoryRepo.save(category);
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }
}