package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserRequest {
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^\\s{2,30}",message = "Name must be at least 2 characters long")
    private String firstName;
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^\\s{2,30}",message = "Name must be at least 2 characters long")
    private String lastName;
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[+-][0-9]{11,15}$",message = "Name must be at least 2 characters long")
    private String phoneNo;
    @NotBlank(message = "Required field")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Required field")
    private String password;
    @Past(message = "Future and current dates not allowed")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate dateBirth;
}
