package com.bipinkish.store.repositories;

import com.bipinkish.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Byte> {
}