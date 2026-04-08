package com.mainApp.service.serviceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainApp.exception.ResourceAlreadyExistsException;
import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.CourseMapper;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.mapper.SubjectMapper;
import com.mainApp.model.Course;
import com.mainApp.model.Student;
import com.mainApp.model.Subject;
import com.mainApp.repository.CourseRepository;
import com.mainApp.repository.StudentRepository;
import com.mainApp.repository.SubjectRepository;
import com.mainApp.requestdto.CourseCreateRequest;
import com.mainApp.requestdto.CourseUpdateRequest;
import com.mainApp.requestdto.SubjectCreateRequest;
import com.mainApp.requestdto.SubjectUpdateRequest;
import com.mainApp.requestdto.CourseSearchRequest;
import com.mainApp.responcedto.BulkOperationResponse;
import com.mainApp.responcedto.CourseAnalyticsResponse;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.EnrollmentTrendResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.roles.CourseStatus;
import com.mainApp.service.serviceInterface.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;
    private final SubjectMapper subjectMapper;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        if (courseRepository.findByCourseCode(request.getCourseCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Course with code " + request.getCourseCode() + " already exists");
        }
        Course course = courseMapper.toEntity(request);
        course = courseRepository.save(course);
        log.info("Course created successfully: {}", course.getCourseCode());
        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByCode(String code) {
        Course course = courseRepository.findByCourseCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + code));
        return courseMapper.toResponse(course);
    }

    @Override
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toResponse);
    }

    @Override
    public List<CourseResponse> getAllCoursesList() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CourseResponse> getCoursesByDepartment(String department, Pageable pageable) {
        return courseRepository.findByDepartment(department, pageable).map(courseMapper::toResponse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(String id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseMapper.updateEntity(request, course);
        course = courseRepository.save(course);
        log.info("Course updated successfully: {}", course.getCourseCode());
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setIsActive(false);
        courseRepository.save(course);
        log.info("Course deactivated: {}", course.getCourseCode());
    }

    @Override
    @Transactional
    public void activateCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setIsActive(true);
        course.setStatus(CourseStatus.ACTIVE);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deactivateCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setIsActive(false);
        course.setStatus(CourseStatus.INACTIVE);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public SubjectResponse addSubjectToCourse(String courseId, SubjectCreateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Subject subject = subjectMapper.toEntity(request);
        subject.setCourse(course);
        subject = subjectRepository.save(subject);
        return subjectMapper.toResponse(subject);
    }

    @Override
    public List<SubjectResponse> getCourseSubjects(String courseId) {
        return subjectRepository.findByCourseId(courseId).stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectResponse> getCourseSubjectsBySemester(String courseId, Integer semester) {
        return subjectRepository.findByCourseIdAndSemesterNumber(courseId, semester).stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeSubjectFromCourse(String courseId, String subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        if (!subject.getCourse().getId().equals(courseId)) {
            throw new IllegalArgumentException("Subject does not belong to this course");
        }
        subjectRepository.delete(subject);
    }

    @Override
    @Transactional
    public void updateSubjectInCourse(String courseId, String subjectId, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        if (!subject.getCourse().getId().equals(courseId)) {
            throw new IllegalArgumentException("Subject does not belong to this course");
        }
        subjectMapper.updateEntity(request, subject);
        subjectRepository.save(subject);
    }

    @Override
    public List<StudentResponse> getCourseStudents(String courseId) {
        return studentRepository.findByCourseId(courseId).stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCourseStudentCount(String courseId) {
        return (int) studentRepository.countByCourseId(courseId);
    }

    @Override
    @Transactional
    public void addStudentToCourse(String courseId, String studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        student.setCourse(course);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void removeStudentFromCourse(String courseId, String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        if (student.getCourse() != null && student.getCourse().getId().equals(courseId)) {
            student.setCourse(null);
            studentRepository.save(student);
        }
    }

    @Override
    public CourseAnalyticsResponse getCourseAnalytics(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        CourseAnalyticsResponse response = new CourseAnalyticsResponse();
        response.setCourseId(course.getId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        response.setTotalStudents((int) studentRepository.countByCourseId(courseId));
        response.setTotalSubjects(subjectRepository.findByCourseId(courseId).size());

        return response;
    }

    @Override
    public EnrollmentTrendResponse getEnrollmentTrend(String courseId) {
        // Placeholder
        return new EnrollmentTrendResponse();
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkCreateCourses(List<CourseCreateRequest> requests) {
        BulkOperationResponse response = new BulkOperationResponse();
        response.setTotalProcessed(requests.size());
        int success = 0;
        for (CourseCreateRequest request : requests) {
            try {
                createCourse(request);
                success++;
            } catch (Exception e) {
                response.getErrorMessages()
                        .add("Failed to create course " + request.getCourseCode() + ": " + e.getMessage());
            }
        }
        response.setSuccessCount(success);
        response.setFailureCount(requests.size() - success);
        return response;
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkUpdateCourses(List<CourseUpdateRequest> requests) {
        // Placeholder
        return new BulkOperationResponse();
    }

    @Override
    public Page<CourseResponse> searchCourses(String keyword, Pageable pageable) {
        return courseRepository.searchCourses(keyword, pageable).map(courseMapper::toResponse);
    }

    @Override
    public List<CourseResponse> findCoursesByCriteria(CourseSearchRequest criteria) {
        // Placeholder
        return courseRepository.findAll().stream().map(courseMapper::toResponse).collect(Collectors.toList());
    }
}
