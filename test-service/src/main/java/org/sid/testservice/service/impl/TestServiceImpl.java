package org.sid.testservice.service.impl;

import org.sid.testservice.dto.JiraDto;
import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Jira;
import org.sid.testservice.entity.Test;
import org.sid.testservice.mapper.TestMapper;
import org.sid.testservice.repository.TestRepository;
import org.sid.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Map<String, Long> getTestCaseCountsByJiraId(Long jiraId) {
        List<Object[]> results = testRepository.findTestCaseCountsByJiraId(jiraId);
        Map<String, Long> counts = new HashMap<>();
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            counts.put(status, count);
        }
        return counts;
    }

    @Override
    public List<TestDto> getAllTests() {
        List<Test> tests = testRepository.findAll();
        return tests.stream()
                .map(TestMapper::maptoTestDto)
                .filter(Objects::nonNull)
                .sorted((test1, test2) -> {
                    if (test1.getPriority() == null && test2.getPriority() == null) {
                        return 0;
                    } else if (test1.getPriority() == null) {
                        return 1;
                    } else if (test2.getPriority() == null) {
                        return -1;
                    }
                    List<String> priorityOrder = Arrays.asList("high", "medium", "low");
                    String priority1 = test1.getPriority().toLowerCase();
                    String priority2 = test2.getPriority().toLowerCase();
                    return Integer.compare(priorityOrder.indexOf(priority1), priorityOrder.indexOf(priority2));
                })
                .collect(Collectors.toList());
    }

    @Override

    public TestDto getTestById(Long id) {
        Test test = testRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Test does not exist: " + id));

        return TestMapper.maptoTestDto(test);
    }

    @Override

    public TestDto createTest(TestDto testDto, JiraDto jiraDto) {
        Test test = TestMapper.maptoTest(testDto);
        if (jiraDto != null) {
            test.setJira(mapJiraDtoToEntity(jiraDto));
        }
        test = testRepository.save(test);
        return TestMapper.maptoTestDto(test);
    }


    @Override
    public TestDto updateTest(Long id, TestDto updatedTest, JiraDto updatedJira) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("id does not exist: " + id)
        );

        test.setName(updatedTest.getName());
        test.setTest_statut(updatedTest.getTest_statut());
        test.setDeadline(updatedTest.getDeadline());
        test.setPriority(updatedTest.getPriority());
        test.setValidation_statut(updatedTest.getValidation_statut());

        if (updatedJira != null) {
            Jira jira = mapJiraDtoToEntity(updatedJira);
            test.setJira(jira);
        }

        Test updatedTestObj = testRepository.save(test);
        return TestMapper.maptoTestDto(updatedTestObj);
    }



    @Override
    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    @Override
    public boolean validateTestCase(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test case not found with id: " + id));

        if ("Validated".equalsIgnoreCase(test.getValidation_statut())) {
            return true;
        } else {
            return false;
        }
    }


    private boolean checkCondition(Test test) {
        return test != null && "Done".equalsIgnoreCase(test.getTest_statut());
    }

    private boolean isDeadlineExceeded(Test test) {
        return test.getDeadline() != null && test.getDeadline().before(new Date());
    }

    private void sendNotification(Test test) {
        boolean statusChanged = isStatusChanged(test);
        boolean deadlineExceeded = isDeadlineExceeded(test);

        if (statusChanged || deadlineExceeded) {
            System.out.println("Notification sent: Test case with ID " + test.getId() +
                    (statusChanged ? " status changed." : "") +
                    (deadlineExceeded ? " deadline exceeded." : ""));
        }
    }

    private boolean isStatusChanged(Test test) {
        Test previousTest = testRepository.findById(test.getId())
                .orElseThrow(() -> new EntityNotFoundException("Test case not found with id: " + test.getId()));
        return !test.getTest_statut().equals(previousTest.getTest_statut());
    }

    @Override
    public List<TestDto> getOverdueTests() {
        List<Test> allTests = testRepository.findAll();
        List<Test> overdueTests = allTests.stream()
                .filter(this::isDeadlineExceeded)
                .collect(Collectors.toList());
        return overdueTests.stream()
                .map(TestMapper::maptoTestDto)
                .collect(Collectors.toList());
    }

    private Jira mapJiraDtoToEntity(JiraDto jiraDto) {
        if (jiraDto == null) {
            return null; // Or handle the case accordingly
        }
        return new Jira(
                jiraDto.getId(),
                jiraDto.getOuvert_par(),
                jiraDto.getN_jira(),
                jiraDto.getCommentaire(),
                jiraDto.getStatut(),
                jiraDto.getGravite(),
                jiraDto.getLibelle()
        );
    }

}
