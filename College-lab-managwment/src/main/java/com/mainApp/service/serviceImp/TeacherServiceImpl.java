package com.mainApp.service.serviceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.AttendanceMapper;
import com.mainApp.mapper.LabMapper;
import com.mainApp.mapper.LabSessionMapper;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.mapper.TeacherMapper;
import com.mainApp.model.Attendance;
import com.mainApp.model.Lab;
import com.mainApp.model.LabSession;
import com.mainApp.model.LabTimetableSlot;
import com.mainApp.model.Student;
import com.mainApp.model.Subject;
import com.mainApp.model.Teacher;
import com.mainApp.model.TeacherSubjectExpertise;
import com.mainApp.repository.AttendanceRepository;
import com.mainApp.repository.LabSessionRepository;
import com.mainApp.repository.LabTimetableSlotRepository;
import com.mainApp.repository.StudentSubjectEnrollmentRepository;
import com.mainApp.repository.StudentRepository;
import com.mainApp.repository.TeacherRepository;
import com.mainApp.repository.TeacherSubjectExpertiseRepository;
import com.mainApp.repository.SubjectRepository;
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
import com.mainApp.roles.ProficiencyLevel;
import com.mainApp.service.serviceInterface.TeacherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {

        private final TeacherRepository teacherRepository;
        private final TeacherSubjectExpertiseRepository teacherSubjectExpertiseRepository;
        private final SubjectRepository subjectRepository;
        private final LabSessionRepository labSessionRepository;
        private final LabTimetableSlotRepository labTimetableSlotRepository;
        private final StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
        private final AttendanceRepository attendanceRepository;
        private final StudentRepository studentRepository;

        private final TeacherMapper teacherMapper;
        private final LabMapper labMapper;
        private final LabSessionMapper labSessionMapper;
        private final StudentMapper studentMapper;
        private final AttendanceMapper attendanceMapper;

        @Override
        @Transactional
        public TeacherResponse createTeacher(TeacherCreateRequest request) {
                Teacher teacher = teacherMapper.toEntity(request);
                teacher = teacherRepository.save(teacher);
                return teacherMapper.toResponse(teacher);
        }

        @Override
        public TeacherResponse getTeacherById(@lombok.NonNull String id) {
                Teacher teacher = teacherRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
                return teacherMapper.toResponse(teacher);
        }

        @Override
        public TeacherResponse getTeacherByEmployeeId(@lombok.NonNull String employeeId) {
                Teacher teacher = teacherRepository.findByEmployeeId(employeeId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found with employee id: " + employeeId));
                return teacherMapper.toResponse(teacher);
        }

        @Override
        public Page<TeacherResponse> getAllTeachers(@lombok.NonNull Pageable pageable) {
                return teacherRepository.findAll(pageable).map(teacherMapper::toResponse);
        }

        @Override
        public Page<TeacherResponse> getTeachersByDepartment(@lombok.NonNull String department,
                        @lombok.NonNull Pageable pageable) {
                return teacherRepository.findByDepartment(department, pageable).map(teacherMapper::toResponse);
        }

        @Override
        @Transactional
        public TeacherResponse updateTeacher(@lombok.NonNull String id, TeacherUpdateRequest request) {
                Teacher teacher = teacherRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
                teacherMapper.updateEntity(request, teacher);
                teacher = teacherRepository.save(teacher);
                return teacherMapper.toResponse(teacher);
        }

        @Override
        @Transactional
        public void deleteTeacher(@lombok.NonNull String id) {
                Teacher teacher = teacherRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
                teacher.setIsActive(false);
                teacherRepository.save(teacher);
        }

        @Override
        @Transactional
        public TeacherExpertiseResponse addSubjectExpertise(@lombok.NonNull String teacherId,
                        SubjectExpertiseRequest request) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found with id: " + teacherId));
                Subject subject = subjectRepository.findById(request.getSubjectId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException("Subject not found with id: "
                                                                + request.getSubjectId()));

                TeacherSubjectExpertise expertise = new TeacherSubjectExpertise();
                expertise.setTeacher(teacher);
                expertise.setSubject(subject);
                expertise.setExperienceYears(request.getExperienceYears());
                expertise.setProficiencyLevel(request.getProficiencyLevel());
                expertise.setIsPrimaryExpert(request.getIsPrimaryExpert());
                expertise.setAssignedDate(LocalDate.now());

                expertise = teacherSubjectExpertiseRepository.save(expertise);

                // Manual mapping to response
                TeacherExpertiseResponse response = new TeacherExpertiseResponse();
                response.setId(expertise.getId());
                response.setSubjectId(subject.getId());
                response.setSubjectCode(subject.getSubjectCode());
                response.setSubjectName(subject.getSubjectName());
                response.setExperienceYears(expertise.getExperienceYears());
                response.setProficiencyLevel(expertise.getProficiencyLevel().name());
                response.setIsPrimaryExpert(expertise.getIsPrimaryExpert());
                response.setAssignedDate(expertise.getAssignedDate());

                return response;
        }

        @Override
        public List<TeacherExpertiseResponse> getTeacherExpertises(String teacherId) {
                return teacherSubjectExpertiseRepository.findByTeacherId(teacherId).stream()
                                .map(expertise -> {
                                        TeacherExpertiseResponse response = new TeacherExpertiseResponse();
                                        response.setId(expertise.getId());
                                        response.setSubjectId(expertise.getSubject().getId());
                                        response.setSubjectCode(expertise.getSubject().getSubjectCode());
                                        response.setSubjectName(expertise.getSubject().getSubjectName());
                                        response.setExperienceYears(expertise.getExperienceYears());
                                        response.setProficiencyLevel(expertise.getProficiencyLevel().name());
                                        response.setIsPrimaryExpert(expertise.getIsPrimaryExpert());
                                        response.setAssignedDate(expertise.getAssignedDate());
                                        return response;
                                })
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void removeSubjectExpertise(String teacherId, String subjectId) {
                teacherSubjectExpertiseRepository.findByTeacherIdAndSubjectId(teacherId, subjectId)
                                .ifPresent(teacherSubjectExpertiseRepository::delete);
        }

        @Override
        @Transactional
        public void updateExpertiseLevel(String teacherId, String subjectId, String proficiencyLevel) {
                teacherSubjectExpertiseRepository.findByTeacherIdAndSubjectId(teacherId, subjectId)
                                .ifPresent(expertise -> {
                                        expertise.setProficiencyLevel(ProficiencyLevel.valueOf(proficiencyLevel));
                                        teacherSubjectExpertiseRepository.save(expertise);
                                });
        }

        @Override
        public List<LabSessionResponse> getAssignedLabSessions(String teacherId) {
                return labSessionRepository.findByTeacherId(teacherId).stream()
                                .map(labSessionMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public LabSessionResponse getUpcomingLabSessions(String teacherId) {
                return labSessionRepository.findByTeacherId(teacherId).stream()
                                .filter(session -> session.getSessionDate().isAfter(LocalDate.now())
                                                || (session.getSessionDate().isEqual(LocalDate.now())))
                                .sorted((s1, s2) -> s1.getSessionDate().compareTo(s2.getSessionDate()))
                                .findFirst()
                                .map(labSessionMapper::toResponse)
                                .orElse(null);
        }

        @Override
        public List<LabResponse> getAssignedLabs(String teacherId) {
                // Get unique labs from timetable slots
                List<Lab> timetableLabs = labTimetableSlotRepository.findByTeacherId(teacherId).stream()
                                .map(LabTimetableSlot::getLab)
                                .collect(Collectors.toList());

                // Get unique labs from actual sessions
                List<Lab> sessionLabs = labSessionRepository.findByTeacherId(teacherId).stream()
                                .map(LabSession::getLab)
                                .collect(Collectors.toList());

                // Merge and unique
                timetableLabs.addAll(sessionLabs);
                return timetableLabs.stream()
                                .distinct()
                                .map(labMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<LabSessionResponse> getLabSessionsByDate(String teacherId, LocalDate date) {
                return labSessionRepository.findByTeacherId(teacherId).stream()
                                .filter(session -> session.getSessionDate().equals(date))
                                .map(labSessionMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<StudentResponse> getStudentsBySubject(String teacherId, String subjectId) {
                return studentSubjectEnrollmentRepository.findBySubjectId(subjectId).stream()
                                .map(enrollment -> studentMapper.toResponse(enrollment.getStudent()))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public AttendanceResponse markAttendance(AttendanceMarkRequest request) {
                // Basic logic: Find session and student, mark attendance
                LabSession session = labSessionRepository.findById(Objects.requireNonNull(request.getLabSessionId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
                Student student = studentRepository.findById(Objects.requireNonNull(request.getStudentId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                Attendance attendance = new Attendance();
                attendance.setLabSession(session);
                attendance.setStudent(student);
                attendance.setStatus(request.getStatus());
                attendance.setEntryTime(java.time.LocalDateTime.now());

                attendance = attendanceRepository.save(attendance);
                return attendanceMapper.toResponse(attendance);
        }

        @Override
        @Transactional
        public void updateAttendance(@lombok.NonNull String attendanceId, AttendanceUpdateRequest request) {
                Attendance attendance = attendanceRepository.findById(attendanceId)
                                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));
                attendance.setStatus(request.getStatus());
                attendance.setRemarks(request.getRemarks());
                attendanceRepository.save(attendance);
        }

        @Override
        public TeacherReportResponse generateTeacherReport(String teacherId, ReportRequest request) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found with id: " + teacherId));

                List<LabSession> sessions = labSessionRepository.findByTeacherId(teacherId);
                List<TeacherSubjectExpertise> expertise = teacherSubjectExpertiseRepository.findByTeacherId(teacherId);

                TeacherReportResponse response = new TeacherReportResponse();
                response.setTeacherId(teacher.getId());
                response.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
                response.setTotalLabsAssigned(expertise.size());
                response.setTotalSessionsConducted(sessions.size());

                // Basic calculation for average attendance
                double avgAttendance = sessions.stream()
                                .mapToLong(s -> attendanceRepository.countByLabSessionId(s.getId()))
                                .average()
                                .orElse(0.0);
                response.setAverageStudentAttendance(avgAttendance);

                response.setExpertiseSubjects(expertise.stream()
                                .map(e -> {
                                        com.mainApp.responcedto.SubjectResponse res = new com.mainApp.responcedto.SubjectResponse();
                                        res.setId(e.getSubject().getId());
                                        res.setSubjectCode(e.getSubject().getSubjectCode());
                                        res.setSubjectName(e.getSubject().getSubjectName());
                                        return res;
                                }).collect(Collectors.toList()));

                return response;
        }

        @Override
        public List<AttendanceReportResponse> getAttendanceReport(String teacherId, String subjectId) {
                List<LabSession> completedSessions = labSessionRepository
                                .findByTeacherId(teacherId).stream()
                                .filter(s -> s.getSubject().getId().equals(subjectId)
                                                && s.getStatus() == com.mainApp.roles.LabSessionStatus.COMPLETED)
                                .collect(Collectors.toList());

                List<Student> students = studentRepository.findBySubjectId(subjectId);

                return students.stream().map(student -> {
                        AttendanceReportResponse report = new AttendanceReportResponse();
                        report.setSessionId(student.getId()); // Using as studentId
                        report.setSubjectName(student.getFirstName() + " " + student.getLastName()); // Using as
                                                                                                     // studentName

                        long attendedCount = completedSessions.stream()
                                        .filter(session -> session.getAttendances().stream()
                                                        .anyMatch(a -> a.getStudent().getId().equals(student.getId())
                                                                        && a.getStatus() == com.mainApp.roles.AttendanceStatus.PRESENT))
                                        .count();

                        report.setTotalStudents(completedSessions.size()); // Using as totalSessions
                        report.setPresentCount((int) attendedCount); // Using as attendedSessions
                        report.setAttendancePercentage(
                                        completedSessions.size() > 0
                                                        ? (double) attendedCount / completedSessions.size() * 100
                                                        : 0.0);

                        return report;
                }).collect(Collectors.toList());
        }

        @Override
        public ScheduleResponse getTeacherSchedule(String teacherId) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found with id: " + teacherId));

                List<LabSession> sessions = labSessionRepository.findByTeacherId(teacherId);

                ScheduleResponse response = new ScheduleResponse();
                response.setTeacherId(teacher.getId());
                response.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
                response.setSessions(sessions.stream()
                                .map(labSessionMapper::toResponse)
                                .collect(Collectors.toList()));

                return response;
        }

        @Override
        @Transactional
        public void updateAvailability(String teacherId, AvailabilityRequest request) {
                // Placeholder implementation
        }

        @Override
        public Page<TeacherResponse> searchTeachers(String keyword, Pageable pageable) {
                return teacherRepository.searchTeachers(keyword, pageable).map(teacherMapper::toResponse);
        }

        @Override
        public List<TeacherResponse> getUnassignedTeachers() {
                return teacherRepository.findUnassignedTeachers().stream()
                                .map(teacherMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse getTeacherSubjectAttendanceReport(
                        @lombok.NonNull String teacherId, @lombok.NonNull String subjectId) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                Subject subject = subjectRepository.findById(subjectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

                List<LabSession> completedSessions = labSessionRepository
                                .findByTeacherId(teacherId).stream()
                                .filter(s -> s.getSubject().getId().equals(subjectId)
                                                && s.getStatus() == com.mainApp.roles.LabSessionStatus.COMPLETED)
                                .collect(Collectors.toList());

                List<Student> students = studentRepository.findBySubjectId(subjectId);

                List<com.mainApp.responcedto.StudentAttendanceStats> stats = students.stream().map(student -> {
                        long attendedCount = completedSessions.stream()
                                        .filter(session -> session.getAttendances().stream()
                                                        .anyMatch(a -> a.getStudent().getId().equals(student.getId())
                                                                        && a.getStatus() == com.mainApp.roles.AttendanceStatus.PRESENT))
                                        .count();

                        double percentage = completedSessions.size() > 0
                                        ? (double) attendedCount / completedSessions.size() * 100
                                        : 0.0;

                        return com.mainApp.responcedto.StudentAttendanceStats.builder()
                                        .studentId(student.getId())
                                        .firstName(student.getFirstName())
                                        .lastName(student.getLastName())
                                        .rollNumber(student.getRollNumber())
                                        .attendedSessionsCount(attendedCount)
                                        .attendancePercentage(Math.round(percentage * 100.0) / 100.0)
                                        .build();
                }).collect(Collectors.toList());

                return com.mainApp.responcedto.TeacherSubjectAttendanceReportResponse.builder()
                                .subjectId(subjectId)
                                .subjectName(subject.getSubjectName())
                                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                                .totalCompletedSessions((long) completedSessions.size())
                                .studentStats(stats)
                                .sessions(completedSessions.stream().map(labSessionMapper::toResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        @Override
        public Page<TeacherResponse> getPendingTeachers(String department, String keyword, Pageable pageable) {
                // Sanitize parameters: convert empty strings from frontend to null so JPA handles them correctly
                String sanitizedDepartment = (department != null && department.trim().isEmpty()) ? null : department;
                String sanitizedKeyword = (keyword != null && keyword.trim().isEmpty()) ? null : keyword;

                return teacherRepository.findPendingTeachers(sanitizedDepartment, sanitizedKeyword, pageable)
                                .map(teacherMapper::toResponse);
        }


        @Override
        @Transactional
        public void approveTeacher(@lombok.NonNull String id) {
                Teacher teacher = teacherRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
                teacher.setIsApproved(true);
                teacher.setIsActive(true);
                teacherRepository.save(teacher);
                log.info("Teacher {} approved by admin", teacher.getUsername());
        }

        @Override
        @Transactional
        public void rejectTeacher(@lombok.NonNull String id) {
                Teacher teacher = teacherRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
                teacher.setIsActive(false);
                teacher.setIsApproved(false);
                teacherRepository.save(teacher);
                log.info("Teacher {} registration rejected by admin", teacher.getUsername());
        }

        @Override
        @Transactional
        public void deleteTeacherWithReassignment(@lombok.NonNull String teacherId,
                        com.mainApp.requestdto.TeacherReassignmentRequest request) {
                Teacher sourceTeacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Teacher not found with id: " + teacherId));

                // 1. Validate all reassignments and check for conflicts
                for (Map.Entry<String, String> entry : request.getSessionTeacherMap().entrySet()) {
                        String sessionId = entry.getKey();
                        String targetTeacherId = entry.getValue();

                        LabSession session = labSessionRepository.findById(sessionId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Lab Session not found with id: " + sessionId));

                        Teacher targetTeacher = teacherRepository.findById(targetTeacherId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Target Teacher not found with id: " + targetTeacherId));

                        if (!Boolean.TRUE.equals(targetTeacher.getIsApproved())) {
                                throw new IllegalStateException(
                                                "Target teacher is not approved: " + targetTeacher.getFirstName() + " "
                                                                + targetTeacher.getLastName());
                        }

                        // Check for conflicts with target teacher's existing sessions
                        List<LabSession> targetSessions = labSessionRepository.findByTeacherId(targetTeacherId);
                        for (LabSession ts : targetSessions) {
                                // Skip if the session being checked is the same one we're reassigning (might
                                // already be in target list)
                                if (ts.getId().equals(session.getId()))
                                        continue;

                                if (ts.getSessionDate().equals(session.getSessionDate())) {
                                        if (session.getStartTime().isBefore(ts.getEndTime())
                                                        && session.getEndTime().isAfter(ts.getStartTime())) {
                                                throw new IllegalStateException("Conflict detected for "
                                                                + targetTeacher.getFirstName() + " "
                                                                + targetTeacher.getLastName() +
                                                                " on " + session.getSessionDate() + " between "
                                                                + session.getStartTime() + "-"
                                                                + session.getEndTime());
                                        }
                                }
                        }

                        // Reassign
                        session.setTeacher(targetTeacher);
                        labSessionRepository.save(session);
                }

                // 2. Ensure NO MORE sessions are assigned to the source teacher
                List<LabSession> remainingSessions = labSessionRepository.findByTeacherId(teacherId);
                if (!remainingSessions.isEmpty()) {
                        throw new IllegalStateException(
                                        "Cannot delete teacher: Some lab sessions have not been reassigned.");
                }

                // 3. Deactivate source teacher
                sourceTeacher.setIsActive(false);
                teacherRepository.save(sourceTeacher);
                log.info("Teacher {} deactivated after reassignment of all sessions",
                                sourceTeacher.getFirstName() + " " + sourceTeacher.getLastName());
        }
}
