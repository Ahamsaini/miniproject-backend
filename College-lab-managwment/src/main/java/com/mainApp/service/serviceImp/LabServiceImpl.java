package com.mainApp.service.serviceImp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.AttendanceMapper;
import com.mainApp.mapper.LabAllocationMapper;
import com.mainApp.mapper.LabMapper;
import com.mainApp.mapper.LabSessionMapper;
import com.mainApp.mapper.SessionCodeMapper;
import com.mainApp.model.Lab;
import com.mainApp.model.LabSession;
import com.mainApp.model.SessionCode;
import com.mainApp.model.Student;
import com.mainApp.model.Subject;
import com.mainApp.model.Teacher;
import com.mainApp.model.Attendance;
import com.mainApp.model.LabAllocation;
import com.mainApp.repository.AttendanceRepository;
import com.mainApp.repository.LabAllocationRepository;
import com.mainApp.repository.LabRepository;
import com.mainApp.repository.LabSessionRepository;
import com.mainApp.repository.SessionCodeRepository;
import com.mainApp.repository.StudentRepository;
import com.mainApp.repository.SubjectRepository;
import com.mainApp.repository.TeacherRepository;
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
import com.mainApp.roles.LabSessionStatus;
import com.mainApp.roles.LabType;
import com.mainApp.roles.AttendanceStatus;
import com.mainApp.roles.AllocationStatus;
import com.mainApp.roles.SessionCodeType;
import com.mainApp.responcedto.SubjectAttendanceReportResponse;
import com.mainApp.responcedto.StudentAttendanceStats;
import com.mainApp.service.serviceInterface.LabService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabServiceImpl implements LabService {

    private final LabRepository labRepository;
    private final LabSessionRepository labSessionRepository;
    private final SessionCodeRepository sessionCodeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LabAllocationRepository labAllocationRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    private final LabMapper labMapper;
    private final LabSessionMapper labSessionMapper;
    private final SessionCodeMapper sessionCodeMapper;
    private final AttendanceMapper attendanceMapper;
    private final LabAllocationMapper labAllocationMapper;

    @Override
    @Transactional
    public LabResponse createLab(LabCreateRequest request) {
        Lab lab = labMapper.toEntity(request);
        lab = labRepository.save(lab);
        return labMapper.toResponse(lab);
    }

    @Override
    @Transactional(readOnly = true)
    public LabResponse getLabById(String id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with id: " + id));
        return labMapper.toResponse(lab);
    }

    @Override
    public LabResponse getLabByCode(String code) {
        Lab lab = labRepository.findByLabCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with code: " + code));
        return labMapper.toResponse(lab);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabResponse> getAllLabs(Pageable pageable) {
        return labRepository.findAll(pageable).map(labMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabResponse> getLabsByType(String type, Pageable pageable) {
        return labRepository.findByLabType(LabType.valueOf(type), pageable).map(labMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabResponse> getLabsByBuilding(String building, Pageable pageable) {
        return labRepository.findByBuilding(building, pageable).map(labMapper::toResponse);
    }

    @Override
    @Transactional
    public LabResponse updateLab(String id, LabUpdateRequest request) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with id: " + id));
        labMapper.updateEntity(request, lab);
        lab = labRepository.save(lab);
        return labMapper.toResponse(lab);
    }

    @Override
    @Transactional
    public void deleteLab(String id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with id: " + id));
        lab.setIsActive(false);
        labRepository.save(lab);
    }

    @Override
    @Transactional
    public LabSessionResponse createLabSession(LabSessionCreateRequest request) {
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with id: " + request.getLabId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));

        LabSession session = labSessionMapper.toEntity(request);
        session.setLab(lab);
        session.setSubject(subject);
        session.setTeacher(teacher);
        session.setSection(request.getSection());
        session.setStatus(LabSessionStatus.SCHEDULED);

        // Perform conflict check
        ScheduleConflictResponse conflicts = checkScheduleConflicts(request);
        if (conflicts.isHasConflict()) {
            throw new RuntimeException("Schedule conflict: " + String.join(", ", conflicts.getMessages()));
        }

        session = labSessionRepository.save(session);

        LabSession savedSession = session;
        // Auto-assign students from the course AND semester AND section
        List<Student> students = studentRepository.findByCourseAndSemesterAndSection(
                subject.getCourse().getId(),
                subject.getSemesterNumber(),
                request.getSection());
        List<Attendance> attendances = students.stream().map(studentRef -> {
            Attendance attendance = new Attendance();
            attendance.setStudent(studentRef);
            attendance.setLabSession(savedSession);
            // Status left as null to indicate "Not yet marked" or initial state
            return attendance;
        }).collect(Collectors.toList());

        attendanceRepository.saveAll(attendances);

        return labSessionMapper.toResponse(session);
    }

    @Override
    @Transactional(readOnly = true)
    public LabSessionResponse getLabSessionById(String sessionId) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with id: " + sessionId));
        return labSessionMapper.toResponse(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabSessionResponse> getLabSessions(String labId) {
        return labSessionRepository.findByLabId(labId).stream()
                .map(labSessionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabSessionResponse> getLabSessionsByDate(String labId, LocalDate date) {
        return labSessionRepository.findByLabIdAndSessionDate(labId, date).stream()
                .map(labSessionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabSessionResponse> getLabSessionsByTeacher(String labId, String teacherId) {
        return labSessionRepository.findByTeacherId(teacherId).stream()
                .filter(s -> s.getLab().getId().equals(labId))
                .map(labSessionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabSessionResponse updateLabSession(String sessionId, LabSessionUpdateRequest request) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with id: " + sessionId));

        // Create a temporary creation request for conflict check
        LabSessionCreateRequest conflictRequest = new LabSessionCreateRequest();
        conflictRequest.setLabId(request.getLabId() != null ? request.getLabId() : session.getLab().getId());
        conflictRequest
                .setTeacherId(request.getTeacherId() != null ? request.getTeacherId() : session.getTeacher().getId());
        conflictRequest
                .setSubjectId(request.getSubjectId() != null ? request.getSubjectId() : session.getSubject().getId());
        conflictRequest
                .setSessionDate(request.getSessionDate() != null ? request.getSessionDate() : session.getSessionDate());
        conflictRequest.setStartTime(request.getStartTime() != null ? request.getStartTime() : session.getStartTime());
        conflictRequest.setEndTime(request.getEndTime() != null ? request.getEndTime() : session.getEndTime());
        conflictRequest.setSection(request.getSection() != null ? request.getSection() : session.getSection());

        ScheduleConflictResponse conflicts = checkScheduleConflictsWithExclude(conflictRequest, sessionId);
        if (conflicts.isHasConflict()) {
            throw new RuntimeException("Schedule conflict: " + String.join(", ", conflicts.getMessages()));
        }

        labSessionMapper.updateEntity(request, session);
        session = labSessionRepository.save(session);
        return session != null ? labSessionMapper.toResponse(session) : null;
    }

    @Override
    @Transactional
    public void deleteLabSession(String sessionId) {
        labSessionRepository.deleteById(sessionId);
    }

    @Override
    @Transactional
    public void cancelLabSession(String sessionId, String reason) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with id: " + sessionId));
        session.setStatus(LabSessionStatus.CANCELLED);
        labSessionRepository.save(session);
    }

    @Override
    public List<String> getOccupiedComputerNumbers(String sessionId) {
        return attendanceRepository.findByLabSessionId(sessionId)
                .stream()
                .filter(a -> a.getPcNumber() != null && !a.getPcNumber().trim().isEmpty() && a.getEntryTime() != null
                        && a.getExitTime() == null)
                .map(a -> a.getPcNumber().toUpperCase())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SessionCodeResponse generateSessionCode(String sessionId, SessionCodeType type) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with id: " + sessionId));

        // Find ANY existing code for this session to avoid UniqueConstraint violation
        // (Ensuring only one row per session ID exists in the DB)
        SessionCode code = sessionCodeRepository.findByLabSessionId(sessionId).stream().findFirst()
                .orElseGet(() -> {
                    SessionCode newCode = new SessionCode();
                    newCode.setLabSession(session);
                    return newCode;
                });

        code.setType(type); // Update to the requested type (ENTRY or EXIT)
        code.setCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        code.setGeneratedAt(LocalDateTime.now());
        code.setExpiresAt(LocalDateTime.now().plusHours(2));
        code.setIsValid(true);
        code.setIsUsed(false);
        code.setTotalUses(0);

        code = sessionCodeRepository.save(code);
        session.setIsCodeGenerated(true);
        session.setCodeGeneratedAt(LocalDateTime.now());

        // Update session status to ONGOING if starting
        if (type == SessionCodeType.ENTRY && session.getStatus() == LabSessionStatus.SCHEDULED) {
            session.setStatus(LabSessionStatus.ONGOING);
        }

        labSessionRepository.save(session);

        return sessionCodeMapper.toResponse(code);
    }

    @Override
    @Transactional
    public AttendanceResponse markAttendanceWithCode(AttendanceCodeRequest request) {
        SessionCode sessionCode = sessionCodeRepository.findByCode(request.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid session code"));

        if (!sessionCode.getIsValid() || sessionCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session code has expired or is invalid");
        }

        if (!sessionCode.getLabSession().getId().equals(request.getSessionId())) {
            throw new RuntimeException("Session code does not match the requested session");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Find existing attendance for this student in this session
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByStudentIdAndLabSessionId(student.getId(), sessionCode.getLabSession().getId());

        if ("EXIT".equalsIgnoreCase(request.getType())) {
            // EXIT logic: must have existing attendance with entry time
            Attendance attendance = existingAttendance
                    .orElseThrow(() -> new IllegalStateException("Cannot mark exit without joining the session first"));

            if (attendance.getExitTime() != null) {
                throw new IllegalStateException("You have already marked exit from this session");
            }

            attendance.setExitTime(LocalDateTime.now());
            attendance.setVerifiedAt(LocalDateTime.now());

            // Calculate duration in minutes
            if (attendance.getEntryTime() != null) {
                long minutes = java.time.Duration.between(attendance.getEntryTime(), attendance.getExitTime())
                        .toMinutes();
                attendance.setDurationMinutes((int) minutes);
            }

            // Ensure status is set
            if (attendance.getStatus() == null) {
                attendance.setStatus(AttendanceStatus.PRESENT);
            }

            attendance = attendanceRepository.save(attendance);
            return attendanceMapper.toResponse(attendance);

        } else {
            // ENTRY logic: should NOT have existing entry time
            if (existingAttendance.isPresent() && existingAttendance.get().getEntryTime() != null) {
                throw new IllegalStateException("You have already joined this session");
            }

            // Normalize and Validate PC number uniqueness (only check active sessions)
            String normalizedPcNumber = request.getPcNumber() != null ? request.getPcNumber().trim().toUpperCase()
                    : null;

            if (normalizedPcNumber != null && !normalizedPcNumber.isEmpty()) {
                boolean pcOccupied = attendanceRepository.existsByLabSessionIdAndPcNumberAndExitTimeIsNull(
                        sessionCode.getLabSession().getId(),
                        normalizedPcNumber);

                if (pcOccupied) {
                    throw new IllegalStateException("Computer " + normalizedPcNumber
                            + " is currently occupied by another student. Please use a different machine.");
                }
            }

            // Create new attendance or use existing one (if exit was marked but coming
            // back)
            Attendance attendance = existingAttendance.orElse(new Attendance());
            attendance.setStudent(student);
            attendance.setLabSession(sessionCode.getLabSession());
            attendance.setEntryTime(LocalDateTime.now());
            attendance.setStatus(AttendanceStatus.PRESENT);
            attendance.setVerifiedAt(LocalDateTime.now());

            if (normalizedPcNumber != null) {
                attendance.setPcNumber(normalizedPcNumber);
            }

            attendance = attendanceRepository.save(attendance);
            return attendanceMapper.toResponse(attendance);
        }
    }

    @Override
    @Transactional
    public void markManualAttendance(String sessionId, ManualAttendanceRequest request) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Attendance attendance = attendanceRepository
                .findByStudentIdAndLabSessionId(student.getId(), session.getId())
                .orElse(new Attendance());

        attendance.setStudent(student);
        attendance.setLabSession(session);
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());
        attendance.setEntryTime(LocalDateTime.now());
        attendance.setVerifiedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }

    @Override
    public List<AttendanceResponse> getSessionAttendance(String sessionId) {
        return attendanceRepository.findByLabSessionId(sessionId).stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceReportResponse getSessionAttendanceReport(String sessionId) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        List<Attendance> attendances = attendanceRepository.findByLabSessionId(sessionId);

        AttendanceReportResponse report = new AttendanceReportResponse();
        report.setSessionId(session.getId());
        report.setSubjectName(session.getSubject().getSubjectName());
        report.setReportDate(session.getSessionDate());
        report.setTotalStudents(attendances.size());
        report.setPresentCount((int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count());
        report.setAbsentCount((int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count());
        report.setAttendancePercentage(
                attendances.size() > 0 ? (double) report.getPresentCount() / attendances.size() * 100 : 0.0);
        report.setStudentAttendances(attendances.stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList()));

        return report;
    }

    @Override
    @Transactional
    public void finalizeLabSession(String sessionId) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setStatus(LabSessionStatus.COMPLETED);
        session.setAttendanceMarked(true);
        session.setAttendanceMarkedAt(LocalDateTime.now());
        labSessionRepository.save(session);

        // Invalidate all codes for this session
        sessionCodeRepository.findByLabSessionId(sessionId).forEach(c -> {
            c.setIsValid(false);
            sessionCodeRepository.save(c);
        });
    }

    @Override
    @Transactional
    public void deleteAttendance(String attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    @Override
    @Transactional
    public LabAllocationResponse allocatePC(String sessionId, LabAllocationRequest request) {
        LabSession session = labSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        LabAllocation allocation = labAllocationRepository
                .findByStudentIdAndLabSessionId(student.getId(), session.getId())
                .orElse(new LabAllocation());

        allocation.setStudent(student);
        allocation.setLabSession(session);
        allocation.setPcNumber(request.getPcNumber());
        allocation.setStatus(request.getStatus() != null ? request.getStatus() : AllocationStatus.ALLOCATED);
        allocation.setAllocationDate(LocalDate.now());
        allocation.setAllocationNotes(request.getAllocationNotes());

        allocation = labAllocationRepository.save(allocation);
        return labAllocationMapper.toResponse(allocation);
    }

    @Override
    public List<LabAllocationResponse> getSessionAllocations(String sessionId) {
        return labAllocationRepository.findByLabSessionId(sessionId).stream()
                .map(labAllocationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deallocatePC(String allocationId) {
        labAllocationRepository.deleteById(allocationId);
    }

    @Override
    public List<TimeSlotResponse> getLabAvailability(String labId, LocalDate date) {
        return List.of();
    }

    @Override
    public boolean isLabAvailable(String labId, LocalDateTime startTime, LocalDateTime endTime) {
        return true;
    }

    @Override
    public ScheduleConflictResponse checkScheduleConflicts(LabSessionCreateRequest request) {
        return checkScheduleConflictsWithExclude(request, null);
    }

    private ScheduleConflictResponse checkScheduleConflictsWithExclude(LabSessionCreateRequest request,
            String excludeSessionId) {
        ScheduleConflictResponse response = new ScheduleConflictResponse();

        if (request.getSessionDate() == null || request.getStartTime() == null || request.getEndTime() == null) {
            return response;
        }

        List<LabSession> sessionsOnDate = labSessionRepository.findBySessionDate(request.getSessionDate());

        // Filter out the session being updated and CANCELLED sessions
        List<LabSession> activeSessions = sessionsOnDate.stream()
                .filter(s -> excludeSessionId == null || !s.getId().equals(excludeSessionId))
                .filter(s -> s.getStatus() != LabSessionStatus.CANCELLED)
                .collect(Collectors.toList());

        Subject newSubject = subjectRepository.findById(request.getSubjectId()).orElse(null);
        if (newSubject == null) {
            // If subject is invalid, we can't check student conflicts properly, but we can
            // check teacher/lab
            log.warn("Checking conflicts for invalid subject ID: {}", request.getSubjectId());
        }

        for (LabSession existing : activeSessions) {
            // Check time overlap
            // Overlap = (StartA < EndB) && (EndA > StartB)
            boolean overlaps = (request.getStartTime().isBefore(existing.getEndTime()) &&
                    request.getEndTime().isAfter(existing.getStartTime()));

            if (overlaps) {
                log.info("Overlap detected with Session ID: {}", existing.getId());

                // 1. Lab Room Conflict
                if (existing.getLab().getId().equals(request.getLabId())) {
                    response.addMessage("Lab room '" + existing.getLab().getLabName() + "' is already booked for " +
                            existing.getSubject().getSubjectName() + " (" + existing.getStartTime() + " - "
                            + existing.getEndTime() + ")");
                }

                // 2. Teacher Conflict
                if (existing.getTeacher().getId().equals(request.getTeacherId())) {
                    response.addMessage("Teacher '" + existing.getTeacher().getFirstName() + " "
                            + existing.getTeacher().getLastName() +
                            "' is already assigned to another lab session (" + existing.getStartTime() + " - "
                            + existing.getEndTime() + ")");
                }

                // 3. Student Group Conflict (Course + Semester + Section)
                if (newSubject != null) {
                    boolean sameCourse = existing.getSubject().getCourse().getId()
                            .equals(newSubject.getCourse().getId());

                    // Explicit checking of nulls to avoid NPE
                    Integer existingSem = existing.getSubject().getSemesterNumber();
                    Integer newSem = newSubject.getSemesterNumber();
                    boolean sameSemester = existingSem != null && newSem != null && existingSem.equals(newSem);

                    // Section Logic:
                    // Conflict if: Both are same, OR one is "ALL"/Null (which covers everyone)
                    String reqSection = request.getSection() != null ? request.getSection().trim() : "";
                    String existSection = existing.getSection() != null ? existing.getSection().trim() : "";

                    boolean sameSection = (reqSection.isEmpty() || reqSection.equalsIgnoreCase("ALL") ||
                            existSection.isEmpty() || existSection.equalsIgnoreCase("ALL") ||
                            existSection.equalsIgnoreCase(reqSection));

                    if (sameCourse && sameSemester && sameSection) {
                        response.addMessage("Students of " + newSubject.getCourse().getCourseName() + " (Sem "
                                + newSubject.getSemesterNumber() + ", Sec "
                                + (reqSection.isEmpty() ? "ALL" : reqSection) +
                                ") already have another lab session: " + existing.getSubject().getSubjectName() +
                                " (" + existing.getStartTime() + " - " + existing.getEndTime() + ")");
                    }
                }
            }
        }

        return response;
    }

    @Override
    public LabUtilizationResponse getLabUtilization(String labId, LocalDate fromDate, LocalDate toDate) {
        return new LabUtilizationResponse();
    }

    @Override
    public EquipmentUsageResponse getEquipmentUsage(String labId) {
        return new EquipmentUsageResponse();
    }

    @Override
    public List<LabSessionResponse> getUpcomingSessions(String labId) {
        return List.of();
    }

    @Override
    @Transactional
    public void scheduleMaintenance(String labId, MaintenanceScheduleRequest request) {
        // Maintenance logic
    }

    @Override
    public List<MaintenanceResponse> getMaintenanceSchedule(String labId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabResponse> searchLabs(String keyword, Pageable pageable) {
        return labRepository.searchLabs(keyword, pageable).map(labMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabSessionResponse> getAllSessions(LabSessionStatus status, String courseId, Integer semester,
            String section, String subjectId, java.time.LocalDate sessionDate, String keyword, Pageable pageable) {
        return labSessionRepository
                .searchSessions(status, courseId, semester, section, subjectId, sessionDate, keyword, pageable)
                .map(labSessionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResponse> getLabsByCourseId(String courseId) {
        return labRepository.findLabsByCourseId(courseId).stream()
                .map(labMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectAttendanceReportResponse getSubjectAttendanceReport(String courseId, Integer semester,
            String subjectId, String section) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        // 1. Find all COMPLETED sessions for this subject/semester/course
        List<LabSession> allCompletedSessions = labSessionRepository
                .findBySubjectIdAndSubjectSemesterNumberAndSubjectCourseIdAndStatus(
                        subjectId, semester, courseId, LabSessionStatus.COMPLETED);

        // 2. Fetch all students in this cohort (Course + Semester)
        List<Student> studentsList = studentRepository.findByCourseAndSemester(courseId, semester);

        // Context-aware filtering
        final String targetSection = (section == null || section.trim().isEmpty() || section.equalsIgnoreCase("all"))
                ? null
                : section.trim();

        // Filter students by section if specified
        if (targetSection != null) {
            final String normalizedTarget = targetSection.toLowerCase().replace("section", "").trim();
            studentsList = studentsList.stream()
                    .filter(s -> {
                        String sSection = s.getSection() != null ? s.getSection().trim() : "";
                        String normalizedStudent = sSection.toLowerCase().replace("section", "").trim();
                        return normalizedTarget.equals(normalizedStudent);
                    })
                    .collect(Collectors.toList());
        }

        // Filter sessions to those relevant for the target section
        // If targetSection is null (ALL), we take all sessions.
        // If targetSection is 'A', we take sessions marked 'A', 'ALL', or null.
        List<LabSession> filteredSessions = allCompletedSessions;
        if (targetSection != null) {
            final String normalizedTarget = targetSection.toLowerCase().replace("section", "").trim();
            filteredSessions = allCompletedSessions.stream()
                    .filter(s -> {
                        String sSection = s.getSection() != null ? s.getSection().trim() : "";
                        if (sSection.isEmpty() || sSection.equalsIgnoreCase("ALL"))
                            return true;

                        String normalizedSession = sSection.toLowerCase().replace("section", "").trim();
                        return normalizedTarget.equals(normalizedSession);
                    })
                    .collect(Collectors.toList());
        }

        final List<LabSession> completedSessionsForCohort = filteredSessions;
        final long totalSessions = completedSessionsForCohort.size();

        final List<Student> students = studentsList;

        // 3. Aggregate stats for each student
        List<StudentAttendanceStats> stats = students.stream().map(student -> {
            long attendedCount = completedSessionsForCohort.stream()
                    .filter(session -> session.getAttendances().stream()
                            .anyMatch(a -> a.getStudent().getId().equals(student.getId()) &&
                                    a.getStatus() == AttendanceStatus.PRESENT))
                    .count();

            double percentage = totalSessions > 0 ? (double) attendedCount / totalSessions * 100 : 0;

            return StudentAttendanceStats.builder()
                    .studentId(student.getId())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .rollNumber(student.getRollNumber())
                    .attendedSessionsCount(attendedCount)
                    .attendancePercentage(Math.round(percentage * 100.0) / 100.0)
                    .build();
        }).collect(Collectors.toList());

        return SubjectAttendanceReportResponse.builder()
                .subjectId(subjectId)
                .subjectName(subject.getSubjectName())
                .subjectCode(subject.getSubjectCode())
                .courseName(subject.getCourse().getCourseName())
                .semester(semester)
                .section(section)
                .totalCompletedSessions(totalSessions)
                .studentStats(stats)
                .sessions(completedSessionsForCohort.stream().map(labSessionMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
