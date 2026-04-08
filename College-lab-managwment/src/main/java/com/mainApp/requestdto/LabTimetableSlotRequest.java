package com.mainApp.requestdto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainApp.roles.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabTimetableSlotRequest {
    @NotBlank
    private String courseId;

    @NotBlank
    private String subjectId;

    @NotBlank
    private String labId;

    @NotBlank
    private String teacherId;

    @NotNull
    private Integer semester;

    private String academicYear;

    private String section;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private DayOfWeek dayOfWeek;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private Boolean isRecurring = true;

    private String recurrencePattern; // WEEKLY, BIWEEKLY

    private String notes;
}
