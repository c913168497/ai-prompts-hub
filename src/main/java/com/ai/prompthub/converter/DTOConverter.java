// DTOConverter.java
package com.ai.prompthub.converter;

import com.ai.prompthub.dto.*;
import com.ai.prompthub.entity.*;
import com.ai.prompthub.repository.GlobalCategoryRepository;
import com.ai.prompthub.repository.ProjectSkeletonRepository;
import com.ai.prompthub.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DTOConverter {

    private final GlobalCategoryRepository categoryRepository;
    private final ProjectSkeletonRepository projectSkeletonRepository;
    private final PromptRepository promptRepository;

    // Project 转换
    public ProjectDTO toProjectDTO(Project project) {
        if (project == null) return null;

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setGitName(project.getGitName());
        dto.setGitRepo(project.getGitRepo());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

    public List<ProjectDTO> toProjectDTOList(List<Project> projects) {
        return projects.stream()
                .map(this::toProjectDTO)
                .collect(Collectors.toList());
    }

    public ProjectDetailDTO toProjectDetailDTO(Project project) {
        if (project == null) return null;

        ProjectDetailDTO dto = new ProjectDetailDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setGitRepo(project.getGitRepo());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        dto.setGitName(project.getGitName());

        // 查询并转换关联的 skeletons
        List<ProjectSkeleton> skeletons = projectSkeletonRepository.findByProjectIdOrderBySortOrderAsc(project.getId());
        dto.setSkeletons(toSkeletonDTOList(skeletons));

        // 查询并转换关联的 prompts
        List<Prompt> prompts = promptRepository.findByProjectId(project.getId());
        dto.setPrompts(toPromptDTOList(prompts));

        return dto;
    }

    // Category 转换
    public CategoryDTO toCategoryDTO(GlobalCategory category) {
        if (category == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    public List<CategoryDTO> toCategoryDTOList(List<GlobalCategory> categories) {
        return categories.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    // Skeleton 转换
    public SkeletonDTO toSkeletonDTO(ProjectSkeleton skeleton) {
        if (skeleton == null) return null;

        SkeletonDTO dto = new SkeletonDTO();
        dto.setId(skeleton.getId());

        // 查询分类信息
        GlobalCategory category = categoryRepository.findById(skeleton.getCategoryId()).orElse(null);
        dto.setCategory(toCategoryDTO(category));
        dto.setSortOrder(skeleton.getSortOrder());
        dto.setCreatedAt(skeleton.getCreatedAt());
        return dto;
    }

    public List<SkeletonDTO> toSkeletonDTOList(List<ProjectSkeleton> skeletons) {
        if (skeletons == null || skeletons.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量查询所有分类，减少数据库查询次数
        Set<Long> categoryIds = skeletons.stream()
                .map(ProjectSkeleton::getCategoryId)
                .collect(Collectors.toSet());

        Map<Long, GlobalCategory> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(GlobalCategory::getId, c -> c));

        return skeletons.stream()
                .map(skeleton -> {
                    SkeletonDTO dto = new SkeletonDTO();
                    dto.setId(skeleton.getId());
                    dto.setCategory(toCategoryDTO(categoryMap.get(skeleton.getCategoryId())));
                    dto.setSortOrder(skeleton.getSortOrder());
                    dto.setCreatedAt(skeleton.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Prompt 转换
    public PromptDTO toPromptDTO(Prompt prompt) {
        if (prompt == null) return null;

        PromptDTO dto = new PromptDTO();
        dto.setId(prompt.getId());
        dto.setTitle(prompt.getTitle());
        dto.setContent(prompt.getContent());

        // 查询分类信息
        GlobalCategory category = categoryRepository.findById(prompt.getCategoryId()).orElse(null);
        dto.setCategory(toCategoryDTO(category));
        dto.setIsSelected(prompt.getIsSelected());
        dto.setCreatedAt(prompt.getCreatedAt());
        dto.setUpdatedAt(prompt.getUpdatedAt());
        return dto;
    }

    public List<PromptDTO> toPromptDTOList(List<Prompt> prompts) {
        if (prompts == null || prompts.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量查询所有分类
        Set<Long> categoryIds = prompts.stream()
                .map(Prompt::getCategoryId)
                .collect(Collectors.toSet());

        Map<Long, GlobalCategory> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(GlobalCategory::getId, c -> c));

        return prompts.stream()
                .map(prompt -> {
                    PromptDTO dto = new PromptDTO();
                    dto.setId(prompt.getId());
                    dto.setTitle(prompt.getTitle());
                    dto.setContent(prompt.getContent());
                    dto.setCategory(toCategoryDTO(categoryMap.get(prompt.getCategoryId())));
                    dto.setIsSelected(prompt.getIsSelected());
                    dto.setCreatedAt(prompt.getCreatedAt());
                    dto.setUpdatedAt(prompt.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public FinalPromptDTO toFinalPromptDTO(FinalPrompt finalPrompt) {
        if (finalPrompt == null) return null;

        FinalPromptDTO dto = new FinalPromptDTO();
        dto.setId(finalPrompt.getId());
        dto.setProjectId(finalPrompt.getProjectId());
        dto.setName(finalPrompt.getName());
        dto.setUuidCode(finalPrompt.getUuidCode());
        dto.setCreatedAt(finalPrompt.getCreatedAt());
        dto.setUpdatedAt(finalPrompt.getUpdatedAt());
        return dto;
    }
}