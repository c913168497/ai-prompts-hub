package com.ai.prompthub.controller;

import com.ai.prompthub.converter.DTOConverter;
import com.ai.prompthub.dto.CreateFinalPromptRequest;
import com.ai.prompthub.dto.FinalPromptDTO;
import com.ai.prompthub.entity.FinalPrompt;
import com.ai.prompthub.service.FinalPromptService;
import com.ai.prompthub.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/final-prompts")
@RequiredArgsConstructor
public class FinalPromptController {

    private final FinalPromptService finalPromptService;
    private final DTOConverter converter;

    @GetMapping
    public ResponseEntity<List<FinalPromptDTO>> getAllFinalPrompts(@PathVariable Long projectId) {
        List<FinalPromptDTO> prompts = finalPromptService.findByProjectIdWithDetails(projectId);
        return ResponseEntity.ok(prompts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFinalPrompt(@PathVariable Long projectId, @PathVariable Long id) {
        try {
            FinalPromptDTO dto = finalPromptService.getFinalPromptDetail(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFinalPrompt(
            @PathVariable Long projectId,
            @RequestBody CreateFinalPromptRequest request) {
        try {
            if (request.getPromptIds() == null || request.getPromptIds().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseUtil.error("至少需要选择一个提示词"));
            }
            FinalPrompt prompt = finalPromptService.create(projectId, request.getPromptIds());
            FinalPromptDTO dto = finalPromptService.getFinalPromptDetail(prompt.getId());
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<?> updateName(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseUtil.error("名称不能为空"));
            }
            FinalPrompt prompt = finalPromptService.updateName(id, newName);
            return ResponseEntity.ok(converter.toFinalPromptDTO(prompt));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/skeleton")
    public ResponseEntity<?> updateSkeleton(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestBody List<Long> categoryIds) {
        try {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseUtil.error("分类列表不能为空"));
            }
            finalPromptService.updateSkeleton(id, categoryIds);
            return ResponseEntity.ok(ApiResponseUtil.success("提示词骨架配置已更新"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/assemble")
    public ResponseEntity<?> assembleFinalPrompt(@PathVariable Long projectId, @PathVariable Long id) {
        try {
            String content = finalPromptService.assembleFinalPrompt(id);
            return ResponseEntity.ok(Map.of("content", content));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFinalPrompt(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        try {
            finalPromptService.delete(id);
            return ResponseEntity.ok(ApiResponseUtil.success("最终 Prompt 已删除"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.error(e.getMessage()));
        }
    }
}