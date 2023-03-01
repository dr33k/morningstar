package com.seven.ije.user_management;

import com.seven.ije.enums.UserRole;
import com.seven.ije.AppRequest;
import com.seven.ije.AppService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
@Transactional
public class UserService implements AppService <UserRecord, AppRequest>, UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private Authentication userAuthentication;

    public UserService(UserRepository userRepository ,
                       BCryptPasswordEncoder passwordEncoder ,
                       Authentication userAuthentication) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthentication = userAuthentication;
    }

    //For Admin
    @Override
    public Set <UserRecord> getAll() {
        List <User> userList = userRepository.findAll();

        Set <UserRecord> userRecords =
                userList.stream().map(UserRecord::copy).collect(Collectors.toSet());

        return userRecords;
    }

    //For Both
    @Override
    public UserRecord get(Object email) {
        User user = (User) userAuthentication.getPrincipal();

        User userFromDb;
        if (email == null) { //Signifies account owner access.
            userFromDb = userRepository.findById(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This user does not exist or has been deleted"));
        } else {  // Signifies admin access. Admin can fetch any user without restrictions
            userFromDb = userRepository.findByEmail(email.toString())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This user does not exist or has been deleted"));
        }
        return UserRecord.copy(userFromDb);
    }

    @Override
    public UserRecord create(AppRequest request) {
        try {
            UserCreateRequest userCreateRequest = (UserCreateRequest) request;

            if (userRepository.existsByEmail(userCreateRequest.getEmail()))
                throw new ResponseStatusException(HttpStatus.CONFLICT , "A user with this email already exists");

            User user = new User();
            BeanUtils.copyProperties(userCreateRequest , user);

            //Set role
            user.setRole(UserRole.PASSENGER);

            //Encode password
            user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            //Save
            userRepository.save(user);

            return UserRecord.copy(user);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "User could not be created, please try again later. Why? " + ex.getMessage());
        }
    }

    //For User
    @Override
    public void delete(Object id) {//Only the user can deactivate their account
        User user = (User) userAuthentication.getPrincipal();
        if (!user.getEmail().equals((String) id)) throw new ResponseStatusException(HttpStatus.FORBIDDEN , "Account Breach");

        userRepository.deleteById((Long) id);
    }

    //For User
    @Override
    public UserRecord update(AppRequest request) {
        try {
            //Retrieve indicated User object from the Authentication principal
            User user = (User) userAuthentication.getPrincipal();
            UserUpdateRequest userUpdateRequest = (UserUpdateRequest) request;
            String email = userUpdateRequest.getEmail();

            if(email == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Please identify the user");
            if (!userRepository.existsByEmail(email))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND , "User account could not be found");

            Boolean modified = false;

            //If the property is not null
            if (userUpdateRequest.getFirstName() != null) {
                user.setFirstName(userUpdateRequest.getFirstName());
                modified = (modified) ? modified : true;
            }
            if (userUpdateRequest.getLastName() != null) {
                user.setLastName(userUpdateRequest.getLastName());
                modified = (modified) ? modified : true;
            }
            if (userUpdateRequest.getPassword() != null) {
                user.setPassword(userUpdateRequest.getPassword());
                modified = (modified) ? modified : true;
            }
            if (userUpdateRequest.getPhoneNo() != null) {
                user.setPhoneNo(userUpdateRequest.getPhoneNo());
                modified = (modified) ? modified : true;
            }
            if (userUpdateRequest.getDateBirth() != null) {
                user.setDateBirth(userUpdateRequest.getDateBirth());
                modified = (modified) ? modified : true;
            }
            if (modified) userRepository.save(user);

            return UserRecord.copy(user);

        }catch (ResponseStatusException ex) {throw ex;}
        catch (Exception ex) {
            throw new RuntimeException("User could not be modified, please contact System Administrator. Why? " + ex.getMessage());
        }
    }

    //For Admin
    public UserRecord updateForAdmin(AppRequest request) {
        try {
            UserUpdateRequest userUpdateRequest = (UserUpdateRequest) request;
            String email = userUpdateRequest.getEmail();
            if(email == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Please identify the user");
            //Retrieve indicated User Object from the Database
            User userReturned = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This user does not exist or has been deleted"));

            Boolean modified = false;

            //If the property is not null
            if (userUpdateRequest.getRole() != null) {//Upgrade or downgrade user role
                userReturned.setRole(userUpdateRequest.getRole());
                modified = true;
            }
            if (modified) {
                userRepository.save(userReturned);
            }
            return UserRecord.copy(userReturned);
        } catch (ResponseStatusException ex) {throw ex;}
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "User could not be modified, please contact System Aministrator. Why? " + ex.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}