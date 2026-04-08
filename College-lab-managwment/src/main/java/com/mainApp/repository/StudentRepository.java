package com.mainApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

        Optional<Student> findByRollNumber(String rollNumber);

        Optional<Student> findByRegistrationNumber(String registrationNumber);

        Optional<Student> findByAadharNumber(String aadharNumber);

        Optional<Student> findByUsername(String username);

        boolean existsByRollNumber(String rollNumber);

        boolean existsByRegistrationNumber(String registrationNumber);

        boolean existsByAadharNumber(String aadharNumber);

        org.springframework.data.domain.Page<Student> findByCourseId(String courseId,
                        org.springframework.data.domain.Pageable pageable);

        List<Student> findByCourseId(String courseId);

        org.springframework.data.domain.Page<Student> findByCurrentSemester(Integer semester,
                        org.springframework.data.domain.Pageable pageable);

        org.springframework.data.domain.Page<Student> findByBatch(String batch,
                        org.springframework.data.domain.Pageable pageable);

        List<Student> findBySection(String section);

        List<Student> findByAcademicYear(String academicYear);

        @Query("SELECT s FROM Student s WHERE s.course.id = :courseId AND s.currentSemester = :semester")
        List<Student> findByCourseAndSemester(@Param("courseId") String courseId,
                        @Param("semester") Integer semester);

        @Query("SELECT s FROM Student s WHERE s.course.id = :courseId AND s.currentSemester = :semester AND (:section IS NULL OR s.section = :section)")
        List<Student> findByCourseAndSemesterAndSection(@Param("courseId") String courseId,
                        @Param("semester") Integer semester,
                        @Param("section") String section);

        @Query("SELECT s FROM Student s WHERE " +
                        "(:courseId IS NULL OR s.course.id = :courseId) AND " +
                        "(:semester IS NULL OR s.currentSemester = :semester) AND " +
                        "(:keyword IS NULL OR " +
                        "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.registrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.guardianName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        org.springframework.data.domain.Page<Student> searchStudents(
                        @Param("courseId") String courseId,
                        @Param("semester") Integer semester,
                        @Param("keyword") String keyword,
                        org.springframework.data.domain.Pageable pageable);

        @Query("SELECT COUNT(s) FROM Student s WHERE s.course.id = :courseId")
        long countByCourseId(@Param("courseId") String courseId);

        @Query("SELECT s FROM Student s WHERE s.currentSemester = :semester AND s.isActive = true")
        List<Student> findActiveStudentsBySemester(@Param("semester") Integer semester);

        @Query("SELECT s FROM Student s JOIN s.subjectEnrollments se WHERE se.subject.id = :subjectId")
        List<Student> findBySubjectId(@Param("subjectId") String subjectId);

        @Query("SELECT s FROM Student s WHERE s.rollNumber IS NULL OR s.rollNumber = '' OR s.registrationNumber IS NULL OR s.registrationNumber = ''")
        List<Student> findUnassignedStudents();

        @Query("SELECT s FROM Student s WHERE s.isApproved = false AND " +
                        "(:courseId IS NULL OR s.course.id = :courseId) AND " +
                        "(:semester IS NULL OR s.currentSemester = :semester) AND " +
                        "(:department IS NULL OR s.course.department = :department) AND " +
                        "(:keyword IS NULL OR " +
                        "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(s.username) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<Student> findPendingStudents(
                        @Param("courseId") String courseId,
                        @Param("semester") Integer semester,
                        @Param("department") String department,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        Page<Student> findByIsApproved(Boolean isApproved, Pageable pageable);
}
