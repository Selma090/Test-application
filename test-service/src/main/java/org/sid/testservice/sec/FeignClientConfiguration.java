package org.sid.testservice.sec;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor keycloakFeignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                if (token != null) {
                    KeycloakSecurityContext context = token.getAccount().getKeycloakSecurityContext();
                    String accessToken = context.getTokenString();
                    requestTemplate.header("Authorization", "Bearer " + accessToken);
                }
            }
        };
    }
}
