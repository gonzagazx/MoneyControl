package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.CategoryRequest;
import com.example.MoneyControl.dto.CategoryResponse;
import com.example.MoneyControl.entity.Category;
import com.example.MoneyControl.exception.ResourceNotFoundException;
import com.example.MoneyControl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));

        category.setName(request.name());
        category.setType(request.type());

        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada: " + id);
        }
        categoryRepository.deleteById(id);
    }
}