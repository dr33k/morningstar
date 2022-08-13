package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.UserRole;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserRecord(
        String firstName,
        String lastName,
        String phoneNo,
        String email,
        String password,
        LocalDate dateBirth,
        LocalDateTime dateReg,
        UserRole role
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
                u.getRole()
        );
    }
}