package com.mainApp.controller;

import com.mainApp.requestdto.*;
import com.mainApp.requestdto.CourseSearchRequest;
import com.mainApp.responcedto.*;
import com.mainApp.service.serviceInterface.CourseService;
import com.mainApp.service.serviceInterface.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final com.mainApp.service.serviceInterface.LabService labService;
    private final DashboardService dashboardService;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody @Valid CourseCreateRequest request) {
        return new ResponseEntity<>(courseService.createCourse(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CourseResponse> getCourseByCode(@PathVariable String code) {
        return ResponseEntity.ok(courseService.getCourseByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(Pageable pageable) {
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> getAllCoursesList() {
        return ResponseEntity.ok(courseService.getAllCoursesList());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<Page<CourseResponse>> getCoursesByDepartment(@PathVariable String department,
            Pageable pageable) {
        return ResponseEntity.ok(courseService.getCoursesByDepartment(department, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable String id,
            @RequestBody @Valid CourseUpdateRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateCourse(@PathVariable String id) {
        courseService.activateCourse(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCourse(@PathVariable String id) {
        courseService.deactivateCourse(id);
        return ResponseEntity.ok().build();
    }

    // Subject Management
    @PostMapping("/{courseId}/subjects")
    public ResponseEntity<SubjectResponse> addSubjectToCourse(@PathVariable String courseId,
            @RequestBody @Valid SubjectCreateRequest request) {
        return new ResponseEntity<>(courseService.addSubjectToCourse(courseId, request), HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}/subjects")
    public ResponseEntity<List<SubjectResponse>> getCourseSubjects(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseSubjects(courseId));
    }

    @GetMapping("/{courseId}/subjects/semester/{semester}")
    public ResponseEntity<List<SubjectResponse>> getCourseSubjectsBySemester(@PathVariable String courseId,
            @PathVariable Integer semester) {
        return ResponseEntity.ok(courseService.getCourseSubjectsBySemester(courseId, semester));
    }

    @DeleteMapping("/{courseId}/subjects/{subjectId}")
    public ResponseEntity<Void> removeSubjectFromCourse(@PathVariable String courseId, @PathVariable String subjectId) {
        courseService.removeSubjectFromCourse(courseId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}/subjects/{subjectId}")
    public ResponseEntity<Void> updateSubjectInCourse(@PathVariable String courseId, @PathVariable String subjectId,
            @RequestBody @Valid SubjectUpdateRequest request) {
        courseService.updateSubjectInCourse(courseId, subjectId, request);
        return ResponseEntity.ok().build();
    }

    // Student Management
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponse>> getCourseStudents(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseStudents(courseId));
    }

    @GetMapping("/{courseId}/students/count")
    public ResponseEntity<Integer> getCourseStudentCount(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseStudentCount(courseId));
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Void> addStudentToCourse(@PathVariable String courseId, @PathVariable String studentId) {
        courseService.addStudentToCourse(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable String courseId, @PathVariable String studentId) {
        courseService.removeStudentFromCourse(courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    // Analytics
    @GetMapping("/{courseId}/analytics")
    public ResponseEntity<CourseAnalyticsResponse> getCourseAnalytics(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseAnalytics(courseId));
    }

    @GetMapping("/{courseId}/labs")
    public ResponseEntity<List<LabResponse>> getCourseLabs(@PathVariable String courseId) {
        return ResponseEntity.ok(labService.getLabsByCourseId(courseId));
    }

    @GetMapping("/{courseId}/enrollment-trends")
    public ResponseEntity<EnrollmentTrendResponse> getEnrollmentTrend(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getEnrollmentTrend(courseId));
    }

    // Bulk Operations
    @PostMapping("/bulk/create")
    public ResponseEntity<BulkOperationResponse> bulkCreateCourses(
            @RequestBody @Valid List<CourseCreateRequest> requests) {
        return new ResponseEntity<>(courseService.bulkCreateCourses(requests), HttpStatus.CREATED);
    }

    @PutMapping("/bulk/update")
    public ResponseEntity<BulkOperationResponse> bulkUpdateCourses(
            @RequestBody @Valid List<CourseUpdateRequest> requests) {
        return ResponseEntity.ok(courseService.bulkUpdateCourses(requests));
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponse>> searchCourses(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(courseService.searchCourses(keyword, pageable));
    }

    @PostMapping("/search/criteria")
    public ResponseEntity<List<CourseResponse>> findCoursesByCriteria(@RequestBody CourseSearchRequest criteria) {
        return ResponseEntity.ok(courseService.findCoursesByCriteria(criteria));
    }

    // Workaround for Dashboard Stats
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
}
