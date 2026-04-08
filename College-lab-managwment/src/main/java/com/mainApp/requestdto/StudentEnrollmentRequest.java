package com.mainApp.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentEnrollmentRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String courseId;
    private String academicYear;
    private Integer semester;
}
