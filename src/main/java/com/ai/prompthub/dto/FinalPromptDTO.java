// FinalPromptDTO.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FinalPromptDTO {
    private Long id;
    private Long projectId;
    private String uuidCode;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SkeletonDTO> skeletons;
    private List<Long> promptIds;
}