package com.mainApp.service.serviceImp;

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
import com.mainApp.mapper.SubjectMapper;
import com.mainApp.model.Subject;
import com.mainApp.repository.SubjectRepository;
import com.mainApp.responcedto.SubjectResponse;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private Subject subject;
    private SubjectResponse subjectResponse;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId("sub1");
        subject.setSubjectCode("CS101");
        subject.setSubjectName("Computer Science");

        subjectResponse = new SubjectResponse();
        subjectResponse.setId("sub1");
        subjectResponse.setSubjectCode("CS101");
        subjectResponse.setSubjectName("Computer Science");
    }

    @Test
    void getSubjectById_Success() {
        when(subjectRepository.findById("sub1")).thenReturn(Optional.of(subject));
        when(subjectMapper.toResponse(subject)).thenReturn(subjectResponse);

        SubjectResponse result = subjectService.getSubjectById("sub1");

        assertNotNull(result);
        assertEquals("CS101", result.getSubjectCode());
        verify(subjectRepository, times(1)).findById("sub1");
    }

    @Test
    void getSubjectById_NotFound() {
        when(subjectRepository.findById("sub2")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.getSubjectById("sub2"));
        verify(subjectRepository, times(1)).findById("sub2");
    }
}
