package com.mainApp.service.serviceInterface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mainApp.requestdto.LoginRequest;
import com.mainApp.requestdto.PasswordChangeRequest;
import com.mainApp.requestdto.PasswordResetRequest;
import com.mainApp.requestdto.UserCreateRequest;
import com.mainApp.requestdto.UserUpdateRequest;
import com.mainApp.responcedto.AuthResponse;
import com.mainApp.responcedto.UserResponse;

public interface UserService {
    // Authentication
    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String token);

    void forgotPassword(String email);

    void resetPassword(PasswordResetRequest request);
    UserResponse getCurrentUser();

    // User Management
    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(String id);

    UserResponse getUserByUsername(String username);

    UserResponse getUserByEmail(String email);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(String id, UserUpdateRequest request);

    void deleteUser(String id);

    void changePassword(String userId, PasswordChangeRequest request);

    void updateUserStatus(String id, boolean isActive);

    void updateLastLogin(String userId);

    // Search & Filter
    Page<UserResponse> searchUsers(String keyword, Pageable pageable);

    List<UserResponse> getUsersByDepartment(String department);

    Page<UserResponse> getUsersByRole(String role, Pageable pageable);
}
