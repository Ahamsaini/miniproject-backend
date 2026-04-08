package com.mainApp.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentUpdateRequest {
    private String firstName;
    private String gender;
    private String lastName;
    @Email
    private String email;
    private String phoneNumber;
    private String address;
    private String emergencyContact;
    private String guardianName;
    private String guardianContact;
    @Pattern(regexp = "^\\d{4}-\\d{4}$")
    private String academicYear;
    private String rollNumber;
    private String registrationNumber;
    @Min(1)
    @Max(12)
    private Integer currentSemester;
    private String section;
    private String batch;
    private String courseId;
    private String bloodGroup;
    private Boolean isActive;
}
