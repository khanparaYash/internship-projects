package org.example.catalogservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);
    private static final String HEADER_NAME = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String correlationId = request.getHeader(HEADER_NAME);

        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        response.setHeader(HEADER_NAME, correlationId);

        long startTime = System.currentTimeMillis();
        log.info("Incoming {} request: method={}, path={} port={}", correlationId, request.getMethod(), request.getRequestURI(), request.getServerPort());

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Request {} completed:  method={}, path={}, status={}, port={}, durationMs={}", correlationId, request.getMethod(), request.getRequestURI(), response.getStatus(), request.getServerPort(), duration);
        }
    }

}