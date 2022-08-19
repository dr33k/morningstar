package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserUpdateRequest {
    @Pattern(regexp = "^[A-Za-z'\\-]{2,30}",message = "Name must be at least 2 characters long")
    String firstName;
    @Pattern(regexp = "^[A-Za-z'\\-]{2,30}",message = "Name must be at least 2 characters long")
    String lastName ;
    @Pattern(regexp = "^[+-][0-9]{11,15}$",message = "Name must be at least 2 characters long")
    String phoneNo ;
    @Pattern(regexp = "^[A-Za-z\\d'.!@#$%^&*_\\-]{8,}",message = "Password must be at least 8 characters long")
    String password;
    @Past(message = "Future and current dates not allowed")
    LocalDate dateBirth;
    Boolean isAccountNonExpired;
    Boolean isAccountNonLocked;
    Boolean isCredentialsNonExpired;
    Boolean isEnabled;
}
