package com.ai.prompthub.controller;

import com.ai.prompthub.converter.DTOConverter;
import com.ai.prompthub.dto.ProjectDTO;
import com.ai.prompthub.entity.Project;
import com.ai.prompthub.service.ProjectService;
import com.ai.prompthub.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final DTOConverter converter;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        return ResponseEntity.ok(converter.toProjectDTOList(projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        Project project = projectService.findById(id);
        return ResponseEntity.ok(converter.toProjectDTO(project));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String gitRepo = request.get("gitRepo");
        Project project = projectService.create(name, gitRepo);
        return ResponseEntity.ok(converter.toProjectDTO(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String gitRepo = request.get("gitRepo");
        Project project = projectService.update(id, name, gitRepo);
        return ResponseEntity.ok(converter.toProjectDTO(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok(ApiResponseUtil.success("项目删除成功"));
    }
}