package com.mainApp.mapper;

import com.mainApp.model.Admin;
import com.mainApp.model.Student;
import com.mainApp.model.Teacher;
import com.mainApp.model.User;
import com.mainApp.requestdto.UserUpdateRequest;
import com.mainApp.responcedto.UserResponse;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private StudentMapper studentMapper;

    // User to UserResponse
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(mapFullName(user));
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setIsActive(user.getIsActive());
        response.setIsApproved(user.getIsApproved());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());

        // Set role-specific fields
        if (user instanceof Admin admin) {
            response.setDepartment(admin.getDepartment());
            response.setDesignation(admin.getDesignation());
        } else if (user instanceof Teacher teacher) {
            response.setDepartment(teacher.getDepartment());
            response.setDesignation(teacher.getDesignation());
        }

        return response;
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Student to StudentResponse (delegating to studentMapper)
    public UserResponse studentToResponse(Student student) {
        return studentMapper.toResponse(student);
    }

    // Teacher to TeacherResponse
    public UserResponse teacherToResponse(Teacher teacher) {
        return toResponse(teacher);
    }

    // Admin to AdminResponse
    public UserResponse adminToResponse(Admin admin) {
        return toResponse(admin);
    }

    private String mapFullName(User user) {
        if (user == null) {
            return null;
        }
        return (user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : "");
    }

    // Update mappings
    public void updateUserFromRequest(UserUpdateRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }
    }

}

