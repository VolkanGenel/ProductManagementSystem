package com.volkan.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateFilter implements Filter {

    private static final int REQUEST_LIMIT = 10;
    private static final long TIME_LIMIT_IN_SECONDS = 60;
    private static final int SC_TOO_MANY_REQUESTS = 429;

    private Map<String, Integer> requestCounts;
    private Map<String, LocalDateTime> lastRequestTimes;

    @Override
    public void init(FilterConfig filterConfig) {
        requestCounts = new HashMap<>();
        lastRequestTimes = new HashMap<>();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String ipAddress = servletRequest.getRemoteAddr();
        int requestCount = requestCounts.getOrDefault(ipAddress, 0);
        LocalDateTime lastRequestTime = lastRequestTimes.getOrDefault(ipAddress, LocalDateTime.now().minusSeconds(61));

        if (ChronoUnit.SECONDS.between(lastRequestTime, LocalDateTime.now()) <= TIME_LIMIT_IN_SECONDS) {
            if (requestCount >= REQUEST_LIMIT) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setHeader("Retry-After", String.valueOf(TIME_LIMIT_IN_SECONDS+" seconds!"));
                response.setStatus(SC_TOO_MANY_REQUESTS);
                response.sendError(SC_TOO_MANY_REQUESTS,"Too Many Requests");
                return;
            } else {
                requestCounts.put(ipAddress, requestCount + 1);
                lastRequestTimes.put(ipAddress, LocalDateTime.now());
            }
        } else {
            requestCounts.put(ipAddress, 1);
            lastRequestTimes.put(ipAddress, LocalDateTime.now());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        requestCounts.clear();
        lastRequestTimes.clear();
    }
}
