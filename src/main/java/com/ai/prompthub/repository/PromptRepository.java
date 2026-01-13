// PromptRepository.java
package com.ai.prompthub.repository;

import com.ai.prompthub.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByProjectId(Long projectId);
    List<Prompt> findByProjectIdAndCategoryId(Long projectId, Long categoryId);
    List<Prompt> findByProjectIdAndIsSelectedTrue(Long projectId);
    void deleteByProjectId(Long projectId);
    void deleteByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Prompt p SET p.isSelected = :selected WHERE p.id = :id")
    void updateSelection(Long id, Boolean selected);
}
