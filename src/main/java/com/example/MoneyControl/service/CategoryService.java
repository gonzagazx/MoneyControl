package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.CategoryRequest;
import com.example.MoneyControl.dto.CategoryResponse;
import com.example.MoneyControl.entity.Category;
import com.example.MoneyControl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setType(request.type());

        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }
}