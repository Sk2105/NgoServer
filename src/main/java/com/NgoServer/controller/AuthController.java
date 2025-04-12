package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.ChangePasswordDTO;
import com.NgoServer.dto.LoginDTO;
import com.NgoServer.dto.OTPBodyDTO;
import com.NgoServer.dto.ResetPasswordDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.TokenResponse;
import com.NgoServer.dto.UserDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.services.AuthServices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    /**
     * This API is used to register a user
     * 
     * @param userDTO contains the information of the user
     * @return the token response
     */
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        TokenResponse dto = authServices.registerUser(userDTO);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * This API is used to login user
     * 
     * @param loginDTO contains email and password
     * @return TokenResponse which contains jwt token
     * @throws UserNotFoundException if user is not found
     */
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenResponse dto = authServices.loginUser(loginDTO);
        return ResponseEntity.ok().body(dto);

    }

    @PostMapping("/verify-otp")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'VOLUNTEER')")
    public ResponseEntity<?> verifyOTP(@RequestBody OTPBodyDTO responseDTO) {
        ResponseDTO message = authServices.verifyUser(responseDTO.otp());
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/resend-otp")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'VOLUNTEER')")
    public ResponseEntity<?> resendOTP() {
        ResponseDTO message = authServices.resendOTP();
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'VOLUNTEER')")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok().body(authServices.getCurrentUserDetails());
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'VOLUNTEER')")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body(authServices.logoutUser());
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'VOLUNTEER')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO)
            throws UserNotFoundException {
        return ResponseEntity.ok()
                .body(authServices.changePassword(changePasswordDTO.password(), changePasswordDTO.newPassword()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(authServices.getAllUsers());
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(authServices.getUserById(id));
    }

    @PutMapping("/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(authServices.updateUser(id, userDTO));
    }

    @GetMapping("/current-donor")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<?> getCurrentDonor() {
        return ResponseEntity.ok().body(authServices.getCurrentDonorDetails());
    }

    @GetMapping("/current-volunteer")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> getCurrentVolunteer() {
        return ResponseEntity.ok().body(authServices.getCurrentVolunteerDetails());
    }


    @PostMapping("/forgot-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> forgotPassword() {
        ResponseDTO message = authServices.forgetPassword();
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        ResponseDTO message = authServices.resetPassword(resetPasswordDTO.otp(), resetPasswordDTO.password());
        return ResponseEntity.ok().body(message);
    }
}
