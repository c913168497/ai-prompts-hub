// FinalPromptSkeletonRepository.java
package com.ai.prompthub.repository;

import com.ai.prompthub.entity.FinalPromptSkeleton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinalPromptSkeletonRepository extends JpaRepository<FinalPromptSkeleton, Long> {
    List<FinalPromptSkeleton> findByFinalPromptIdOrderBySortOrderAsc(Long finalPromptId);
    void deleteByFinalPromptId(Long finalPromptId);
    void deleteByCategoryId(Long categoryId);
}