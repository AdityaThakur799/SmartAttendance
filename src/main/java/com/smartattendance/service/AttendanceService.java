package com.smartattendance.service;

import com.smartattendance.database.DatabaseConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    // ============================================
    // PARSE QR (QR contains only studentId)
    // ============================================
    private int parseStudentIdFromQRCode(String qr) {
        try {
            return Integer.parseInt(qr.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    // ============================================
    // GET STUDENT NAME
    // ============================================
    private String getStudentName(int studentId) {

        String sql = "SELECT name FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================================
    // GET LAST RECORD FOR A STUDENT
    // ============================================
    private AttendanceRecordData getLastRecord(int studentId) {

        String sql = """
                SELECT timestamp, type
                FROM attendance_records
                WHERE student_id = ?
                ORDER BY timestamp DESC
                LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new AttendanceRecordData(
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("type")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================================
    // PROCESS QR — CHECK-IN / CHECK-OUT
    // ============================================
    public String processQR(String qrData) {

        int studentId = parseStudentIdFromQRCode(qrData);
        if (studentId == -1)
            return "❌ Invalid QR Code";

        String studentName = getStudentName(studentId);
        if (studentName == null)
            return "❌ Student Not Found";

        AttendanceRecordData last = getLastRecord(studentId);
        LocalDateTime now = LocalDateTime.now();

        // FIRST TIME OR LAST IS CHECKOUT → CHECKIN
        if (last == null || last.type.equals("CHECKOUT")) {
            saveRecord(studentId, "CHECKIN");
            return "✅ " + studentName + " checked IN";
        }

        // Already checked-in → enforce 30-min rule
        long mins = Duration.between(last.time, now).toMinutes();

        if (mins < 30) {
            return "⛔ " + studentName + " can checkout after " + (30 - mins) + " min";
        }

        // 30 mins passed → CHECKOUT
        saveRecord(studentId, "CHECKOUT");
        return "📤 " + studentName + " checked OUT";
    }

    // ============================================
    // SAVE ATTENDANCE RECORD
    // ============================================
    private void saveRecord(int studentId, String type) {

        String sql =
                "INSERT INTO attendance_records (student_id, timestamp, type) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, type);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // ------------------  DASHBOARD METHODS -----------------------
    // ============================================================

    // Count Present Today
    public int countPresentOn(LocalDate date) {

        String sql = """
                SELECT COUNT(*) FROM attendance_records
                WHERE DATE(timestamp)=? AND type='CHECKIN'
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    // Count Late Today
    public int countLateOn(LocalDate date) {

        String sql = """
                SELECT COUNT(*) FROM attendance_records
                WHERE DATE(timestamp)=?
                AND type='CHECKIN'
                AND HOUR(timestamp) > 9
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    // Last N days present count
    public List<Integer> getPresentCountsLastNDays(int n) {

        List<Integer> list = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = n - 1; i >= 0; i--) {
            list.add(countPresentOn(today.minusDays(i)));
        }

        return list;
    }

    // Monthly attendance %
    public List<Integer> getMonthlyAttendancePercent(int months) {

        List<Integer> list = new ArrayList<>();

        String sql = """
                SELECT MONTH(timestamp) AS m,
                (SUM(CASE WHEN type='CHECKIN' THEN 1 ELSE 0 END) * 100 / COUNT(*)) AS pct
                FROM attendance_records
                WHERE timestamp >= DATE_SUB(CURDATE(), INTERVAL ? MONTH)
                GROUP BY MONTH(timestamp)
                ORDER BY m
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, months);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(rs.getInt("pct"));

        } catch (Exception e) { e.printStackTrace(); }

        while (list.size() < months) list.add(0);

        return list;
    }

    // Latest activity feed
    public List<String> getLatestActivityMessages(int limit) {

        List<String> msgs = new ArrayList<>();

        String sql = """
                SELECT s.name, ar.timestamp, ar.type
                FROM students s
                JOIN attendance_records ar ON s.id = ar.student_id
                ORDER BY ar.timestamp DESC
                LIMIT ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                msgs.add(
                        rs.getString("name") +
                                " " + rs.getString("type") +
                                " at " + rs.getTimestamp("timestamp")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }

        return msgs;
    }

    // Top 5 students by attendance
    public List<TopStudent> getTopStudents(int limit) {

        List<TopStudent> list = new ArrayList<>();

        String sql = """
                SELECT 
                    s.id, s.name, s.roll_no,
                    (SUM(CASE WHEN ar.type='CHECKIN' THEN 1 ELSE 0 END) * 100.0 / COUNT(ar.id)) AS pct
                FROM students s
                LEFT JOIN attendance_records ar 
                    ON s.id = ar.student_id
                GROUP BY s.id, s.name, s.roll_no
                ORDER BY pct DESC
                LIMIT ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new TopStudent(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getDouble("pct")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // Internal DTO
    private static class AttendanceRecordData {
        LocalDateTime time;
        String type;

        AttendanceRecordData(LocalDateTime time, String type) {
            this.time = time;
            this.type = type;
        }
    }
}
