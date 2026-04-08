package com.mainApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
        List<Attendance> findByLabSessionId(String labSessionId);

        List<Attendance> findByStudentId(String studentId);

        Page<Attendance> findByStudentId(String studentId, Pageable pageable);

        @org.springframework.data.jpa.repository.Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.labSession.subject.id = :subjectId")
        List<Attendance> findByStudentIdAndSubjectId(
                        @org.springframework.data.repository.query.Param("studentId") String studentId,
                        @org.springframework.data.repository.query.Param("subjectId") String subjectId);

        // Check if a PC number is already assigned in a session
        boolean existsByLabSessionIdAndPcNumber(String labSessionId, String pcNumber);

        boolean existsByLabSessionIdAndPcNumberAndExitTimeIsNull(String labSessionId, String pcNumber);

        Optional<Attendance> findByStudentIdAndLabSessionId(String studentId, String labSessionId);

        long countByLabSessionId(String labSessionId);

        @org.springframework.data.jpa.repository.Query("SELECT COUNT(a) FROM Attendance a WHERE a.labSession.sessionDate = :date AND (a.status IS NULL OR a.status != :status)")
        long countBySessionDateAndStatusNot(
                        @org.springframework.data.repository.query.Param("date") java.time.LocalDate date,
                        @org.springframework.data.repository.query.Param("status") com.mainApp.roles.AttendanceStatus status);
}
