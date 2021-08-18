package com.github.viktornar.handbook.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class RedirectToWelcomeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if (servletRequest.getRequestURI().split("\\.").length == 1) {
            servletResponse.sendRedirect(servletRequest.getServletPath() + "/index.html");
            return;
        }

        chain.doFilter(request, response);
    }
}
