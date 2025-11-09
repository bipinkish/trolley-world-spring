package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.CategoryDto;
import com.bipinkish.store.dto.UserDto;
import com.bipinkish.store.mappers.CategoryMapper;
import com.bipinkish.store.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getUserById(@PathVariable Byte categoryId) {
        var category = categoryRepository.findById(categoryId).orElse(null);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto, UriComponentsBuilder uriBuilder) {
        var entity = categoryMapper.toEntity(categoryDto);
        var uri = uriBuilder.path("/categories/{id}").buildAndExpand(entity.getId()).toUri();
        var newCategory = categoryRepository.save(entity);
        return ResponseEntity.created(uri).body(categoryMapper.toDto(newCategory));
    }

}
