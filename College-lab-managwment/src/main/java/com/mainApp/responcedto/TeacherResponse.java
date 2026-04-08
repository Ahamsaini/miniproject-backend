package com.mainApp.responcedto;

import java.util.List;
import lombok.Data;

@Data
public class TeacherResponse {

    private String id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String designation;
    private String qualification;
    private String fullName;
    private Boolean isActive;
    private Boolean isApproved;

    private List<TeacherExpertiseResponse> subjectExpertises;
}
