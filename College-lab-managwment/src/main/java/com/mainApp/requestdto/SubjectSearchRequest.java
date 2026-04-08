package com.mainApp.requestdto;

import lombok.Data;

@Data
public class SubjectSearchRequest {
    private String subjectName;
    private String subjectCode;
    private Long courseId;
    private Integer semesterNumber;
}
