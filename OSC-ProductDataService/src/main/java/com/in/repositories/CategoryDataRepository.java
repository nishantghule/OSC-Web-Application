package com.in.repositories;

import com.in.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDataRepository extends JpaRepository<CategoryEntity, String> {
}
