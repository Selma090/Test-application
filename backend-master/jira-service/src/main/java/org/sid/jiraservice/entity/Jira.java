package org.sid.jiraservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Jira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ouvert_par;
    private String n_jira;
    private String libelle;
    private String statut;
    private String gravite;
    private String commentaire;
}
