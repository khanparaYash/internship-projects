package project3authentication.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import project3authentication.product.dto.ProductRequestDto;
import project3authentication.product.dto.ProductPageResponseDto;
import project3authentication.product.dto.ProductResponseDto;
import project3authentication.category.entity.Category;
import project3authentication.product.entity.Product;

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
