package com.mainApp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mainApp.model.LectureSchedule;

@Repository
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, String> {
    List<LectureSchedule> findByTeacherId(String teacherId);

    List<LectureSchedule> findBySubjectId(String subjectId);
}
