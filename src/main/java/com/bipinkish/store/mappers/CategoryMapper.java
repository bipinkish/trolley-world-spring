package com.bipinkish.store.mappers;

import com.bipinkish.store.dto.CategoryDto;
import com.bipinkish.store.dto.ProductDto;
import com.bipinkish.store.entities.Category;
import com.bipinkish.store.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto productDto);
}
