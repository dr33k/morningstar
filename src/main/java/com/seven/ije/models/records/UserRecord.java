package com.seven.ije.models.records;

import com.seven.ije.models.entities.User;
import com.seven.ije.models.enums.UserRole;
import com.seven.ije.models.requests.UserCreateRequest;
import com.seven.ije.models.requests.UserUpdateRequest;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record UserRecord(
        String firstName,
        String lastName,
        String phoneNo,
        String email,
        String password,
        LocalDate dateBirth,
        ZonedDateTime dateReg,
        ZonedDateTime dateModified,
        UserRole role,
        Boolean isAccountNonExpired,
        Boolean isAccountNonLocked,
        Boolean isCredentialsNonExpired,
        Boolean isEnabled,
        String message
) {
    public static UserRecord copy(User u){
        return new UserRecord(
                u.getFirstName(),
                u.getLastName(),
                u.getPhoneNo(),
                u.getEmail(),
                u.getPassword(),
                u.getDateBirth(),
                u.getDateReg(),
                u.getDateModified(),
                u.getRole(),
                u.isAccountNonExpired(),
                u.isAccountNonLocked(),
                u.isCredentialsNonExpired(),
                u.isEnabled(),
                null
        );
    }
    public static UserRecord copy(UserUpdateRequest u){
        return new UserRecord(
                u.getFirstName(),
                u.getLastName(),
                u.getPhoneNo(),
                null,
                u.getPassword(),
                u.getDateBirth(),
                null,
                null,
                null,
                u.getIsAccountNonExpired(),
                u.getIsAccountNonLocked(),
                u.getIsCredentialsNonExpired(),
                u.getIsEnabled(),
                null
        );
    }
    public static UserRecord copy(UserCreateRequest u){
        return new UserRecord(
                u.getFirstName(),
                u.getLastName(),
                u.getPhoneNo(),
                u.getEmail(),
                u.getPassword(),
                u.getDateBirth(),
                null,
                null,
                null,
                true,
                true,
                true,
                true,
                null
        );
    }
    public String toString(){
        return this.email;
    }
}