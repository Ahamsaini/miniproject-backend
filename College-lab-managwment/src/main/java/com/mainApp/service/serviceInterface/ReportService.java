package com.mainApp.service.serviceInterface;

import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.SubjectAttendanceReportResponse;
import java.util.List;

public interface ReportService {
    byte[] generateTeacherSubjectReportCSV(List<AttendanceReportResponse> data);

    byte[] generateSubjectAttendanceReportCSV(SubjectAttendanceReportResponse data);

    byte[] generateTeacherDetailedReportCSV(com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse data);
}
