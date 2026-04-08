package com.mainApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.LabAllocation;

@Repository
public interface LabAllocationRepository extends JpaRepository<LabAllocation, String> {
    List<LabAllocation> findByLabSessionId(String labSessionId);

    Optional<LabAllocation> findByStudentIdAndLabSessionId(String studentId, String labSessionId);

    List<LabAllocation> findByStudentId(String studentId);
}
