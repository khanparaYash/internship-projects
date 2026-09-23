package project3authentication.category.mapper;

import org.mapstruct.Mapper;
import project3authentication.category.dto.CategoryRequestDto;
import project3authentication.category.dto.CategoryResponseDto;
import project3authentication.category.entity.Category;
import project3authentication.product.mapper.ProductMapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = ProductMapper.class)
public interface CategoryMapper {

//    @Mapping(target = "products", ignore = true)
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> toDtoList(List<Category> categories);
}
