package com.mentorlik.mentorlik_backend.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:4200",
        "http://localhost",
        "http://localhost:80"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        // Get Origin header
        String origin = request.getHeader("Origin");
        String method = request.getMethod();
        String path = request.getRequestURI();
        
        log.info("CORS Filter processing request: {} {} from Origin: {}", method, path, origin);
        
        // Check if origin is one of our allowed origins and set it back in the response
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            log.debug("Allowing Origin: {}", origin);
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else if (origin != null) {
            log.warn("Rejected Origin: {}", origin);
        }
        
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", 
            "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control, X-Auth-Token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // Для OPTIONS запросов сразу возвращаем ответ 200 OK
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.debug("Responding with 200 OK to OPTIONS request");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.debug("Continuing filter chain for {} request", method);
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("CustomCorsFilter initialized");
    }

    @Override
    public void destroy() {
        log.info("CustomCorsFilter destroyed");
    }
} 