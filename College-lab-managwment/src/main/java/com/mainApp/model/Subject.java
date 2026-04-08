package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {

    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @Column(name = "subject_name", nullable = false, length = 200)
    private String subjectName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "theory_hours")
    private Integer theoryHours;

    @Column(name = "lab_hours")
    private Integer labHours;

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(name = "prerequisites", columnDefinition = "JSON")
    private String prerequisites;

    @Column(name = "syllabus", columnDefinition = "TEXT")
    private String syllabus;

    @Column(name = "reference_books", columnDefinition = "JSON")
    private String referenceBooks;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StudentSubjectEnrollment> studentEnrollments = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeacherSubjectExpertise> teacherExpertises = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LabSession> labSessions = new ArrayList<>();
}