package com.mainApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    Optional<Subject> findBySubjectCode(String subjectCode);

    List<Subject> findByCourseId(String courseId);

    Page<Subject> findByCourseId(String courseId, Pageable pageable);

    List<Subject> findByCourseIdAndSemesterNumber(String courseId, Integer semesterNumber);

    Page<Subject> findBySemesterNumber(Integer semesterNumber, Pageable pageable);

    @Query(value = "SELECT s FROM Subject s WHERE " +
            "LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            countQuery = "SELECT COUNT(s) FROM Subject s WHERE " +
            "LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Subject> searchSubjects(@Param("keyword") String keyword, Pageable pageable);
}
