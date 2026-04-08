package com.mainApp.requestdto;

import lombok.Data;

@Data
public class SubjectUpdateRequest {
    private String subjectName;
    private String description;
    private Integer semesterNumber;
    private Integer theoryHours;
    private Integer labHours;
    private Integer totalCredits;
    private String prerequisites; // JSON
    private String syllabus;
    private String referenceBooks; // JSON
    private Long courseId;
}
