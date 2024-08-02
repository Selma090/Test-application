package org.sid.testservice.sec;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final KeycloakRestTemplate keycloakRestTemplate;

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    public KeycloakService(KeycloakRestTemplate keycloakRestTemplate) {
        this.keycloakRestTemplate = keycloakRestTemplate;
    }

    public List<Map<String, Object>> getAllUsers() {
        String url = String.format("%s/admin/realms/test-realm/%s/users", keycloakAuthServerUrl, keycloakRealm);
        try {
            ResponseEntity<List> response = keycloakRestTemplate.getForEntity(url, List.class);
            return response.getBody();
        } catch (RestClientException e) {
            e.printStackTrace(); // Log exception details
            throw new RuntimeException("Failed to retrieve users from Keycloak", e);
        }
    }

    public Map<String, Object> getUserById(String userId) {
        String url = String.format("%s/admin/realms/%s/users/%s", keycloakAuthServerUrl, keycloakRealm, userId);
        try {
            ResponseEntity<Map> response = keycloakRestTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to retrieve user from Keycloak", e);
        }
    }
}
