package com.ai.prompthub.controller;

import com.ai.prompthub.converter.DTOConverter;
import com.ai.prompthub.dto.CategoryDTO;
import com.ai.prompthub.entity.GlobalCategory;
import com.ai.prompthub.service.GlobalCategoryService;
import com.ai.prompthub.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class GlobalCategoryController {

    private final GlobalCategoryService categoryService;
    private final DTOConverter converter;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<GlobalCategory> categories = categoryService.findAll();
        return ResponseEntity.ok(converter.toCategoryDTOList(categories));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        GlobalCategory category = categoryService.create(name);
        return ResponseEntity.ok(converter.toCategoryDTO(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String name = request.get("name");
        GlobalCategory category = categoryService.update(id, name);
        return ResponseEntity.ok(converter.toCategoryDTO(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponseUtil.success("分类删除成功"));
    }
}