package com.bipinkish.store.dto;

import com.bipinkish.store.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
    private Category category;
}
