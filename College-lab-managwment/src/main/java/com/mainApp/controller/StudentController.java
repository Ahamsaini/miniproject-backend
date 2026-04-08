package com.mainApp.controller;

import com.mainApp.requestdto.StudentCreateRequest;
import com.mainApp.requestdto.StudentUpdateRequest;
import com.mainApp.requestdto.CourseEnrollmentRequest;
import com.mainApp.requestdto.SubjectEnrollmentRequest;
import com.mainApp.requestdto.StudentEnrollmentRequest;
import com.mainApp.requestdto.StudentSearchRequest;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.StudentEnrollmentResponse;
import com.mainApp.responcedto.StudentSubjectEnrollmentResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.AttendanceSummaryResponse;
import com.mainApp.responcedto.AttendanceResponse;
import com.mainApp.responcedto.BulkOperationResponse;
import com.mainApp.service.serviceInterface.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody @Valid StudentCreateRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    // List/Search Operations
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(courseId, semester, keyword, pageable));
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<StudentResponse>> getPendingStudents(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(studentService.getPendingStudents(courseId, semester, department, keyword, pageable));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<StudentResponse>> getUnassignedStudents() {
        return ResponseEntity.ok(studentService.getUnassignedStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<StudentResponse>> getStudentsByCourse(@PathVariable String courseId, Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudentsByCourse(courseId, pageable));
    }

    @GetMapping("/batch/{batch}")
    public ResponseEntity<Page<StudentResponse>> getStudentsByBatch(@PathVariable String batch, Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudentsByBatch(batch, pageable));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<Page<StudentResponse>> getStudentsBySemester(@PathVariable Integer semester,
            Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudentsBySemester(semester, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String id,
            @RequestBody @Valid StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateStudent(@PathVariable String id) {
        studentService.deactivateStudent(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateStudent(@PathVariable String id) {
        studentService.activateStudent(id);
        return ResponseEntity.ok().build();
    }

    // Enrollment Management
    @PostMapping("/{id}/enroll-course")
    public ResponseEntity<StudentEnrollmentResponse> enrollStudentInCourse(@PathVariable String id,
            @RequestBody @Valid CourseEnrollmentRequest request) {
        return ResponseEntity.ok(studentService.enrollStudentInCourse(id, request));
    }

    @PostMapping("/{id}/enroll-subject")
    public ResponseEntity<StudentSubjectEnrollmentResponse> enrollStudentInSubject(@PathVariable String id,
            @RequestBody @Valid SubjectEnrollmentRequest request) {
        return ResponseEntity.ok(studentService.enrollStudentInSubject(id, request));
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<SubjectResponse>> getStudentSubjects(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentSubjects(id));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponse>> getStudentCourses(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentCourses(id));
    }

    @DeleteMapping("/{id}/drop-subject/{subjectId}")
    public ResponseEntity<Void> dropSubject(@PathVariable String id, @PathVariable String subjectId) {
        studentService.dropSubject(id, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-section")
    public ResponseEntity<Void> changeSection(@PathVariable String id, @RequestParam String section) {
        studentService.changeSection(id, section);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<Void> promoteToNextSemester(@PathVariable String id) {
        studentService.promoteToNextSemester(id);
        return ResponseEntity.ok().build();
    }

    // Attendance
    @GetMapping("/{id}/attendance/summary")
    public ResponseEntity<AttendanceSummaryResponse> getAttendanceSummary(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getAttendanceSummary(id));
    }

    @GetMapping("/{id}/attendance")
    public ResponseEntity<Page<AttendanceResponse>> getStudentAttendance(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudentAttendance(id, pageable));
    }

    @GetMapping("/{id}/attendance/subject/{subjectId}")
    public ResponseEntity<List<AttendanceResponse>> getStudentAttendanceBySubject(@PathVariable String id,
            @PathVariable String subjectId) {
        return ResponseEntity.ok(studentService.getStudentAttendanceBySubject(id, subjectId));
    }

    // Bulk Operations
    @PostMapping("/bulk/create")
    public ResponseEntity<BulkOperationResponse> bulkCreateStudents(
            @RequestBody @Valid List<StudentCreateRequest> requests) {
        return new ResponseEntity<>(studentService.bulkCreateStudents(requests), HttpStatus.CREATED);
    }

    @PutMapping("/bulk/update")
    public ResponseEntity<BulkOperationResponse> bulkUpdateStudents(
            @RequestBody @Valid List<StudentUpdateRequest> requests) {
        return ResponseEntity.ok(studentService.bulkUpdateStudents(requests));
    }

    @PostMapping("/bulk/enroll")
    public ResponseEntity<BulkOperationResponse> bulkEnrollStudents(
            @RequestBody @Valid List<StudentEnrollmentRequest> requests) {
        return ResponseEntity.ok(studentService.bulkEnrollStudents(requests));
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponse>> searchStudents(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(studentService.searchStudents(keyword, pageable));
    }

    @PostMapping("/search/criteria")
    public ResponseEntity<List<StudentResponse>> findStudentsByCriteria(@RequestBody StudentSearchRequest criteria) {
        return ResponseEntity.ok(studentService.findStudentsByCriteria(criteria));
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<StudentResponse> getStudentByRollNumber(@PathVariable String rollNumber) {
        return ResponseEntity.ok(studentService.getStudentByRollNumber(rollNumber));
    }

    @GetMapping("/reg/{regNumber}")
    public ResponseEntity<StudentResponse> getStudentByRegistrationNumber(@PathVariable String regNumber) {
        return ResponseEntity.ok(studentService.getStudentByRegistrationNumber(regNumber));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveStudent(@PathVariable String id) {
        studentService.approveStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectStudent(@PathVariable String id) {
        studentService.rejectStudent(id);
        return ResponseEntity.ok().build();
    }
}
