package com.seven.ije.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

public class UserAuthentication{
    @Bean
    @ApplicationScope
    public Authentication getInstance(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}