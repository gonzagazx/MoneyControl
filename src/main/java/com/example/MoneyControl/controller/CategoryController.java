package com.example.MoneyControl.controller;

import com.example.MoneyControl.dto.CategoryRequest;
import com.example.MoneyControl.dto.CategoryResponse;
import com.example.MoneyControl.entity.User;
import com.example.MoneyControl.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request,
                                                   @AuthenticationPrincipal User user) {
        CategoryResponse response = categoryService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<CategoryResponse> findAll(@AuthenticationPrincipal User user) {
        return categoryService.findAll(user);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @Valid @RequestBody CategoryRequest request,
                                   @AuthenticationPrincipal User user) {
        return categoryService.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        categoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}