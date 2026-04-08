package com.mainApp.responcedto;

import lombok.Data;
import java.util.Map;

@Data
public class SubjectAnalyticsResponse {
    private String subjectId;
    private String subjectCode;
    private String subjectName;
    private Double averageAttendance;
    private Integer totalStudents;
    private Integer totalSessions;
    private Map<String, Double> attendanceTrend; // Date -> Percentage
    private Map<String, Long> statusDistribution; // Status -> Count
}
