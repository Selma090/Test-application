package org.sid.testservice.service;

import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Test;

import java.util.List;

public interface TestService {

    List<TestDto> getAllTests();

    TestDto getTestById(Long id);

    TestDto createTest(TestDto testDto);

    TestDto updateTest(Long id, TestDto updatedTest);

    void deleteTest(Long id);
}
