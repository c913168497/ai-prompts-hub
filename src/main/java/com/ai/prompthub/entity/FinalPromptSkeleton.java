// FinalPromptSkeleton.java
package com.ai.prompthub.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "final_prompt_skeleton")
public class FinalPromptSkeleton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "final_prompt_id", nullable = false)
    private Long finalPromptId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}