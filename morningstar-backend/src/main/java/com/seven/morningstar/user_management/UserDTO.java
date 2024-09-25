package com.seven.morningstar.user_management;

import lombok.Builder;

@Builder
public class UserDTO {
    public UserRecord data;
    public String token;
}
