package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

import com.mainApp.roles.DayOfWeek;

@Data
@Entity
@Table(name = "lecture_schedules")
public class LectureSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "lectureSchedules", "labSessions", "studentEnrollments", "teacherExpertises" })
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "lectureSchedules", "labSessions", "subjectExpertises" })
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "classroom", length = 20)
    private String classroom;

    @Column(name = "section", length = 5)
    private String section;

    @Column(name = "academic_year", length = 9)
    private String academicYear;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "is_recurring")
    private Boolean isRecurring = true;

    @Column(name = "recurrence_pattern", length = 50)
    private String recurrencePattern; // WEEKLY, BIWEEKLY

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;
}
