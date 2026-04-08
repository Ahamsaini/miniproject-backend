package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.mainApp.roles.AttendanceStatus;

@Data
@Entity
@Table(name = "attendances", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "lab_session_id" }))
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "attendances", "subjectEnrollments", "labAllocations" })
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_session_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "attendances", "labAllocations", "sessionCodes" })
    private LabSession labSession;

    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "pc_number")
    private String pcNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status;

    @Column(name = "is_late")
    private Boolean isLate = false;

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "is_early_exit")
    private Boolean isEarlyExit = false;

    @Column(name = "early_exit_minutes")
    private Integer earlyExitMinutes;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "verified_by")
    private String verifiedBy; // Teacher ID who verified

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
}
