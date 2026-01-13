package com.ai.prompthub.repository;

import com.ai.prompthub.entity.GlobalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GlobalCategoryRepository extends JpaRepository<GlobalCategory, Long> {
    Optional<GlobalCategory> findByName(String name);
    boolean existsByName(String name);
}