package com.smartattendance.repository;

import com.smartattendance.model.DailyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Long> {

    Optional<DailyAttendance> findByStudentIdAndDate(int studentId, LocalDate date);
}
