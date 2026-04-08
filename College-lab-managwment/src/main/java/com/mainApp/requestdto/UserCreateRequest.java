package com.mainApp.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String phoneNumber;
    @NotBlank
    private String role;
    private String department;
    private String designation;

    // Student specific fields for registration
    private String rollNumber;
    private String registrationNumber;
    private String batch;
    private String section;
    private String courseCode;
    private String courseName;
    private String academicYear;
    private Integer currentSemester;
}
