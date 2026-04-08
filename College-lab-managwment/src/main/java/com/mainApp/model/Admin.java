package com.mainApp.model;



import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    
    @Column(name = "employee_id", unique = true, length = 20)
    private String employeeId;
    
    @Column(name = "department", length = 100)
    private String department;
    
    @Column(name = "designation", length = 50)
    private String designation;
    
    @Column(name = "admin_level")
    private Integer adminLevel; // 1-Super Admin, 2-Admin, 3-Support
    
    @Column(name = "permission_level", length = 20)
    private String permissionLevel;
}