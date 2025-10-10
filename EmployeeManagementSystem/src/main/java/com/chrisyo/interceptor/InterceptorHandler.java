package com.chrisyo.interceptor;

import com.chrisyo.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Component
public class InterceptorHandler implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //Return true if let go, false means no
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //Non-login request:
        String token = request.getHeader("token");
        //No token, then not authenticated
        if (!StringUtils.hasLength(token)) {
            log.error("No token in request header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> body = Map.of(
                    "code", HttpServletResponse.SC_UNAUTHORIZED,
                    "message", "No token in request header."
            );
            objectMapper.writeValue(response.getWriter(), body);
            response.getWriter().flush();
            return false;

        }
        try {
            Claims claims = JwtUtils.parseJwt(token);
            return true; //Authenticated
        } catch (Exception e) {
            log.error("Invalid token in request header: {}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> body = Map.of(
                    "code", HttpServletResponse.SC_UNAUTHORIZED,
                    "message", "invalid token, Try login again."
            );
            objectMapper.writeValue(response.getWriter(), body);
            response.getWriter().flush();
            return false;
        }
    }






    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
