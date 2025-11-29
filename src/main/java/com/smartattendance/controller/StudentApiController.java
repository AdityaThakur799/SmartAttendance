package com.smartattendance.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.smartattendance.repository.StudentRepository;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.smartattendance.model.Student;

@RestController
public class StudentApiController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/api/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
