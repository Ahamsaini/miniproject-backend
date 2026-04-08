package com.mainApp.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mainApp.model.Lab;
import com.mainApp.roles.LabType;

@Repository
public interface LabRepository extends JpaRepository<Lab, String> {
    Optional<Lab> findByLabCode(String labCode);

    Page<Lab> findByLabType(LabType labType, Pageable pageable);

    Page<Lab> findByBuilding(String building, Pageable pageable);

    @Query("SELECT l FROM Lab l WHERE " +
            "LOWER(l.labCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.labName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.building) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.roomNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Lab> searchLabs(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT l FROM Lab l " +
            "JOIN l.labSessions s " +
            "JOIN s.subject sub " +
            "JOIN sub.course c " +
            "WHERE c.id = :courseId")
    List<Lab> findLabsByCourseId(@Param("courseId") String courseId);
   
}
