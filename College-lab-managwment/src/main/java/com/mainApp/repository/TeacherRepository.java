package com.mainApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

        Optional<Teacher> findByEmployeeId(String employeeId);

        List<Teacher> findByDepartment(String department);

        org.springframework.data.domain.Page<Teacher> findByDepartment(String department,
                        org.springframework.data.domain.Pageable pageable);

        List<Teacher> findByDesignation(String designation);

        List<Teacher> findByIsActive(Boolean isActive);

        @Query(value = "SELECT t FROM Teacher t WHERE " +
                        "(:keyword IS NULL OR " +
                        "LOWER(t.employeeId) LIKE :keyword OR " +
                        "LOWER(t.firstName) LIKE :keyword OR " +
                        "LOWER(t.lastName) LIKE :keyword OR " +
                        "LOWER(t.department) LIKE :keyword OR " +
                        "LOWER(t.specialization) LIKE :keyword)", countQuery = "SELECT COUNT(t) FROM Teacher t WHERE "
                                        +
                                        "(:keyword IS NULL OR " +
                                        "LOWER(t.employeeId) LIKE :keyword OR " +
                                        "LOWER(t.firstName) LIKE :keyword OR " +
                                        "LOWER(t.lastName) LIKE :keyword OR " +
                                        "LOWER(t.department) LIKE :keyword OR " +
                                        "LOWER(t.specialization) LIKE :keyword)")
        org.springframework.data.domain.Page<Teacher> searchTeachers(@Param("keyword") String keyword,
                        org.springframework.data.domain.Pageable pageable);

        @Query("SELECT t FROM Teacher t JOIN t.subjectExpertises se WHERE se.subject.id = :subjectId")
        List<Teacher> findBySubjectId(@Param("subjectId") String subjectId);

        @Query("SELECT t FROM Teacher t WHERE t.department = :department AND t.isActive = true")
        List<Teacher> findActiveTeachersByDepartment(@Param("department") String department);

        @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department = :department")
        long countByDepartment(@Param("department") String department);

        @Query(value = "SELECT t FROM Teacher t WHERE t.isApproved = false AND " +
                        "(:department IS NULL OR t.department = :department) AND " +
                        "(:keyword IS NULL OR " +
                        "LOWER(t.firstName) LIKE :keyword OR " +
                        "LOWER(t.lastName) LIKE :keyword OR " +
                        "LOWER(t.employeeId) LIKE :keyword OR " +
                        "LOWER(t.username) LIKE :keyword)", countQuery = "SELECT COUNT(t) FROM Teacher t WHERE t.isApproved = false AND "
                                        +
                                        "(:department IS NULL OR t.department = :department) AND " +
                                        "(:keyword IS NULL OR " +
                                        "LOWER(t.firstName) LIKE :keyword OR " +
                                        "LOWER(t.lastName) LIKE :keyword OR " +
                                        "LOWER(t.employeeId) LIKE :keyword OR " +
                                        "LOWER(t.username) LIKE :keyword)")
        org.springframework.data.domain.Page<Teacher> findPendingTeachers(
                        @Param("department") String department,
                        @Param("keyword") String keyword,
                        org.springframework.data.domain.Pageable pageable);

        @Query("SELECT t FROM Teacher t WHERE t.employeeId IS NULL OR t.employeeId = ''")
        List<Teacher> findUnassignedTeachers();
}
