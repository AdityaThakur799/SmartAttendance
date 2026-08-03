package com.smartattendance.controller;

import com.smartattendance.service.DashboardService;
import com.smartattendance.service.StudentService;
import com.smartattendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;

    public DashboardController(
            DashboardService dashboardService,
            StudentService studentService,
            AttendanceService attendanceService
    ) {
        this.dashboardService = dashboardService;
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {

        LocalDate today = LocalDate.now();

        // ---- TODAY COUNTS ----
        model.addAttribute("totalStudents", studentService.getStudentCount());
        model.addAttribute("todayPresent", dashboardService.getTodayPresentCount());
        model.addAttribute("todayAbsent", dashboardService.getTodayAbsentCount());
        model.addAttribute("todayLate", dashboardService.getTodayLateCount());

        // ---- WEEKLY TREND ----
        model.addAttribute("weeklyHeatmap", attendanceService.getWeeklyAttendance());

        // ---- MONTHLY TREND ----
        model.addAttribute("monthlyChart", attendanceService.getMonthlyAttendance6Months());

        // ---- LATEST ACTIVITY ----
        model.addAttribute("activityFeed", attendanceService.getLatestActivityMessages(10));

        // ---- TOP STUDENTS ----
        model.addAttribute("topStudents", attendanceService.getTopStudents(5));

        return "dashboard";
    }
}
