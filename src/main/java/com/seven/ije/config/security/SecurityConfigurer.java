package com.seven.ije.config.security;

import com.seven.ije.services.UserService;
import org.apache.catalina.startup.WebappServiceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/register/**").permitAll()
                .antMatchers("/administrator/**").hasRole("ADMIN")
                .antMatchers("/user/**", "/booking/**", "/ticket/**","/payment/**").hasAnyRole("ADMIN", "PASSENGER", "OFFICER")
                .antMatchers(HttpMethod.GET,"/dashboard/**","/locationDetails/**","/voyageDetails/**").hasAnyRole("ADMIN","PASSENGER","OFFICER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
//                .loginPage("/login").permitAll()
//                .failureUrl("/login")
                .defaultSuccessUrl("/dashboard", true)

                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
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
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

}