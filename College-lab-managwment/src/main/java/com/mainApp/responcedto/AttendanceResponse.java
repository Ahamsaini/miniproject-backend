package com.mainApp.responcedto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AttendanceResponse {
    private String id;
    private StudentResponse student;
    private LabSessionResponse labSession;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String pcNumber;
    private String status;
    private Boolean isLate;
    private Integer lateMinutes;
    private Boolean isEarlyExit;
    private Integer earlyExitMinutes;
    private String remarks;
    private String verifiedBy;
    private LocalDateTime verifiedAt;
}