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
import com.mainApp.mapper.CourseMapper;
import com.mainApp.model.Course;
import com.mainApp.repository.CourseRepository;
import com.mainApp.responcedto.CourseResponse;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId("c1");
        course.setCourseCode("BTECH");
        course.setCourseName("Bachelor of Technology");

        courseResponse = new CourseResponse();
        courseResponse.setId("c1");
        courseResponse.setCourseCode("BTECH");
    }

    @Test
    void getCourseById_Success() {
        when(courseRepository.findById("c1")).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.getCourseById("c1");

        assertNotNull(result);
        assertEquals("BTECH", result.getCourseCode());
        verify(courseRepository, times(1)).findById("c1");
    }

    @Test
    void getCourseById_NotFound() {
        when(courseRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById("invalid"));
    }
}
