package com.mainApp.service.serviceImp;

import com.mainApp.roles.UserRole;
import com.mainApp.repository.*;
import com.mainApp.responcedto.DashboardStatsResponse;
import com.mainApp.roles.LabSessionStatus;
import com.mainApp.service.serviceInterface.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

        private final CourseRepository courseRepository;
        private final StudentRepository studentRepository;
        private final LabSessionRepository labSessionRepository;
        private final UserRepository userRepository;
        private final TeacherRepository teacherRepository;
        private final AttendanceRepository attendanceRepository;

        @Override
        public DashboardStatsResponse getDashboardStats() {
                log.info(">>> [LOG] Generating Dashboard Statistics <<<");
                LocalDate today = LocalDate.now();

                long totalCourses = 0;
                long totalStudents = 0;
                long totalSessions = 0;
                long todaySessions = 0;
                long activeSessions = 0;
                long adminCount = 0;
                long teacherCount = 0;
                long absentTodayCount = 0;

                try {
                        totalCourses = courseRepository.count();
                } catch (Exception e) {
                        log.error("Error fetching totalCourses", e);
                }
                try {
                        totalStudents = studentRepository.count();
                } catch (Exception e) {
                        log.error("Error fetching totalStudents", e);
                }
                try {
                        totalSessions = labSessionRepository.count();
                } catch (Exception e) {
                        log.error("Error fetching totalSessions", e);
                }

                // Active and Today's Sessions
                try {
                        todaySessions = labSessionRepository.countBySessionDate(today);
                } catch (Exception e) {
                        log.error("Error fetching todaySessions", e);
                }
                try {
                        activeSessions = labSessionRepository.countByStatus(LabSessionStatus.ONGOING);
                } catch (Exception e) {
                        log.error("Error fetching activeSessions", e);
                }

                // Calculate Absent Today
                try {
                        absentTodayCount = attendanceRepository.countBySessionDateAndStatusNot(today,
                                        com.mainApp.roles.AttendanceStatus.PRESENT);
                        log.debug("Absent today count: {}", absentTodayCount);
                } catch (Exception e) {
                        log.error("Error calculating absentToday", e);
                }

                // 1. Session Data (Last 7 Days)
                List<Map<String, Object>> sessionData = new ArrayList<>();
                try {
                        LocalDate startDate = today.minusDays(6);
                        for (int i = 0; i < 7; i++) {
                                LocalDate date = startDate.plusDays(i);
                                long count = labSessionRepository.countBySessionDate(date);

                                Map<String, Object> dataPoint = new HashMap<>();
                                dataPoint.put("name",
                                                date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
                                dataPoint.put("sessions", count);
                                dataPoint.put("students", (long) (count * 20)); // Estimation for graph
                                sessionData.add(dataPoint);
                        }
                } catch (Exception e) {
                        log.error("Error fetching sessionData", e);
                }

                // 2. User Distribution
                try {
                        adminCount = userRepository.countByRole(UserRole.ADMIN);
                } catch (Exception e) {
                        log.error("Error fetching adminCount", e);
                }
                try {
                        teacherCount = teacherRepository.count();
                } catch (Exception e) {
                        log.error("Error fetching teacherCount", e);
                }

                List<Map<String, Object>> userDistribution = new ArrayList<>();
                userDistribution.add(Map.of("name", "Students", "value", totalStudents, "color", "#1976d2"));
                userDistribution.add(Map.of("name", "Teachers", "value", teacherCount, "color", "#42a5f5"));
                userDistribution.add(Map.of("name", "Admins", "value", adminCount, "color", "#90caf9"));

                log.info("Dashboard stats generated successfully: Courses={}, Students={}, ActiveSessions={}",
                                totalCourses, totalStudents, activeSessions);

                return DashboardStatsResponse.builder()
                                .totalCourses(totalCourses)
                                .totalStudents(totalStudents)
                                .totalSessions(totalSessions)
                                .todaySessions(todaySessions)
                                .activeSessions(activeSessions)
                                .absentToday(absentTodayCount)
                                .sessionData(sessionData)
                                .attendanceTrend(new ArrayList<>())
                                .userDistribution(userDistribution)
                                .build();
        }
}
