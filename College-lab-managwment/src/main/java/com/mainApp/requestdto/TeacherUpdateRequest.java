package com.mainApp.requestdto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherUpdateRequest extends UserUpdateRequest {
    private String employeeId;
    private String qualification;
    private String department;
    private String designation;
    private Integer yearsOfExperience;
    private String officeRoom;
    private String officeHours;
    private String specialization;
}
