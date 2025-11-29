package com.smartattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendanceController {

    @GetMapping("/attendance")
    public String showAttendancePage() {
        return "attendance";  // opens attendance.html
    }
}
