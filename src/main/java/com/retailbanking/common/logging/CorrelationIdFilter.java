package com.retailbanking.common.logging;

import com.retailbanking.common.constants.CorrelationConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.retailbanking.common.constants.CorrelationConstants.CORRELATION_ID;

@Slf4j
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //String correlationId = UUID.randomUUID().toString();
        String correlationId = request.getHeader(CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        response.setHeader(CORRELATION_ID, correlationId);
        MDC.put(CORRELATION_ID, correlationId);
        //System.out.println("Correlation ID = " + correlationId);
        log.info("Correlation ID : {}", correlationId);
        //System.out.println("Request URI = " + request.getRequestURI());
        log.info("Request URI : {}", request.getRequestURI());
        try {
            filterChain.doFilter(request, response);
        }finally {
            MDC.remove(CORRELATION_ID);
        }
    }
}