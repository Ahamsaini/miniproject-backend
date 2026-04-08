package com.mainApp.responcedto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

//CourseResponse.java
@Data
public class CourseResponse {
    private String id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer durationYears;
    private Integer totalSemesters;
    private String department;
    private Integer totalCredits;
    private String status;
    private Integer studentCount;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("course")
    private List<SubjectResponse> subjects;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("course")
    private List<StudentResponse> enrolledStudents;
    private LocalDateTime createdAt;
}
