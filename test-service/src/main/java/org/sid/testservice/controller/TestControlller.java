package org.sid.testservice.controller;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.sid.testservice.dto.JiraDto;
import org.sid.testservice.dto.TestDto;
import org.sid.testservice.service.JiraServiceClient;
import org.sid.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")

public class TestControlller {

    private final TestService testService;
    private final JiraServiceClient jiraServiceClient;
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;
    @Autowired
    public TestControlller(TestService testService, JiraServiceClient jiraServiceClient) {

        this.testService = testService;
        this.jiraServiceClient = jiraServiceClient;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestDto>> getAllTests() {
        List<TestDto> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/counts/{jiraId}")
    public Map<String, Long> getTestCaseCounts(@PathVariable Long jiraId) {
        return testService.getTestCaseCountsByJiraId(jiraId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TestDto> getTestById(@PathVariable("id") Long id){
        TestDto testDto = testService.getTestById(id);
        return ResponseEntity.ok(testDto);
    }

    @PostMapping("/create")
    public ResponseEntity<TestDto> createTest(@RequestBody @Valid TestDto testDto, @RequestParam(value = "id", required = false) Long jiraId) {
        System.out.println("Received Jira ID: " + jiraId);
        System.out.println("Received Test Data: " + testDto);
        JiraDto jiraDto = null;
        if (jiraId != null) {
            jiraDto = jiraServiceClient.getJiraById(jiraId);
        }
        TestDto createdTest = testService.createTest(testDto, jiraDto);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<TestDto> updateTest(@PathVariable("id") Long id, @RequestBody @Valid TestDto updatedTest, @RequestParam(value = "jiraId", required = false) Long jiraId) {
        System.out.println("Updating Test ID: " + id);
        System.out.println("Received Jira ID: " + jiraId);
        System.out.println("Received Test Data: " + updatedTest);
        JiraDto updatedJira = null;
        if (jiraId != null) {
            updatedJira = jiraServiceClient.getJiraById(jiraId);
        }
        TestDto testDto = testService.updateTest(id, updatedTest, updatedJira);
        return ResponseEntity.ok(testDto);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<String> validateTestCase(@PathVariable("id") Long id) {

        boolean validationSuccessful = testService.validateTestCase(id);
        if (validationSuccessful) {
            return ResponseEntity.ok("Test case with ID " + id + " has been validated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Validation of test case with ID " + id + " failed.");
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TestDto>> getOverdueTests() {
        List<TestDto> overdueTests = testService.getOverdueTests();
        return ResponseEntity.ok(overdueTests);
    }

    @Configuration
    public class AppConfig {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
