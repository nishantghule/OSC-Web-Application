package com.in.repositories;

import com.in.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionDataRepository extends JpaRepository<SessionEntity, String>{
}
