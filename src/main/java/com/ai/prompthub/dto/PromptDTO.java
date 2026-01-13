// dto/PromptDTO.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PromptDTO {
    private Long id;
    private String title;
    private String content;
    private CategoryDTO category;
    private Boolean isSelected;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}