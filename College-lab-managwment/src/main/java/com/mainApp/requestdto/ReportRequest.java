package com.mainApp.requestdto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReportRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reportType;
    private String format; // PDF, EXCEL
}
