package com.mainApp.requestdto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseEnrollmentRequest {
    @NotNull
    private String courseId;
    private String enrollmentNumber; // If applicable
    private String academicYear;
    private Integer semester;
}
