package org.sid.testservice.mapper;

import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Test;

public class TestMapper {

    public static TestDto maptoTestDto(Test test){
        return new TestDto(
                test.getId(),
                test.getOuvert(),
                test.getName(),
                test.getPriority(),
                test.getTest_statut(),
                test.getDeadline(),
                test.getValidation_statut(),
                test.getJira()
        );
    }

    public static Test maptoTest(TestDto testDto){
        return new Test(
                testDto.getId(),
                testDto.getOuvert(),
                testDto.getName(),
                testDto.getPriority(),
                testDto.getTest_statut(),
                testDto.getValidation_statut(),
                testDto.getDeadline(),
                testDto.getJira()
        );
    }
}
