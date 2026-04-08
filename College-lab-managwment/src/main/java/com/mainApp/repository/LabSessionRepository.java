package com.mainApp.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Lab;
import com.mainApp.model.LabSession;

@Repository
public interface LabSessionRepository extends JpaRepository<LabSession, String> {
        List<LabSession> findByLabId(String labId);

        List<LabSession> findByLabIdAndSessionDate(String labId, LocalDate sessionDate);

        List<LabSession> findBySessionDate(LocalDate sessionDate);

        List<LabSession> findByTeacherId(String teacherId);

        List<LabSession> findBySubjectId(String subjectId);

        @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "lab", "subject", "subject.course",
                        "teacher" })
        @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT s FROM LabSession s " +
                        "LEFT JOIN s.lab l " +
                        "LEFT JOIN s.subject sub " +
                        "LEFT JOIN sub.course c " +
                        "LEFT JOIN s.teacher t " +
                        "WHERE " +
                        "(:status IS NULL OR s.status = :status) AND " +
                        "(:courseId IS NULL OR c.id = :courseId) AND " +
                        "(:semester IS NULL OR sub.semesterNumber = :semester) AND " +
                        "(:section IS NULL OR s.section = :section) AND " +
                        "(:subjectId IS NULL OR sub.id = :subjectId) AND " +
                        "(:sessionDate IS NULL OR s.sessionDate = :sessionDate) AND " +
                        "(:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(l.labName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(sub.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))  " +
                        "ORDER BY s.sessionDate DESC, s.startTime DESC", 
                        countQuery = "SELECT COUNT(DISTINCT s) FROM LabSession s " +
                        "LEFT JOIN s.lab l " +
                        "LEFT JOIN s.subject sub " +
                        "LEFT JOIN sub.course c " +
                        "LEFT JOIN s.teacher t " +
                        "WHERE " +
                        "(:status IS NULL OR s.status = :status) AND " +
                        "(:courseId IS NULL OR c.id = :courseId) AND " +
                        "(:semester IS NULL OR sub.semesterNumber = :semester) AND " +
                        "(:section IS NULL OR s.section = :section) AND " +
                        "(:subjectId IS NULL OR sub.id = :subjectId) AND " +
                        "(:sessionDate IS NULL OR s.sessionDate = :sessionDate) AND " +
                        "(:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(l.labName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(sub.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        org.springframework.data.domain.Page<LabSession> searchSessions(
                        @org.springframework.data.repository.query.Param("status") com.mainApp.roles.LabSessionStatus status,
                        @org.springframework.data.repository.query.Param("courseId") String courseId,
                        @org.springframework.data.repository.query.Param("semester") Integer semester,
                        @org.springframework.data.repository.query.Param("section") String section,
                        @org.springframework.data.repository.query.Param("subjectId") String subjectId,
                        @org.springframework.data.repository.query.Param("sessionDate") java.time.LocalDate sessionDate,
                        @org.springframework.data.repository.query.Param("keyword") String keyword,
                        org.springframework.data.domain.Pageable pageable);

        long countBySessionDate(LocalDate sessionDate);

        long countByStatus(com.mainApp.roles.LabSessionStatus status);

        // Check if session exists for duplicate prevention
        boolean existsByLabAndSessionDateAndStartTime(Lab lab, LocalDate sessionDate, LocalTime startTime);

        List<LabSession> findBySubjectIdAndSubjectSemesterNumberAndSubjectCourseIdAndStatus(
                        String subjectId, Integer semesterNumber, String courseId,
                        com.mainApp.roles.LabSessionStatus status);
}
