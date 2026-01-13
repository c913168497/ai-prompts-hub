// dto/ProjectDTO.java
package com.ai.prompthub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String gitName;
    private String gitRepo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}