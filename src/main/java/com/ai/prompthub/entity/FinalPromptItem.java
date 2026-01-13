// FinalPromptItem.java
package com.ai.prompthub.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "final_prompt_item")
public class FinalPromptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "final_prompt_id", nullable = false)
    private Long finalPromptId;

    @Column(name = "prompt_id", nullable = false)
    private Long promptId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}