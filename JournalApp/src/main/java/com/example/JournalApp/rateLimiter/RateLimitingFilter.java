package com.example.JournalApp.rateLimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> bucketCache =
            new ConcurrentHashMap<>();

    private Bucket createNewBucket(){

        Bandwidth limit =
                Bandwidth.classic(
                        1,
                        Refill.greedy(
                                1,
                                Duration.ofMinutes(1)
                        )
                );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String ip =
                request.getRemoteAddr();

        Bucket bucket =
                bucketCache.computeIfAbsent(
                        ip,
                        k -> createNewBucket()
                );

        if(bucket.tryConsume(1)){

            filterChain.doFilter(
                    request,
                    response
            );

        } else {

            response.setStatus(429);

            response.getWriter()
                    .write("Too many requests. Try again after 1 minute.");

            return;
        }
    }
}