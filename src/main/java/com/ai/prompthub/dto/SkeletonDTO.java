// dto/SkeletonDTO.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SkeletonDTO {
    private Long id;
    private CategoryDTO category;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}