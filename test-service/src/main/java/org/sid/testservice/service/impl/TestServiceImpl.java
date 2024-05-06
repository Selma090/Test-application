package org.sid.testservice.service.impl;

import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Test;
import org.sid.testservice.mapper.TestMapper;
import org.sid.testservice.repository.TestRepository;
import org.sid.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Autowired
    public TestServiceImpl(TestRepository testRepository) {

        this.testRepository = testRepository;
    }

    @Override
    public List<TestDto> getAllTests() {
        List<Test> tests = testRepository.findAll();
        return tests.stream()
                .map(test -> TestMapper.maptoTestDto(test))
                .filter(Objects::nonNull)
                .sorted((test1, test2) -> {
                    // Sort by priority, handling null values
                    if (test1.getPriority() == null && test2.getPriority() == null) {
                        return 0;
                    } else if (test1.getPriority() == null) {
                        return 1; // test1 is considered higher priority if it's null
                    } else if (test2.getPriority() == null) {
                        return -1; // test2 is considered higher priority if it's null
                    }
                    // Custom ordering based on priority levels, case-insensitive
                    List<String> priorityOrder = Arrays.asList("high", "medium", "low");
                    String priority1 = test1.getPriority().toLowerCase();
                    String priority2 = test2.getPriority().toLowerCase();
                    return Integer.compare(priorityOrder.indexOf(priority1), priorityOrder.indexOf(priority2));
                })
                .collect(Collectors.toList());
    }

    @Override
    public TestDto getTestById(Long id) {

        Test test = testRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Test does not exist : "+ id));
        return TestMapper.maptoTestDto(test);
    }

    @Override
    public TestDto createTest(TestDto testDto) {

        Test test = TestMapper.maptoTest(testDto);
        Test createdTest = testRepository.save(test);

        return TestMapper.maptoTestDto(createdTest);
    }

    @Override
    public TestDto updateTest(Long id, TestDto updatedTest) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("id does not exist : " + id)
        );

        test.setName(updatedTest.getName());


        Test updatedTestObj = testRepository.save(test);

        return TestMapper.maptoTestDto(updatedTestObj);
    }

    @Override
    public void deleteTest(Long id) {

        testRepository.deleteById(id);
    }
}
