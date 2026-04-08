package com.mainApp.service.serviceInterface;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    // CRUD Operations
    StudentResponse createStudent(StudentCreateRequest request);

    StudentResponse getStudentById(String id);

    StudentResponse getStudentByRollNumber(String rollNumber);

    StudentResponse getStudentByRegistrationNumber(String regNumber);

    Page<StudentResponse> getAllStudents(String courseId, Integer semester, String keyword, Pageable pageable);

    Page<StudentResponse> getStudentsByCourse(String courseId, Pageable pageable);

    Page<StudentResponse> getStudentsByBatch(String batch, Pageable pageable);

    Page<StudentResponse> getStudentsBySemester(Integer semester, Pageable pageable);

    StudentResponse updateStudent(String id, StudentUpdateRequest request);

    void deleteStudent(String id);

    void deactivateStudent(String id);

    void activateStudent(String id);

    // Enrollment Management
    StudentEnrollmentResponse enrollStudentInCourse(String studentId, CourseEnrollmentRequest request);

    StudentSubjectEnrollmentResponse enrollStudentInSubject(String studentId, SubjectEnrollmentRequest request);

    List<SubjectResponse> getStudentSubjects(String studentId);

    List<CourseResponse> getStudentCourses(String studentId);

    void dropSubject(String studentId, String subjectId);

    void changeSection(String studentId, String section);

    void promoteToNextSemester(String studentId);

    // Attendance
    AttendanceSummaryResponse getAttendanceSummary(String studentId);

    Page<AttendanceResponse> getStudentAttendance(String studentId, Pageable pageable);

    List<AttendanceResponse> getStudentAttendanceBySubject(String studentId, String subjectId);

    // Bulk Operations
    BulkOperationResponse bulkCreateStudents(List<StudentCreateRequest> requests);

    BulkOperationResponse bulkUpdateStudents(List<StudentUpdateRequest> requests);

    BulkOperationResponse bulkEnrollStudents(List<StudentEnrollmentRequest> requests);

    // Search
    Page<StudentResponse> searchStudents(String keyword, Pageable pageable);

    List<StudentResponse> findStudentsByCriteria(StudentSearchRequest criteria);

    List<StudentResponse> getUnassignedStudents();

    // Onboarding
    Page<StudentResponse> getPendingStudents(String courseId, Integer semester, String department, String keyword,
            Pageable pageable);

    void approveStudent(String id);

    void rejectStudent(String id);
}
