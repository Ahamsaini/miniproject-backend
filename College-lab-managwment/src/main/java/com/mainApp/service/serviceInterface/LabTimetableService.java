package com.mainApp.service.serviceInterface;

import com.mainApp.requestdto.LabTimetableSlotRequest;
import com.mainApp.responcedto.LabTimetableSlotResponse;
import com.mainApp.responcedto.TimetableConflictResponse;

import java.time.LocalDate;
import java.util.List;

public interface LabTimetableService {

    // Create a new timetable slot
    LabTimetableSlotResponse createTimetableSlot(LabTimetableSlotRequest request);

    // Update an existing timetable slot
    LabTimetableSlotResponse updateTimetableSlot(String slotId, LabTimetableSlotRequest request);

    // Delete a timetable slot
    void deleteTimetableSlot(String slotId);

    // Get timetable for a specific course and semester
    List<LabTimetableSlotResponse> getCourseTimetable(String courseId, Integer semester);

    // Get timetable for a student (based on their course and semester)
    List<LabTimetableSlotResponse> getStudentTimetable(String studentId);

    // Check for conflicts before creating/updating a slot
    TimetableConflictResponse checkConflicts(LabTimetableSlotRequest request, String excludeSlotId);

    // Generate lab sessions from timetable template for a date range
    void generateLabSessionsFromTimetable(String courseId, Integer semester,
            LocalDate startDate, LocalDate endDate);
}
