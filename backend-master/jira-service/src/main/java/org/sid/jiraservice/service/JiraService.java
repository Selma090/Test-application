package org.sid.jiraservice.service;

import org.sid.jiraservice.dto.JiraDto;

import java.util.List;

public interface JiraService {
    List<JiraDto> getAllJiras();

    JiraDto getJiraById(Long id);

    JiraDto createJira(JiraDto jiraDto);

    JiraDto updateJira(Long id, JiraDto updatedJira);

    void deleteJira(Long id);
}
