package com.mainApp.requestdto;

import com.mainApp.roles.CourseStatus;
import lombok.Data;

@Data
public class CourseUpdateRequest {
    private String courseName;
    private String description;
    private Integer durationYears;
    private Integer totalSemesters;
    private String department;
    private Integer totalCredits;
    private String eligibilityCriteria;
    private String feeStructure; // JSON string
    private CourseStatus status;
}
