package com.mainApp.service.serviceInterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mainApp.requestdto.AttendanceCodeRequest;
import com.mainApp.requestdto.LabAllocationRequest;
import com.mainApp.requestdto.LabCreateRequest;
import com.mainApp.requestdto.LabSessionCreateRequest;
import com.mainApp.requestdto.LabSessionUpdateRequest;
import com.mainApp.requestdto.LabUpdateRequest;
import com.mainApp.requestdto.MaintenanceScheduleRequest;
import com.mainApp.requestdto.ManualAttendanceRequest;
import com.mainApp.responcedto.LabResponse;
import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.AttendanceResponse;
import com.mainApp.responcedto.EquipmentUsageResponse;
import com.mainApp.responcedto.LabAllocationResponse;
import com.mainApp.responcedto.LabSessionResponse;
import com.mainApp.responcedto.LabUtilizationResponse;
import com.mainApp.responcedto.MaintenanceResponse;
import com.mainApp.responcedto.ScheduleConflictResponse;
import com.mainApp.responcedto.SessionCodeResponse;
import com.mainApp.responcedto.TimeSlotResponse;

import com.mainApp.roles.SessionCodeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mainApp.responcedto.SubjectAttendanceReportResponse;

public interface LabService {
        // CRUD Operations
        LabResponse createLab(LabCreateRequest request);

        LabResponse getLabById(String id);

        LabResponse getLabByCode(String code);

        Page<LabResponse> getAllLabs(Pageable pageable);

        Page<LabResponse> getLabsByType(String type, Pageable pageable);

        Page<LabResponse> getLabsByBuilding(String building, Pageable pageable);

        LabResponse updateLab(String id, LabUpdateRequest request);

        void deleteLab(String id);

        // Session Management
        LabSessionResponse createLabSession(LabSessionCreateRequest request);

        LabSessionResponse getLabSessionById(String sessionId);

        List<LabSessionResponse> getLabSessions(String labId);

        List<LabSessionResponse> getLabSessionsByDate(String labId, LocalDate date);

        List<LabSessionResponse> getLabSessionsByTeacher(String labId, String teacherId);

        Page<LabSessionResponse> getAllSessions(com.mainApp.roles.LabSessionStatus status, String courseId,
                        Integer semester, String section, String subjectId, java.time.LocalDate sessionDate,
                        String keyword, Pageable pageable);

        LabSessionResponse updateLabSession(String sessionId, LabSessionUpdateRequest request);

        void deleteLabSession(String sessionId);

        void cancelLabSession(String sessionId, String reason);

        // Code Generation & Attendance
        SessionCodeResponse generateSessionCode(String sessionId, SessionCodeType type);

        AttendanceResponse markAttendanceWithCode(AttendanceCodeRequest request);

        void markManualAttendance(String sessionId, ManualAttendanceRequest request);

        List<AttendanceResponse> getSessionAttendance(String sessionId);

        AttendanceReportResponse getSessionAttendanceReport(String sessionId);

        void finalizeLabSession(String sessionId);

        void deleteAttendance(String attendanceId);

        // Get list of occupied PC numbers for a session
        List<String> getOccupiedComputerNumbers(String sessionId);

        // Allocation Management
        LabAllocationResponse allocatePC(String sessionId, LabAllocationRequest request);

        List<LabAllocationResponse> getSessionAllocations(String sessionId);

        void deallocatePC(String allocationId);

        // Availability & Scheduling
        List<TimeSlotResponse> getLabAvailability(String labId, LocalDate date);

        boolean isLabAvailable(String labId, LocalDateTime startTime, LocalDateTime endTime);

        ScheduleConflictResponse checkScheduleConflicts(LabSessionCreateRequest request);

        // Utilization & Analytics
        LabUtilizationResponse getLabUtilization(String labId, LocalDate fromDate, LocalDate toDate);

        EquipmentUsageResponse getEquipmentUsage(String labId);

        List<LabSessionResponse> getUpcomingSessions(String labId);

        // Maintenance
        void scheduleMaintenance(String labId, MaintenanceScheduleRequest request);

        List<MaintenanceResponse> getMaintenanceSchedule(String labId);

        // Search
        Page<LabResponse> searchLabs(String keyword, Pageable pageable);

        List<LabResponse> getLabsByCourseId(String courseId);

        SubjectAttendanceReportResponse getSubjectAttendanceReport(String courseId, Integer semester, String subjectId,
                        String section);
}