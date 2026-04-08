package com.mainApp.service.serviceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainApp.exception.ResourceAlreadyExistsException;
import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.LabSessionMapper;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.mapper.SubjectMapper;
import com.mainApp.mapper.TeacherMapper;
import com.mainApp.model.Course;
import com.mainApp.model.LabSession;
import com.mainApp.model.Subject;
import com.mainApp.model.Teacher;
import com.mainApp.model.TeacherSubjectExpertise;
import com.mainApp.repository.CourseRepository;
import com.mainApp.repository.LabRepository;
import com.mainApp.repository.LabSessionRepository;
import com.mainApp.repository.StudentSubjectEnrollmentRepository;
import com.mainApp.repository.SubjectRepository;
import com.mainApp.repository.TeacherRepository;
import com.mainApp.repository.TeacherSubjectExpertiseRepository;
import com.mainApp.requestdto.LabSessionCreateRequest;
import com.mainApp.requestdto.SubjectCreateRequest;
import com.mainApp.requestdto.SubjectExpertiseRequest;
import com.mainApp.requestdto.SubjectSearchRequest;
import com.mainApp.requestdto.SubjectUpdateRequest;
import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.BulkOperationResponse;
import com.mainApp.responcedto.LabSessionResponse;
import com.mainApp.responcedto.StudentEnrollmentResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.SubjectAnalyticsResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.responcedto.TeacherResponse;
import com.mainApp.service.serviceInterface.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final LabRepository labRepository;
    private final TeacherRepository teacherRepository;
    private final LabSessionRepository labSessionRepository;
    private final TeacherSubjectExpertiseRepository teacherSubjectExpertiseRepository;
    private final StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;

    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final LabSessionMapper labSessionMapper;

    @Override
    @Transactional
    public SubjectResponse createSubject(SubjectCreateRequest request) {
        if (subjectRepository.findBySubjectCode(request.getSubjectCode()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Subject with code " + request.getSubjectCode() + " already exists");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Subject subject = subjectMapper.toEntity(request);
        subject.setCourse(course);
        subject = subjectRepository.save(subject);
        log.info("Subject created successfully: {}", subject.getSubjectCode());
        return subjectMapper.toResponse(subject);
    }

    @Override
    public SubjectResponse getSubjectById(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        return subjectMapper.toResponse(subject);
    }

    @Override
    public SubjectResponse getSubjectByCode(String code) {
        Subject subject = subjectRepository.findBySubjectCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + code));
        return subjectMapper.toResponse(subject);
    }

    @Override
    public Page<SubjectResponse> getAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable).map(subjectMapper::toResponse);
    }

    @Override
    public Page<SubjectResponse> getSubjectsBySemester(Integer semester, Pageable pageable) {
        // This would require a custom query in repository if not already there
        return subjectRepository.findBySemesterNumber(semester, pageable)
                .map(subjectMapper::toResponse);
    }

    @Override
    public Page<SubjectResponse> getSubjectsByCourse(String courseId, Pageable pageable) {
        return subjectRepository.findByCourseId(courseId, pageable).map(subjectMapper::toResponse);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(String id, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        subjectMapper.updateEntity(request, subject);
        subject = subjectRepository.save(subject);
        return subjectMapper.toResponse(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    @Override
    public List<TeacherResponse> getSubjectTeachers(String subjectId) {
        return teacherSubjectExpertiseRepository.findBySubjectId(subjectId).stream()
                .map(expertise -> teacherMapper.toResponse(expertise.getTeacher()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignTeacherToSubject(String subjectId, String teacherId, SubjectExpertiseRequest request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        TeacherSubjectExpertise expertise = new TeacherSubjectExpertise();
        expertise.setSubject(subject);
        expertise.setTeacher(teacher);
        expertise.setExperienceYears(request.getExperienceYears());
        expertise.setProficiencyLevel(request.getProficiencyLevel());
        expertise.setIsPrimaryExpert(request.getIsPrimaryExpert());

        teacherSubjectExpertiseRepository.save(expertise);
    }

    @Override
    @Transactional
    public void removeTeacherFromSubject(String subjectId, String teacherId) {
        teacherSubjectExpertiseRepository.findByTeacherIdAndSubjectId(teacherId, subjectId)
                .ifPresent(teacherSubjectExpertiseRepository::delete);
    }

    @Override
    public List<StudentResponse> getSubjectStudents(String subjectId) {
        return studentSubjectEnrollmentRepository.findBySubjectId(subjectId).stream()
                .map(enrollment -> studentMapper.toResponse(enrollment.getStudent()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getSubjectStudentCount(String subjectId) {
        return studentSubjectEnrollmentRepository.findBySubjectId(subjectId).size();
    }

    @Override
    public List<StudentEnrollmentResponse> getSubjectEnrollments(String subjectId) {
        return studentSubjectEnrollmentRepository.findBySubjectId(subjectId).stream()
                .map(enrollment -> {
                    StudentEnrollmentResponse response = new StudentEnrollmentResponse();
                    response.setEnrollmentId(enrollment.getId());
                    response.setStudent(studentMapper.toResponse(enrollment.getStudent()));
                    response.setAcademicYear(enrollment.getAcademicYear());
                    response.setSemester(enrollment.getSemester());
                    response.setStatus(enrollment.getStatus().name());
                    response.setEnrollmentDate(enrollment.getEnrollmentDate());
                    return response;
                }).collect(Collectors.toList());
    }

    @Override
    public List<LabSessionResponse> getSubjectLabSessions(String subjectId) {
        return labSessionRepository.findBySubjectId(subjectId).stream()
                .map(labSessionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabSessionResponse createLabSessionForSubject(String subjectId, LabSessionCreateRequest request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        com.mainApp.model.Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found"));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        LabSession session = labSessionMapper.toEntity(request);
        session.setSubject(subject);
        session.setLab(lab);
        session.setTeacher(teacher);
        session.setStatus(com.mainApp.roles.LabSessionStatus.SCHEDULED);

        session = labSessionRepository.save(session);
        return labSessionMapper.toResponse(session);
    }

    @Override
    public SubjectAnalyticsResponse getSubjectAnalytics(String subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        List<LabSession> sessions = labSessionRepository.findBySubjectId(subjectId);
        int studentCount = studentSubjectEnrollmentRepository.findBySubjectId(subjectId).size();

        SubjectAnalyticsResponse response = new SubjectAnalyticsResponse();
        response.setSubjectId(subject.getId());
        response.setSubjectCode(subject.getSubjectCode());
        response.setSubjectName(subject.getSubjectName());
        response.setTotalStudents(studentCount);
        response.setTotalSessions(sessions.size());

        return response;
    }

    @Override
    public AttendanceReportResponse getSubjectAttendanceReport(String subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        AttendanceReportResponse response = new AttendanceReportResponse();
        response.setSubjectName(subject.getSubjectName());
        response.setReportDate(java.time.LocalDate.now());

        return response;
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkCreateSubjects(List<SubjectCreateRequest> requests) {
        BulkOperationResponse response = new BulkOperationResponse();
        response.setTotalProcessed(requests.size());

        int success = 0;
        for (SubjectCreateRequest request : requests) {
            try {
                createSubject(request);
                success++;
            } catch (Exception e) {
                response.getErrorMessages()
                        .add("Failed to create subject " + request.getSubjectCode() + ": " + e.getMessage());
            }
        }

        response.setSuccessCount(success);
        response.setFailureCount(requests.size() - success);
        return response;
    }

    @Override
    public Page<SubjectResponse> searchSubjects(String keyword, Pageable pageable) {
        return subjectRepository.searchSubjects(keyword, pageable).map(subjectMapper::toResponse);
    }

    @Override
    public List<SubjectResponse> findSubjectsByCriteria(SubjectSearchRequest criteria) {
        return List.of();
    }
}
