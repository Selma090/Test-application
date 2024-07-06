package org.sid.jiraservice.sec;

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
                .cors().disable()
                .authorizeRequests()
                        .antMatchers("/api/jiras/all").hasAnyAuthority("CE", "CP", "Testeur")
                        .antMatchers("/api/jiras/create").hasAnyAuthority("CE", "Testeur")
                        .antMatchers("/api/jiras/delete/{id}").hasAuthority("CE")
                        .antMatchers("/api/jiras/update/{id}").hasAnyAuthority("CE", "Testeur")
                        .antMatchers("/api/jiras/{id}").hasAnyAuthority("CE", "Testeur")
                        .anyRequest().authenticated();
    }
}
