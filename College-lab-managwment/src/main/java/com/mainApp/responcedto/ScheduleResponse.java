package com.mainApp.responcedto;

import lombok.Data;
import java.util.List;

@Data
public class ScheduleResponse {
    private String teacherId;
    private String teacherName;
    private List<LabSessionResponse> sessions;
    private List<TimeSlotResponse> availableSlots;
}
