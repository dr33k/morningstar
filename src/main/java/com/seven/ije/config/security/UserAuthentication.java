package com.seven.ije.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthentication{
    public Authentication getInstance(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}