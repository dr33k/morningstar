package com.seven.ije.config.security;

import com.seven.ije.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfigurer{

    @Autowired
    UserService userService;

    @Qualifier("bCryptPasswordEncoder")
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auths->{
                    auths.
                        requestMatchers(HttpMethod.GET, "/**","/register/**").permitAll()
                       .requestMatchers("/administrator/**","/swagger-ui.html/**").hasRole("ADMIN")
                       .requestMatchers("/user/**", "/booking/**", "/ticket/**","/payment/**").hasAnyRole("ADMIN", "PASSENGER", "OFFICER")
                       .requestMatchers(HttpMethod.GET,"/dashboard/**","/locationDetails/**","/voyageDetails/**").hasAnyRole("ADMIN","PASSENGER","OFFICER")
                       .anyRequest().authenticated();
                        }
                )
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login")
                .defaultSuccessUrl("/swagger-ui.html", true)

                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(userService)
                .key("398hhf984h93h9rh3ihni4r98on3i74hoskh3i47hi3hb4uyh9hs9h487hughs87h4i8h3kuks4y7gf48o37h8o0o0znbzkzb64t7a9983h8ga837gh83brjvn8s7hr78z6g76hgvVhvhc7ggv7v7vctrCccdn88g834hn3nr8378ha7g38b7a67ffad5s33s4333577ag8egiuauyvjahgf87h8ff7gh84374g76g87igsuj6grb78g76g475f7grg746g4g76tgsbiu4g")
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(14))

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login")

                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/login")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSION", "remember-me");

        return http.build();
    }

    @Bean
    public UserDetailsManager configure(AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();

        AuthenticationManager authenticationManager = auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .and().build();

        manager.setAuthenticationManager(authenticationManager);
        return manager;
    }

}