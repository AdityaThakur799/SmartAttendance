package com.smartattendance.controller;

import com.smartattendance.model.Student;
import com.smartattendance.service.StudentService;
import com.smartattendance.util.QRCodeGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final StudentService studentService;

    public QRCodeController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/generate/{id}")
    public String generateQR(@PathVariable int id) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            return "❌ Student not found!";
        }

        try {
            // QR text = studentId only
            String qrText = String.valueOf(id);

            // Save image in your qrcodes folder
            String filePath = "qrcodes/student_" + id + ".png";
            QRCodeGenerator.generateQRCode(qrText, filePath, 300, 300);

            // Save QR text in database
            studentService.saveQrCodeText(id, qrText);

            return "✅ QR Code created successfully!";
        }
        catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
