package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import com.mainApp.roles.LabType;

@Data
@Entity
@Table(name = "labs")
public class Lab extends BaseEntity {

    @Column(name = "lab_code", nullable = false, unique = true, length = 20)
    private String labCode;

    @Column(name = "lab_name", nullable = false, length = 100)
    private String labName;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "building", length = 50)
    private String building;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "available_pcs")
    private Integer availablePcs;

    @Column(name = "equipment_details", columnDefinition = "JSON")
    private String equipmentDetails;

    @Column(name = "software_installed", columnDefinition = "JSON")
    private String softwareInstalled;

    @Column(name = "is_air_conditioned")
    private Boolean isAirConditioned = false;

    @Column(name = "has_projector")
    private Boolean hasProjector = false;

    @Column(name = "internet_speed")
    private String internetSpeed;

    @Column(name = "maintenance_schedule", columnDefinition = "JSON")
    private String maintenanceSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "lab_type")
    private LabType labType;

    // Relationships
    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<LabSession> labSessions = new ArrayList<>();
}
