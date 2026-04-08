package com.mainApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mainApp.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

    java.util.List<Admin> findByDepartment(String department);
}
