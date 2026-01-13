// dto/ProjectDetailDTO.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDetailDTO {
    private Long id;
    private String name;
    private String gitName;
    private String gitRepo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SkeletonDTO> skeletons;
    private List<PromptDTO> prompts;
}