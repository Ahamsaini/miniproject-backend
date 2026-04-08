package com.mainApp.scheduler;

import com.mainApp.model.Course;
import com.mainApp.repository.CourseRepository;
import com.mainApp.service.serviceInterface.LabTimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LabSessionScheduler {

    private final LabTimetableService labTimetableService;
    private final CourseRepository courseRepository;

    /**
     * Automatically generates lab sessions for the next 2 weeks
     * Runs every Sunday at midnight
     */
    @Scheduled(cron = "0 0 0 * * SUN")
    public void scheduleWeeklyLabSessions() {
        log.info("Starting scheduled lab session generation...");

        List<Course> courses = courseRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(1); // Starting from Monday
        LocalDate endDate = today.plusDays(14); // For next 2 weeks

        for (Course course : courses) {
            // Assuming we generate for all active semesters (usually 1-8)
            for (int semester = 1; semester <= 8; semester++) {
                try {
                    labTimetableService.generateLabSessionsFromTimetable(
                            course.getId(), semester, startDate, endDate);
                } catch (Exception e) {
                    // Log and continue (some semesters might not have slots)
                    log.debug("No slots or error for course {} semester {}: {}",
                            course.getCourseName(), semester, e.getMessage());
                }
            }
        }

        log.info("Finished scheduled lab session generation.");
    }
}
