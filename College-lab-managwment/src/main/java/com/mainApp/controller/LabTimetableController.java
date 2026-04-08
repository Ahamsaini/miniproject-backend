package com.mainApp.controller;

import com.mainApp.requestdto.LabTimetableSlotRequest;
import com.mainApp.responcedto.LabTimetableSlotResponse;
import com.mainApp.responcedto.TimetableConflictResponse;
import com.mainApp.service.serviceInterface.LabTimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/timetable")
@RequiredArgsConstructor
public class LabTimetableController {

    private final LabTimetableService labTimetableService;

    // Create new timetable slot
    @PostMapping("/slots")
    public ResponseEntity<LabTimetableSlotResponse> createTimetableSlot(
            @Valid @RequestBody LabTimetableSlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(labTimetableService.createTimetableSlot(request));
    }

    // Update timetable slot
    @PutMapping("/slots/{slotId}")
    public ResponseEntity<LabTimetableSlotResponse> updateTimetableSlot(
            @PathVariable String slotId,
            @Valid @RequestBody LabTimetableSlotRequest request) {
        return ResponseEntity.ok(labTimetableService.updateTimetableSlot(slotId, request));
    }

    // Delete timetable slot
    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> deleteTimetableSlot(@PathVariable String slotId) {
        labTimetableService.deleteTimetableSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    // Get timetable for a course-semester
    @GetMapping("/courses/{courseId}/semesters/{semester}")
    public ResponseEntity<?> getCourseTimetable(
            @PathVariable String courseId,
            @PathVariable Integer semester) {
        try {
            return ResponseEntity.ok(labTimetableService.getCourseTimetable(courseId, semester));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching timetable: " + e.getMessage());
        }
    }

    // Get timetable for a student
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<LabTimetableSlotResponse>> getStudentTimetable(
            @PathVariable String studentId) {
        return ResponseEntity.ok(labTimetableService.getStudentTimetable(studentId));
    }

    // Check for conflicts
    @PostMapping("/check-conflicts")
    public ResponseEntity<TimetableConflictResponse> checkConflicts(
            @Valid @RequestBody LabTimetableSlotRequest request,
            @RequestParam(required = false, defaultValue = "") String excludeSlotId) {
        return ResponseEntity.ok(labTimetableService.checkConflicts(request, excludeSlotId));
    }

    // Generate lab sessions from timetable template
    @PostMapping("/courses/{courseId}/semesters/{semester}/generate-sessions")
    public ResponseEntity<Void> generateLabSessions(
            @PathVariable String courseId,
            @PathVariable Integer semester,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        labTimetableService.generateLabSessionsFromTimetable(courseId, semester, startDate, endDate);
        return ResponseEntity.ok().build();
    }
}
