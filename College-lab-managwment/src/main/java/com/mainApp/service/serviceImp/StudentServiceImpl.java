package com.mainApp.service.serviceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainApp.exception.ResourceAlreadyExistsException;
import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.model.Attendance;
import com.mainApp.model.Course;
import com.mainApp.model.Student;
import com.mainApp.model.StudentSubjectEnrollment;
import com.mainApp.model.Subject;
import com.mainApp.repository.AttendanceRepository;
import com.mainApp.repository.CourseRepository;
import com.mainApp.repository.StudentRepository;
import com.mainApp.repository.StudentSubjectEnrollmentRepository;
import com.mainApp.repository.SubjectRepository;
import com.mainApp.mapper.AttendanceMapper;
import com.mainApp.requestdto.CourseEnrollmentRequest;
import com.mainApp.requestdto.StudentCreateRequest;
import com.mainApp.requestdto.StudentEnrollmentRequest;
import com.mainApp.requestdto.StudentSearchRequest;
import com.mainApp.requestdto.StudentUpdateRequest;
import com.mainApp.requestdto.SubjectEnrollmentRequest;
import com.mainApp.responcedto.AttendanceResponse;
import com.mainApp.responcedto.AttendanceSummaryResponse;
import com.mainApp.responcedto.BulkOperationResponse;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.StudentEnrollmentResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.StudentSubjectEnrollmentResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.roles.EnrollmentStatus;
import com.mainApp.service.serviceInterface.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final com.mainApp.repository.LabSessionRepository labSessionRepository;
    private final StudentMapper studentMapper;
    private final AttendanceMapper attendanceMapper;

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        if (studentRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Student with username " + request.getUsername() + " already exists");
        }
        Student student = studentMapper.toEntity(request);
        student = studentRepository.save(student);
        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with roll number: " + rollNumber));
        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentByRegistrationNumber(String regNumber) {
        // Roll number used as placeholder if method not in repository
        return getStudentByRollNumber(regNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(String courseId, Integer semester, String keyword, Pageable pageable) {
        // Sanitize parameters: convert empty strings from frontend to null so JPA handles them correctly
        String sanitizedCourseId = (courseId != null && courseId.trim().isEmpty()) ? null : courseId;
        String sanitizedKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : "%" + keyword.trim().toLowerCase() + "%";

        return studentRepository.searchStudents(sanitizedCourseId, semester, sanitizedKeyword, pageable)
                .map(studentMapper::toResponse);
    }

    @Override
    public Page<StudentResponse> getStudentsByCourse(String courseId, Pageable pageable) {
        return studentRepository.findByCourseId(courseId, pageable).map(studentMapper::toResponse);
    }

    @Override
    public Page<StudentResponse> getStudentsByBatch(String batch, Pageable pageable) {
        return studentRepository.findByBatch(batch, pageable).map(studentMapper::toResponse);
    }

    @Override
    public Page<StudentResponse> getStudentsBySemester(Integer semester, Pageable pageable) {
        return studentRepository.findByCurrentSemester(semester, pageable).map(studentMapper::toResponse);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(String id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Update basic fields
        studentMapper.updateEntity(request, student);

        // Update Course if provided
        if (request.getCourseId() != null && !request.getCourseId().isEmpty()) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            student.setCourse(course);
        }

        if (request.getIsActive() != null) {
            student.setIsActive(request.getIsActive());
        }

        student = studentRepository.save(student);
        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional
    public void deleteStudent(@lombok.NonNull String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        student.setIsActive(false);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deactivateStudent(@lombok.NonNull String id) {
        deleteStudent(id);
    }

    @Override
    @Transactional
    public void activateStudent(@lombok.NonNull String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        student.setIsActive(true);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public StudentEnrollmentResponse enrollStudentInCourse(String studentId, CourseEnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        // Maintain bidirectional relationship
        student.setCourse(course);
        student.setAcademicYear(request.getAcademicYear());
        student.setCurrentSemester(request.getSemester() != null ? request.getSemester() : 1);

        // Add student to course's students list if not already present
        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
        }

        studentRepository.save(student);
        courseRepository.save(course); // Persist the updated course

        StudentEnrollmentResponse response = new StudentEnrollmentResponse();
        response.setStudent(studentMapper.toResponse(student));
        return response;
    }

    @Override
    @Transactional
    public StudentSubjectEnrollmentResponse enrollStudentInSubject(String studentId, SubjectEnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));

        StudentSubjectEnrollment enrollment = new StudentSubjectEnrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setSemester(request.getSemester());
        enrollment.setAcademicYear(request.getAcademicYear());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(request.getStatus() != null ? request.getStatus() : EnrollmentStatus.ACTIVE);

        studentSubjectEnrollmentRepository.save(enrollment);

        return new StudentSubjectEnrollmentResponse();
    }

    @Override
    public List<SubjectResponse> getStudentSubjects(String studentId) {
        return studentSubjectEnrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollment -> {
                    Subject s = enrollment.getSubject();
                    SubjectResponse res = new SubjectResponse();
                    res.setId(s.getId());
                    res.setSubjectCode(s.getSubjectCode());
                    res.setSubjectName(s.getSubjectName());
                    return res;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getStudentCourses(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        if (student.getCourse() == null)
            return List.of();

        Course c = student.getCourse();
        CourseResponse res = new CourseResponse();
        res.setId(c.getId());
        res.setCourseCode(c.getCourseCode());
        res.setCourseName(c.getCourseName());
        return List.of(res);
    }

    @Override
    @Transactional
    public void dropSubject(String studentId, String subjectId) {
        studentSubjectEnrollmentRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .ifPresent(studentSubjectEnrollmentRepository::delete);
    }

    @Override
    @Transactional
    public void changeSection(String studentId, String section) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        student.setSection(section);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void promoteToNextSemester(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        if (student.getCurrentSemester() != null) {
            student.setCurrentSemester(student.getCurrentSemester() + 1);
            studentRepository.save(student);
        }
    }

    @Override
    public AttendanceSummaryResponse getAttendanceSummary(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);

        AttendanceSummaryResponse response = new AttendanceSummaryResponse();
        response.setStudentId(student.getId());
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        response.setCourseName(student.getCourse() != null ? student.getCourse().getCourseName() : "N/A");

        // Use a more accurate way to calculate total sessions conduct for the cohort
        java.util.Map<String, AttendanceSummaryResponse.SubjectAttendanceSummary> subjectSummaryMap = new java.util.HashMap<>();

        int totalConductedAggregate = 0;
        int totalAttendedAggregate = 0;

        // 1. baseline: Fetch all subjects for the student's COURSE and SEMESTER
        // This ensures data is shown even if explicit subject-level enrollment is
        // missing
        List<Subject> cohortSubjects = subjectRepository.findByCourseIdAndSemesterNumber(
                student.getCourse() != null ? student.getCourse().getId() : null,
                student.getCurrentSemester());

        // 2. Add any subjects from explicit enrollments (handles backlogs/electives)
        java.util.Set<Subject> allRelevantSubjects = new java.util.HashSet<>(cohortSubjects);
        student.getSubjectEnrollments().forEach(e -> allRelevantSubjects.add(e.getSubject()));

        for (Subject subject : allRelevantSubjects) {
            AttendanceSummaryResponse.SubjectAttendanceSummary subSummary = new AttendanceSummaryResponse.SubjectAttendanceSummary();
            subSummary.setId(subject.getId());
            subSummary.setSubjectCode(subject.getSubjectCode());
            subSummary.setSubjectName(subject.getSubjectName());

            // 3. Fetch COMPLETED sessions for this subject
            List<com.mainApp.model.LabSession> sessions = labSessionRepository
                    .findBySubjectId(subject.getId()).stream()
                    .filter(s -> s.getStatus() == com.mainApp.roles.LabSessionStatus.COMPLETED)
                    .collect(Collectors.toList());

            // 4. Robust Section Filter with fuzzy matching
            final String studentSection = student.getSection() != null ? student.getSection().trim() : "";

            sessions = sessions.stream()
                    .filter(s -> {
                        String sessionSection = s.getSection() != null ? s.getSection().trim() : "";
                        // Match if:
                        // - Student has no section
                        // - Session is for ALL
                        // - Session section is empty
                        // - Session section fuzzy matches student section (e.g., "A" vs "Section A")
                        if (studentSection.isEmpty() || sessionSection.isEmpty()
                                || sessionSection.equalsIgnoreCase("ALL")) {
                            return true;
                        }
                        String s1 = studentSection.toLowerCase().replace("section", "").trim();
                        String s2 = sessionSection.toLowerCase().replace("section", "").trim();
                        return s1.equals(s2);
                    })
                    .collect(Collectors.toList());

            int conducted = sessions.size();
            int attended = (int) attendances.stream()
                    .filter(a -> a.getLabSession() != null
                            && a.getLabSession().getSubject().getId().equals(subject.getId())
                            && a.getStatus() == com.mainApp.roles.AttendanceStatus.PRESENT)
                    .count();

            subSummary.setConductedSessions(conducted);
            subSummary.setAttendedSessions(attended);
            subSummary.setTotalPlannedSessions(subject.getLabHours() != null ? subject.getLabHours() : 15);
            subSummary.setTotalSessions(subSummary.getTotalPlannedSessions());
            subSummary.setAttendancePercentage(conducted > 0 ? (double) attended / conducted * 100 : 0.0);

            subjectSummaryMap.put(subject.getId(), subSummary);

            totalConductedAggregate += conducted;
            totalAttendedAggregate += attended;
        }

        response.setTotalSessions(totalConductedAggregate);
        response.setPresentCount(totalAttendedAggregate);
        response.setAttendancePercentage(
                totalConductedAggregate > 0 ? (double) totalAttendedAggregate / totalConductedAggregate * 100 : 0.0);

        response.setSubjectWiseSummary(subjectSummaryMap);

        return response;
    }

    @Override
    public Page<AttendanceResponse> getStudentAttendance(String studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable)
                .map(attendanceMapper::toResponse);
    }

    @Override
    public List<AttendanceResponse> getStudentAttendanceBySubject(String studentId, String subjectId) {
        return attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId).stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkCreateStudents(List<StudentCreateRequest> requests) {
        BulkOperationResponse response = new BulkOperationResponse();
        int success = 0;
        for (StudentCreateRequest request : requests) {
            try {
                createStudent(request);
                success++;
            } catch (Exception e) {
                response.getErrorMessages()
                        .add("Failed to create student " + request.getUsername() + ": " + e.getMessage());
            }
        }
        response.setSuccessCount(success);
        response.setFailureCount(requests.size() - success);
        return response;
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkUpdateStudents(List<StudentUpdateRequest> requests) {
        BulkOperationResponse response = new BulkOperationResponse();
        // Similar loop
        return response;
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkEnrollStudents(List<StudentEnrollmentRequest> requests) {
        BulkOperationResponse response = new BulkOperationResponse();
        // Similar loop
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> searchStudents(String keyword, Pageable pageable) {
        String sanitizedKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : "%" + keyword.trim().toLowerCase() + "%";
        return studentRepository.searchStudents(null, null, sanitizedKeyword, pageable).map(studentMapper::toResponse);
    }

    @Override
    public List<StudentResponse> findStudentsByCriteria(StudentSearchRequest criteria) {
        return List.of();
    }

    @Override
    public List<StudentResponse> getUnassignedStudents() {
        return studentRepository.findUnassignedStudents().stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> getPendingStudents(String courseId, Integer semester, String department,
            String keyword,
            Pageable pageable) {
        // Sanitize parameters: convert empty strings from frontend to null so JPA handles them correctly
        String sanitizedCourseId = (courseId != null && courseId.trim().isEmpty()) ? null : courseId;
        String sanitizedDepartment = (department != null && department.trim().isEmpty()) ? null : department;
        String sanitizedKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : "%" + keyword.trim().toLowerCase() + "%";

        return studentRepository.findPendingStudents(sanitizedCourseId, semester, sanitizedDepartment, sanitizedKeyword,
                pageable)
                .map(studentMapper::toResponse);
    }


    @Override
    @Transactional
    public void approveStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        student.setIsApproved(true);
        student.setIsActive(true);
        studentRepository.save(student);
        log.info("Student {} approved by admin", student.getUsername());
    }

    @Override
    @Transactional
    public void rejectStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        // For rejection, we can either delete or just keep as unapproved
        // Requirement says "accept and reject button"
        // If rejected, maybe it should be deleted or marked as rejected explicitly?
        // Let's just delete for now to keep it clean, or keep unapproved.
        // I'll just deactivate and keep unapproved.
        student.setIsApproved(false);
        student.setIsActive(false);
        studentRepository.save(student);
        log.info("Student {} rejected by admin", student.getUsername());
    }
}
