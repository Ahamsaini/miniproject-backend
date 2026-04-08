package com.mainApp.requestdto;

import com.mainApp.roles.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectEnrollmentRequest {
    @NotNull
    private String studentId;
    @NotNull
    private String subjectId;
    @NotNull
    private Integer semester;
    @NotNull
    private String academicYear;
    private EnrollmentStatus status;
}
