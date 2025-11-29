package com.smartattendance.controller;

import com.smartattendance.model.AttendanceRecord;
import com.smartattendance.model.Student;
import com.smartattendance.repository.StudentRepository;
import com.smartattendance.repository.AttendanceRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @PostMapping("/manual")
    public String saveManualAttendance(@RequestBody Map<Integer, String> attendanceData) {

        attendanceData.forEach((studentId, status) -> {

            Student student = studentRepository.findById(studentId).orElse(null);
            if (student == null) return;

            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(student.getId());
            record.setStudentName(student.getName());
            record.setRollNo(student.getRollNo());
            record.setTimestamp(LocalDateTime.now());
            record.setStatus(status);

            attendanceRecordRepository.save(record);
        });

        return "Attendance saved!";
    }
}
