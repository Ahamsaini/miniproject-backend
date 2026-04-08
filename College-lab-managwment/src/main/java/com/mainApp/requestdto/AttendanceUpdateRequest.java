package com.mainApp.requestdto;

import com.mainApp.roles.AttendanceStatus;
import lombok.Data;

@Data
public class AttendanceUpdateRequest {
    private AttendanceStatus status;
    private String pcNumber;
    private Boolean isLate;
    private Integer lateMinutes;
    private Boolean isEarlyExit;
    private Integer earlyExitMinutes;
    private String remarks;
}
