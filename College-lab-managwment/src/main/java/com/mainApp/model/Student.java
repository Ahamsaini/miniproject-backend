package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mainApp.roles.Gender;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {

    @Column(name = "roll_number", unique = true, length = 20)
    private String rollNumber;

    @Column(name = "registration_number", unique = true, length = 20)
    private String registrationNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "emergency_contact", length = 15)
    private String emergencyContact;

    @Column(name = "guardian_name", length = 100)
    private String guardianName;

    @Column(name = "guardian_contact", length = 15)
    private String guardianContact;

    @Column(name = "academic_year", length = 9) // Format: 2024-2025
    private String academicYear;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name = "section", length = 5)
    private String section;

    @Column(name = "batch", length = 10)
    private String batch;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "aadhar_number", unique = true, length = 12)
    private String aadharNumber;

    @Column(name = "pan_number", unique = true, length = 10)
    private String panNumber;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<StudentSubjectEnrollment> subjectEnrollments = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<LabAllocation> labAllocations = new ArrayList<>();
}
