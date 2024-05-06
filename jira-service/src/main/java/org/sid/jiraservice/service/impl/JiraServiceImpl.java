package org.sid.jiraservice.service.impl;

import org.sid.jiraservice.dto.JiraDto;
import org.sid.jiraservice.entity.Jira;
import org.sid.jiraservice.mapper.JiraMapper;
import org.sid.jiraservice.repository.JiraRepository;
import org.sid.jiraservice.service.JiraService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JiraServiceImpl implements JiraService {

    private final JiraRepository jiraRepository;

    public JiraServiceImpl(JiraRepository jiraRepository) {

        this.jiraRepository = jiraRepository;
    }
    @Override
    public List<JiraDto> getAllJiras() {

        List<Jira> jiras = jiraRepository.findAll();
        return jiras.stream().map(jira -> JiraMapper.maptoJiraDto(jira))
                .collect(Collectors.toList());
    }

    @Override
    public JiraDto getJiraById(Long id) {

        Jira jira = jiraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jira does not exist : "+ id));
        return JiraMapper.maptoJiraDto(jira);
    }

    @Override
    public JiraDto createJira(JiraDto jiraDto) {

        Jira jira = JiraMapper.maptoJira(jiraDto);
        Jira createdJira = jiraRepository.save(jira);

        return JiraMapper.maptoJiraDto(createdJira);
    }

    @Override
    public JiraDto updateJira(Long id, JiraDto updatedJira) {

        Jira jira = jiraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id does not exist : " + id)
        );

        jira.setOuvert_par(updatedJira.getOuvert_par());
        jira.setN_jira(updatedJira.getN_jira());
        jira.setStatut(updatedJira.getStatut());
        jira.setLibelle(updatedJira.getLibelle());
        jira.setCommentaire(updatedJira.getCommentaire());
        jira.setGravite(updatedJira.getGravite());

        Jira updatedJiraObj = jiraRepository.save(jira);

        return JiraMapper.maptoJiraDto(updatedJiraObj);
    }

    @Override
    public void deleteJira(Long id) {

        jiraRepository.deleteById(id);
    }
}
