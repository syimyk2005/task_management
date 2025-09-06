package com.task_manager.task_service.repository;

import com.task_manager.task_service.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
