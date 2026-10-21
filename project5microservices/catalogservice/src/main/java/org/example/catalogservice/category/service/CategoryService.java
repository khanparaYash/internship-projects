package org.example.catalogservice.category.service;

import jakarta.transaction.Transactional;
import org.example.catalogservice.category.dto.CategoryRequestDto;
import org.example.catalogservice.category.dto.CategoryResponseDto;
import org.example.catalogservice.category.entity.Category;
import org.example.catalogservice.category.mapper.CategoryMapper;

import org.example.catalogservice.category.repository.CategoryRepository;
import org.example.catalogservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;

    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category category =categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }



}
