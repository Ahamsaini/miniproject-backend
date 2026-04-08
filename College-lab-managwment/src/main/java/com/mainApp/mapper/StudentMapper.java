package com.mainApp.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mainApp.model.Course;
import com.mainApp.model.Student;
import com.mainApp.model.StudentSubjectEnrollment;
import com.mainApp.requestdto.StudentCreateRequest;
import com.mainApp.requestdto.StudentUpdateRequest;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.StudentResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.roles.Gender;
import java.util.Collections;

import com.mainApp.roles.UserRole;

@Component
public class StudentMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public Student toEntity(StudentCreateRequest request) {
        if (request == null)
            return null;
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setEmail(request.getEmail());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setGender(stringToGender(request.getGender()));
        student.setRollNumber(request.getRollNumber());
        student.setRegistrationNumber(request.getRegistrationNumber());
        student.setAcademicYear(request.getAcademicYear());
        student.setCurrentSemester(request.getCurrentSemester());
        student.setSection(request.getSection());
        student.setBatch(request.getBatch());
        student.setAddress(request.getAddress());
        student.setEmergencyContact(request.getEmergencyContact());
        student.setGuardianName(request.getGuardianName());
        student.setGuardianContact(request.getGuardianContact());
        student.setBloodGroup(request.getBloodGroup());

        student.setRole(UserRole.STUDENT);
        student.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        student.setEmailVerified(false);
        student.setAccountLocked(false);
        student.setFailedLoginAttempts(0);
        student.setIsActive(true);
        return student;
    }

    public StudentResponse toResponse(Student student) {
        if (student == null)
            return null;
        StudentResponse res = new StudentResponse();
        res.setId(student.getId());
        res.setUsername(student.getUsername());
        res.setEmail(student.getEmail());
        res.setFirstName(student.getFirstName());
        res.setLastName(student.getLastName());
        res.setFullName(student.getFirstName() + " " + student.getLastName());
        res.setPhoneNumber(student.getPhoneNumber());
        res.setRole("STUDENT");
        res.setIsActive(student.getIsActive());
        res.setIsApproved(student.getIsApproved());
        res.setCreatedAt(student.getCreatedAt());
        res.setLastLogin(student.getLastLogin());

        res.setRollNumber(student.getRollNumber());
        res.setRegistrationNumber(student.getRegistrationNumber());
        res.setDateOfBirth(student.getDateOfBirth());
        res.setGender(student.getGender() != null ? student.getGender().name() : null);
        res.setAddress(student.getAddress());
        res.setEmergencyContact(student.getEmergencyContact());
        res.setGuardianName(student.getGuardianName());
        res.setGuardianContact(student.getGuardianContact());
        res.setAcademicYear(student.getAcademicYear());
        res.setCurrentSemester(student.getCurrentSemester());
        res.setSection(student.getSection());
        res.setBatch(student.getBatch());
        res.setBloodGroup(student.getBloodGroup());

        res.setCourse(mapCourse(student.getCourse()));
        res.setEnrolledSubjects(mapEnrolledSubjects(student.getSubjectEnrollments()));

        return res;
    }

    public void updateEntity(StudentUpdateRequest request, Student student) {
        if (request == null)
            return;
        if (request.getFirstName() != null)
            student.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            student.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null)
            student.setPhoneNumber(request.getPhoneNumber());
        if (request.getGender() != null)
            student.setGender(stringToGender(request.getGender()));
        if (request.getAddress() != null)
            student.setAddress(request.getAddress());
        if (request.getEmergencyContact() != null)
            student.setEmergencyContact(request.getEmergencyContact());
        if (request.getGuardianName() != null)
            student.setGuardianName(request.getGuardianName());
        if (request.getGuardianContact() != null)
            student.setGuardianContact(request.getGuardianContact());
        if (request.getRollNumber() != null)
            student.setRollNumber(request.getRollNumber());
        if (request.getRegistrationNumber() != null)
            student.setRegistrationNumber(request.getRegistrationNumber());
        if (request.getAcademicYear() != null)
            student.setAcademicYear(request.getAcademicYear());
        if (request.getCurrentSemester() != null)
            student.setCurrentSemester(request.getCurrentSemester());
        if (request.getSection() != null)
            student.setSection(request.getSection());
        if (request.getBatch() != null)
            student.setBatch(request.getBatch());
        if (request.getBloodGroup() != null)
            student.setBloodGroup(request.getBloodGroup());
    }

    private Gender stringToGender(String gender) {
        if (gender == null)
            return null;
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private CourseResponse mapCourse(Course course) {
        if (course == null)
            return null;
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        response.setDepartment(course.getDepartment());
        return response;
    }

    private List<SubjectResponse> mapEnrolledSubjects(List<StudentSubjectEnrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty())
            return Collections.emptyList();

        return enrollments.stream()
                .map(enrollment -> {
                    SubjectResponse response = new SubjectResponse();
                    response.setId(enrollment.getSubject().getId());
                    response.setSubjectCode(enrollment.getSubject().getSubjectCode());
                    response.setSubjectName(enrollment.getSubject().getSubjectName());
                    response.setSemesterNumber(enrollment.getSubject().getSemesterNumber());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
