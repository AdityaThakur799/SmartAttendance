package com.smartattendance.service;

import com.smartattendance.database.DatabaseConnection;
import com.smartattendance.model.Student;
import com.smartattendance.util.QRCodeGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    // LOGIN AUTHENTICATION
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
            System.err.println("Authentication Database Error: " + e.getMessage());
        }
        return false;
    }

    // REGISTER NEW STUDENT + AUTO QR GENERATION
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
            e.printStackTrace();
        }

        return newId;
    }

    // SAVE QR TEXT IN DATABASE
    public boolean saveQrCodeText(int id, String qrText) {
        String sql = "UPDATE students SET qr_code = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, qrText);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error saving QR text: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // GET ALL STUDENTS
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, roll_no, qr_code FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no")
                );

                try {
                    String qr = rs.getString("qr_code");
                    if (qr != null) s.setQrCode(qr);
                } catch (SQLException ignore) {}

                list.add(s);
            }

        } catch (Exception e) {
            System.err.println("Error fetching students: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // GET STUDENT BY ID
    public Student getStudentById(int id) {
        String sql = "SELECT id, name, roll_no, qr_code FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Student s = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("roll_no")
                    );

                    try {
                        s.setQrCode(rs.getString("qr_code"));
                    } catch (SQLException ignore) {}

                    return s;
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching student by id: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // DELETE STUDENT + DELETE QR IMAGE
    public boolean deleteStudentById(int id) {

        try {
            File qrFile = new File("qrcodes/student_" + id + ".png");
            if (qrFile.exists()) {
                qrFile.delete();
            }
        } catch (Exception e) {
            System.err.println("QR file delete error: " + e.getMessage());
        }

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // COUNT STUDENTS
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) AS total FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.err.println("Error getting student count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
