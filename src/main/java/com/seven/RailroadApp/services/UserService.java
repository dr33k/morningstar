package com.seven.RailroadApp.services;

import com.seven.RailroadApp.config.security.PasswordEncoder;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.UserRole;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements com.seven.RailroadApp.services.Service, UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Set<UserRecord> getAll() {
        Set<UserRecord> userRecords = new HashSet<>(0);
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            UserRecord userRecord = UserRecord.copy(user);
            userRecords.add(userRecord);
        }
        return userRecords;
    }

    @Override
    public Record get(Object id) {
        try {
            String userId = (String) id;
            Optional<User> userReturned = userRepository.findByEmail(userId);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return userReturned.map(UserRecord::copy).orElse(null);
        } catch (Exception ex) {
            return new UserRecord(null, null, null,null, null, null, null, null, null, null, null, null,
                    "User not found, please make sure search credentials are entered properly. Possibly: " + ex.getMessage()
            );
        }
    }

    @Override
    public Record create(Record recordObject) {
        try {//Cast into UserRecord
            UserRecord userRecord = (UserRecord) recordObject;

            Optional<User> userReturned = userRepository.findByEmail(userRecord.email());

            if (userReturned.isEmpty()) {
                User user = new User();
                BeanUtils.copyProperties(userRecord, user);

                //Set role
                user.setRole(UserRole.PASSENGER);
                //Set date of registration
                user.setDateReg(LocalDateTime.now());
                user.setPassword(passwordEncoder.encode(userRecord.password()));

                //Save
                userRepository.save(user);
                return UserRecord.copy(user);
            }
            return new UserRecord(null, null, null, null, null, null, null, null,null, null, null, null,
                    "User with this email already exists");
        } catch (Exception ex) {
            return new UserRecord(null, null, null, null, null, null, null, null,null, null, null, null,
                    "User could be created, please try again later. Why? " + ex.getMessage());
        }
    }

    @Override
    public Boolean delete(Object id) {
        try {
            String email = (String) id;
            Optional<User> uOpt = userRepository.findByEmail(email);
            if (uOpt.isPresent()) {
                userRepository.deleteByEmail(email);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
    @Override
    public Record update(Record recordObject) {
        UserRecord ur = (UserRecord) recordObject;
        try {//Retrieve indicated User Object from the Database
            Optional<User> userReturned = userRepository.findByEmail(ur.email());

            if (userReturned.isPresent()) {
                User user = userReturned.get();
                Boolean modified = false;

                //If the property is not null and is a different value from before
                if (ur.firstName() != null && !ur.firstName().equals(user.getFirstName())) {
                    user.setFirstName(ur.firstName());
                    modified = (modified) ? modified : true;
                } else if (ur.lastName() != null && !ur.lastName().equals(user.getLastName())) {
                    user.setLastName(ur.lastName());
                    modified = (modified) ? modified : true;
                } else if (ur.password() != null && !ur.password().equals(user.getPassword())) {
                    user.setPassword(ur.password());
                    modified = (modified) ? modified : true;
                } else if (ur.phoneNo() != null && !ur.phoneNo().equals(user.getPhoneNo())) {
                    user.setPhoneNo(ur.phoneNo());
                    modified = (modified) ? modified : true;
                } else if (ur.dateBirth()!= null && !ur.dateBirth().equals(user.getDateBirth())) {
                    user.setDateBirth(ur.dateBirth());
                    modified = (modified) ? modified : true;
                }
            if (modified) {
                userRepository.save(user);
                return UserRecord.copy(user);
            }
        }
    } catch(   Exception ex)

    {
        return new UserRecord(null, null, null, null,null, null, null, null, null, null, null, null,
                "User could not be modified, please try again. Why? " + ex.getMessage()
        );
    }
        return null;
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
    }
}