package com.smartattendance.repository;

import com.smartattendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Long countByStatus(String status);

    /* DAILY (NO LPAD, NO CONCAT, Hibernate compatible) */
    @Query("SELECT YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp), COUNT(ar) " +
            "FROM AttendanceRecord ar " +
            "GROUP BY YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp) " +
            "ORDER BY YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp)")
    List<Object[]> countDailyAttendance();

    /* STUDENT DAILY */
    @Query("SELECT YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp), COUNT(ar) " +
            "FROM AttendanceRecord ar " +
            "WHERE ar.studentId = :studentId " +
            "GROUP BY YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp) " +
            "ORDER BY YEAR(ar.timestamp), MONTH(ar.timestamp), DAY(ar.timestamp)")
    List<Object[]> countDailyForStudent(@Param("studentId") int studentId);

    /* MONTHLY SUMMARY (Hibernate–safe) */
    @Query("SELECT YEAR(ar.timestamp), MONTH(ar.timestamp), " +
            "SUM(CASE WHEN ar.status = 'PRESENT' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ar.status = 'ABSENT' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ar.status = 'LATE' THEN 1 ELSE 0 END) " +
            "FROM AttendanceRecord ar " +
            "WHERE ar.studentId = :studentId " +
            "GROUP BY YEAR(ar.timestamp), MONTH(ar.timestamp) " +
            "ORDER BY YEAR(ar.timestamp), MONTH(ar.timestamp)")
    List<Object[]> countMonthlySummaryForStudent(@Param("studentId") int studentId);
}
