package com.mainApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.StudentSubjectEnrollment;

@Repository
public interface StudentSubjectEnrollmentRepository extends JpaRepository<StudentSubjectEnrollment, String> {
    List<StudentSubjectEnrollment> findByStudentId(String studentId);

    List<StudentSubjectEnrollment> findBySubjectId(String subjectId);

    Optional<StudentSubjectEnrollment> findByStudentIdAndSubjectId(String studentId, String subjectId);

    List<StudentSubjectEnrollment> findByStudentIdAndSemester(String studentId, Integer semester);
}
