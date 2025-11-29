import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String USER = "root"; // CHANGE THIS TO YOUR MYSQL USERNAME
    private static final String PASSWORD = "newpassword"; // CHANGE THIS TO YOUR MYSQL PASSWORD

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean recordAttendance(int studentId) {
        String sql = "INSERT INTO attendance_records (student_id, timestamp, status) VALUES (?, ?, 'PRESENT')";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setObject(2, LocalDateTime.now());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}