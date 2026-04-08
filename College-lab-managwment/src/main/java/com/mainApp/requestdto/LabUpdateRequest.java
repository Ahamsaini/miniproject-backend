package com.mainApp.requestdto;

import com.mainApp.roles.LabType;
import lombok.Data;

@Data
public class LabUpdateRequest {
    private String labName;
    private String roomNumber;
    private Integer floor;
    private String building;
    private Integer capacity;
    private Integer availablePcs;
    private String equipmentDetails; // JSON
    private String softwareInstalled; // JSON
    private Boolean isAirConditioned;
    private Boolean hasProjector;
    private String internetSpeed;
    private String maintenanceSchedule; // JSON
    private LabType labType;
}
