package com.mainApp.responcedto;

import com.mainApp.roles.LabType;
import lombok.Data;
import java.util.List;

@Data
public class LabResponse {
    private String id;
    private String labCode;
    private String labName;
    private String roomNumber;
    private Integer floor;
    private String building;
    private Integer capacity;
    private Integer availablePcs;
    private String equipmentDetails;
    private String softwareInstalled;
    private Boolean isAirConditioned;
    private Boolean hasProjector;
    private String internetSpeed;
    private String maintenanceSchedule;
    private LabType labType;
    private Boolean isActive;
}
