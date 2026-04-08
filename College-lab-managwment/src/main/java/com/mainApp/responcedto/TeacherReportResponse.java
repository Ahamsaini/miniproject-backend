package com.mainApp.responcedto;

import lombok.Data;
import java.util.List;

@Data
public class TeacherReportResponse {
    private String teacherId;
    private String teacherName;
    private Integer totalLabsAssigned;
    private Integer totalSessionsConducted;
    private Double averageStudentAttendance;
    private List<SubjectResponse> expertiseSubjects;
    private List<AttendanceReportResponse> recentAttendanceReports;
}
