package com.github.viktornar.handbook.github.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimitHealthIndicator extends AbstractHealthIndicator {
    private final GithubClient githubClient;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        RateLimit rateLimitInfo = this.githubClient.fetchRateLimitInfo();
        builder = builder.withDetails(rateLimitInfo.asMap());
        if (rateLimitInfo.getRemaining() > 0) {
            builder.up();
        } else {
            builder.outOfService();
        }
    }
}
