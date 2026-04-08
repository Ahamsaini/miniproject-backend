package com.mainApp.requestdto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    @Email private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private Boolean isActive;
    private String department;
    private String designation;
}
