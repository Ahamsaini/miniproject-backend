package com.mainApp.controller;

import com.mainApp.requestdto.LoginRequest;
import com.mainApp.requestdto.PasswordResetRequest;
import com.mainApp.responcedto.AuthResponse;
import com.mainApp.service.serviceInterface.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from College Lab Management API!");
    }

    @GetMapping("/me")
    public ResponseEntity<com.mainApp.responcedto.UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/register")
    public ResponseEntity<com.mainApp.responcedto.UserResponse> register(
            @RequestBody @Valid com.mainApp.requestdto.UserCreateRequest request) {
        return new ResponseEntity<>(userService.createUser(request), org.springframework.http.HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        // Typically the token comes as "Bearer <token>", so we might need to strip
        // prefix if service expects raw token
        // But userService.logout(token) signature usually expects the raw header or
        // token.
        // Based on typical implementations, checking if "Bearer " exists is good
        // practice.
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
