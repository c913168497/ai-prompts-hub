// ProjectSkeletonRepository.java
package com.ai.prompthub.repository;

import com.ai.prompthub.entity.ProjectSkeleton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectSkeletonRepository extends JpaRepository<ProjectSkeleton, Long> {
    List<ProjectSkeleton> findByProjectIdOrderBySortOrderAsc(Long projectId);
    void deleteByProjectIdAndCategoryId(Long projectId, Long categoryId);
    void deleteByProjectId(Long projectId);
    void deleteByCategoryId(Long categoryId);

    @Query("SELECT MAX(ps.sortOrder) FROM ProjectSkeleton ps WHERE ps.projectId = :projectId")
    Integer findMaxSortOrderByProjectId(Long projectId);
}