package com.ai.prompthub.repository;

import com.ai.prompthub.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByGitRepo(String gitRepo);
    boolean existsByGitRepo(String gitRepo);
}