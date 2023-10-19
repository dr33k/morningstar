package com.seven.morningstar.constants;

import com.seven.morningstar.enums.UserRole;
import com.seven.morningstar.user_management.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestConstants {
    String TOKEN = "Bearer token";
    Claims ADMINCLAIMS = new DefaultClaims(
            Map.of(
                    "sub", "user",
                    "role", "ADMIN",
                    "privileges", List.of(
                            "user:c", "user:r", "user:u", "user:d"
                    ),
                    "exp", new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
            )
    );

    User ADMIN = new User("John", "Doe", "phone", "admin@seven.com", "password", LocalDate.of(1000, 1, 1), UserRole.ADMIN);


}
