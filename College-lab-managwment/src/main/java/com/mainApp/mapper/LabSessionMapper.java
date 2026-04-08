package com.mainApp.mapper;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mainApp.model.LabSession;
import com.mainApp.requestdto.LabSessionCreateRequest;
import com.mainApp.requestdto.LabSessionUpdateRequest;
import com.mainApp.responcedto.LabSessionResponse;
import com.mainApp.roles.LabSessionStatus;

@Component
public class LabSessionMapper {

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    public LabSession toEntity(LabSessionCreateRequest request) {
        if (request == null) {
            return null;
        }

        LabSession session = new LabSession();
        session.setSessionDate(request.getSessionDate());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setSection(request.getSection());
        session.setSessionTopic(request.getSessionTopic());
        session.setExperimentName(request.getExperimentName());
        session.setObjectives(request.getObjectives());
        session.setMaterialsRequired(request.getMaterialsRequired());
        
        session.setIsActive(true);
        session.setStatus(LabSessionStatus.SCHEDULED);
        session.setIsCodeGenerated(false);
        session.setAttendanceMarked(false);

        return session;
    }

    public LabSessionResponse toResponse(LabSession session) {
        if (session == null) {
            return null;
        }

        LabSessionResponse response = new LabSessionResponse();
        response.setId(session.getId());
        response.setSessionDate(session.getSessionDate());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setSection(session.getSection());
        response.setSessionTopic(session.getSessionTopic());
        response.setExperimentName(session.getExperimentName());
        response.setStatus(session.getStatus() != null ? session.getStatus().name() : null);
        response.setIsCodeGenerated(session.getIsCodeGenerated());

        // Relationships
        if (session.getLab() != null) {
            response.setLab(labMapper.toResponse(session.getLab()));
        }
        if (session.getSubject() != null) {
            response.setSubject(subjectMapper.toResponse(session.getSubject()));
        }
        if (session.getTeacher() != null) {
            response.setTeacher(teacherMapper.toResponse(session.getTeacher()));
        }
        
        // Calculated fields
        response.setPresentCount(calculatePresent(session));
        response.setAbsentCount(calculateAbsent(session));
        response.setTotalStudents(calculateTotal(session));

        return response;
    }

    public List<LabSessionResponse> toResponseList(List<LabSession> sessions) {
        if (sessions == null) {
            return Collections.emptyList();
        }
        return sessions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntity(LabSessionUpdateRequest request, LabSession session) {
        if (request == null || session == null) {
            return;
        }

        if (request.getSessionDate() != null) session.setSessionDate(request.getSessionDate());
        if (request.getStartTime() != null) session.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) session.setEndTime(request.getEndTime());
        if (request.getSection() != null) session.setSection(request.getSection());
        if (request.getSessionTopic() != null) session.setSessionTopic(request.getSessionTopic());
        if (request.getExperimentName() != null) session.setExperimentName(request.getExperimentName());
        if (request.getObjectives() != null) session.setObjectives(request.getObjectives());
        if (request.getMaterialsRequired() != null) session.setMaterialsRequired(request.getMaterialsRequired());
        if (request.getStatus() != null) {
            session.setStatus(request.getStatus());
        }
    }

    public Integer calculatePresent(LabSession session) {
        try {
            if (session.getAttendances() == null)
                return 0;
            return (int) session.getAttendances().stream()
                    .filter(a -> a.getStatus() == com.mainApp.roles.AttendanceStatus.PRESENT)
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer calculateAbsent(LabSession session) {
        try {
            if (session.getAttendances() == null)
                return 0;
            return (int) session.getAttendances().stream()
                    .filter(a -> a.getStatus() == null || a.getStatus() == com.mainApp.roles.AttendanceStatus.ABSENT)
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer calculateTotal(LabSession session) {
        try {
            if (session.getAttendances() == null)
                return 0;
            return session.getAttendances().size();
        } catch (Exception e) {
            return 0;
        }
    }
}


