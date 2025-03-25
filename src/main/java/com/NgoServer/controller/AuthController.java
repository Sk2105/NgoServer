package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.ChangePasswordDTO;
import com.NgoServer.dto.LoginDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.TokenResponse;
import com.NgoServer.dto.UserDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.services.AuthServices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        ResponseDTO dto = authServices.registerUser(userDTO);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenResponse dto = authServices.loginUser(loginDTO);
        return ResponseEntity.ok().body(dto);

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

}
