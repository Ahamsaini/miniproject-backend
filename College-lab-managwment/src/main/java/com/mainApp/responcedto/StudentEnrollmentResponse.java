package com.mainApp.responcedto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class StudentEnrollmentResponse {
    private String enrollmentId;
    private StudentResponse student;
    private CourseResponse course;
    private String academicYear;
    private Integer semester;
    private String section;
    private String status;
    private LocalDate enrollmentDate;
    private List<SubjectEnrollmentResponse> subjectEnrollments;
}