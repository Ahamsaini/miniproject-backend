package com.mainApp.responcedto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentSubjectEnrollmentResponse {
    private String enrollmentId;
    private String studentId;
    private String studentName;
    private String subjectId;
    private String subjectCode;
    private String subjectName;
    private Integer semester;
    private String academicYear;
    private LocalDate enrollmentDate;
    private String status;
}
