package com.mainApp.responcedto;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
public class SubjectResponse {

    private String id;

    private String subjectCode;
    private String subjectName;
    private String description;

    private Integer semesterNumber;
    private Integer theoryHours;
    private Integer labHours;
    private Integer totalCredits;

    private String prerequisites;
    private String syllabus;
    private String referenceBooks;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CourseResponse course;

}
