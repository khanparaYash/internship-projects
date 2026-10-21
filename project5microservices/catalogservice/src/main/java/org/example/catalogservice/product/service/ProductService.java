package org.example.catalogservice.product.service;

import jakarta.transaction.Transactional;
import org.example.catalogservice.category.entity.Category;
import org.example.catalogservice.category.repository.CategoryRepository;
import org.example.catalogservice.exception.ResourceNotFoundException;
import org.example.catalogservice.product.dto.ProductRequestDto;
import org.example.catalogservice.product.dto.ProductResponseDto;
import org.example.catalogservice.product.entity.Product;
import org.example.catalogservice.product.mapper.ProductMapper;
import org.example.catalogservice.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        Product updated = productMapper.toEntity(request);
        // preserve id and any other fields from existing as needed
        updated.setId(existing.getId());
        updated.setCategory(category);

        Product saved = productRepository.save(updated);
        return productMapper.toDto(saved);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    public Page<ProductResponseDto> getAllProducts(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    public Page<ProductResponseDto> searchProducts(String name, int page, int size, String sort) {
        String normalizedName = name == null ? "" : name.trim();
        Pageable pageable = createPageable(page, size, sort);
        if (normalizedName.isEmpty()) {
            return getAllProducts(page, size, sort);
        }

        return productRepository.findByNameContainingIgnoreCase(normalizedName, pageable)
                .map(productMapper::toDto);
    }

    public Page<ProductResponseDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        if (categoryId == null && minPrice == null && maxPrice == null) {
            return getAllProducts(page, size, sort);
        }

        return productRepository.findProductsByFilters(categoryId, null, minPrice, maxPrice, pageable)
                .map(productMapper::toDto);
    }

    private Pageable createPageable(int page, int size, String sort) {
        String sortValue = (sort == null || sort.isBlank()) ? "price,asc" : sort;
        String[] sortParts = sortValue.split(",");
        String sortField = sortParts.length > 0 && !sortParts[0].isBlank() ? sortParts[0].trim() : "price";
        String direction = sortParts.length > 1 && !sortParts[1].isBlank() ? sortParts[1].trim() : "asc";

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
    }
}
