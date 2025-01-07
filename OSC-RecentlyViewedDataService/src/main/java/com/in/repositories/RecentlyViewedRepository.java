package com.in.repositories;

import com.in.entities.RecentlyViewedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewedEntity, String> {
}
