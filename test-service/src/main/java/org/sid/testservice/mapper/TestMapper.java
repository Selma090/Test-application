package org.sid.testservice.mapper;

import org.sid.testservice.dto.TestDto;
import org.sid.testservice.entity.Test;

public class TestMapper {

    public static TestDto maptoTestDto(Test test){
        return new TestDto(
                test.getId(),
                test.getName(),
                test.getPriority()
        );
    }

    public static Test maptoTest(TestDto testDto){
        return new Test(
                testDto.getId(),
                testDto.getName(),
                testDto.getPriority()
        );
    }
}