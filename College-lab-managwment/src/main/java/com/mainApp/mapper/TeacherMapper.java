package com.mainApp.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mainApp.model.Teacher;
import com.mainApp.model.TeacherSubjectExpertise;
import com.mainApp.requestdto.TeacherCreateRequest;
import com.mainApp.requestdto.TeacherUpdateRequest;
import com.mainApp.responcedto.TeacherExpertiseResponse;
import com.mainApp.responcedto.TeacherResponse;
import com.mainApp.roles.UserRole;

@Component
public class TeacherMapper {

        @Autowired
        protected PasswordEncoder passwordEncoder;

        public Teacher toEntity(TeacherCreateRequest request) {
                if (request == null) {
                        return null;
                }

                Teacher teacher = new Teacher();
                teacher.setUsername(request.getUsername());
                teacher.setEmail(request.getEmail());
                teacher.setFirstName(request.getFirstName());
                teacher.setLastName(request.getLastName());
                teacher.setPhoneNumber(request.getPhoneNumber());
                teacher.setDepartment(request.getDepartment());
                teacher.setDesignation(request.getDesignation());
                teacher.setQualification(request.getQualification());
                teacher.setYearsOfExperience(request.getYearsOfExperience());
                teacher.setOfficeRoom(request.getOfficeRoom());
                teacher.setOfficeHours(request.getOfficeHours());
                teacher.setSpecialization(request.getSpecialization());

                teacher.setRole(UserRole.TEACHER);
                if (request.getPassword() != null) {
                        teacher.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                }
                
                teacher.setEmailVerified(false);
                teacher.setAccountLocked(false);
                teacher.setFailedLoginAttempts(0);
                teacher.setIsActive(true);

                return teacher;
        }

        public TeacherResponse toResponse(Teacher teacher) {
                if (teacher == null) {
                        return null;
                }

                TeacherResponse response = new TeacherResponse();
                response.setId(teacher.getId());
                response.setEmail(teacher.getEmail());
                response.setFirstName(teacher.getFirstName());
                response.setLastName(teacher.getLastName());
                response.setFullName(teacher.getFirstName() + " " + teacher.getLastName());
                response.setIsActive(teacher.getIsActive());
                response.setIsApproved(teacher.getIsApproved());
                
                response.setEmployeeId(teacher.getEmployeeId());
                response.setDepartment(teacher.getDepartment());
                response.setDesignation(teacher.getDesignation());
                response.setQualification(teacher.getQualification());

                response.setSubjectExpertises(mapExpertises(teacher.getSubjectExpertises()));

                return response;
        }

        public void updateEntity(TeacherUpdateRequest request, Teacher teacher) {
                if (request == null || teacher == null) {
                        return;
                }

                if (request.getFirstName() != null) teacher.setFirstName(request.getFirstName());
                if (request.getLastName() != null) teacher.setLastName(request.getLastName());
                if (request.getPhoneNumber() != null) teacher.setPhoneNumber(request.getPhoneNumber());
                if (request.getDepartment() != null) teacher.setDepartment(request.getDepartment());
                if (request.getDesignation() != null) teacher.setDesignation(request.getDesignation());
                if (request.getQualification() != null) teacher.setQualification(request.getQualification());
                if (request.getYearsOfExperience() != null) teacher.setYearsOfExperience(request.getYearsOfExperience());
                if (request.getOfficeRoom() != null) teacher.setOfficeRoom(request.getOfficeRoom());
                if (request.getOfficeHours() != null) teacher.setOfficeHours(request.getOfficeHours());
                if (request.getSpecialization() != null) teacher.setSpecialization(request.getSpecialization());
                if (request.getIsActive() != null) teacher.setIsActive(request.getIsActive());
        }

        protected List<TeacherExpertiseResponse> mapExpertises(List<TeacherSubjectExpertise> expertises) {
                if (expertises == null || expertises.isEmpty()) {
                        return Collections.emptyList();
                }

                return expertises.stream()
                                .map(expertise -> {
                                        TeacherExpertiseResponse response = new TeacherExpertiseResponse();
                                        response.setId(expertise.getId());
                                        if (expertise.getSubject() != null) {
                                                response.setSubjectId(expertise.getSubject().getId());
                                                response.setSubjectCode(expertise.getSubject().getSubjectCode());
                                                response.setSubjectName(expertise.getSubject().getSubjectName());
                                        }
                                        response.setExperienceYears(expertise.getExperienceYears());
                                        response.setProficiencyLevel(expertise.getProficiencyLevel() != null ? expertise.getProficiencyLevel().name() : null);
                                        response.setIsPrimaryExpert(expertise.getIsPrimaryExpert());
                                        response.setAssignedDate(expertise.getAssignedDate());
                                        return response;
                                })
                                .collect(Collectors.toList());
        }
}

