package com.mainApp.requestdto;

import com.mainApp.roles.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TeacherAssignmentRequest {
    @NotNull
    private String teacherId;
    @NotNull
    private String subjectId;
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    private String classroom;
    private String section;
    private String academicYear;
    private Integer semester;
    private Boolean isRecurring;
    private String recurrencePattern;
    private LocalDate validFrom;
    private LocalDate validTo;
}
