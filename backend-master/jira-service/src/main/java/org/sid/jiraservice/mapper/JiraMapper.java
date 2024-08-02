package org.sid.jiraservice.mapper;

import org.sid.jiraservice.dto.JiraDto;
import org.sid.jiraservice.entity.Jira;

public class JiraMapper {

    public static JiraDto maptoJiraDto(Jira jira){
        return  new JiraDto(
                jira.getId(),
                jira.getOuvert_par(),
                jira.getN_jira(),
                jira.getLibelle(),
                jira.getStatut(),
                jira.getGravite(),
                jira.getCommentaire()
        );
    }

    public static Jira maptoJira(JiraDto jiraDto){
        return new Jira(
                jiraDto.getId(),
                jiraDto.getOuvert_par(),
                jiraDto.getN_jira(),
                jiraDto.getLibelle(),
                jiraDto.getStatut(),
                jiraDto.getGravite(),
                jiraDto.getCommentaire()
        );
    }
}
