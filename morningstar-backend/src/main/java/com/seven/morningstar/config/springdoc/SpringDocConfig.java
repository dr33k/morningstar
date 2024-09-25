package com.seven.morningstar.config.springdoc;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwtAuth", scheme="bearer", bearerFormat = "JWT")
public class SpringDocConfig { }