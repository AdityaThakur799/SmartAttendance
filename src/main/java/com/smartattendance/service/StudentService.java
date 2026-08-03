package com.smartattendance.service;

import com.smartattendance.database.DatabaseConnection;
import com.smartattendance.model.Student;
import com.smartattendance.repository.StudentRepository;
import com.smartattendance.util.QRCodeGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Inject JPA Repository
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ============================
    // LOGIN
    // ============================
    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    return storedPassword.equals(password);
                }
            }
        } catch (SQLException e) {
            System.err.println("Auth error: " + e.getMessage());
        }
        return false;
    }

    // ============================
    // REGISTER NEW STUDENT
    // ============================
    public int registerNewStudent(Student student) {

        String insertSql = "INSERT INTO students (name, roll_no, qr_code) VALUES (?, ?, ?)";
        int newId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNo());
            pstmt.setString(3, null);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        newId = rs.getInt(1);
                        student.setId(newId);
                    }
                }
            }

            if (newId > 0) {
                String qrText = String.valueOf(newId);
                String filePath = "src/main/resources/static/qrcodes/student_" + newId + ".png";

                QRCodeGenerator.generateQRCode(qrText, filePath, 300, 300);

                saveQrCodeText(newId, qrText);
            }

        } catch (Exception e) {
            System.err.println("Error registering student: " + e.getMessage());
        }

        return newId;
    }

    // ============================
    // SAVE QR TEXT
    // ============================
    public boolean saveQrCodeText(int id, String qrText) {
        String sql = "UPDATE students SET qr_code = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, qrText);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("QR save error: " + e.getMessage());
        }
        return false;
    }

    // ============================
    // GET ALL STUDENTS
    // ============================
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ============================
    // GET STUDENT BY ID
    // ============================
    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    // ============================
    // DELETE STUDENT
    // ============================
    public boolean deleteStudentById(int id) {

        try {
            File qr = new File("qrcodes/student_" + id + ".png");
            if (qr.exists()) qr.delete();
        } catch (Exception ignored) {}

        studentRepository.deleteById(id);
        return true;
    }

    // ============================
    // COUNT STUDENTS
    // ============================
    public int getStudentCount() {
        return (int) studentRepository.count();
    }

    // ============================
    // 🔍 SEARCH STUDENTS (NEW)
    // ============================
    public List<Student> search(String query) {
        return studentRepository.findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(query, query);
    }
}
