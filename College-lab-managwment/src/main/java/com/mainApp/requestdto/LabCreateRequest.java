package com.mainApp.requestdto;

import com.mainApp.roles.LabType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LabCreateRequest {
    @NotBlank
    private String labCode;
    @NotBlank
    private String labName;
    private String roomNumber;
    private Integer floor;
    private String building;
    @Min(1)
    private Integer capacity;
    @Min(0)
    private Integer availablePcs;
    private String equipmentDetails; // JSON
    private String softwareInstalled; // JSON
    private Boolean isAirConditioned;
    private Boolean hasProjector;
    private String internetSpeed;
    private String maintenanceSchedule; // JSON
    @NotNull
    private LabType labType;
}
