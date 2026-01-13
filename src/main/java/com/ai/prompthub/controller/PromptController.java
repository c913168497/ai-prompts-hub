package com.ai.prompthub.controller;

import com.ai.prompthub.converter.DTOConverter;
import com.ai.prompthub.dto.PromptDTO;
import com.ai.prompthub.entity.Prompt;
import com.ai.prompthub.service.PromptService;
import com.ai.prompthub.service.PromptAssemblyService;
import com.ai.prompthub.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/prompts")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;
    private final PromptAssemblyService assemblyService;
    private final DTOConverter converter;

    @GetMapping
    public ResponseEntity<List<PromptDTO>> getPrompts(@PathVariable Long projectId) {
        List<Prompt> prompts = promptService.findByProjectId(projectId);
        return ResponseEntity.ok(converter.toPromptDTOList(prompts));
    }

    @PostMapping
    public ResponseEntity<PromptDTO> createPrompt(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        Long categoryId = ((Number) request.get("categoryId")).longValue();
        String content = (String) request.get("content");
        Prompt prompt = promptService.create(projectId, categoryId, title, content);
        return ResponseEntity.ok(converter.toPromptDTO(prompt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromptDTO> updatePrompt(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        Long categoryId = request.containsKey("categoryId") ?
                ((Number) request.get("categoryId")).longValue() : null;
        String content = (String) request.get("content");
        Prompt prompt = promptService.update(id, title, categoryId, content);
        return ResponseEntity.ok(converter.toPromptDTO(prompt));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<PromptDTO> toggleSelection(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        Prompt prompt = promptService.toggleSelection(id);
        return ResponseEntity.ok(converter.toPromptDTO(prompt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePrompt(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        promptService.delete(id);
        return ResponseEntity.ok(ApiResponseUtil.success("Prompt删除成功"));
    }

    @GetMapping("/assemble")
    public ResponseEntity<Map<String, Object>> assemblePrompt(@PathVariable Long projectId) {
        String content = assemblyService.generateFinalPrompt(projectId);
        String uniqueId = assemblyService.generateUniqueId();
        return ResponseEntity.ok(Map.of(
                "content", content,
                "id", uniqueId
        ));
    }

}