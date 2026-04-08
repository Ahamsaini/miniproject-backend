package com.mainApp.responcedto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private String fullName;
    private String phoneNumber;
    private String role;
    private String department;
    private String designation;
    private Boolean isActive;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
