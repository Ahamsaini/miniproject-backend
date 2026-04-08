package com.mainApp.service.serviceInterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

import java.util.List;

public interface CourseService {
    // CRUD Operations
    CourseResponse createCourse(CourseCreateRequest request);

    CourseResponse getCourseById(String id);

    CourseResponse getCourseByCode(String code);

    Page<CourseResponse> getAllCourses(Pageable pageable);

    List<CourseResponse> getAllCoursesList();

    Page<CourseResponse> getCoursesByDepartment(String department, Pageable pageable);

    CourseResponse updateCourse(String id, CourseUpdateRequest request);

    void deleteCourse(String id);

    void activateCourse(String id);

    void deactivateCourse(String id);

    // Subject Management
    SubjectResponse addSubjectToCourse(String courseId, SubjectCreateRequest request);

    List<SubjectResponse> getCourseSubjects(String courseId);

    List<SubjectResponse> getCourseSubjectsBySemester(String courseId, Integer semester);

    void removeSubjectFromCourse(String courseId, String subjectId);

    void updateSubjectInCourse(String courseId, String subjectId, SubjectUpdateRequest request);

    // Student Management
    List<StudentResponse> getCourseStudents(String courseId);

    Integer getCourseStudentCount(String courseId);

    void addStudentToCourse(String courseId, String studentId);

    void removeStudentFromCourse(String courseId, String studentId);

    // Analytics
    CourseAnalyticsResponse getCourseAnalytics(String courseId);

    EnrollmentTrendResponse getEnrollmentTrend(String courseId);

    // Bulk Operations
    BulkOperationResponse bulkCreateCourses(List<CourseCreateRequest> requests);

    BulkOperationResponse bulkUpdateCourses(List<CourseUpdateRequest> requests);

    // Search
    Page<CourseResponse> searchCourses(String keyword, Pageable pageable);

    List<CourseResponse> findCoursesByCriteria(CourseSearchRequest criteria);
}
