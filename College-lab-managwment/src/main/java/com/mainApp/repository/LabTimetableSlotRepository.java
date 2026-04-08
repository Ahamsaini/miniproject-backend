package com.mainApp.repository;

import com.mainApp.model.LabTimetableSlot;
import com.mainApp.roles.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface LabTimetableSlotRepository extends JpaRepository<LabTimetableSlot, String> {

        // Find all timetable slots for a specific course and semester
        List<LabTimetableSlot> findByCourseIdAndSemesterOrderByDayOfWeekAscStartTimeAsc(
                        String courseId, Integer semester);

        // Find timetable slots by day for a course-semester
        List<LabTimetableSlot> findByCourseIdAndSemesterAndDayOfWeek(
                        String courseId, Integer semester, DayOfWeek dayOfWeek);

        List<LabTimetableSlot> findByTeacherId(String teacherId);

        // Check for lab conflicts (same lab, day, overlapping time)
        @Query("SELECT COUNT(t) > 0 FROM LabTimetableSlot t WHERE " +
                        "t.lab.id = :labId AND t.dayOfWeek = :day AND " +
                        "((t.startTime < :endTime AND t.endTime > :startTime)) AND " +
                        "t.id != :excludeId")
        boolean existsLabConflict(@Param("labId") String labId,
                        @Param("day") DayOfWeek day,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime,
                        @Param("excludeId") String excludeId);

        // Check for teacher conflicts (same teacher, day, overlapping time)
        @Query("SELECT COUNT(t) > 0 FROM LabTimetableSlot t WHERE " +
                        "t.teacher.id = :teacherId AND t.dayOfWeek = :day AND " +
                        "((t.startTime < :endTime AND t.endTime > :startTime)) AND " +
                        "t.id != :excludeId")
        boolean existsTeacherConflict(@Param("teacherId") String teacherId,
                        @Param("day") DayOfWeek day,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime,
                        @Param("excludeId") String excludeId);

        // Find all slots for a specific student (based on their course and semester)
        @Query("SELECT t FROM LabTimetableSlot t WHERE " +
                        "t.course.id = :courseId AND t.semester = :semester " +
                        "ORDER BY t.dayOfWeek ASC, t.startTime ASC")
        List<LabTimetableSlot> findStudentTimetable(@Param("courseId") String courseId,
                        @Param("semester") Integer semester);

        // Delete all slots for a course-semester
        void deleteByCourseIdAndSemester(String courseId, Integer semester);
}
