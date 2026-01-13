package com.ai.prompthub.controller;


import com.ai.prompthub.service.FinalPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/projects/mcp")
@RequiredArgsConstructor
public class McpController {

    private final FinalPromptService finalPromptService;

    @GetMapping("/{uuidCode}/assembleByUuidCode")
    public ResponseEntity<Map<String, String>> assembleFinalPromptByUuid(@PathVariable String uuidCode) {
        String content = finalPromptService.assembleFinalPrompt(uuidCode);
        return ResponseEntity.ok(Map.of("content", content));
    }

}
