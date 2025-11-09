package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.CategoryDto;
import com.bipinkish.store.dto.ProductDto;
import com.bipinkish.store.dto.UpdateProductRequest;
import com.bipinkish.store.entities.Product;
import com.bipinkish.store.mappers.ProductMapper;
import com.bipinkish.store.repositories.CategoryRepository;
import com.bipinkish.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        if(categoryId != null) {
            var product = productRepository.findByCategoryId(categoryId);
            System.out.println("Category : "+product.get(0).getCategory().getName());
            return product.stream().map(productMapper::toDto).toList();
        }
        return productRepository.findAllWithCategory().stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getUserById(@PathVariable Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto request, UriComponentsBuilder uriBuilder) {
        var productEntity = productMapper.toEntity(request);
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        productEntity.setCategory(category);
        var newProduct = productRepository.save(productEntity);
        var productDto = productMapper.toDto(newProduct);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "productId") Long productId, @RequestBody UpdateProductRequest request) {
        var existingProduct = productRepository.findById(productId).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        productMapper.mapDtoToEntity(request, existingProduct);
        existingProduct.setCategory(category);
        productRepository.save(existingProduct);
        return ResponseEntity.ok(productMapper.toDto(existingProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "productId") Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
