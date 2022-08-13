package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="r_user")
@Table(name="r_user")
@Data
public class User {
    @Id
    @SequenceGenerator(name = "r_user_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(generator = "r_user_sequence",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false,length=15)
    private String phoneNo;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @Column(nullable = false)
    private LocalDateTime dateReg = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {
    }
}
