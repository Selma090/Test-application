package org.sid.testservice.service;

import org.sid.testservice.dto.JiraDto;
import org.sid.testservice.sec.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "jira-service", configuration = FeignClientConfiguration.class)
public interface JiraServiceClient {

    @GetMapping("/api/jiras/{id}")
    JiraDto getJiraById(@PathVariable("id") Long id);

    @GetMapping("/api/jiras/all")
    List<JiraDto> getAllJiraIssues();
}
