package com.github.viktornar.handbook;

import com.github.viktornar.handbook.filter.AccessTokenV1Filter;
import com.github.viktornar.handbook.filter.RedirectToWelcomeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;

@Configuration
public class FilterConfig {
    @Bean
    FilterRegistrationBean<Filter> guidesForwardFilter() {
        FilterRegistrationBean<Filter> forwardFilter = new FilterRegistrationBean<>();
        forwardFilter.setFilter(new RedirectToWelcomeFilter());
        forwardFilter.setUrlPatterns(List.of("/guides/*"));
        return forwardFilter;
    }

    @Bean
    FilterRegistrationBean<Filter> accessTokenV1Filter(HandbookProperties properties) {
        FilterRegistrationBean<Filter> accessTokenFilter = new FilterRegistrationBean<>();
        accessTokenFilter.setFilter(new AccessTokenV1Filter());
        accessTokenFilter.setUrlPatterns(List.of("/api/v1/*"));
        accessTokenFilter.setInitParameters(new HashMap<>(){{
            put("access_token", properties.getWeb().getAccessToken());
        }});
        return accessTokenFilter;
    }
}