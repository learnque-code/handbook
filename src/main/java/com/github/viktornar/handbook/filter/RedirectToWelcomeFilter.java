package com.github.viktornar.handbook.filter;

import com.github.viktornar.handbook.dao.GuideDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@AllArgsConstructor
@NoArgsConstructor
public class RedirectToWelcomeFilter implements Filter {
    private GuideDao guideDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if (servletRequest.getRequestURI().split("\\.").length == 1) {
            if (guideDao != null &&
                    guideDao.activeByUrlPath(servletRequest.getServletPath())) {
                servletResponse.sendRedirect(servletRequest.getServletPath() + "/index.html");
            } else {
                servletResponse.sendRedirect("/");
            }
            return;
        }

        chain.doFilter(request, response);
    }
}
