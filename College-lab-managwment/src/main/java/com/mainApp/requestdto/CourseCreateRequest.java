package com.mainApp.requestdto;

import com.mainApp.roles.CourseStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseCreateRequest {
    @NotBlank
    private String courseCode;
    @NotBlank
    private String courseName;
    private String description;
    @Min(1)
    private Integer durationYears;
    @Min(1)
    private Integer totalSemesters;
    private String department;
    @Min(0)
    private Integer totalCredits;
    private String eligibilityCriteria;
    private String feeStructure; // JSON string
    @NotNull
    private CourseStatus status;
}
