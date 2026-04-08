package com.mainApp.service.serviceInterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mainApp.requestdto.AttendanceMarkRequest;
import com.mainApp.requestdto.AttendanceUpdateRequest;
import com.mainApp.requestdto.AvailabilityRequest;
import com.mainApp.requestdto.ReportRequest;
import com.mainApp.requestdto.SubjectExpertiseRequest;
import com.mainApp.requestdto.TeacherCreateRequest;
import com.mainApp.requestdto.TeacherUpdateRequest;
import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.AttendanceResponse;
import com.mainApp.responcedto.LabResponse;
import com.mainApp.responcedto.LabSessionResponse;
import com.mainApp.responcedto.ScheduleResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.TeacherExpertiseResponse;
import com.mainApp.responcedto.TeacherReportResponse;
import com.mainApp.responcedto.TeacherResponse;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService {
    // CRUD Operations
    TeacherResponse createTeacher(TeacherCreateRequest request);

    TeacherResponse getTeacherById(String id);

    TeacherResponse getTeacherByEmployeeId(String employeeId);

    Page<TeacherResponse> getAllTeachers(Pageable pageable);

    Page<TeacherResponse> getTeachersByDepartment(String department, Pageable pageable);

    TeacherResponse updateTeacher(String id, TeacherUpdateRequest request);

    void deleteTeacher(String id);

    // Expertise Management
    TeacherExpertiseResponse addSubjectExpertise(String teacherId, SubjectExpertiseRequest request);

    List<TeacherExpertiseResponse> getTeacherExpertises(String teacherId);

    void removeSubjectExpertise(String teacherId, String subjectId);

    void updateExpertiseLevel(String teacherId, String subjectId, String proficiencyLevel);

    // Lab Session Management
    List<LabSessionResponse> getAssignedLabSessions(String teacherId);

    LabSessionResponse getUpcomingLabSessions(String teacherId);

    List<LabSessionResponse> getLabSessionsByDate(String teacherId, LocalDate date);

    List<LabResponse> getAssignedLabs(String teacherId);

    // Student Management
    List<StudentResponse> getStudentsBySubject(String teacherId, String subjectId);

    AttendanceResponse markAttendance(AttendanceMarkRequest request);

    void updateAttendance(String attendanceId, AttendanceUpdateRequest request);

    // Reports
    TeacherReportResponse generateTeacherReport(String teacherId, ReportRequest request);

    List<AttendanceReportResponse> getAttendanceReport(String teacherId, String subjectId);

    com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse getTeacherSubjectAttendanceReport(String teacherId,
            String subjectId);

    // Schedule
    ScheduleResponse getTeacherSchedule(String teacherId);

    void updateAvailability(String teacherId, AvailabilityRequest request);

    // Search
    Page<TeacherResponse> searchTeachers(String keyword, Pageable pageable);

    List<TeacherResponse> getUnassignedTeachers();

    Page<TeacherResponse> getPendingTeachers(String department, String keyword, Pageable pageable);

    void approveTeacher(String id);

    void rejectTeacher(String id);

    void deleteTeacherWithReassignment(String teacherId, com.mainApp.requestdto.TeacherReassignmentRequest request);
}