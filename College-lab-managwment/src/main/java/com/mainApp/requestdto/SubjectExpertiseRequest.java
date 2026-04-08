package com.mainApp.requestdto;

import com.mainApp.roles.ProficiencyLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubjectExpertiseRequest {
    @NotNull
    private String teacherId;
    @NotNull
    private String subjectId;
    private Integer experienceYears;
    private ProficiencyLevel proficiencyLevel;
    private Boolean isPrimaryExpert;
    private LocalDate assignedDate;
    private String notes;
}
