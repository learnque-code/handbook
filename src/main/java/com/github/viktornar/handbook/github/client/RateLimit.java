package com.github.viktornar.handbook.github.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateLimit {
    @Getter
    private final Integer limit;

    @Getter
    private final Integer remaining;

    @Getter
    private final Instant reset;

    @Getter
    private final Integer used;

    @JsonCreator
    public RateLimit(@JsonProperty("rate") Map<String, String> rate) {
        this.limit = Integer.parseInt(rate.get("limit"));
        this.remaining = Integer.parseInt(rate.get("remaining"));
        this.reset = Instant.ofEpochSecond(Long.parseLong(rate.get("reset")));
        this.used = Integer.parseInt(rate.get("used"));
    }

    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", this.limit);
        map.put("remaining", this.remaining);
        map.put("reset", this.reset);
        map.put("used", this.used);
        return map;
    }

}
