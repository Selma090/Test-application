package org.sid.jiraservice.repository;

import org.sid.jiraservice.entity.Jira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraRepository extends JpaRepository<Jira, Long> {
}
