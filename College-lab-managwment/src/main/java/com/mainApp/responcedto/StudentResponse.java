package com.mainApp.responcedto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
public class StudentResponse extends UserResponse {

	private String fullName;

	private String rollNumber;
	private String registrationNumber;
	private LocalDate dateOfBirth;
	private String gender;
	private String address;
	private String emergencyContact;
	private String guardianName;
	private String guardianContact;
	private String academicYear;
	private Integer currentSemester;
	private String section;
	private String batch;
	private String bloodGroup;

	private AttendanceSummaryResponse attendanceSummary;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private CourseResponse course;

	// Mapped via @Named("mapEnrolledSubjects")
	private List<SubjectResponse> enrolledSubjects;
}
