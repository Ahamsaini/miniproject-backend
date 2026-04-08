package com.mainApp.requestdto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentCreateRequest {
    // Basic User Fields (copied from UserCreateRequest)
    @NotBlank
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

    // Student Specific Fields
    @NotBlank
    private String rollNumber;
    private String registrationNumber;
    @NotNull
    private LocalDate dateOfBirth;
    @NotBlank
    private String gender;
    private String address;
    private String emergencyContact;
    private String guardianName;
    private String guardianContact;
    @Pattern(regexp = "^\\d{4}-\\d{4}$")
    private String academicYear;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer currentSemester;
    private String section;
    private String batch;
    @NotBlank
    private String courseId;
    private String bloodGroup;
    @Size(min = 12, max = 12)
    private String aadharNumber;
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
    private String panNumber;
}
