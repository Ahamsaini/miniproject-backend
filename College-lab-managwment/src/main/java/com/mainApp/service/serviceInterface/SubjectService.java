package com.mainApp.service.serviceInterface;

import com.mainApp.requestdto.LabSessionCreateRequest;
import com.mainApp.requestdto.SubjectCreateRequest;
import com.mainApp.requestdto.SubjectSearchRequest;
import com.mainApp.requestdto.SubjectUpdateRequest;
import com.mainApp.requestdto.SubjectExpertiseRequest;
import com.mainApp.responcedto.AttendanceReportResponse;
import com.mainApp.responcedto.BulkOperationResponse;
import com.mainApp.responcedto.LabSessionResponse;
import com.mainApp.responcedto.StudentEnrollmentResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.SubjectAnalyticsResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.responcedto.TeacherResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {
    // CRUD Operations
    SubjectResponse createSubject(SubjectCreateRequest request);

    SubjectResponse getSubjectById(String id);

    SubjectResponse getSubjectByCode(String code);

    Page<SubjectResponse> getAllSubjects(Pageable pageable);

    Page<SubjectResponse> getSubjectsBySemester(Integer semester, Pageable pageable);

    Page<SubjectResponse> getSubjectsByCourse(String courseId, Pageable pageable);

    SubjectResponse updateSubject(String id, SubjectUpdateRequest request);

    void deleteSubject(String id);

    // Teacher Expertise
    List<TeacherResponse> getSubjectTeachers(String subjectId);

    void assignTeacherToSubject(String subjectId, String teacherId, SubjectExpertiseRequest request);

    void removeTeacherFromSubject(String subjectId, String teacherId);

    // Student Enrollment
    List<StudentResponse> getSubjectStudents(String subjectId);

    Integer getSubjectStudentCount(String subjectId);

    List<StudentEnrollmentResponse> getSubjectEnrollments(String subjectId);

    // Lab Sessions
    List<LabSessionResponse> getSubjectLabSessions(String subjectId);

    LabSessionResponse createLabSessionForSubject(String subjectId, LabSessionCreateRequest request);

    // Analytics
    SubjectAnalyticsResponse getSubjectAnalytics(String subjectId);

    AttendanceReportResponse getSubjectAttendanceReport(String subjectId);

    // Bulk Operations
    BulkOperationResponse bulkCreateSubjects(List<SubjectCreateRequest> requests);

    // Search
    Page<SubjectResponse> searchSubjects(String keyword, Pageable pageable);

    List<SubjectResponse> findSubjectsByCriteria(SubjectSearchRequest criteria);
}
