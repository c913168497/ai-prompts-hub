// FinalPromptRepository.java
package com.ai.prompthub.repository;

import com.ai.prompthub.entity.FinalPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinalPromptRepository extends JpaRepository<FinalPrompt, Long> {
    List<FinalPrompt> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    FinalPrompt findByUuidCode(String uuidCode);
    void deleteByProjectId(Long projectId);
}
