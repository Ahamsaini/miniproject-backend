package com.mainApp.responcedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceStats {
    private String studentId;
    private String firstName;
    private String lastName;
    private String rollNumber;
    private Long attendedSessionsCount;
    private Double attendancePercentage;
}
