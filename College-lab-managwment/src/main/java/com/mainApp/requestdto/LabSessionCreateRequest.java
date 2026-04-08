package com.mainApp.requestdto;

import com.mainApp.roles.LabSessionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LabSessionCreateRequest {
    @NotNull
    private String labId;
    @NotNull
    private String subjectId;
    @NotNull
    private String teacherId;
    @NotNull
    private LocalDate sessionDate;
    @NotNull
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @NotNull
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String sessionTopic;
    private String experimentName;
    private String objectives;
    private String materialsRequired; // JSON
    private LabSessionStatus status;
    private String section;
}
