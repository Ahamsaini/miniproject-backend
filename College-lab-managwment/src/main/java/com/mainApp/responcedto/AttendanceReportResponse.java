package com.mainApp.responcedto;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
public class AttendanceReportResponse {
    private String sessionId;
    private String subjectName;
    private LocalDate reportDate;
    private Integer totalStudents;
    private Integer presentCount;
    private Integer absentCount;
    private Double attendancePercentage;
    private List<AttendanceResponse> studentAttendances;
}
