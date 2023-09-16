package com.seven.ije.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var jwtSubject = request.getAttribute("subject");
        var jwtRole = request.getAttribute("role");
        Set<String> jwtPrivileges = (Set<String>) request.getAttribute("privileges");

        Method handlerMethod = (Method) handler;
        Authorize authorize = handlerMethod.getAnnotation(Authorize.class);

        if(authorize == null) return true;

        Set<String> subs = Arrays.stream(authorize.subs()).collect(Collectors.toSet());
        Set<String> roles = Arrays.stream(authorize.roles()).collect(Collectors.toSet());
        Set<String> privileges = Arrays.stream(authorize.privileges()).collect(Collectors.toSet());

        if(subs.contains(jwtSubject) || roles.contains(jwtRole) || privileges.stream().anyMatch((p)->jwtPrivileges.contains(p))) return true;

        return false;
    }
}
