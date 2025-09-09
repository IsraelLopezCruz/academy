package com.priceshoes.academy.component;

import datadog.trace.api.CorrelationIdentifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class MDCFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String USER_ID_KEY = "userId";
    public static final String CLIENT_ID_KEY = "clientId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken){
            var principal = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
            String clientId = null;
            String userId = null;
            if (principal != null) {
                var jwt = (Jwt) principal.getPrincipal();
                //if is a idtoken the property change names
                if (jwt.getClaim("aud") != null) {
                    ArrayList<String> claims = jwt.getClaim("aud");
                    clientId = claims.get(0);
                } else {
                    clientId = jwt.getClaim("client_id").toString();
                }
                userId = jwt.getClaim("email");
            }

            MDC.put(TRACE_ID_KEY, CorrelationIdentifier.getTraceId());
            MDC.put(USER_ID_KEY, userId);
            MDC.put(CLIENT_ID_KEY, clientId);
            try {
                filterChain.doFilter(request, response);
            } finally {
                MDC.remove(TRACE_ID_KEY);
            }
        } else {
            try {
                filterChain.doFilter(request, response);
            } finally {
                MDC.remove(TRACE_ID_KEY);
            }
        }
    }
}