package org.sid.testservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.testservice.entity.Jira;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private Long id;
    private String name;
    private String priority;
    private String test_statut;
    private Date deadline;
    private String validation_statut;

    private Jira jira;
}
