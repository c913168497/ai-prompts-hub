// FinalPromptService.java
package com.ai.prompthub.service;

import com.ai.prompthub.dto.CategoryDTO;
import com.ai.prompthub.dto.FinalPromptDTO;
import com.ai.prompthub.dto.SkeletonDTO;
import com.ai.prompthub.entity.*;
import com.ai.prompthub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinalPromptService {

    private final FinalPromptRepository finalPromptRepository;
    private final FinalPromptSkeletonRepository skeletonRepository;
    private final FinalPromptItemRepository itemRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkeletonRepository projectSkeletonRepository;
    private final PromptRepository promptRepository;
    private final GlobalCategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<FinalPrompt> findByProjectId(Long projectId) {
        return finalPromptRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }

    @Transactional(readOnly = true)
    public FinalPrompt findById(Long id) {
        return finalPromptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("最终 Prompt 不存在"));
    }

    // FinalPromptService.java 添加以下方法

    @Transactional(readOnly = true)
    public List<FinalPromptDTO> findByProjectIdWithDetails(Long projectId) {
        // 查询所有最终提示词
        List<FinalPrompt> finalPrompts = finalPromptRepository.findByProjectIdOrderByCreatedAtDesc(projectId);

        if (finalPrompts.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取所有 finalPromptId
        List<Long> finalPromptIds = finalPrompts.stream()
                .map(FinalPrompt::getId)
                .collect(Collectors.toList());

        // 批量查询所有骨架
        List<FinalPromptSkeleton> allSkeletons = finalPromptIds.stream()
                .flatMap(id -> skeletonRepository.findByFinalPromptIdOrderBySortOrderAsc(id).stream())
                .collect(Collectors.toList());

        // 批量查询所有项
        List<FinalPromptItem> allItems = finalPromptIds.stream()
                .flatMap(id -> itemRepository.findByFinalPromptId(id).stream())
                .collect(Collectors.toList());

        // 提取所有分类 ID
        Set<Long> categoryIds = allSkeletons.stream()
                .map(FinalPromptSkeleton::getCategoryId)
                .collect(Collectors.toSet());

        // 批量查询所有分类
        Map<Long, GlobalCategory> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(GlobalCategory::getId, c -> c));

        // 将骨架和项按 finalPromptId 分组
        Map<Long, List<FinalPromptSkeleton>> skeletonMap = allSkeletons.stream()
                .collect(Collectors.groupingBy(FinalPromptSkeleton::getFinalPromptId));

        Map<Long, List<FinalPromptItem>> itemMap = allItems.stream()
                .collect(Collectors.groupingBy(FinalPromptItem::getFinalPromptId));

        // 转换为 DTO
        return finalPrompts.stream()
                .map(finalPrompt -> {
                    FinalPromptDTO dto = new FinalPromptDTO();
                    dto.setId(finalPrompt.getId());
                    dto.setProjectId(finalPrompt.getProjectId());
                    dto.setUuidCode(finalPrompt.getUuidCode());
                    dto.setName(finalPrompt.getName());
                    dto.setCreatedAt(finalPrompt.getCreatedAt());
                    dto.setUpdatedAt(finalPrompt.getUpdatedAt());

                    // 转换骨架
                    List<FinalPromptSkeleton> skeletons = skeletonMap.getOrDefault(finalPrompt.getId(), new ArrayList<>());
                    List<SkeletonDTO> skeletonDTOs = skeletons.stream()
                            .map(skeleton -> {
                                SkeletonDTO skeletonDTO = new SkeletonDTO();
                                skeletonDTO.setId(skeleton.getId());
                                skeletonDTO.setSortOrder(skeleton.getSortOrder());
                                skeletonDTO.setCreatedAt(skeleton.getCreatedAt());

                                GlobalCategory category = categoryMap.get(skeleton.getCategoryId());
                                if (category != null) {
                                    CategoryDTO categoryDTO = new CategoryDTO();
                                    categoryDTO.setId(category.getId());
                                    categoryDTO.setName(category.getName());
                                    categoryDTO.setCreatedAt(category.getCreatedAt());
                                    categoryDTO.setUpdatedAt(category.getUpdatedAt());
                                    skeletonDTO.setCategory(categoryDTO);
                                }
                                return skeletonDTO;
                            })
                            .collect(Collectors.toList());
                    dto.setSkeletons(skeletonDTOs);

                    // 提取提示词 ID
                    List<FinalPromptItem> items = itemMap.getOrDefault(finalPrompt.getId(), new ArrayList<>());
                    List<Long> promptIds = items.stream()
                            .map(FinalPromptItem::getPromptId)
                            .collect(Collectors.toList());
                    dto.setPromptIds(promptIds);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FinalPromptDTO getFinalPromptDetail(Long id) {
        FinalPrompt finalPrompt = findById(id);

        // 获取骨架
        List<FinalPromptSkeleton> skeletons =
                skeletonRepository.findByFinalPromptIdOrderBySortOrderAsc(id);

        // 获取提示词项
        List<FinalPromptItem> items = itemRepository.findByFinalPromptId(id);

        // 批量查询分类
        Set<Long> categoryIds = skeletons.stream()
                .map(FinalPromptSkeleton::getCategoryId)
                .collect(Collectors.toSet());

        Map<Long, GlobalCategory> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(GlobalCategory::getId, c -> c));

        // 构建 DTO
        FinalPromptDTO dto = new FinalPromptDTO();
        dto.setId(finalPrompt.getId());
        dto.setProjectId(finalPrompt.getProjectId());
        dto.setUuidCode(finalPrompt.getUuidCode());
        dto.setName(finalPrompt.getName());
        dto.setCreatedAt(finalPrompt.getCreatedAt());
        dto.setUpdatedAt(finalPrompt.getUpdatedAt());

        // 转换骨架
        List<SkeletonDTO> skeletonDTOs = skeletons.stream()
                .map(skeleton -> {
                    SkeletonDTO skeletonDTO = new SkeletonDTO();
                    skeletonDTO.setId(skeleton.getId());
                    skeletonDTO.setSortOrder(skeleton.getSortOrder());
                    skeletonDTO.setCreatedAt(skeleton.getCreatedAt());

                    GlobalCategory category = categoryMap.get(skeleton.getCategoryId());
                    if (category != null) {
                        CategoryDTO categoryDTO = new CategoryDTO();
                        categoryDTO.setId(category.getId());
                        categoryDTO.setName(category.getName());
                        categoryDTO.setCreatedAt(category.getCreatedAt());
                        categoryDTO.setUpdatedAt(category.getUpdatedAt());
                        skeletonDTO.setCategory(categoryDTO);
                    }
                    return skeletonDTO;
                })
                .collect(Collectors.toList());
        dto.setSkeletons(skeletonDTOs);

        // 提取提示词 ID
        List<Long> promptIds = items.stream()
                .map(FinalPromptItem::getPromptId)
                .collect(Collectors.toList());
        dto.setPromptIds(promptIds);

        return dto;
    }
    @Transactional(readOnly = true)
    public FinalPrompt findByUuidCode(String uuidCode) {
        return finalPromptRepository.findByUuidCode(uuidCode);
    }

    @Transactional
    public FinalPrompt create(Long projectId, List<Long> selectedPromptIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("项目不存在"));

        // 生成名称：项目名-时间戳
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String name = project.getName() + "-" + timestamp;

        FinalPrompt finalPrompt = new FinalPrompt();
        finalPrompt.setProjectId(projectId);
        finalPrompt.setName(name);
        finalPrompt.setUuidCode(UUID.randomUUID().toString());
        finalPromptRepository.save(finalPrompt);

        // 继承项目骨架配置
        List<ProjectSkeleton> projectSkeletons =
                projectSkeletonRepository.findByProjectIdOrderBySortOrderAsc(projectId);

        for (ProjectSkeleton ps : projectSkeletons) {
            FinalPromptSkeleton fps = new FinalPromptSkeleton();
            fps.setFinalPromptId(finalPrompt.getId());
            fps.setCategoryId(ps.getCategoryId());
            fps.setSortOrder(ps.getSortOrder());
            skeletonRepository.save(fps);
        }

        // 保存关联的提示词块
        for (Long promptId : selectedPromptIds) {
            if (!promptRepository.existsById(promptId)) {
                throw new RuntimeException("Prompt 不存在: " + promptId);
            }

            FinalPromptItem item = new FinalPromptItem();
            item.setFinalPromptId(finalPrompt.getId());
            item.setPromptId(promptId);
            itemRepository.save(item);
        }

        return finalPrompt;
    }

    @Transactional
    public FinalPrompt updateName(Long id, String newName) {
        FinalPrompt finalPrompt = findById(id);
        finalPrompt.setName(newName);
        return finalPromptRepository.save(finalPrompt);
    }

    @Transactional
    public List<FinalPromptSkeleton> updateSkeleton(Long id, List<Long> categoryIds) {
        FinalPrompt finalPrompt = findById(id);

        // 删除旧骨架
        skeletonRepository.deleteByFinalPromptId(id);

        // 创建新骨架
        for (int i = 0; i < categoryIds.size(); i++) {
            Long categoryId = categoryIds.get(i);
            if (!categoryRepository.existsById(categoryId)) {
                throw new RuntimeException("分类不存在: " + categoryId);
            }

            FinalPromptSkeleton skeleton = new FinalPromptSkeleton();
            skeleton.setFinalPromptId(finalPrompt.getId());
            skeleton.setCategoryId(categoryId);
            skeleton.setSortOrder(i + 1);
            skeletonRepository.save(skeleton);
        }

        return skeletonRepository.findByFinalPromptIdOrderBySortOrderAsc(id);
    }

    @Transactional
    public void delete(Long id) {
        if (!finalPromptRepository.existsById(id)) {
            throw new RuntimeException("最终 Prompt 不存在");
        }
        // 级联删除相关数据
        skeletonRepository.deleteByFinalPromptId(id);
        itemRepository.deleteByFinalPromptId(id);
        finalPromptRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String assembleFinalPrompt(Long id) {
        FinalPrompt finalPrompt = findById(id);
        return assembleFinalPromptInternal(id);
    }

    @Transactional(readOnly = true)
    public String assembleFinalPrompt(String uuidCode) {
        FinalPrompt finalPrompt = findByUuidCode(uuidCode);
        if (finalPrompt == null) {
            throw new RuntimeException("Prompt 不存在: " + uuidCode);
        }
        return assembleFinalPromptInternal(finalPrompt.getId());
    }

    private String assembleFinalPromptInternal(Long finalPromptId) {
        // 获取骨架结构
        List<FinalPromptSkeleton> skeletons =
                skeletonRepository.findByFinalPromptIdOrderBySortOrderAsc(finalPromptId);

        // 获取提示词项
        List<FinalPromptItem> items = itemRepository.findByFinalPromptId(finalPromptId);

        // 批量查询分类和提示词
        Set<Long> categoryIds = skeletons.stream()
                .map(FinalPromptSkeleton::getCategoryId)
                .collect(Collectors.toSet());

        Set<Long> promptIds = items.stream()
                .map(FinalPromptItem::getPromptId)
                .collect(Collectors.toSet());

        Map<Long, GlobalCategory> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(GlobalCategory::getId, c -> c));

        Map<Long, Prompt> promptMap = promptRepository.findAllById(promptIds)
                .stream()
                .collect(Collectors.toMap(Prompt::getId, p -> p));

        // 组装最终文本
        StringBuilder sb = new StringBuilder();
        int globalIndex = 1;

        for (FinalPromptSkeleton sk : skeletons) {
            GlobalCategory category = categoryMap.get(sk.getCategoryId());
            if (category == null) continue;

            List<Prompt> categoryPrompts = items.stream()
                    .map(item -> promptMap.get(item.getPromptId()))
                    .filter(Objects::nonNull)
                    .filter(p -> p.getCategoryId().equals(sk.getCategoryId()))
                    .collect(Collectors.toList());

            if (!categoryPrompts.isEmpty()) {
                sb.append("## ").append(category.getName()).append("\n\n");

                for (Prompt p : categoryPrompts) {
                    sb.append("### ").append(globalIndex).append(". ")
                            .append(p.getTitle()).append("\n\n");
                    sb.append(p.getContent()).append("\n\n");
                    globalIndex++;
                }
            }
        }

        return sb.toString().trim();
    }
}