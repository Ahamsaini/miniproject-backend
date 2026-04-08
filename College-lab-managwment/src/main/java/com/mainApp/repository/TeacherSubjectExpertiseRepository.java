package com.mainApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.TeacherSubjectExpertise;

@Repository
public interface TeacherSubjectExpertiseRepository extends JpaRepository<TeacherSubjectExpertise, String> {
    List<TeacherSubjectExpertise> findByTeacherId(String teacherId);

    List<TeacherSubjectExpertise> findBySubjectId(String subjectId);

    Optional<TeacherSubjectExpertise> findByTeacherIdAndSubjectId(String teacherId, String subjectId);
}
