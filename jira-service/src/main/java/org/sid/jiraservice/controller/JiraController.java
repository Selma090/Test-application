package org.sid.jiraservice.controller;

import org.sid.jiraservice.dto.JiraDto;
import org.sid.jiraservice.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/jiras")
public class JiraController {

    private final JiraService jiraService;

    @Autowired
    public JiraController(JiraService jiraService) {

        this.jiraService = jiraService;
    }

    @GetMapping
    public ResponseEntity<List<JiraDto>> getAllJiras() {
        List<JiraDto> jiras = jiraService.getAllJiras();
        return ResponseEntity.ok(jiras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JiraDto> getJiraById(@PathVariable("id") Long id) {

        JiraDto jiraDto = jiraService.getJiraById(id);
        return ResponseEntity.ok(jiraDto);
    }

    @PostMapping
    public ResponseEntity<JiraDto> createJira(@RequestBody JiraDto jiraDto) {
        JiraDto createdJira = jiraService.createJira(jiraDto);
        return new ResponseEntity<>(createdJira, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JiraDto> updateJira(@PathVariable("id") Long id, @RequestBody @Valid JiraDto updatedjira) {
        JiraDto jiraDto = jiraService.updateJira(id, updatedjira);
        return ResponseEntity.ok(jiraDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJira(@PathVariable Long id) {
        jiraService.deleteJira(id);
        return ResponseEntity.noContent().build();
    }
}
