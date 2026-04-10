package com.mainApp.service.serviceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mainApp.config.JwtTokenProvider;
import com.mainApp.exception.AuthenticationException;
import com.mainApp.exception.ResourceAlreadyExistsException;
import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.mapper.UserMapper;
import com.mainApp.model.Admin;
import com.mainApp.model.Course;
import com.mainApp.model.Student;
import com.mainApp.model.Teacher;
import com.mainApp.model.User;
import com.mainApp.repository.AdminRepository;
import com.mainApp.repository.CourseRepository;
import com.mainApp.repository.StudentRepository;
import com.mainApp.repository.TeacherRepository;
import com.mainApp.repository.UserRepository;
import com.mainApp.requestdto.LoginRequest;
import com.mainApp.requestdto.PasswordChangeRequest;
import com.mainApp.requestdto.PasswordResetRequest;
import com.mainApp.requestdto.UserCreateRequest;
import com.mainApp.requestdto.UserUpdateRequest;
import com.mainApp.responcedto.AuthResponse;
import com.mainApp.responcedto.UserResponse;
import com.mainApp.roles.UserRole;
import com.mainApp.service.serviceInterface.UserService;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    // TeacherMapper removed as it was unused

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details from repository using username from authentication
            String username = authentication.getName();
            User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found after authentication"));

            // Check if account is approved (Exempt ADMIN role)
            if (!UserRole.ADMIN.equals(user.getRole()) && !Boolean.TRUE.equals(user.getIsApproved())) {
                log.warn("Login blocked: User {} (Role: {}) is not approved", user.getEmail(), user.getRole());
                throw new AuthenticationException(
                        "Your account is pending approval by an administrator. Please try again after your registration is approved.");
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            // Generate tokens
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            // Map to response
            UserResponse userResponse = mapUserToResponse(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(userResponse)
                    .expiresIn(jwtTokenProvider.getAccessTokenValidity())
                    .build();

        } catch (org.springframework.security.core.AuthenticationException e) {
            // Handle failed login attempts ONLY for real authentication failures
            userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getEmail(), request.getEmail())
                    .ifPresent(user -> {
                        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                        if (user.getFailedLoginAttempts() >= 5) {
                            user.setAccountLocked(true);
                            log.warn("User {} account locked due to multiple failed login attempts",
                                    user.getUsername());
                        }
                        userRepository.save(user);
                    });

            throw new com.mainApp.exception.AuthenticationException("Invalid credentials: " + e.getMessage());
        } catch (Exception e) {
            log.error("Login unexpected error", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getIsActive() || user.getAccountLocked()) {
            throw new AuthenticationException("User account is inactive or locked");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        UserResponse userResponse = mapUserToResponse(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(userResponse)
                .expiresIn(jwtTokenProvider.getAccessTokenValidity())
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        // In a real application, you might want to blacklist the token
        // For now, we just clear the security context
        SecurityContextHolder.clearContext();
        log.info("User logged out");
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Generate reset token (in real app, send email)
        // String resetToken = UUID.randomUUID().toString();
        // Save reset token to user (you'd need a field for this)
        // user.setResetToken(resetToken);
        // user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // In production: Send email with reset link
        log.info("Password reset token generated for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        // Validate reset token (you'd need to implement this)
        // User user = userRepository.findByResetToken(request.getResetToken())
        // .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

        // if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
        // throw new InvalidTokenException("Reset token has expired");
        // }

        // if (!request.getNewPassword().equals(request.getConfirmPassword())) {
        // throw new ValidationException("Passwords do not match");
        // }

        // user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        // user.setResetToken(null);
        // user.setResetTokenExpiry(null);

        // userRepository.save(user);
        // log.info("Password reset successfully for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Use email as username if not provided
        String effectiveUsername = (request.getUsername() == null || request.getUsername().isBlank())
                ? request.getEmail()
                : request.getUsername();

        // Check if username already exists
        if (userRepository.existsByUsername(effectiveUsername)) {
            throw new ResourceAlreadyExistsException("Username already exists: " + effectiveUsername);
        }

        User user;

        // Create user based on role
        switch (request.getRole().toUpperCase()) {
            case "STUDENT":
                Student student = new Student();
                student.setUsername(effectiveUsername);
                student.setEmail(request.getEmail());
                student.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                student.setFirstName(request.getFirstName());
                student.setLastName(request.getLastName());
                student.setPhoneNumber(request.getPhoneNumber());
                student.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
                student.setEmailVerified(false);
                student.setAccountLocked(false);
                student.setFailedLoginAttempts(0);
                student.setIsActive(false);


                // New registration fields
                student.setRollNumber(request.getRollNumber());
                student.setRegistrationNumber(request.getRegistrationNumber());
                student.setBatch(request.getBatch());
                student.setSection(request.getSection());
                student.setAcademicYear(request.getAcademicYear());
                student.setCurrentSemester(request.getCurrentSemester());
                student.setIsApproved(false); // Students need approval

                // Map course if provided and validate
                if (request.getCourseCode() != null && request.getCourseName() != null) {
                    Course course = courseRepository.findByCourseCodeAndCourseName(
                            request.getCourseCode(), request.getCourseName())
                            .orElseThrow(() -> new ValidationException(
                                    "Invalid Course: Course Code and Name do not match our records."));
                    student.setCourse(course);
                }

                student = studentRepository.save(student);
                user = student;
                break;

            case "TEACHER":
                Teacher teacher = new Teacher();
                teacher.setUsername(effectiveUsername);
                teacher.setEmail(request.getEmail());
                teacher.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                teacher.setFirstName(request.getFirstName());
                teacher.setLastName(request.getLastName());
                teacher.setPhoneNumber(request.getPhoneNumber());
                teacher.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
                teacher.setDepartment(request.getDepartment());
                teacher.setDesignation(request.getDesignation());
                teacher.setEmailVerified(false);
                teacher.setAccountLocked(false);
                teacher.setFailedLoginAttempts(0);
                teacher.setIsActive(false);

                teacher.setIsApproved(false); // Teachers also need approval now

                teacher = teacherRepository.save(teacher);
                user = teacher;
                break;

            case "ADMIN":
                Admin admin = new Admin();
                admin.setUsername(effectiveUsername);
                admin.setEmail(request.getEmail());
                admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                admin.setFirstName(request.getFirstName());
                admin.setLastName(request.getLastName());
                admin.setPhoneNumber(request.getPhoneNumber());
                admin.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
                admin.setDepartment(request.getDepartment());
                admin.setDesignation(request.getDesignation());
                admin.setEmailVerified(false);
                admin.setAccountLocked(false);
                admin.setFailedLoginAttempts(0);
                admin.setIsActive(true);
                admin.setIsApproved(true);

                admin = adminRepository.save(admin);
                user = admin;
                break;

            default:
                throw new ValidationException("Invalid user role: " + request.getRole());
        }

        log.info("User created successfully: {}", user.getUsername());
        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update fields if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
            user.setEmailVerified(false); // Require email verification
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

        // Update role-specific fields
        if (user instanceof Teacher teacher && request.getDepartment() != null) {
            teacher.setDepartment(request.getDepartment());
        }
        if (user instanceof Teacher teacher && request.getDesignation() != null) {
            teacher.setDesignation(request.getDesignation());
        }
        if (user instanceof Admin admin && request.getDepartment() != null) {
            admin.setDepartment(request.getDepartment());
        }
        if (user instanceof Admin admin && request.getDesignation() != null) {
            admin.setDesignation(request.getDesignation());
        }

        user = userRepository.save(user);
        log.info("User updated successfully: {}", user.getUsername());

        return mapUserToResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Soft delete - set inactive
        user.setIsActive(false);
        userRepository.save(user);

        log.info("User deactivated: {}", user.getUsername());
    }

    // @Override
    // @Transactional
    // public void changePassword(String userId, PasswordChangeRequest request) {
    // User user = userRepository.findById(userId)
    // .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
    // userId));
    //
    // // Verify current password
    // if (!passwordEncoder.matches(request.getCurrentPassword(),
    // user.getPasswordHash())) {
    // throw new ValidationException("Current password is incorrect");
    // }
    //
    // // Check if new password matches confirmation
    // if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
    // throw new ValidationException("New passwords do not match");
    // }
    //
    // // Update password
    // user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    // userRepository.save(user);
    //
    // log.info("Password changed successfully for user: {}", user.getUsername());
    // }

    @Override
    @Transactional
    public void updateUserStatus(String id, boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setIsActive(isActive);
        userRepository.save(user);

        log.info("User status updated: {} -> active: {}", user.getUsername(), isActive);
    }

    @Override
    @Transactional
    public void updateLastLogin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByDepartment(String department) {
        List<User> users = new java.util.ArrayList<>();
        users.addAll(teacherRepository.findByDepartment(department));
        users.addAll(adminRepository.findByDepartment(department));

        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    // Helper method to map User to UserResponse based on type
    private UserResponse mapUserToResponse(User user) {
        if (user instanceof Student student) {
            return studentMapper.toResponse(student);
        } else if (user instanceof Admin admin) {
            return userMapper.adminToResponse(admin);
        } else {
            return userMapper.toResponse(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String keyword, org.springframework.data.domain.Pageable pageable) {
        String sanitizedKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : "%" + keyword.trim().toLowerCase() + "%";
        return userRepository.searchUsers(sanitizedKeyword, pageable)
                .map(this::mapUserToResponse);
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return mapUserToResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(String userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ValidationException("New passwords do not match");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", user.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByRole(String role, Pageable pageable) {
        return userRepository.findByRole(UserRole.valueOf(role.toUpperCase()), pageable)
                .map(this::mapUserToResponse);
    }

}
