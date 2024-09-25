package com.seven.morningstar.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.ApplicationScope;
@Configuration
public class UserAuthentication{
    @Bean
    @ApplicationScope
    public Authentication getInstance(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}