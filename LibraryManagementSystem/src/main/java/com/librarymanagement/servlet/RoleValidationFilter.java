package com.librarymanagement.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter("/*")
public class RoleValidationFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
//        httpResponse.setDateHeader("Expires", 0);

        String uri = httpRequest.getRequestURI();

        // Check for admin pages
        if (uri.contains("admin")) {
            String admin = (String) httpRequest.getSession().getAttribute("admin");
            String user = (String) httpRequest.getSession().getAttribute("user");
            if (admin == null && user==null) {
                httpResponse.sendRedirect("index.html");
                return;
            }
        }

        // Check for user pages
        if (uri.contains("user")) {
            String admin = (String) httpRequest.getSession().getAttribute("admin");
            String user = (String) httpRequest.getSession().getAttribute("user");
            if (user == null && admin==null && !uri.contains("viewuser") ) {
                httpResponse.sendRedirect("index.html");
                return;
            }
        }

        chain.doFilter(request, response); // Continue processing the request
    }

    public void destroy() {
        // Cleanup if needed
    }
}

