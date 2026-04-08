package com.mainApp.mapper;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mainApp.model.Lab;
import com.mainApp.requestdto.LabCreateRequest;
import com.mainApp.requestdto.LabUpdateRequest;
import com.mainApp.responcedto.LabResponse;

@Component
public class LabMapper {

    public Lab toEntity(LabCreateRequest request) {
        if (request == null) {
            return null;
        }

        Lab lab = new Lab();
        lab.setLabCode(request.getLabCode());
        lab.setLabName(request.getLabName());
        lab.setLabType(request.getLabType());
        lab.setCapacity(request.getCapacity());
        lab.setBuilding(request.getBuilding());
        lab.setFloor(request.getFloor());
        lab.setRoomNumber(request.getRoomNumber());
        lab.setAvailablePcs(request.getAvailablePcs());
        lab.setIsAirConditioned(request.getIsAirConditioned());
        lab.setHasProjector(request.getHasProjector());
        lab.setInternetSpeed(request.getInternetSpeed());
        lab.setSoftwareInstalled(request.getSoftwareInstalled());
        lab.setEquipmentDetails(request.getEquipmentDetails());
        lab.setMaintenanceSchedule(request.getMaintenanceSchedule());
        
        lab.setIsActive(true);
        return lab;
    }

    public LabResponse toResponse(Lab lab) {
        if (lab == null) {
            return null;
        }

        LabResponse response = new LabResponse();
        response.setId(lab.getId());
        response.setLabCode(lab.getLabCode());
        response.setLabName(lab.getLabName());
        response.setLabType(lab.getLabType());
        response.setCapacity(lab.getCapacity());
        response.setBuilding(lab.getBuilding());
        response.setFloor(lab.getFloor());
        response.setRoomNumber(lab.getRoomNumber());
        response.setAvailablePcs(lab.getAvailablePcs());
        response.setIsAirConditioned(lab.getIsAirConditioned());
        response.setHasProjector(lab.getHasProjector());
        response.setInternetSpeed(lab.getInternetSpeed());
        response.setSoftwareInstalled(lab.getSoftwareInstalled());
        response.setEquipmentDetails(lab.getEquipmentDetails());
        response.setMaintenanceSchedule(lab.getMaintenanceSchedule());
        response.setIsActive(lab.getIsActive());

        return response;
    }

    public List<LabResponse> toResponseList(List<Lab> labs) {
        if (labs == null) {
            return Collections.emptyList();
        }
        return labs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntity(LabUpdateRequest request, Lab lab) {
        if (request == null || lab == null) {
            return;
        }

        if (request.getLabName() != null) lab.setLabName(request.getLabName());
        if (request.getLabType() != null) lab.setLabType(request.getLabType());
        if (request.getCapacity() != null) lab.setCapacity(request.getCapacity());
        if (request.getBuilding() != null) lab.setBuilding(request.getBuilding());
        if (request.getFloor() != null) lab.setFloor(request.getFloor());
        if (request.getRoomNumber() != null) lab.setRoomNumber(request.getRoomNumber());
        if (request.getAvailablePcs() != null) lab.setAvailablePcs(request.getAvailablePcs());
        if (request.getIsAirConditioned() != null) lab.setIsAirConditioned(request.getIsAirConditioned());
        if (request.getHasProjector() != null) lab.setHasProjector(request.getHasProjector());
        if (request.getInternetSpeed() != null) lab.setInternetSpeed(request.getInternetSpeed());
        if (request.getSoftwareInstalled() != null) lab.setSoftwareInstalled(request.getSoftwareInstalled());
        if (request.getEquipmentDetails() != null) lab.setEquipmentDetails(request.getEquipmentDetails());
        if (request.getMaintenanceSchedule() != null) lab.setMaintenanceSchedule(request.getMaintenanceSchedule());
    }
}

