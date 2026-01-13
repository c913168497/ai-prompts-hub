// PromptAssemblyService.java (保持不变，因为它不直接操作实体关系)
package com.ai.prompthub.service;

import com.ai.prompthub.entity.Prompt;
import com.ai.prompthub.entity.ProjectSkeleton;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptAssemblyService {

    private final ProjectSkeletonService skeletonService;
    private final PromptService promptService;
    private final GlobalCategoryService categoryService;

    public String generateFinalPrompt(Long projectId) {
        List<ProjectSkeleton> skeletons = skeletonService.findByProjectId(projectId);
        List<Prompt> selectedPrompts = promptService.findSelectedByProjectId(projectId);

        StringBuilder result = new StringBuilder();

        for (ProjectSkeleton skeleton : skeletons) {
            List<Prompt> categoryPrompts = selectedPrompts.stream()
                    .filter(p -> p.getCategoryId().equals(skeleton.getCategoryId()))
                    .collect(Collectors.toList());

            if (!categoryPrompts.isEmpty()) {
                String categoryName = categoryService.findById(skeleton.getCategoryId()).getName();
                result.append("## ").append(categoryName).append("\n\n");

                for (Prompt prompt : categoryPrompts) {
                    result.append("### ").append(prompt.getTitle()).append("\n\n");
                    result.append(prompt.getContent()).append("\n\n");
                }
            }
        }

        return result.toString();
    }

    public String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        String base36 = Long.toString(timestamp, 36).toUpperCase();
        String random = Long.toString((long)(Math.random() * 1000000), 36).toUpperCase();
        return "TPL-" + base36 + "-" + random;
    }
}