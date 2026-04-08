package com.mainApp.controller;

import com.mainApp.requestdto.*;
import com.mainApp.responcedto.LabResponse;
import com.mainApp.responcedto.*;
import com.mainApp.service.serviceInterface.LabService;
import com.mainApp.service.serviceInterface.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.mainApp.roles.SessionCodeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
public class LabController {

    private final LabService labService;
    private final ReportService reportService;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<LabResponse> createLab(@RequestBody @Valid LabCreateRequest request) {
        return new ResponseEntity<>(labService.createLab(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResponse> getLabById(@PathVariable String id) {
        return ResponseEntity.ok(labService.getLabById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<LabResponse> getLabByCode(@PathVariable String code) {
        return ResponseEntity.ok(labService.getLabByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<LabResponse>> getAllLabs(Pageable pageable) {
        return ResponseEntity.ok(labService.getAllLabs(pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<LabResponse>> getLabsByType(@PathVariable String type, Pageable pageable) {
        return ResponseEntity.ok(labService.getLabsByType(type, pageable));
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<Page<LabResponse>> getLabsByBuilding(@PathVariable String building, Pageable pageable) {
        return ResponseEntity.ok(labService.getLabsByBuilding(building, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResponse> updateLab(@PathVariable String id,
            @RequestBody @Valid LabUpdateRequest request) {
        return ResponseEntity.ok(labService.updateLab(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable String id) {
        labService.deleteLab(id);
        return ResponseEntity.noContent().build();
    }

    // Session Management
    @PostMapping("/sessions")
    public ResponseEntity<LabSessionResponse> createLabSession(@RequestBody @Valid LabSessionCreateRequest request) {
        return new ResponseEntity<>(labService.createLabSession(request), HttpStatus.CREATED);
    }

    @GetMapping("/sessions")
    public ResponseEntity<Page<LabSessionResponse>> getAllSessions(
            @RequestParam(required = false) com.mainApp.roles.LabSessionStatus status,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity
                .ok(labService.getAllSessions(status, courseId, semester, section, subjectId, sessionDate, keyword,
                        pageable));
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<LabSessionResponse> getLabSessionById(@PathVariable String sessionId) {
        return ResponseEntity.ok(labService.getLabSessionById(sessionId));
    }

    @GetMapping("/{labId}/sessions")
    public ResponseEntity<List<LabSessionResponse>> getLabSessions(@PathVariable String labId) {
        return ResponseEntity.ok(labService.getLabSessions(labId));
    }

    @GetMapping("/{labId}/sessions/date/{date}")
    public ResponseEntity<List<LabSessionResponse>> getLabSessionsByDate(@PathVariable String labId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(labService.getLabSessionsByDate(labId, date));
    }

    @GetMapping("/{labId}/sessions/teacher/{teacherId}")
    public ResponseEntity<List<LabSessionResponse>> getLabSessionsByTeacher(@PathVariable String labId,
            @PathVariable String teacherId) {
        return ResponseEntity.ok(labService.getLabSessionsByTeacher(labId, teacherId));
    }

    @PutMapping("/sessions/{sessionId}")
    public ResponseEntity<LabSessionResponse> updateLabSession(@PathVariable String sessionId,
            @RequestBody @Valid LabSessionUpdateRequest request) {
        return ResponseEntity.ok(labService.updateLabSession(sessionId, request));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteLabSession(@PathVariable String sessionId) {
        labService.deleteLabSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sessions/{sessionId}/cancel")
    public ResponseEntity<Void> cancelLabSession(@PathVariable String sessionId, @RequestParam String reason) {
        labService.cancelLabSession(sessionId, reason);
        return ResponseEntity.ok().build();
    }

    // Code Generation & Attendance
    @PostMapping("/sessions/{sessionId}/generate-code")
    public ResponseEntity<SessionCodeResponse> generateSessionCode(@PathVariable String sessionId,
            @RequestParam SessionCodeType type) {
        return ResponseEntity.ok(labService.generateSessionCode(sessionId, type));
    }

    @PostMapping("/sessions/attendance/mark")
    public ResponseEntity<AttendanceResponse> markAttendanceWithCode(
            @RequestBody @Valid AttendanceCodeRequest request) {
        return ResponseEntity.ok(labService.markAttendanceWithCode(request));
    }

    @PostMapping("/sessions/{sessionId}/attendance/manual")
    public ResponseEntity<Void> markManualAttendance(@PathVariable String sessionId,
            @RequestBody @Valid ManualAttendanceRequest request) {
        labService.markManualAttendance(sessionId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions/{sessionId}/attendance")
    public ResponseEntity<List<AttendanceResponse>> getSessionAttendance(@PathVariable String sessionId) {
        return ResponseEntity.ok(labService.getSessionAttendance(sessionId));
    }

    @GetMapping("/sessions/{sessionId}/attendance/report")
    public ResponseEntity<AttendanceReportResponse> getSessionAttendanceReport(@PathVariable String sessionId) {
        return ResponseEntity.ok(labService.getSessionAttendanceReport(sessionId));
    }

    @PostMapping("/sessions/{sessionId}/finalize")
    public ResponseEntity<Void> finalizeLabSession(@PathVariable String sessionId) {
        labService.finalizeLabSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sessions/attendance/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable String attendanceId) {
        labService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions/{sessionId}/occupied-computers")
    public ResponseEntity<List<String>> getOccupiedComputers(@PathVariable String sessionId) {
        return ResponseEntity.ok(labService.getOccupiedComputerNumbers(sessionId));
    }

    // Allocation Management
    @PostMapping("/sessions/{sessionId}/allocations")
    public ResponseEntity<LabAllocationResponse> allocatePC(@PathVariable String sessionId,
            @RequestBody @Valid LabAllocationRequest request) {
        return ResponseEntity.ok(labService.allocatePC(sessionId, request));
    }

    @GetMapping("/sessions/{sessionId}/allocations")
    public ResponseEntity<List<LabAllocationResponse>> getSessionAllocations(@PathVariable String sessionId) {
        return ResponseEntity.ok(labService.getSessionAllocations(sessionId));
    }

    @DeleteMapping("/allocations/{allocationId}")
    public ResponseEntity<Void> deallocatePC(@PathVariable String allocationId) {
        labService.deallocatePC(allocationId);
        return ResponseEntity.noContent().build();
    }

    // Availability & Scheduling
    @GetMapping("/{labId}/availability")
    public ResponseEntity<List<TimeSlotResponse>> getLabAvailability(@PathVariable String labId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(labService.getLabAvailability(labId, date));
    }

    @GetMapping("/{labId}/is-available")
    public ResponseEntity<Boolean> isLabAvailable(@PathVariable String labId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(labService.isLabAvailable(labId, startTime, endTime));
    }

    @PostMapping("/check-conflicts")
    public ResponseEntity<ScheduleConflictResponse> checkScheduleConflicts(
            @RequestBody @Valid LabSessionCreateRequest request) {
        return ResponseEntity.ok(labService.checkScheduleConflicts(request));
    }

    // Utilization & Analytics
    @GetMapping("/{labId}/utilization")
    public ResponseEntity<LabUtilizationResponse> getLabUtilization(@PathVariable String labId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(labService.getLabUtilization(labId, fromDate, toDate));
    }

    @GetMapping("/{labId}/equipment-usage")
    public ResponseEntity<EquipmentUsageResponse> getEquipmentUsage(@PathVariable String labId) {
        return ResponseEntity.ok(labService.getEquipmentUsage(labId));
    }

    @GetMapping("/{labId}/upcoming-sessions")
    public ResponseEntity<List<LabSessionResponse>> getUpcomingSessions(@PathVariable String labId) {
        return ResponseEntity.ok(labService.getUpcomingSessions(labId));
    }

    // Maintenance
    @PostMapping("/{labId}/maintenance")
    public ResponseEntity<Void> scheduleMaintenance(@PathVariable String labId,
            @RequestBody @Valid MaintenanceScheduleRequest request) {
        labService.scheduleMaintenance(labId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{labId}/maintenance")
    public ResponseEntity<List<MaintenanceResponse>> getMaintenanceSchedule(@PathVariable String labId) {
        return ResponseEntity.ok(labService.getMaintenanceSchedule(labId));
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<Page<LabResponse>> searchLabs(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(labService.searchLabs(keyword, pageable));
    }

    @GetMapping("/reports/subject-attendance")
    public ResponseEntity<SubjectAttendanceReportResponse> getSubjectAttendanceReport(
            @RequestParam String courseId,
            @RequestParam Integer semester,
            @RequestParam String subjectId,
            @RequestParam(required = false) String section) {
        return ResponseEntity.ok(labService.getSubjectAttendanceReport(courseId, semester, subjectId, section));
    }

    @GetMapping("/reports/subject-attendance/export")
    public ResponseEntity<byte[]> exportSubjectAttendanceReport(
            @RequestParam String courseId,
            @RequestParam Integer semester,
            @RequestParam String subjectId,
            @RequestParam(required = false) String section) {
        SubjectAttendanceReportResponse data = labService.getSubjectAttendanceReport(courseId, semester, subjectId,
                section);
        byte[] csv = reportService.generateSubjectAttendanceReportCSV(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=subject_attendance_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
