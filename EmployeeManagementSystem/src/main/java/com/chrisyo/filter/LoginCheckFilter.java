package com.chrisyo.filter;

import com.chrisyo.exception.BusinessException;
import com.chrisyo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@WebFilter("/*") similar for urlPatterns, value = ..., and value can be striped
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {


    //Everytime it intercepts a request, it invokes once.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //Cast ServletRequest to HttpServletRequest
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //log.info(req.getRequestURI()); /emps
        //log.info(String.valueOf(req.getRequestURL())); http://localhost:8080/emps
        String uri = req.getRequestURI();
        //Let go of the login request
        if (uri.contains("login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            //end current request
            return;
        }
        //Non-login request:
        String token = req.getHeader("token");
        //No token, then not authenticated
        if (token == null) {
            log.error("No token in request header");
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token in request header");
            return;
            //throw new BusinessException("Not Authenticated, please login first");
        }
        try {
            Claims claims = JwtUtils.parseJwt(token);
            filterChain.doFilter(req, response); //Authenticated
        } catch (Exception e) {
            log.error("Invalid token in request header: {}", e.getMessage());
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token in request header");
            //throw new BusinessException("Not Authenticated, please login first");
        }

    }


    //Initialization method, when the web server runs, it invokes once
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    //When Web Server shutdown normally, it invokes once
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
