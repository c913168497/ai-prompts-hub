// ProjectSkeletonService.java
package com.ai.prompthub.service;

import com.ai.prompthub.entity.ProjectSkeleton;
import com.ai.prompthub.repository.ProjectSkeletonRepository;
import com.ai.prompthub.repository.ProjectRepository;
import com.ai.prompthub.repository.GlobalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectSkeletonService {

    private final ProjectSkeletonRepository skeletonRepository;
    private final ProjectRepository projectRepository;
    private final GlobalCategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProjectSkeleton> findByProjectId(Long projectId) {
        return skeletonRepository.findByProjectIdOrderBySortOrderAsc(projectId);
    }

    @Transactional
    public ProjectSkeleton addCategoryToProject(Long projectId, Long categoryId) {
        // 验证项目和分类是否存在
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("项目不存在");
        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("分类不存在");
        }

        // 检查是否已存在该分类
        List<ProjectSkeleton> existingSkeletons = skeletonRepository.findByProjectIdOrderBySortOrderAsc(projectId);
        boolean alreadyExists = existingSkeletons.stream()
                .anyMatch(s -> s.getCategoryId().equals(categoryId));

        if (alreadyExists) {
            throw new RuntimeException("该分类已在骨架中");
        }

        // 获取当前最大排序号
        Integer maxSortOrder = skeletonRepository.findMaxSortOrderByProjectId(projectId);
        int newSortOrder = (maxSortOrder == null) ? 1 : maxSortOrder + 1;

        // 创建新骨架
        ProjectSkeleton skeleton = new ProjectSkeleton();
        skeleton.setProjectId(projectId);
        skeleton.setCategoryId(categoryId);
        skeleton.setSortOrder(newSortOrder);
        return skeletonRepository.save(skeleton);
    }

    @Transactional
    public List<ProjectSkeleton> updateSkeletonOrder(Long projectId, List<Long> categoryIds) {
        // 验证项目是否存在
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("项目不存在");
        }

        // 验证所有分类是否存在
        for (Long categoryId : categoryIds) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new RuntimeException("分类不存在: " + categoryId);
            }
        }

        // 删除项目的所有旧骨架
        skeletonRepository.deleteByProjectId(projectId);

        // 按新顺序创建骨架
        for (int i = 0; i < categoryIds.size(); i++) {
            ProjectSkeleton skeleton = new ProjectSkeleton();
            skeleton.setProjectId(projectId);
            skeleton.setCategoryId(categoryIds.get(i));
            skeleton.setSortOrder(i + 1);
            skeletonRepository.save(skeleton);
        }

        return skeletonRepository.findByProjectIdOrderBySortOrderAsc(projectId);
    }

    @Transactional
    public void removeFromSkeleton(Long projectId, Long categoryId) {
        // 验证项目是否存在
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("项目不存在");
        }

        // 删除指定的骨架项
        skeletonRepository.deleteByProjectIdAndCategoryId(projectId, categoryId);

        // 重新排序剩余的骨架项
        List<ProjectSkeleton> remainingSkeletons = skeletonRepository.findByProjectIdOrderBySortOrderAsc(projectId);
        for (int i = 0; i < remainingSkeletons.size(); i++) {
            ProjectSkeleton skeleton = remainingSkeletons.get(i);
            skeleton.setSortOrder(i + 1);
            skeletonRepository.save(skeleton);
        }
    }

    @Transactional
    public ProjectSkeleton create(Long projectId, Long categoryId, Integer sortOrder) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("项目不存在");
        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("分类不存在");
        }

        ProjectSkeleton skeleton = new ProjectSkeleton();
        skeleton.setProjectId(projectId);
        skeleton.setCategoryId(categoryId);
        skeleton.setSortOrder(sortOrder);
        return skeletonRepository.save(skeleton);
    }

    @Transactional
    public void delete(Long id) {
        skeletonRepository.deleteById(id);
    }
}