package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.mainApp.roles.LabSessionStatus;

@Data
@Entity
@Table(name = "lab_sessions")
public class LabSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("labSessions")
    private Lab lab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labSessions", "studentEnrollments", "teacherExpertises" })
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labSessions", "teacherExpertises" })
    private Teacher teacher;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "session_topic", length = 200)
    private String sessionTopic;

    @Column(name = "experiment_name", length = 200)
    private String experimentName;

    @Column(name = "objectives", columnDefinition = "TEXT")
    private String objectives;

    @Column(name = "materials_required", columnDefinition = "JSON")
    private String materialsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LabSessionStatus status;

    @Column(name = "section", length = 5)
    private String section;

    @Column(name = "is_code_generated")
    private Boolean isCodeGenerated = false;

    @Column(name = "code_generated_at")
    private LocalDateTime codeGeneratedAt;

    @Column(name = "attendance_marked")
    private Boolean attendanceMarked = false;

    @Column(name = "attendance_marked_at")
    private LocalDateTime attendanceMarkedAt;

    // Relationships
    @OneToMany(mappedBy = "labSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<SessionCode> sessionCodes = new ArrayList<>();

    @OneToMany(mappedBy = "labSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "labSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<LabAllocation> labAllocations = new ArrayList<>();
}
