package com.smartattendance.repository;

import com.smartattendance.model.TimeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeSessionRepository extends JpaRepository<TimeSession, Long> {

    List<TimeSession> findByDate(LocalDate date);
}
