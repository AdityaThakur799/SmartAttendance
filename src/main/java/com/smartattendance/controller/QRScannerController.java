package com.smartattendance.controller;

import com.smartattendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class QRScannerController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/qrscanner")
    public String openScannerPage() {
        return "qrscanner";  // Loads qrscanner.html
    }

    @PostMapping(value = "/scan", consumes = "text/plain")
    @ResponseBody
    public String processQR(@RequestBody String qrValue) {

        // Clean QR raw text
        qrValue = qrValue.trim();

        // Directly call service
        return attendanceService.processQR(qrValue);
    }
}
