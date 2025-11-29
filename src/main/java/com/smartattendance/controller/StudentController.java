package com.smartattendance.controller;

import com.smartattendance.service.StudentService;
import com.smartattendance.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ----------------------------------------------------
    // SHOW STUDENT LIST (student.html)
    // ----------------------------------------------------
    @GetMapping("/students")
    public String showStudentList(Model model) {

        List<Student> students = studentService.getAllStudents();
        model.addAttribute("studentsList", students);

        return "student";  // student.html
    }

    // ----------------------------------------------------
    // SHOW ADD STUDENT FORM (add-student.html)
    // ----------------------------------------------------
    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());
        return "add-student";  // add-student.html
    }

    // ----------------------------------------------------
    // SAVE NEW STUDENT (Register + Auto QR)
    // ----------------------------------------------------
    @PostMapping("/students/add")
    public String saveStudent(@ModelAttribute Student student) {

        studentService.registerNewStudent(student);
        return "redirect:/students";
    }

    // ----------------------------------------------------
    // VIEW INDIVIDUAL STUDENT QR (view-qr.html)
    // ----------------------------------------------------
    @GetMapping("/students/viewqr/{id}")
    public String viewStudentQr(@PathVariable int id, Model model) {

        Student s = studentService.getStudentById(id);

        if (s == null) {
            return "redirect:/students";  // student not found
        }

        String qrPath = "/qrcodes/student_" + s.getId() + ".png";

        model.addAttribute("student", s);
        model.addAttribute("qrPath", qrPath);

        return "view-qr";  // view-qr.html
    }

    // ----------------------------------------------------
    // DELETE STUDENT
    // ----------------------------------------------------
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id) {

        studentService.deleteStudentById(id);
        return "redirect:/students";
    }

    // ----------------------------------------------------
    // ⭐ NEW: SHOW ALL STUDENTS QR CODES (all-qrcodes.html)
    // ----------------------------------------------------
    @GetMapping("/students/qrcodes")
    public String showAllQrCodes(Model model) {

        List<Student> list = studentService.getAllStudents();
        model.addAttribute("students", list);

        return "all-qrcodes";   // all-qrcodes.html
    }
}
