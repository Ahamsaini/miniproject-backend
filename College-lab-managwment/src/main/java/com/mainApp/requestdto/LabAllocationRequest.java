package com.mainApp.requestdto;

import com.mainApp.roles.AllocationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LabAllocationRequest {
    @NotNull
    private String studentId;
    @NotNull
    private String labSessionId;
    @NotBlank
    private String pcNumber;
    private AllocationStatus status;
    private String allocationNotes;
}
