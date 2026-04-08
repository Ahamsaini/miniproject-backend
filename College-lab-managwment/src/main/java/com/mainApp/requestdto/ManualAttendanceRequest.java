package com.mainApp.requestdto;

import jakarta.validation.constraints.NotBlank;
import com.mainApp.roles.AttendanceStatus;
import lombok.Data;

@Data
public class ManualAttendanceRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String sessionId;
    private AttendanceStatus status;
    private String remarks;
}
