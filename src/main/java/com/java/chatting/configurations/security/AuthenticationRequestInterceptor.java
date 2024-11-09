package com.java.chatting.configurations.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AuthenticationRequestInterceptor is a Feign RequestInterceptor that retrieves
 * the current request's Authorization header and passes it along in the Feign request.
 * This ensures the token-based authentication is propagated in downstream requests.
 */
@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    /**
     * Intercepts the Feign request to inject the Authorization header from the current HTTP request.
     *
     * @param requestTemplate The Feign request template to which the Authorization header will be added.
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Retrieve current request attributes (for accessing the Authorization header)
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Ensure the servletRequestAttributes is not null (could happen in a non-web request context)
        if (servletRequestAttributes != null) {
            // Get the Authorization header from the current HTTP request
            var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

            // Log the extracted header for debugging purposes
            log.info("Authorization Header: {}", authHeader);

            // If the Authorization header is present and not empty, add it to the Feign request
            if (StringUtils.hasText(authHeader)) {
                requestTemplate.header("Authorization", authHeader);
            }
        }
    }
}
