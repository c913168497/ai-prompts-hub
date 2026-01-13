// FinalPrompt.java
package com.ai.prompthub.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "final_prompt")
public class FinalPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_code")
    private String uuidCode;

    // 纯 ID 引用，移除对象关联
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 移除 @OneToMany 集合，由 Service 层手动查询

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}