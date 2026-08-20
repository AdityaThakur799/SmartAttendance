package com.smartattendance.service;

import org.springframework.stereotype.Service;

@Service
public class DailyAttendanceService {

    private final AttendanceService attendanceService;

    public DailyAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public String scanQR(int studentId) {
        return attendanceService.processQR(String.valueOf(studentId));
    }
}
// Daily attendance QR service
