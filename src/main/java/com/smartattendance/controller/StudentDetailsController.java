package com.smartattendance.controller;

import com.smartattendance.model.Student;
import com.smartattendance.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentDetailsController {

    private final StudentService studentService;

    public StudentDetailsController(StudentService studentService) {
        this.studentService = studentService;
    }

    // SHOW STUDENT PROFILE PAGE
    @GetMapping("/{id}")
    public String getStudentPage(@PathVariable int id, Model model) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            model.addAttribute("error", "Student not found");
            return "student-profile";  // show error
        }

        model.addAttribute("student", student);
        return "student-profile";   // thymeleaf page
    }
}
