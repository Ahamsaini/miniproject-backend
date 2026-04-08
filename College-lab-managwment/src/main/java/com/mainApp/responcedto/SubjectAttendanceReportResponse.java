package com.mainApp.responcedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectAttendanceReportResponse {
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private String courseName;
    private Integer semester;
    private String section;
    private Long totalCompletedSessions;
    private List<StudentAttendanceStats> studentStats;
    private List<LabSessionResponse> sessions;
}
