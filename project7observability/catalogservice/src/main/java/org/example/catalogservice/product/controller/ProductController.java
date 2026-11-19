package org.example.catalogservice.product.controller;

import com.example.api.ProductsApi;
import com.example.dto.ProductPageResponseDto;
import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import org.example.catalogservice.product.mapper.ProductMapper;
import org.example.catalogservice.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductsApi {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<ProductResponseDto> createProduct(ProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Override
    public ResponseEntity<ProductResponseDto> updateProduct(Long id, ProductRequestDto request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Override
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<String> delay(Integer ms)  {
//        Thread.sleep(ms);
        return ResponseEntity.ok("delay completed");
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
//                Thread.sleep(6000);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductPageResponseDto> getAllProducts(Integer page, Integer size, String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(productService.getAllProducts(page, size, sort)));
    }

    @Override
    public ResponseEntity<ProductPageResponseDto> searchProducts(String name, Integer page, Integer size, String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(productService.searchProducts(name, page, size, sort)));
    }

    @Override
    public ResponseEntity<ProductPageResponseDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, Integer page, Integer size, String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(productService.filterProducts(categoryId, minPrice, maxPrice, page, size, sort)));
    }
}
