// ProjectService.java
package com.ai.prompthub.service;

import com.ai.prompthub.entity.Project;
import com.ai.prompthub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkeletonRepository projectSkeletonRepository;
    private final PromptRepository promptRepository;
    private final FinalPromptRepository finalPromptRepository;
    private final FinalPromptSkeletonRepository finalPromptSkeletonRepository;
    private final FinalPromptItemRepository finalPromptItemRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("项目不存在: " + id));
    }

    private String extractGitProjectName(String gitRepo) {
        if (gitRepo == null || gitRepo.trim().isEmpty()) {
            return "";
        }

        String projectName = gitRepo.trim();

        if (projectName.endsWith(".git")) {
            projectName = projectName.substring(0, projectName.length() - 4);
        }

        String[] parts = projectName.split("[/:]");
        for (int i = parts.length - 1; i >= 0; i--) {
            if (!parts[i].isEmpty()) {
                return parts[i];
            }
        }

        return "";
    }

    @Transactional
    public Project create(String name, String gitRepo) {
        if (gitRepo != null && projectRepository.existsByGitRepo(gitRepo)) {
            throw new RuntimeException("Git 仓库已存在: " + gitRepo);
        }

        Project project = new Project();
        project.setName(name);
        project.setGitRepo(gitRepo);
        project.setGitName(extractGitProjectName(gitRepo));
        return projectRepository.save(project);
    }

    @Transactional
    public Project update(Long id, String name, String gitRepo) {
        Project project = findById(id);

        if (gitRepo != null && !gitRepo.equals(project.getGitRepo())
                && projectRepository.existsByGitRepo(gitRepo)) {
            throw new RuntimeException("Git 仓库已存在: " + gitRepo);
        }

        project.setName(name);
        project.setGitRepo(gitRepo);
        project.setGitName(extractGitProjectName(gitRepo));
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        // 级联删除项目相关的所有数据
        projectSkeletonRepository.deleteByProjectId(id);
        promptRepository.deleteByProjectId(id);

        // 删除该项目的所有最终提示词及其关联数据
        List<Long> finalPromptIds = finalPromptRepository.findByProjectIdOrderByCreatedAtDesc(id)
                .stream()
                .map(fp -> fp.getId())
                .toList();

        for (Long finalPromptId : finalPromptIds) {
            finalPromptSkeletonRepository.deleteByFinalPromptId(finalPromptId);
            finalPromptItemRepository.deleteByFinalPromptId(finalPromptId);
        }
        finalPromptRepository.deleteByProjectId(id);

        projectRepository.deleteById(id);
    }
}