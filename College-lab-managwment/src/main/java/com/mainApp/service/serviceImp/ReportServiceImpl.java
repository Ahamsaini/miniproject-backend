package com.mainApp.service.serviceImp;

import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.SubjectAttendanceReportResponse;
import com.mainApp.responcedto.StudentAttendanceStats;
import com.mainApp.service.serviceInterface.ReportService;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public byte[] generateTeacherSubjectReportCSV(List<AttendanceReportResponse> data) {
        StringBuilder csv = new StringBuilder();
        csv.append("Student Name,Total Sessions,Attended Sessions,Attendance Percentage\n");

        for (AttendanceReportResponse row : data) {
            csv.append(String.format("%s,%d,%d,%.2f%%\n",
                    row.getSubjectName(), // We hijacked subjectName for studentName
                    row.getTotalStudents(),
                    row.getPresentCount(),
                    row.getAttendancePercentage()));
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] generateSubjectAttendanceReportCSV(SubjectAttendanceReportResponse data) {
        StringBuilder csv = new StringBuilder();
        csv.append("Course,Subject,Semester,Section,Total Sessions\n");
        csv.append(String.format("%s,%s,%d,%s,%d\n\n",
                data.getCourseName(),
                data.getSubjectName(),
                data.getSemester(),
                data.getSection() != null ? data.getSection() : "ALL",
                data.getTotalCompletedSessions()));

        csv.append("Roll Number,Student Name,Attended,Total,Percentage,Status\n");
        for (StudentAttendanceStats stat : data.getStudentStats()) {
            csv.append(String.format("%s,%s %s,%d,%d,%.2f%%,%s\n",
                    stat.getRollNumber(),
                    stat.getFirstName(),
                    stat.getLastName(),
                    stat.getAttendedSessionsCount(),
                    data.getTotalCompletedSessions(),
                    stat.getAttendancePercentage(),
                    stat.getAttendancePercentage() >= 75 ? "Regular" : "Low"));
        }

        csv.append("\n--- SESSION HISTORY ---\n");
        csv.append("Date,Time Slot,Lab,Section,Teacher,Status\n");
        if (data.getSessions() != null) {
            for (com.mainApp.responcedto.LabSessionResponse session : data.getSessions()) {
                String labName = session.getLab() != null ? session.getLab().getLabName() : "-";
                String teacherName = session.getTeacher() != null ? session.getTeacher().getFullName() : "-";
                csv.append(String.format("%s,%s - %s,%s,%s,%s,%s\n",
                        session.getSessionDate(),
                        session.getStartTime(),
                        session.getEndTime(),
                        labName,
                        session.getSection(),
                        teacherName,
                        session.getStatus()));
            }
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] generateTeacherDetailedReportCSV(
            com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse data) {
        StringBuilder csv = new StringBuilder();
        csv.append("Subject,Teacher,Total Sessions\n");
        csv.append(String.format("%s,%s,%d\n\n",
                data.getSubjectName(),
                data.getTeacherName(),
                data.getTotalCompletedSessions()));

        csv.append("--- STUDENT PERFORMANCE ---\n");
        csv.append("Roll Number,Student Name,Attended,Total,Percentage,Status\n");
        for (StudentAttendanceStats stat : data.getStudentStats()) {
            csv.append(String.format("%s,%s %s,%d,%d,%.2f%%,%s\n",
                    stat.getRollNumber(),
                    stat.getFirstName(),
                    stat.getLastName(),
                    stat.getAttendedSessionsCount(),
                    data.getTotalCompletedSessions(),
                    stat.getAttendancePercentage(),
                    stat.getAttendancePercentage() >= 75 ? "Regular" : "Low"));
        }

        csv.append("\n--- SESSION HISTORY ---\n");
        csv.append("Date,Time Slot,Lab,Section,Status\n");
        if (data.getSessions() != null) {
            for (com.mainApp.responcedto.LabSessionResponse session : data.getSessions()) {
                String labName = session.getLab() != null ? session.getLab().getLabName() : "-";
                csv.append(String.format("%s,%s - %s,%s,%s,%s\n",
                        session.getSessionDate(),
                        session.getStartTime(),
                        session.getEndTime(),
                        labName,
                        session.getSection(),
                        session.getStatus()));
            }
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
}
