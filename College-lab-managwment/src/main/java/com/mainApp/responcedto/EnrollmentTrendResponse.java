package com.mainApp.responcedto;

import lombok.Data;
import java.util.Map;

@Data
public class EnrollmentTrendResponse {
    private String courseId;
    private String courseName;
    private Map<String, Integer> enrollmentByYear; // Year -> Count
    private Map<String, Integer> enrollmentBySemester; // Semester -> Count
}
