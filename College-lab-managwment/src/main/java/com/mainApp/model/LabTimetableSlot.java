package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

import com.mainApp.roles.DayOfWeek;

@Data
@Entity
@Table(name = "lab_timetable_slots")
public class LabTimetableSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "students", "subjects" })
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labSessions", "studentEnrollments", "teacherExpertises" })
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("labSessions")
    private Lab lab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labSessions", "subjectExpertises" })
    private Teacher teacher;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "academic_year", length = 9)
    private String academicYear;

    @Column(name = "section", length = 10)
    private String section;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_recurring")
    private Boolean isRecurring = true;

    @Column(name = "recurrence_pattern", length = 50)
    private String recurrencePattern; // WEEKLY, BIWEEKLY

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
