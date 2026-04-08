package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

import com.mainApp.roles.EnrollmentStatus;

@Data
@Entity
@Table(name = "student_subject_enrollments", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id",
        "subject_id", "semester", "academic_year" }))
public class StudentSubjectEnrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "subjectEnrollments", "labAllocations", "attendances" })
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "studentEnrollments", "teacherExpertises", "labSessions" })
    private Subject subject;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "academic_year", length = 9)
    private String academicYear;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

    @Column(name = "grade")
    private String grade;

    @Column(name = "attendance_percentage")
    private Double attendancePercentage;
}
