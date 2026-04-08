package com.mainApp.mapper;

import org.springframework.stereotype.Component;

import com.mainApp.model.Course;
import com.mainApp.model.Subject;
import com.mainApp.requestdto.SubjectCreateRequest;
import com.mainApp.requestdto.SubjectUpdateRequest;
import com.mainApp.responcedto.CourseResponse;
import com.mainApp.responcedto.SubjectResponse;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectCreateRequest request) {
        if (request == null)
            return null;
        Subject subject = new Subject();
        subject.setSubjectCode(request.getSubjectCode());
        subject.setSubjectName(request.getSubjectName());
        subject.setDescription(request.getDescription());
        subject.setSemesterNumber(request.getSemesterNumber());
        subject.setTheoryHours(request.getTheoryHours());
        subject.setLabHours(request.getLabHours());
        subject.setTotalCredits(request.getTotalCredits());
        subject.setPrerequisites(request.getPrerequisites());
        subject.setSyllabus(request.getSyllabus());
        subject.setReferenceBooks(request.getReferenceBooks());
        subject.setIsActive(true);
        return subject;
    }

    public SubjectResponse toResponse(Subject subject) {
        if (subject == null)
            return null;
        SubjectResponse response = new SubjectResponse();
        response.setId(subject.getId());
        response.setSubjectCode(subject.getSubjectCode());
        response.setSubjectName(subject.getSubjectName());
        response.setDescription(subject.getDescription());
        response.setSemesterNumber(subject.getSemesterNumber());
        response.setTheoryHours(subject.getTheoryHours());
        response.setLabHours(subject.getLabHours());
        response.setTotalCredits(subject.getTotalCredits());
        response.setPrerequisites(subject.getPrerequisites());
        response.setSyllabus(subject.getSyllabus());
        response.setReferenceBooks(subject.getReferenceBooks());

        response.setCourse(mapCourseSimple(subject.getCourse()));
        return response;
    }

    public CourseResponse mapCourseSimple(Course course) {
        if (course == null)
            return null;
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        return response;
    }

    public void updateEntity(SubjectUpdateRequest request, Subject subject) {
        if (request == null)
            return;
        if (request.getSubjectName() != null)
            subject.setSubjectName(request.getSubjectName());
        if (request.getDescription() != null)
            subject.setDescription(request.getDescription());
        if (request.getSemesterNumber() != null)
            subject.setSemesterNumber(request.getSemesterNumber());
        if (request.getTheoryHours() != null)
            subject.setTheoryHours(request.getTheoryHours());
        if (request.getLabHours() != null)
            subject.setLabHours(request.getLabHours());
        if (request.getTotalCredits() != null)
            subject.setTotalCredits(request.getTotalCredits());
        if (request.getPrerequisites() != null)
            subject.setPrerequisites(request.getPrerequisites());
        if (request.getSyllabus() != null)
            subject.setSyllabus(request.getSyllabus());
        if (request.getReferenceBooks() != null)
            subject.setReferenceBooks(request.getReferenceBooks());
    }
}
