package com.mainApp.mapper;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mainApp.model.LabAllocation;
import com.mainApp.responcedto.LabAllocationResponse;

@Component
public class LabAllocationMapper {

    public LabAllocationResponse toResponse(LabAllocation allocation) {
        if (allocation == null) {
            return null;
        }

        LabAllocationResponse response = new LabAllocationResponse();
        // LabAllocationResponse is currently empty in the codebase
        return response;
    }

    public List<LabAllocationResponse> toResponseList(List<LabAllocation> allocations) {
        if (allocations == null) {
            return Collections.emptyList();
        }
        return allocations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

