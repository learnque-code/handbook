package com.github.viktornar.handbook;

import com.github.viktornar.handbook.filter.RedirectToWelcomeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class FilterConfig {
    @Bean
    FilterRegistrationBean<Filter> welcomeForwardFilter () {
        FilterRegistrationBean<Filter> forwardFilter = new FilterRegistrationBean<>();
        forwardFilter.setFilter(new RedirectToWelcomeFilter());
        forwardFilter.setUrlPatterns(List.of("/guides/*"));
        return forwardFilter;
    }
}