package com.smartattendance.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    // Constructor injection
    public DashboardService(AttendanceService attendanceService, StudentService studentService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    // ==========================================================
    //                    TODAY COUNTS
    // ==========================================================

    // Present Today
    public int getTodayPresentCount() {
        return attendanceService.countPresentOn(LocalDate.now());
    }

    // Absent Today (total - present)
    public int getTodayAbsentCount() {
        int total = studentService.getStudentCount();
        int present = getTodayPresentCount();
        return Math.max(0, total - present);
    }

    // Late Today
    public int getTodayLateCount() {
        return attendanceService.countLateOn(LocalDate.now());
    }

    // ==========================================================
    //                    WEEKLY TREND  (Real Data)
    // ==========================================================

    // OLD = getPresentCountsLastNDays(7)
    // NEW = getWeeklyAttendance() → REAL DB DATA (8 weeks)
    public List<Integer> getWeeklyHeatmap() {
        return attendanceService.getWeeklyAttendance();
    }

    // ==========================================================
    //                    MONTHLY TREND  (Real Data)
    // ==========================================================

    // OLD = getMonthlyAttendancePercent(6)
    // NEW = getMonthlyAttendance6Months() → REAL DB DATA
    public List<Integer> getMonthlyAttendanceChart() {
        return attendanceService.getMonthlyAttendance6Months();
    }

    // ==========================================================
    //                 LATEST ACTIVITY FEED
    // ==========================================================
    public List<String> getLatestActivity() {
        return attendanceService.getLatestActivityMessages(10);
    }

    // ==========================================================
    //                  TOP 5 STUDENTS
    // ==========================================================
    public List<TopStudent> getTopAttendanceStudents() {
        return attendanceService.getTopStudents(5);
    }
}
