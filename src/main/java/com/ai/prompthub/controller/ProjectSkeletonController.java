package com.ai.prompthub.controller;

import com.ai.prompthub.converter.DTOConverter;
import com.ai.prompthub.dto.SkeletonDTO;
import com.ai.prompthub.entity.ProjectSkeleton;
import com.ai.prompthub.service.ProjectSkeletonService;
import com.ai.prompthub.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/skeleton")
@RequiredArgsConstructor
public class ProjectSkeletonController {

    private final ProjectSkeletonService skeletonService;
    private final DTOConverter converter;

    @GetMapping
    public ResponseEntity<List<SkeletonDTO>> getSkeleton(@PathVariable Long projectId) {
        List<ProjectSkeleton> skeletons = skeletonService.findByProjectId(projectId);
        return ResponseEntity.ok(converter.toSkeletonDTOList(skeletons));
    }

    @PostMapping
    public ResponseEntity<SkeletonDTO> addToSkeleton(
            @PathVariable Long projectId,
            @RequestBody Map<String, Long> request) {
        Long categoryId = request.get("categoryId");
        ProjectSkeleton skeleton = skeletonService.addCategoryToProject(projectId, categoryId);
        return ResponseEntity.ok(converter.toSkeletonDTO(skeleton));
    }

    @PutMapping
    public ResponseEntity<List<SkeletonDTO>> updateSkeletonOrder(
            @PathVariable Long projectId,
            @RequestBody List<Long> categoryIds) {
        List<ProjectSkeleton> skeletons = skeletonService.updateSkeletonOrder(projectId, categoryIds);
        return ResponseEntity.ok(converter.toSkeletonDTOList(skeletons));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> removeFromSkeleton(
            @PathVariable Long projectId,
            @PathVariable Long categoryId) {
        skeletonService.removeFromSkeleton(projectId, categoryId);
        return ResponseEntity.ok(ApiResponseUtil.success("已从骨架配置中移除"));
    }
}