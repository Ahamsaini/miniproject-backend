package com.mainApp.responcedto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainApp.roles.DayOfWeek;
import lombok.Data;

@Data
public class LabTimetableSlotResponse {
    private String id;
    private String courseId;
    private String courseName;
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private String labId;
    private String labName;
    private String labCode;
    private String teacherId;
    private String teacherName;
    private Integer semester;
    private String academicYear;
    private String section;
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Boolean isRecurring;
    private String recurrencePattern;
    private String notes;
    private Boolean isTarget;
}
