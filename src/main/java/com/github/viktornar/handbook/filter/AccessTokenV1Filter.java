package com.github.viktornar.handbook.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter
public class AccessTokenV1Filter implements Filter {
    private String accessToken = "";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        accessToken = filterConfig.getInitParameter("access_token");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.info("Try to check if access token is correct {{}}", accessToken);
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        var givenAccessToken = request.getParameter("access_token");

        if (accessToken.equals(givenAccessToken)) {
            log.info("Access token is valid. Passing request");
            chain.doFilter(req, res);
        } else {
            log.warn("Access token is not valid. Unauthorized access");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=utf-8");
            var w = response.getWriter();
            w.print("{ \"message\": \"Bad credentials\" }");
            w.close();
        }
    }
}
