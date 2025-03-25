package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.NgoServer.dto.LoginDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.TokenResponse;
import com.NgoServer.dto.UserDTO;
import com.NgoServer.exceptions.FoundEmptyElementException;
import com.NgoServer.exceptions.PasswordNotMatchException;
import com.NgoServer.exceptions.UserAlreadyExists;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.jwt.JwtUtil;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.models.Volunteer;
import com.NgoServer.repo.AuthRepository;
import com.NgoServer.repo.DonorRepository;
import com.NgoServer.repo.VolunteerRepository;
import com.NgoServer.utils.Role;

@Service
public class AuthServices implements UserDetailsService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    // register user
    public ResponseDTO registerUser(UserDTO userDTO) throws UserAlreadyExists {

        // check if user is already exists
        Optional<User> obj = authRepository.findByEmail(userDTO.email());
        if (obj.isPresent()) {
            throw new UserAlreadyExists("User Already Exists");
        }
        User user = toUser(userDTO);

        if (userDTO.role() == Role.DONOR) {
            Donor donor = new Donor();
            user.setRole(Role.DONOR);
            donor.setUser(user);
            authRepository.save(user);
            donorRepository.save(donor);
        } else if (userDTO.role() == Role.VOLUNTEER) {
            user.setRole(Role.VOLUNTEER);
            authRepository.save(user);
            Volunteer volunteer = new Volunteer();
            volunteer.setUser(user);
            volunteerRepository.save(volunteer);
        } else {
            user.setRole(Role.ADMIN);
            authRepository.save(user);
        }

        return new ResponseDTO("User Registered", HttpStatus.CREATED.value());

    }

    // login user
    public TokenResponse loginUser(LoginDTO loginDTO) throws UserNotFoundException {

        // check user is existed or not
        Optional<User> existedUser = authRepository.findByEmail(loginDTO.email());

        if (existedUser.isEmpty()) {
            throw new UserNotFoundException("User Not Found");
        }

        // check password
        String passwordHash = existedUser.get().getPasswordHash();
        if (!encoder.matches(loginDTO.password(), passwordHash)) {
            throw new PasswordNotMatchException("Invalid Password");
        }

        // generate token
        String token = jwtUtil.generateToken(existedUser.get().getEmail());
        return new TokenResponse(token, HttpStatus.OK.value());

    }

    private User toUser(UserDTO userDTO) {
        User user = new User();
        // username not empty
        if (userDTO.username().isEmpty()) {
            throw new FoundEmptyElementException("Username is required");
        }
        // set user
        user.setUsername(userDTO.username());

        // password not empty
        if (userDTO.password().isEmpty()) {
            throw new FoundEmptyElementException("Password is required");
        }
        // set password
        user.setPasswordHash(encoder.encode(userDTO.password()));

        // role not empty
        if (userDTO.role() == null) {
            throw new FoundEmptyElementException("Role is required");
        }

        // email not empty
        if (userDTO.email().isEmpty()) {
            throw new FoundEmptyElementException("Email is required");
        }
        // set email
        user.setEmail(userDTO.email());
        user.setPhoneNumber(userDTO.phoneNumber());

        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = authRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        if (user.get().getRole() == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail())
                .password(user.get().getPasswordHash()).authorities("ROLE_" + user.get().getRole().toString()).build();

        return org.springframework.security.core.userdetails.User.withUserDetails(userDetails)
                .password(user.get().getPasswordHash()).build();
    }

    public User getCurrentUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return authRepository.findByEmail(principal.getUsername()).get();
    }

    public ResponseDTO logoutUser() {
        SecurityContextHolder.clearContext();
        return new ResponseDTO("Logout Successfully", HttpStatus.OK.value());
    }

    public ResponseDTO changePassword(String oldPassword, String newPassword) throws PasswordNotMatchException {
        User user = getCurrentUserDetails();
        if (encoder.matches(oldPassword, user.getPasswordHash())) {
            user.setPasswordHash(encoder.encode(newPassword));
            authRepository.save(user);
            return new ResponseDTO("Password Changed Successfully", HttpStatus.OK.value());
        } else {
            throw new PasswordNotMatchException("Invalid Password");
        }
    }

    public List<User> getAllUsers() {
        return authRepository.findAll();
    }

}
