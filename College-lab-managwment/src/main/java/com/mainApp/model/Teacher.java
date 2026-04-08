package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "teachers")
@DiscriminatorValue("TEACHER")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {

    @Column(name = "employee_id", unique = true, length = 20)
    private String employeeId;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "designation", length = 50)
    private String designation;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "office_room", length = 20)
    private String officeRoom;

    @Column(name = "office_hours", length = 100)
    private String officeHours;

    @Column(name = "specialization", length = 200)
    private String specialization;

    // Relationships
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<TeacherSubjectExpertise> subjectExpertises = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<LabSession> labSessions = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<LectureSchedule> lectureSchedules = new ArrayList<>();
}
