package com.authentification.configuation;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.authentification.utils.RSAKeyProperties;

import javax.ws.rs.HttpMethod;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final RSAKeyProperties keys;

    public SecurityConfig(RSAKeyProperties keys) {
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(@Autowired UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {

                    // Public Endpoints
                    auth.antMatchers("/auth/**").permitAll();

                    // Authenticated Endpoints
                    auth.antMatchers("/user/update-password").authenticated();

                    // Admin Endpoints
                    auth.antMatchers("/admin/**").hasRole("CP");

                    // Documentation Endpoints
                    auth.antMatchers(HttpMethod.POST, "/api/documentation").hasRole("CE");
                    auth.antMatchers("/api/documentation/{fileName}").hasAnyRole("CE", "TEST");
                    auth.antMatchers(HttpMethod.GET, "/api/documentation/files").hasAnyRole("CE", "TEST");

                    // Jira Endpoints
                    auth.antMatchers(HttpMethod.GET, "/api/jiras/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.PUT, "/api/jiras/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.DELETE, "/api/jiras/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.POST, "/api/jiras").hasAnyRole("CE", "TEST");
                    auth.antMatchers(HttpMethod.GET, "/api/jiras").hasAnyRole("CE", "CP");

                    // Test Endpoints
                    auth.antMatchers(HttpMethod.GET, "/api/tests/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.PUT, "/api/tests/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.DELETE, "/api/tests/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.POST, "/api/tests").hasRole("CE");
                    auth.antMatchers("/api/tests/counts/{jiraId}").hasAnyRole("CP", "CE", "TEST");
                    auth.antMatchers("/api/tests/{id}/validate").hasAnyRole("CE", "TEST");
                    auth.antMatchers(HttpMethod.GET, "/api/tests").hasAnyRole("CE", "TEST", "CP");

                    // KPI Endpoints
                    auth.antMatchers(HttpMethod.GET, "/api/kpis").hasRole("CE");
                    auth.antMatchers(HttpMethod.GET, "/api/kpis/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.PUT, "/api/kpis/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.DELETE, "/api/kpis/{id}").hasRole("CE");
                    auth.antMatchers(HttpMethod.POST, "/api/kpis").hasRole("CE");

                    // User Endpoints
                    auth.antMatchers("/user/**").hasAnyRole("CP", "CE", "TEST");

                    // Any other request needs to be authenticated
                    auth.anyRequest().authenticated();
                });

        http.headers().frameOptions().disable();

        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }
}
