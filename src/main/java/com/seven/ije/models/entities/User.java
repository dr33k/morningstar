package com.seven.ije.models.entities;

import com.seven.ije.models.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity(name="r_user")
@Table(name="r_user")
@Data
@ToString
@NoArgsConstructor
public class User implements Serializable, UserDetails {
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
    private LocalDateTime dateReg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Boolean isAccountNonExpired = true;
    @Column(nullable = false)
    private Boolean isAccountNonLocked = true;
    @Column(nullable = false)
    private Boolean isCredentialsNonExpired = true;
    @Column(nullable = false)
    private Boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_"+this.role.name().toUpperCase());
        Set<SimpleGrantedAuthority> authorities = Set.of(sga);
        return authorities;
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
