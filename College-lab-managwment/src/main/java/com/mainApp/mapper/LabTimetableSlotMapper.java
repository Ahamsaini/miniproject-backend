package com.mainApp.mapper;

import com.mainApp.model.*;
import com.mainApp.requestdto.LabTimetableSlotRequest;
import com.mainApp.responcedto.LabTimetableSlotResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LabTimetableSlotMapper {

    public LabTimetableSlotResponse toResponse(LabTimetableSlot slot) {
        if (slot == null)
            return null;

        LabTimetableSlotResponse response = new LabTimetableSlotResponse();
        response.setId(slot.getId());
        response.setCourseId(slot.getCourse().getId());
        response.setCourseName(slot.getCourse().getCourseName());
        if (slot.getSubject() != null) {
            response.setSubjectId(slot.getSubject().getId());
            response.setSubjectName(slot.getSubject().getSubjectName());
            response.setSubjectCode(slot.getSubject().getSubjectCode());
        }

        if (slot.getLab() != null) {
            response.setLabId(slot.getLab().getId());
            response.setLabName(slot.getLab().getLabName());
            response.setLabCode(slot.getLab().getLabCode());
        }

        if (slot.getTeacher() != null) {
            response.setTeacherId(slot.getTeacher().getId());
            response.setTeacherName(slot.getTeacher().getFirstName() + " " + slot.getTeacher().getLastName());
        }
        response.setSemester(slot.getSemester());
        response.setAcademicYear(slot.getAcademicYear());
        response.setSection(slot.getSection());
        response.setDayOfWeek(slot.getDayOfWeek());
        response.setStartTime(slot.getStartTime());
        response.setEndTime(slot.getEndTime());
        response.setIsRecurring(slot.getIsRecurring());
        response.setRecurrencePattern(slot.getRecurrencePattern());
        response.setNotes(slot.getNotes());

        return response;
    }

    public List<LabTimetableSlotResponse> toResponseList(List<LabTimetableSlot> slots) {
        if (slots == null || slots.isEmpty())
            return Collections.emptyList();

        List<LabTimetableSlotResponse> list = new ArrayList<>();
        for (LabTimetableSlot slot : slots) {
            list.add(toResponse(slot));
        }
        return list;
    }

    public LabTimetableSlot toEntity(LabTimetableSlotRequest request,
            Course course, Subject subject,
            Lab lab, Teacher teacher) {
        if (request == null)
            return null;

        LabTimetableSlot slot = new LabTimetableSlot();
        slot.setCourse(course);
        slot.setSubject(subject);
        slot.setLab(lab);
        slot.setTeacher(teacher);
        slot.setSemester(request.getSemester());
        slot.setAcademicYear(request.getAcademicYear());
        slot.setSection(request.getSection());
        slot.setDayOfWeek(request.getDayOfWeek());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setIsRecurring(request.getIsRecurring());
        slot.setRecurrencePattern(request.getRecurrencePattern());
        slot.setNotes(request.getNotes());

        return slot;
    }

    public void updateEntity(LabTimetableSlotRequest request, LabTimetableSlot slot,
            Course course, Subject subject, Lab lab, Teacher teacher) {
        if (request == null || slot == null)
            return;

        slot.setCourse(course);
        slot.setSubject(subject);
        slot.setLab(lab);
        slot.setTeacher(teacher);
        slot.setSemester(request.getSemester());
        slot.setAcademicYear(request.getAcademicYear());
        slot.setSection(request.getSection());
        slot.setDayOfWeek(request.getDayOfWeek());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setIsRecurring(request.getIsRecurring());
        slot.setRecurrencePattern(request.getRecurrencePattern());
        slot.setNotes(request.getNotes());
    }
}
