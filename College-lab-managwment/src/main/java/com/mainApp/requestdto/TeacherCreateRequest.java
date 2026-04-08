package com.mainApp.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherCreateRequest extends UserCreateRequest {
    @NotBlank
    private String employeeId;
    private String qualification;
    private String department;
    private String designation;
    @Min(0)
    private Integer yearsOfExperience;
    private String officeRoom;
    private String officeHours;
    private String specialization;
}
