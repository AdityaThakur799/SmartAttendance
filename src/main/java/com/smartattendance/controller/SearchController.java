package com.smartattendance.controller;

import com.smartattendance.model.Student;
import com.smartattendance.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final StudentService studentService;

    public SearchController(StudentService studentService) {
        this.studentService = studentService;
    }

    // SEARCH API → /api/search?q=aditya
    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // empty list
        }
        return studentService.search(query);
    }
}
