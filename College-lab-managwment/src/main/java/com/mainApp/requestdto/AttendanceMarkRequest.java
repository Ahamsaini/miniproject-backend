package com.mainApp.requestdto;

import com.mainApp.roles.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceMarkRequest {
    @NotNull
    private String studentId;
    @NotNull
    private String labSessionId;
    @NotNull
    private AttendanceStatus status; // PRESENT, ABSENT, etc.
    private String pcNumber;
    private String remarks;
}
