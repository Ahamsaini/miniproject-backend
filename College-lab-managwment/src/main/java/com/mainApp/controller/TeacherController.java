package com.mainApp.controller;

import com.mainApp.requestdto.*;
import com.mainApp.responcedto.*;
import com.mainApp.service.serviceInterface.TeacherService;
import com.mainApp.service.serviceInterface.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final ReportService reportService;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody @Valid TeacherCreateRequest request) {
        return new ResponseEntity<>(teacherService.createTeacher(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable String id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<TeacherResponse> getTeacherByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(teacherService.getTeacherByEmployeeId(employeeId));
    }

    @GetMapping
    public ResponseEntity<Page<TeacherResponse>> getAllTeachers(Pageable pageable) {
        return ResponseEntity.ok(teacherService.getAllTeachers(pageable));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<Page<TeacherResponse>> getTeachersByDepartment(@PathVariable String department,
            Pageable pageable) {
        return ResponseEntity.ok(teacherService.getTeachersByDepartment(department, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable String id,
            @RequestBody @Valid TeacherUpdateRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    // Expertise Management
    @PostMapping("/{teacherId}/expertise")
    public ResponseEntity<TeacherExpertiseResponse> addSubjectExpertise(@PathVariable String teacherId,
            @RequestBody @Valid SubjectExpertiseRequest request) {
        return new ResponseEntity<>(teacherService.addSubjectExpertise(teacherId, request), HttpStatus.CREATED);
    }

    @GetMapping("/{teacherId}/expertise")
    public ResponseEntity<List<TeacherExpertiseResponse>> getTeacherExpertises(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherExpertises(teacherId));
    }

    @DeleteMapping("/{teacherId}/expertise/{subjectId}")
    public ResponseEntity<Void> removeSubjectExpertise(@PathVariable String teacherId, @PathVariable String subjectId) {
        teacherService.removeSubjectExpertise(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{teacherId}/expertise/{subjectId}")
    public ResponseEntity<Void> updateExpertiseLevel(@PathVariable String teacherId, @PathVariable String subjectId,
            @RequestParam String proficiencyLevel) {
        teacherService.updateExpertiseLevel(teacherId, subjectId, proficiencyLevel);
        return ResponseEntity.ok().build();
    }

    // Lab Session Management
    @GetMapping("/{teacherId}/lab-sessions")
    public ResponseEntity<List<LabSessionResponse>> getAssignedLabSessions(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getAssignedLabSessions(teacherId));
    }

    @GetMapping("/{teacherId}/assigned-labs")
    public ResponseEntity<List<LabResponse>> getAssignedLabs(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getAssignedLabs(teacherId));
    }

    @GetMapping("/{teacherId}/lab-sessions/upcoming")
    public ResponseEntity<LabSessionResponse> getUpcomingLabSessions(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getUpcomingLabSessions(teacherId));
    }

    @GetMapping("/{teacherId}/lab-sessions/date/{date}")
    public ResponseEntity<List<LabSessionResponse>> getLabSessionsByDate(@PathVariable String teacherId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(teacherService.getLabSessionsByDate(teacherId, date));
    }

    // Student Management
    @GetMapping("/{teacherId}/subjects/{subjectId}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsBySubject(@PathVariable String teacherId,
            @PathVariable String subjectId) {
        return ResponseEntity.ok(teacherService.getStudentsBySubject(teacherId, subjectId));
    }

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody @Valid AttendanceMarkRequest request) {
        return ResponseEntity.ok(teacherService.markAttendance(request));
    }

    @PutMapping("/attendance/{attendanceId}")
    public ResponseEntity<Void> updateAttendance(@PathVariable String attendanceId,
            @RequestBody @Valid AttendanceUpdateRequest request) {
        teacherService.updateAttendance(attendanceId, request);
        return ResponseEntity.ok().build();
    }

    // Reports
    @PostMapping("/{teacherId}/reports")
    public ResponseEntity<TeacherReportResponse> generateTeacherReport(@PathVariable String teacherId,
            @RequestBody @Valid ReportRequest request) {
        return ResponseEntity.ok(teacherService.generateTeacherReport(teacherId, request));
    }

    @GetMapping("/{teacherId}/subjects/{subjectId}/attendance-report")
    public ResponseEntity<List<AttendanceReportResponse>> getAttendanceReport(@PathVariable String teacherId,
            @PathVariable String subjectId) {
        return ResponseEntity.ok(teacherService.getAttendanceReport(teacherId, subjectId));
    }

    @GetMapping("/{teacherId}/subjects/{subjectId}/detailed-report")
    public ResponseEntity<com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse> getDetailedAttendanceReport(
            @PathVariable String teacherId,
            @PathVariable String subjectId) {
        return ResponseEntity.ok(teacherService.getTeacherSubjectAttendanceReport(teacherId, subjectId));
    }

    @GetMapping("/{teacherId}/subjects/{subjectId}/attendance-report/export")
    public ResponseEntity<byte[]> exportAttendanceReport(@PathVariable String teacherId,
            @PathVariable String subjectId) {
        com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse data = teacherService
                .getTeacherSubjectAttendanceReport(teacherId, subjectId);
        byte[] csv = reportService.generateTeacherDetailedReportCSV(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=attendance_report_%s.csv", subjectId))
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    // Schedule
    @GetMapping("/{teacherId}/schedule")
    public ResponseEntity<ScheduleResponse> getTeacherSchedule(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherSchedule(teacherId));
    }

    @PutMapping("/{teacherId}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable String teacherId,
            @RequestBody @Valid AvailabilityRequest request) {
        teacherService.updateAvailability(teacherId, request);
        return ResponseEntity.ok().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<Page<TeacherResponse>> searchTeachers(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(teacherService.searchTeachers(keyword, pageable));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<TeacherResponse>> getUnassignedTeachers() {
        return ResponseEntity.ok(teacherService.getUnassignedTeachers());
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<TeacherResponse>> getPendingTeachers(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(teacherService.getPendingTeachers(department, keyword, pageable));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveTeacher(@PathVariable String id) {
        teacherService.approveTeacher(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectTeacher(@PathVariable String id) {
        teacherService.rejectTeacher(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reassign-and-delete")
    public ResponseEntity<Void> deleteTeacherWithReassignment(
            @PathVariable String id,
            @RequestBody @Valid com.mainApp.requestdto.TeacherReassignmentRequest request) {
        teacherService.deleteTeacherWithReassignment(id, request);
        return ResponseEntity.ok().build();
    }
}
