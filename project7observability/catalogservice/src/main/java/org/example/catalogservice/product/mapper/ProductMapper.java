package org.example.catalogservice.product.mapper;

import org.example.catalogservice.category.entity.Category;
import org.example.catalogservice.product.dto.ProductPageResponseDto;
import org.example.catalogservice.product.dto.ProductRequestDto;
import org.example.catalogservice.product.dto.ProductResponseDto;
import org.example.catalogservice.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", expression = "java(mapCategoryToName(product.getCategory()))")
    ProductResponseDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto request);

    @Mapping(source = "number", target = "page")
    ProductPageResponseDto toPageResponse(Page<ProductResponseDto> products);

    default String mapCategoryToName(Category category) {
        return category == null ? null : category.getName();
    }
}
