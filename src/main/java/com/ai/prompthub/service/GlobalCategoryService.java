// GlobalCategoryService.java
package com.ai.prompthub.service;

import com.ai.prompthub.entity.GlobalCategory;
import com.ai.prompthub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalCategoryService {

    private final GlobalCategoryRepository categoryRepository;
    private final ProjectSkeletonRepository projectSkeletonRepository;
    private final PromptRepository promptRepository;
    private final FinalPromptSkeletonRepository finalPromptSkeletonRepository;

    public List<GlobalCategory> findAll() {
        return categoryRepository.findAll();
    }

    public GlobalCategory findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + id));
    }

    @Transactional
    public GlobalCategory create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("分类名称已存在: " + name);
        }
        GlobalCategory category = new GlobalCategory();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public GlobalCategory update(Long id, String name) {
        GlobalCategory category = findById(id);

        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new RuntimeException("分类名称已存在: " + name);
        }

        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        // 级联删除所有关联数据
        projectSkeletonRepository.deleteByCategoryId(id);
        promptRepository.deleteByCategoryId(id);
        finalPromptSkeletonRepository.deleteByCategoryId(id);
        categoryRepository.deleteById(id);
    }
}