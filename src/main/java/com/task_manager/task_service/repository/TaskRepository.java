package com.task_manager.task_service.repository;

import com.task_manager.task_service.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t " +
            "WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:teamId IS NULL OR t.team.id = :teamId)")
    Page<Task> findAllWithFilters(Pageable pageable,
                                  @Param("status") String status,
                                  @Param("priority") Integer priority,
                                  @Param("teamId") Long teamId);

}
