package com.mainApp.responcedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private long totalCourses;
    private long totalStudents;
    private long totalSessions;
    private long todaySessions;
    private long activeSessions;
    private long absentToday;

    // Graph Data
    private List<Map<String, Object>> sessionData; // Weekly sessions
    private List<Map<String, Object>> attendanceTrend; // Trend data
    private List<Map<String, Object>> userDistribution; // Student vs Teacher vs Admin
}
