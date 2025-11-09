package com.bipinkish.store.dto;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private String price;
    private Byte categoryId;
}
