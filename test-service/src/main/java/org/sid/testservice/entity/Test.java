package org.sid.testservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String priority;
    private String test_statut;
    private String validation_statut;
    private Date deadline;

    @ManyToOne(fetch = FetchType.EAGER) // Lazy fetch for optimization
    @JoinColumn(name = "jira_id") // Name of the foreign key column in the Test table
    private Jira jira;


}
