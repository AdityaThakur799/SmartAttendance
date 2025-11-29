package com.smartattendance.controller;

import com.smartattendance.service.DailyAttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/daily-attendance")
public class DailyAttendanceController {

    private final DailyAttendanceService service;

    public DailyAttendanceController(DailyAttendanceService service) {
        this.service = service;
    }

    @PostMapping("/scan")
    public ResponseEntity<?> scanQR(@RequestBody Map<String, Object> body) {

        if (!body.containsKey("studentId")) {
            return ResponseEntity.badRequest().body("❌ studentId missing");
        }

        int studentId;

        try {
            studentId = Integer.parseInt(body.get("studentId").toString());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ studentId must be a number");
        }

        String resultMessage = service.scanQR(studentId);

        return ResponseEntity.ok(resultMessage);
    }
}
