package com.smartattendance.controller;

import com.smartattendance.service.DashboardService;
import com.smartattendance.service.StudentService;
import com.smartattendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;

    // ---- Constructor Injection (BEST PRACTICE) ----
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

        model.addAttribute("totalStudents", studentService.getStudentCount());
        model.addAttribute("todayPresent", dashboardService.getTodayPresentCount());
        model.addAttribute("todayAbsent", dashboardService.getTodayAbsentCount());
        model.addAttribute("todayLate", dashboardService.getTodayLateCount());

        model.addAttribute("weeklyHeatmap", dashboardService.getWeeklyHeatmap());
        model.addAttribute("monthlyChart", dashboardService.getMonthlyAttendanceChart());
        model.addAttribute("activityFeed", dashboardService.getLatestActivity());
        model.addAttribute("topStudents", dashboardService.getTopAttendanceStudents());

        return "dashboard";
    }
}
