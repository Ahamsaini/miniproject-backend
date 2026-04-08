package com.mainApp.requestdto;

import com.mainApp.roles.CourseStatus;
import lombok.Data;

@Data
public class CourseSearchRequest {
    private String courseName;
    private String courseCode;
    private String department;
    private CourseStatus status;
}
