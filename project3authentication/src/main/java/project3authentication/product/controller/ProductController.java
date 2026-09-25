package project3authentication.product.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project3authentication.product.dto.ProductPageResponseDto;
import project3authentication.product.dto.ProductRequestDto;
import project3authentication.product.dto.ProductResponseDto;
import project3authentication.product.mapper.ProductMapper;
import project3authentication.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management and catalog search")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    @Operation(summary = "Create a product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class)))
    })
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Product or category not found",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class)))
    })
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class)))
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product returned",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = project3authentication.exception.ApiError.class)))
    })
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    @Operation(summary = "List products")
    @ApiResponse(responseCode = "200", description = "Products returned",
            content = @Content(schema = @Schema(implementation = ProductPageResponseDto.class)))
    public ResponseEntity<ProductPageResponseDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "price,asc") String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(productService.getAllProducts(page, size, sort)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    @ApiResponse(responseCode = "200", description = "Matching products returned",
            content = @Content(schema = @Schema(implementation = ProductPageResponseDto.class)))
    public ResponseEntity<ProductPageResponseDto> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "price,asc") String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(productService.searchProducts(name, page, size, sort)));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products")
    @ApiResponse(responseCode = "200", description = "Filtered products returned",
            content = @Content(schema = @Schema(implementation = ProductPageResponseDto.class)))
    public ResponseEntity<ProductPageResponseDto> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "price,asc") String sort) {
        return ResponseEntity.ok(productMapper.toPageResponse(
                productService.filterProducts(categoryId, minPrice, maxPrice, page, size, sort)));
    }
}
