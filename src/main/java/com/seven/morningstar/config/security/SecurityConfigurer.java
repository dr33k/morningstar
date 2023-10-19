package com.seven.morningstar.config.security;

import com.seven.morningstar.user_management.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigurer{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
//                .csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                .csrf().disable()

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/v1/register/**", "/v1/login/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui.html/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao =
                new DaoAuthenticationProvider();
        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(bCryptPasswordEncoder);
        return dao;
    }
}