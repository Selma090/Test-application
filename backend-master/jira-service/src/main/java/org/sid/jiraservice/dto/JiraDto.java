package org.sid.jiraservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraDto {

    private Long id;
    private String ouvert_par;
    private String n_jira;
    private String libelle;
    private String statut;
    private String gravite;
    private String commentaire;
}
