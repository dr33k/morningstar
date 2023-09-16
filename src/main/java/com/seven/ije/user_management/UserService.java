package com.seven.ije.user_management;

import com.seven.ije.config.security.JwtService;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
@Transactional
public class UserService implements AppService <UserRecord, AppRequest>, UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Authentication userAuthentication;
    private JwtService jwtService;

    public UserService(UserRepository userRepository ,
                       BCryptPasswordEncoder passwordEncoder ,
                       Authentication userAuthentication) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
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
    public UserRecord get(){ return get(null);}
    @Override
    public UserRecord get(Object email) {
        String id = String.valueOf(userAuthentication.getPrincipal());

        User userFromDb;
        if (email == null) { //Signifies account owner access.
            userFromDb = userRepository.findByEmail(id)
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
            user.setPassword(bCryptPasswordEncoder.encode(userCreateRequest.getPassword()));
            //Save
            userRepository.save(user);

            return UserRecord.copy(user);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "User could not be created, please try again later. Why? " + ex.getMessage());
        }
    }

    public UserDTO register(AppRequest request){
        UserRecord record = create(request);
        String token = jwtService.generateToken(record.email(),
                Map.of("role", record.role().name(),
                            "privileges", record.role().privileges));

        return UserDTO.builder().data(record).token(token).build();
    }
    public UserDTO login (User user){
        String token = jwtService.generateToken(user.getUsername(),Map.of("role", user.getRole().name(),
                "privileges", user.getRole().privileges));
        return UserDTO.builder().data(UserRecord.copy(user)).token(token).build();
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
            UserUpdateRequest userUpdateRequest = (UserUpdateRequest) request;
            User user = userRepository.findByEmail(userUpdateRequest.getEmail())
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User account could not be found"));

            Boolean modified = false;

            //If the property is not null
            if (userUpdateRequest.getFirstName() != null) {
                user.setFirstName(userUpdateRequest.getFirstName());
                modified = true;
            }
            if (userUpdateRequest.getLastName() != null) {
                user.setLastName(userUpdateRequest.getLastName());
                modified = true;
            }
            if (userUpdateRequest.getPassword() != null) {
                user.setPassword(userUpdateRequest.getPassword());
                modified = true;
            }
            if (userUpdateRequest.getPhoneNo() != null) {
                user.setPhoneNo(userUpdateRequest.getPhoneNo());
                modified = true;
            }
            if (userUpdateRequest.getDateBirth() != null) {
                user.setDateBirth(userUpdateRequest.getDateBirth());
                modified = true;
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
            //Retrieve indicated User Object from the Database
            User userReturned = userRepository.findByEmail(userUpdateRequest.email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This user does not exist or has been deleted"));

            Boolean modified = false;

            //If the property is not null
            if (userUpdateRequest.getRole() != null) {//Upgrade or downgrade user role
                userReturned.setRole(userUpdateRequest.getRole());
                modified = true;
            }
            if (modified) userRepository.save(userReturned);

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