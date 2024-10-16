package com.inditex.apps.platform.backend.middlewares;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no special action is needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Generate a unique identifier for the http request
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);// add to Mapped Diagnostic Context
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        // no special action is needed
    }
}
