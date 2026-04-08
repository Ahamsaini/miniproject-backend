package com.mainApp.controller;

import com.mainApp.requestdto.*;
import com.mainApp.responcedto.*;
import com.mainApp.service.serviceInterface.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody @Valid SubjectCreateRequest request) {
        return new ResponseEntity<>(subjectService.createSubject(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable String id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<SubjectResponse> getSubjectByCode(@PathVariable String code) {
        return ResponseEntity.ok(subjectService.getSubjectByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<SubjectResponse>> getAllSubjects(Pageable pageable) {
        return ResponseEntity.ok(subjectService.getAllSubjects(pageable));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<Page<SubjectResponse>> getSubjectsBySemester(@PathVariable Integer semester,
            Pageable pageable) {
        return ResponseEntity.ok(subjectService.getSubjectsBySemester(semester, pageable));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<SubjectResponse>> getSubjectsByCourse(@PathVariable String courseId, Pageable pageable) {
        return ResponseEntity.ok(subjectService.getSubjectsByCourse(courseId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable String id,
            @RequestBody @Valid SubjectUpdateRequest request) {
        return ResponseEntity.ok(subjectService.updateSubject(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    // Teacher Expertise
    @GetMapping("/{subjectId}/teachers")
    public ResponseEntity<List<TeacherResponse>> getSubjectTeachers(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectTeachers(subjectId));
    }

    @PostMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<Void> assignTeacherToSubject(@PathVariable String subjectId, @PathVariable String teacherId,
            @RequestBody @Valid SubjectExpertiseRequest request) {
        subjectService.assignTeacherToSubject(subjectId, teacherId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<Void> removeTeacherFromSubject(@PathVariable String subjectId,
            @PathVariable String teacherId) {
        subjectService.removeTeacherFromSubject(subjectId, teacherId);
        return ResponseEntity.noContent().build();
    }

    // Student Enrollment
    @GetMapping("/{subjectId}/students")
    public ResponseEntity<List<StudentResponse>> getSubjectStudents(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectStudents(subjectId));
    }

    @GetMapping("/{subjectId}/students/count")
    public ResponseEntity<Integer> getSubjectStudentCount(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectStudentCount(subjectId));
    }

    @GetMapping("/{subjectId}/enrollments")
    public ResponseEntity<List<StudentEnrollmentResponse>> getSubjectEnrollments(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectEnrollments(subjectId));
    }

    // Lab Sessions
    @GetMapping("/{subjectId}/lab-sessions")
    public ResponseEntity<List<LabSessionResponse>> getSubjectLabSessions(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectLabSessions(subjectId));
    }

    @PostMapping("/{subjectId}/lab-sessions")
    public ResponseEntity<LabSessionResponse> createLabSessionForSubject(@PathVariable String subjectId,
            @RequestBody @Valid LabSessionCreateRequest request) {
        return new ResponseEntity<>(subjectService.createLabSessionForSubject(subjectId, request), HttpStatus.CREATED);
    }

    // Analytics
    @GetMapping("/{subjectId}/analytics")
    public ResponseEntity<SubjectAnalyticsResponse> getSubjectAnalytics(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectAnalytics(subjectId));
    }

    @GetMapping("/{subjectId}/attendance-report")
    public ResponseEntity<AttendanceReportResponse> getSubjectAttendanceReport(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectAttendanceReport(subjectId));
    }

    // Bulk Operations
    @PostMapping("/bulk/create")
    public ResponseEntity<BulkOperationResponse> bulkCreateSubjects(
            @RequestBody @Valid List<SubjectCreateRequest> requests) {
        return new ResponseEntity<>(subjectService.bulkCreateSubjects(requests), HttpStatus.CREATED);
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<Page<SubjectResponse>> searchSubjects(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(subjectService.searchSubjects(keyword, pageable));
    }

    @PostMapping("/search/criteria")
    public ResponseEntity<List<SubjectResponse>> findSubjectsByCriteria(@RequestBody SubjectSearchRequest criteria) {
        return ResponseEntity.ok(subjectService.findSubjectsByCriteria(criteria));
    }
}
