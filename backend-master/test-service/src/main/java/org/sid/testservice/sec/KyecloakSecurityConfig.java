package org.sid.testservice.sec;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class KyecloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
                .cors().disable() // Disable CORS handling
                .authorizeRequests()
                .antMatchers("/api/tests/counts/{jiraId}").hasAnyAuthority("CE", "CP", "Testeur")
                .antMatchers("/api/tests/{id}/validate").hasAnyAuthority("CE", "Testeur")
                .antMatchers("/api/tests/all").hasAnyAuthority("CE", "CP", "Testeur")
                .antMatchers("/api/tests/create").hasAnyAuthority("CE", "Testeur")
                .antMatchers("/api/tests/delete/{id}").hasAuthority("CE")
                .antMatchers("/api/tests/update/{id}").hasAnyAuthority("CE", "Testeur")
                .antMatchers("/api/tests/{id}").hasAnyAuthority("CE", "Testeur","CP")
                .antMatchers("/api/tests//user/{userId}/count").hasAnyAuthority("CE", "Testeur","CP")
                .anyRequest().authenticated();
    }
}
