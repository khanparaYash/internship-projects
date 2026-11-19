package org.example.catalogservice.category.controller;

import com.example.api.CategoriesApi;
import com.example.dto.CategoryRequestDto;
import com.example.dto.CategoryResponseDto;
import org.example.catalogservice.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController implements CategoriesApi {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public ResponseEntity<CategoryResponseDto> createCategory(CategoryRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @Override
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Override
    public ResponseEntity<CategoryResponseDto> getCategoryById(Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
