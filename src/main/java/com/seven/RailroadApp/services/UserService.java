package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.UserRole;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService extends com.seven.RailroadApp.services.Service {
    @Autowired
    UserRepository userRepository;

    @Override
    Set<? extends Record> getAll() {
        Set<UserRecord> userRecords = new HashSet<>(0);
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            UserRecord userRecord = UserRecord.copy(user);
            userRecords.add(userRecord);
        }
        return userRecords;
    }
    @Override
    Record get(Object id) {
        try {
            String userId = (String) id;
            Optional<User> userReturned = userRepository.findByEmail(userId);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return userReturned.map(UserRecord::copy).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    Record create(Record recordObject) {
        try {
            UserRecord userRecord = (UserRecord) recordObject;
            User user = new User();
            BeanUtils.copyProperties(userRecord, user);

            //Set role
            user.setRole(UserRole.PASSENGER);
            //Set date of registration
            user.setDateReg(LocalDateTime.now());

            //Save
            userRepository.save(user);

            return UserRecord.copy(user);
        }catch (Exception ex){return null;}
    }

    @Override
    Boolean delete(Object id) {
        try {
            UserRecord userRecord = (UserRecord)get((String) id);
            if(userRecord != null) userRepository.deleteByEmail(userRecord.email());
        } catch (Exception ex) {return false;}
        return false;
    }

    @Override
    Record update(Record recordObject) {
        try {//Retrieve indicated User Object from the Database
            UserRecord propertiesToUpdate = (UserRecord) recordObject;
            Optional<User> userReturned = userRepository.findByEmail(propertiesToUpdate.email());

            if (userReturned.isPresent()) {
                User user = userReturned.get();
                Boolean modified = false;
                //If the property is not null and is a different value from before
                if(propertiesToUpdate.firstName()!=null && !propertiesToUpdate.firstName().equals(user.getFirstName())) {
                    user.setFirstName(propertiesToUpdate.firstName());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.lastName()!=null && !propertiesToUpdate.lastName().equals(user.getLastName())) {
                    user.setLastName(propertiesToUpdate.lastName());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.email()!=null && !propertiesToUpdate.email().equals(user.getEmail())) {
                    user.setEmail(propertiesToUpdate.email());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.password()!=null && !propertiesToUpdate.password().equals(user.getPassword())) {
                    user.setPassword(propertiesToUpdate.password());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.dateBirth()!=null && !propertiesToUpdate.dateBirth().equals(user.getDateBirth())) {
                    user.setDateBirth(propertiesToUpdate.dateBirth());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.firstName()!=null && !propertiesToUpdate.firstName().equals(user.getFirstName())) {
                    user.setFirstName(propertiesToUpdate.firstName());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.phoneNo()!=null && !propertiesToUpdate.phoneNo().equals(user.getPhoneNo())) {
                    user.setPhoneNo(propertiesToUpdate.phoneNo());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.role()!=null && !propertiesToUpdate.role().equals(user.getRole())) {
                    user.setRole(propertiesToUpdate.role());
                    modified =  (modified)?modified:true;
                }

                if(modified) {
                    userRepository.save(user);
                    return UserRecord.copy(user);
                }
            }
        } catch (Exception ex) {return null;}
        return null;
    }
}