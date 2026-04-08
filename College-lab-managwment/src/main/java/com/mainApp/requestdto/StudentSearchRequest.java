package com.mainApp.requestdto;

import lombok.Data;

@Data
public class StudentSearchRequest {
    private String name;
    private String rollNumber;
    private String courseId;
    private String academicYear;
    private Integer currentSemester;
    private String section;
}
