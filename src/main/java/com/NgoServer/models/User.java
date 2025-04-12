package com.NgoServer.models;

import java.time.LocalDateTime;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.NgoServer.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String passwordHash;
    @Column(nullable = false)
    private String phoneNumber = "";
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ADMIN, DONOR, VOLUNTEER
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private Boolean isVerified = false;
    @JsonIgnore
    private Integer verificationCode = 0;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonIgnore
    private LocalDateTime verificationCodeExpiry = LocalDateTime.now().plusMinutes(5);

    public User(Long id, String username, String email, String phoneNumber, LocalDateTime createdAt, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
    }

}
