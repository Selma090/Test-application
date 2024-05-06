package org.sid.testservice.controller;

import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Test;
import org.sid.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestControlller {

    private final TestService testService;

    @Autowired
    public TestControlller(TestService testService) {

        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<List<TestDto>> getAllTests() {
        List<TestDto> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDto> getTestById(@PathVariable("id") Long id) {

        TestDto testDto = testService.getTestById(id);
        return ResponseEntity.ok(testDto);
    }

    @PostMapping
    public ResponseEntity<TestDto> createTest(@RequestBody TestDto testDto) {
        TestDto createdTest = testService.createTest(testDto);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestDto> updateTest(@PathVariable("id") Long id, @RequestBody @Valid TestDto updatedtest) {
        TestDto testDto = testService.updateTest(id, updatedtest);
        return ResponseEntity.ok(testDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
