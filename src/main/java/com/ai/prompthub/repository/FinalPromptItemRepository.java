// FinalPromptItemRepository.java
package com.ai.prompthub.repository;

import com.ai.prompthub.entity.FinalPromptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinalPromptItemRepository extends JpaRepository<FinalPromptItem, Long> {
    List<FinalPromptItem> findByFinalPromptId(Long finalPromptId);
    void deleteByFinalPromptId(Long finalPromptId);
    void deleteByPromptId(Long promptId);
}