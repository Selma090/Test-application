package org.sid.testservice.service;

import org.sid.testservice.dto.JiraDto;
import org.sid.testservice.dto.TestDto;

import java.util.List;
import java.util.Map;

public interface TestService {
    Map<String, Long> getTestCaseCountsByJiraId(Long jiraId);
    List<TestDto> getAllTests();
    TestDto getTestById(Long id);
    TestDto createTest(TestDto testDto, JiraDto jiraDto);
    TestDto updateTest(Long id, TestDto updatedTest, JiraDto updatedJira);
    void deleteTest(Long id);
    boolean validateTestCase(Long id);
    List<TestDto> getOverdueTests();
}
