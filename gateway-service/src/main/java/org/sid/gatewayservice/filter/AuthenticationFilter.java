package org.sid.gatewayservice.filter;

import com.google.common.net.HttpHeaders;
import org.sid.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouterValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            logger.info("Request URI: " + request.getURI());

            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.warning("Missing authorization header");
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                logger.info("Authorization Header: " + authHeader);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    logger.warning("Invalid authorization header format");
                    throw new RuntimeException("invalid authorization header");
                }

                try {
                    jwtUtil.validateToken(authHeader);
                    String username = jwtUtil.exctractUsername(authHeader);
                    logger.info("Extracted Username: " + username);

                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", username)
                            .build();

                } catch (Exception e) {
                    logger.severe("Invalid access: " + e.getMessage());
                    throw new RuntimeException("unauthorized access to application");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        // Put the configuration properties for your filter here
    }
}
