package org.example.catalogservice.category.mapper;

import org.example.catalogservice.category.dto.CategoryRequestDto;
import org.example.catalogservice.category.dto.CategoryResponseDto;
import org.example.catalogservice.category.entity.Category;
import org.example.catalogservice.product.mapper.ProductMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = ProductMapper.class)
public interface CategoryMapper {

//    @Mapping(target = "products", ignore = true)
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> toDtoList(List<Category> categories);
}
