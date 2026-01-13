// PromptService.java
package com.ai.prompthub.service;

import com.ai.prompthub.entity.GlobalCategory;
import com.ai.prompthub.entity.Project;
import com.ai.prompthub.entity.Prompt;
import com.ai.prompthub.repository.GlobalCategoryRepository;
import com.ai.prompthub.repository.ProjectRepository;
import com.ai.prompthub.repository.PromptRepository;
import com.ai.prompthub.repository.FinalPromptItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;
    private final ProjectRepository projectRepository;
    private final GlobalCategoryRepository categoryRepository;
    private final FinalPromptItemRepository finalPromptItemRepository;

    public List<Prompt> findByProjectId(Long projectId) {
        return promptRepository.findByProjectId(projectId);
    }

    public List<Prompt> findSelectedByProjectId(Long projectId) {
        return promptRepository.findByProjectIdAndIsSelectedTrue(projectId);
    }

    public Prompt findById(Long id) {
        return promptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prompt 不存在: " + id));
    }

    @Transactional
    public Prompt create(Long projectId, Long categoryId, String title, String content) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("项目不存在");
        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("分类不存在");
        }

        Prompt prompt = new Prompt();
        prompt.setProjectId(projectId);
        prompt.setCategoryId(categoryId);
        prompt.setTitle(title);
        prompt.setContent(content);
        prompt.setIsSelected(false);
        return promptRepository.save(prompt);
    }

    @Transactional
    public Prompt update(Long id, String title, Long categoryId, String content) {
        Prompt prompt = findById(id);
        prompt.setTitle(title);
        prompt.setContent(content);
        if (categoryId != null) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new RuntimeException("分类不存在");
            }
            prompt.setCategoryId(categoryId);
        }
        return promptRepository.save(prompt);
    }

    @Transactional
    public Prompt toggleSelection(Long id) {
        Prompt prompt = findById(id);
        prompt.setIsSelected(!prompt.getIsSelected());
        return promptRepository.save(prompt);
    }

    @Transactional
    public void delete(Long id) {
        // 删除关联的 FinalPromptItem
        finalPromptItemRepository.deleteByPromptId(id);
        promptRepository.deleteById(id);
    }
}