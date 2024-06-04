package org.sid.testservice.repository;

import org.sid.testservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    @Query("SELECT t.test_statut AS status, COUNT(t) AS count FROM Test t WHERE t.jira.id = :jiraId GROUP BY t.test_statut")
    List<Object[]> findTestCaseCountsByJiraId(@Param("jiraId") Long jiraId);
}
