package com.in.repositories;

import com.in.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDataRepository extends JpaRepository<ProductEntity, String> {
}
