package com.in.repositories;

import com.in.entities.ViewCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductViewCountRepository extends JpaRepository<ViewCountEntity, String> {
}
