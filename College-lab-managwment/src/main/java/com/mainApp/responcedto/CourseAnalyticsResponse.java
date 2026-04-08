package com.mainApp.responcedto;

import lombok.Data;
import java.util.Map;

@Data
public class CourseAnalyticsResponse {
    private String courseId;
    private String courseCode;
    private String courseName;
    private Integer totalStudents;
    private Integer totalSubjects;
    private Double averageAttendance;
    private Map<String, Integer> studentsPerSemester;
}
