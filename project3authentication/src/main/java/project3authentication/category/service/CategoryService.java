package project3authentication.category.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import project3authentication.category.mapper.CategoryMapper;
import project3authentication.category.dto.CategoryRequestDto;
import project3authentication.category.dto.CategoryResponseDto;
import project3authentication.category.entity.Category;
import project3authentication.exception.ResourceNotFoundException;
import project3authentication.category.repository.CategoryRepository;

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

        Category category =categoryMapper.toEntity(request) ;
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

//    private CategoryResponseDto toDto(Category category) {
//        CategoryResponseDto baseDto = categoryMapper.toDto(category);
//
//        List<ProductResponseDto> products = category.getProducts() == null ? List.of() : category.getProducts().stream()
//                .map(productMapper::toDto)
//                .toList();
//
//        return new CategoryResponseDto(
//                baseDto.id(),
//                baseDto.name(),
//                baseDto.description(),
//                products
//        );
//    }

//    private ProductResponseDto toProductDto(Product product) {
//        return new ProductResponseDto(
//                product.getId(),
//                product.getName(),
//                product.getPrice(),
//                product.getQuantity(),
//                product.getCategory() != null ? product.getCategory().getName() : null,
//                product.getImageUrl()
//        );
//    }
}
