package com.mainApp.controller;

import com.mainApp.responcedto.DashboardStatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        System.out.println(">>> [LOG] Stats Request Received (MOCK) <<<");
        DashboardStatsResponse mockResponse = DashboardStatsResponse.builder()
                .totalCourses(10)
                .totalStudents(100)
                .totalSessions(5)
                .activeSessions(2)
                .absentToday(3)
                .sessionData(new ArrayList<>())
                .userDistribution(new ArrayList<>())
                .build();
        return ResponseEntity.ok(mockResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Dashboard Controller is reachable");
    }
}
