package com.mainApp.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mainApp.model.Attendance;
import com.mainApp.responcedto.AttendanceResponse;
import java.util.Collections;

@Component
public class AttendanceMapper {

    @Autowired
    private StudentMapper studentMapper;

    public AttendanceResponse toResponse(Attendance attendance) {
        if (attendance == null)
            return null;
        AttendanceResponse res = new AttendanceResponse();
        res.setId(attendance.getId());
        res.setEntryTime(attendance.getEntryTime());
        res.setExitTime(attendance.getExitTime());
        res.setPcNumber(attendance.getPcNumber());
        res.setStatus(attendance.getStatus() != null ? attendance.getStatus().name() : null);
        res.setIsLate(attendance.getIsLate());
        res.setLateMinutes(attendance.getLateMinutes());
        res.setIsEarlyExit(attendance.getIsEarlyExit());
        res.setEarlyExitMinutes(attendance.getEarlyExitMinutes());
        res.setRemarks(attendance.getRemarks());
        res.setVerifiedBy(attendance.getVerifiedBy());
        res.setVerifiedAt(attendance.getVerifiedAt());

        // Map basic info but avoid full recursion
        res.setStudent(studentMapper.toResponse(attendance.getStudent()));
        if (attendance.getLabSession() != null) {
            com.mainApp.responcedto.LabSessionResponse sessionRes = new com.mainApp.responcedto.LabSessionResponse();
            sessionRes.setId(attendance.getLabSession().getId());
            sessionRes.setSessionDate(attendance.getLabSession().getSessionDate());
            res.setLabSession(sessionRes);
        }

        return res;
    }

    public List<AttendanceResponse> toResponseList(List<Attendance> attendances) {
        if (attendances == null)
            return Collections.emptyList();
        return attendances.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
