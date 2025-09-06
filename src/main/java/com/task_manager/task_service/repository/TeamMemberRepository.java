package com.task_manager.task_service.repository;

import com.task_manager.task_service.model.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    void deleteByTeamIdAndUserId(Long team_id, Long user_id);
}
