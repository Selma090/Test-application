package org.sid.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/api/tests",  // Note added slash here for consistency
            "/auth/register-admin",
            "/auth/register-CE"
    );

    private static final Logger logger = Logger.getLogger(RouterValidator.class.getName());

    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                logger.info("Request Path: " + path);

                boolean isSecured = openApiEndpoints.stream().noneMatch(uri -> path.contains(uri));
                logger.info("Is Secured: " + isSecured);

                return isSecured;
            };
}
