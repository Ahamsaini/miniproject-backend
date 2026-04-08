package com.mainApp.responcedto;

import java.util.Map;

import lombok.Data;

@Data
public class AttendanceSummaryResponse {
    private String studentId;
    private String studentName;
    private String courseName;
    private Integer totalSessions;
    private Integer presentCount;
    private Integer absentCount;
    private Integer lateCount;
    private Double attendancePercentage;
    private Map<String, SubjectAttendanceSummary> subjectWiseSummary;

    @Data
    public static class SubjectAttendanceSummary {
        private String id;
        private String subjectCode;
        private String subjectName;
        private Integer totalSessions; // Will now represent Total Planned in syllabus
        private Integer conductedSessions; // Labs actually held
        private Integer attendedSessions; // Student participation count
        private Integer totalPlannedSessions; // Explicit alias for totalSessions for clarity
        private Double attendancePercentage;
    }
}
