package com.bipinkish.store.mappers;

import com.bipinkish.store.dto.ProductDto;
import com.bipinkish.store.dto.UpdateProductRequest;
import com.bipinkish.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto productDto);
    void mapDtoToEntity(UpdateProductRequest request, @MappingTarget Product product);
}
