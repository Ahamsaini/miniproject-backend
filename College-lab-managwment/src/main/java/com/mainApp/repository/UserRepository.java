package com.mainApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mainApp.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    org.springframework.data.domain.Page<User> findByRole(com.mainApp.roles.UserRole role, Pageable pageable);

    List<User> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE :keyword OR " +
            "LOWER(u.email) LIKE :keyword OR " +
            "LOWER(u.firstName) LIKE :keyword OR " +
            "LOWER(u.lastName) LIKE :keyword")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") com.mainApp.roles.UserRole role);

    @Query("SELECT u FROM User u WHERE u.lastLogin IS NOT NULL ORDER BY u.lastLogin DESC LIMIT 10")
    List<User> findRecentlyActiveUsers();

    Page<User> findAll(Pageable pageable);

    Page<User> findByIsApproved(Boolean isApproved, Pageable pageable);

    Page<User> findByRoleAndIsApproved(com.mainApp.roles.UserRole role, Boolean isApproved, Pageable pageable);
}