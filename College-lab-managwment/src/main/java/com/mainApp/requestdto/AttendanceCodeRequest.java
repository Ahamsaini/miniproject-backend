package com.mainApp.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttendanceCodeRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String sessionId;
    @NotBlank
    private String code;
    private String type; // ENTRY or EXIT
    private String pcNumber;
}
