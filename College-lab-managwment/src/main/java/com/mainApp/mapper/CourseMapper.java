package com.mainApp.mapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.mainApp.model.Course;
import com.mainApp.model.Subject;
import com.mainApp.model.Student;
import com.mainApp.requestdto.CourseCreateRequest;
import com.mainApp.requestdto.CourseUpdateRequest;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.SubjectResponse;
import com.mainApp.responcedto.StudentResponse;

import com.mainApp.roles.CourseStatus;

@Component
public class CourseMapper {

    public Course toEntity(CourseCreateRequest request) {
        if (request == null)
            return null;
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setDurationYears(request.getDurationYears());
        course.setTotalSemesters(request.getTotalSemesters());
        course.setDepartment(request.getDepartment());
        course.setTotalCredits(request.getTotalCredits());
        course.setEligibilityCriteria(request.getEligibilityCriteria());
        course.setFeeStructure(request.getFeeStructure());
        course.setIsActive(true);
        course.setStatus(CourseStatus.ACTIVE);
        return course;
    }

    public CourseResponse toResponse(Course course) {
        if (course == null)
            return null;
        CourseResponse res = new CourseResponse();
        res.setId(course.getId());
        res.setCourseCode(course.getCourseCode());
        res.setCourseName(course.getCourseName());
        res.setDescription(course.getDescription());
        res.setDurationYears(course.getDurationYears());
        res.setTotalSemesters(course.getTotalSemesters());
        res.setDepartment(course.getDepartment());
        res.setTotalCredits(course.getTotalCredits());
        res.setCreatedAt(course.getCreatedAt());
        if (course.getStatus() != null) {
            res.setStatus(course.getStatus().name());
        }

        // Manual list mapping to break recursion
        res.setSubjects(mapSubjectsManually(course.getSubjects()));
        res.setEnrolledStudents(mapStudentsManually(course.getStudents()));

        return res;
    }

    public List<CourseResponse> toResponseList(List<Course> courses) {
        if (courses == null)
            return Collections.emptyList();
        List<CourseResponse> list = new ArrayList<>();
        for (Course c : courses) {
            list.add(toResponse(c));
        }
        return list;
    }

    private List<SubjectResponse> mapSubjectsManually(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty())
            return Collections.emptyList();
        List<SubjectResponse> list = new ArrayList<>();
        for (Subject s : subjects) {
            SubjectResponse res = new SubjectResponse();
            res.setId(s.getId());
            res.setSubjectCode(s.getSubjectCode());
            res.setSubjectName(s.getSubjectName());
            res.setDescription(s.getDescription());
            res.setSemesterNumber(s.getSemesterNumber());
            res.setTheoryHours(s.getTheoryHours());
            res.setLabHours(s.getLabHours());
            res.setTotalCredits(s.getTotalCredits());
            res.setPrerequisites(s.getPrerequisites());
            res.setSyllabus(s.getSyllabus());
            res.setReferenceBooks(s.getReferenceBooks());
            list.add(res);
        }
        return list;
    }

    private List<StudentResponse> mapStudentsManually(List<Student> students) {
        if (students == null || students.isEmpty())
            return Collections.emptyList();
        List<StudentResponse> list = new ArrayList<>();
        for (Student s : students) {
            StudentResponse res = new StudentResponse();
            res.setId(s.getId());
            res.setUsername(s.getUsername());
            res.setEmail(s.getEmail());
            res.setFullName(s.getFirstName() + " " + s.getLastName());
            res.setRollNumber(s.getRollNumber());
            res.setRegistrationNumber(s.getRegistrationNumber());
            res.setAcademicYear(s.getAcademicYear());
            res.setCurrentSemester(s.getCurrentSemester());
            res.setSection(s.getSection());
            res.setBatch(s.getBatch());
            res.setIsActive(s.getIsActive());
            list.add(res);
        }
        return list;
    }

    public void updateEntity(CourseUpdateRequest request, Course course) {
        if (request == null)
            return;
        if (request.getCourseName() != null)
            course.setCourseName(request.getCourseName());
        if (request.getDescription() != null)
            course.setDescription(request.getDescription());
        if (request.getDurationYears() != null)
            course.setDurationYears(request.getDurationYears());
        if (request.getTotalSemesters() != null)
            course.setTotalSemesters(request.getTotalSemesters());
        if (request.getDepartment() != null)
            course.setDepartment(request.getDepartment());
        if (request.getTotalCredits() != null)
            course.setTotalCredits(request.getTotalCredits());
        if (request.getEligibilityCriteria() != null)
            course.setEligibilityCriteria(request.getEligibilityCriteria());
        if (request.getFeeStructure() != null)
            course.setFeeStructure(request.getFeeStructure());
        if (request.getStatus() != null)
            course.setStatus(request.getStatus());
    }

    public Integer mapStudentCount(List<Student> students) {
        return students != null ? students.size() : 0;
    }
}