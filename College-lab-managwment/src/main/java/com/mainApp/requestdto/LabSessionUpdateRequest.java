package com.mainApp.requestdto;

import com.mainApp.roles.LabSessionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LabSessionUpdateRequest {
    private String labId;
    private String subjectId;
    private String teacherId;
    private LocalDate sessionDate;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String sessionTopic;
    private String experimentName;
    private String objectives;
    private String materialsRequired; // JSON
    private LabSessionStatus status;
    private String section;
}
