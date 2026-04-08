package com.mainApp.responcedto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mainApp.model.Teacher;

import lombok.Data;

@Data
public class TeacherExpertiseResponse {

    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    private Boolean isActive = true;

    private Teacher teacher;

    private String subjectId;

    private String subjectCode;

    private String subjectName;

    private Integer experienceYears;

    private String proficiencyLevel;

    private Boolean isPrimaryExpert = false;

    private LocalDate assignedDate;

    private String notes;

}
