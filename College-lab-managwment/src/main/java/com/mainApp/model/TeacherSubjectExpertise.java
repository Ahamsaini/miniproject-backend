package com.mainApp.model;

import java.time.LocalDate;

import com.mainApp.roles.ProficiencyLevel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "teacher_subject_expertise", uniqueConstraints = @UniqueConstraint(columnNames = { "teacher_id",
        "subject_id" }))
public class TeacherSubjectExpertise extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "subjectExpertises", "labSessions", "lectureSchedules" })
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "teacherExpertises", "studentEnrollments", "labSessions" })
    private Subject subject;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level")
    private ProficiencyLevel proficiencyLevel;

    @Column(name = "is_primary_expert")
    private Boolean isPrimaryExpert = false;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
