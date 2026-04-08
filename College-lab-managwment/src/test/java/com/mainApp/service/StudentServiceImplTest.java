package com.mainApp.service;

import com.mainApp.service.serviceImp.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.StudentMapper;
import com.mainApp.model.Student;
import com.mainApp.repository.StudentRepository;
import com.mainApp.responcedto.StudentResponse;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentResponse studentResponse;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId("s1");
        student.setUsername("johndoe");

        studentResponse = new StudentResponse();
        studentResponse.setId("s1");
        studentResponse.setUsername("johndoe");
    }

    @Test
    void getStudentById_Success() {
        when(studentRepository.findById("s1")).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        StudentResponse result = studentService.getStudentById("s1");

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void getStudentById_NotFound() {
        when(studentRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById("invalid"));
    }
}
