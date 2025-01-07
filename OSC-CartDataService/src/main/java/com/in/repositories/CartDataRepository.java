package com.in.repositories;

import com.in.entities.CartDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDataRepository extends JpaRepository<CartDataEntity, String> {
}
