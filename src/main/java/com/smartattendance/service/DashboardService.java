package com.smartattendance.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    // Constructor injection (Spring auto-wires beans)
    public DashboardService(AttendanceService attendanceService, StudentService studentService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    // ---------------------------------------------
    // TODAY COUNTS
    // ---------------------------------------------

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

    // ---------------------------------------------
    // WEEKLY HEATMAP (Last 7 days)
    // ---------------------------------------------
    public List<Integer> getWeeklyHeatmap() {
        return attendanceService.getPresentCountsLastNDays(7);
    }

    // ---------------------------------------------
    // MONTHLY CHART (Last 6 months)
    // ---------------------------------------------
    public List<Integer> getMonthlyAttendanceChart() {
        return attendanceService.getMonthlyAttendancePercent(6);
    }

    // ---------------------------------------------
    // LATEST ACTIVITY FEED
    // ---------------------------------------------
    public List<String> getLatestActivity() {
        return attendanceService.getLatestActivityMessages(12);
    }

    // ---------------------------------------------
    // TOP STUDENTS (Best Attendance)
    // ---------------------------------------------
    public List<TopStudent> getTopAttendanceStudents() {
        return attendanceService.getTopStudents(5);
    }
}
