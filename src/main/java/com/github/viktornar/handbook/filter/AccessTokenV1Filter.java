package com.github.viktornar.handbook.filter;

import com.github.viktornar.handbook.HandbookProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter
@RequiredArgsConstructor
public class AccessTokenV1Filter implements Filter {
    private final HandbookProperties properties;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.info("Try to check if access token is correct {{}}", properties.getWeb().getAccessToken());
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        var accessToken = request.getParameter("access_token");
        // TODO: replace with apache commons String utils since thymeleaf may be replaced with groovy template page.
        if (StringUtils.equals(accessToken, properties.getWeb().getAccessToken())) {
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
