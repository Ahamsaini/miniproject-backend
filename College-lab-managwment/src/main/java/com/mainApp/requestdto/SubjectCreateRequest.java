package com.mainApp.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectCreateRequest {
    @NotBlank
    private String subjectCode;
    @NotBlank
    private String subjectName;
    private String description;
    @NotNull
    @Min(1)
    private Integer semesterNumber;
    @Min(0)
    private Integer theoryHours;
    @Min(0)
    private Integer labHours;
    @Min(0)
    private Integer totalCredits;
    private String prerequisites; // JSON
    private String syllabus;
    private String referenceBooks; // JSON
    @NotNull
    private String courseId;
}
