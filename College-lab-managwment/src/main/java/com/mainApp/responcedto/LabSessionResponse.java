package com.mainApp.responcedto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.mainApp.responcedto.LabResponse;

import lombok.Data;

//LabSessionResponse.java
@Data
public class LabSessionResponse {
    private String id;
    private LabResponse lab;
    private SubjectResponse subject;
    private TeacherResponse teacher;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String sessionTopic;
    private String experimentName;
    private String status;
    private String section;
    private Boolean isCodeGenerated;
    private List<SessionCodeResponse> sessionCodes;
    private List<AttendanceResponse> attendances;
    private Integer presentCount;
    private Integer absentCount;
    private Integer totalStudents;
}
