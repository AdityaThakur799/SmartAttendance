package com.smartattendance.service;

import com.smartattendance.model.TimeSession;
import com.smartattendance.repository.TimeSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
public class TimeSessionService {

    private final TimeSessionRepository repo;

    public TimeSessionService(TimeSessionRepository repo) {
        this.repo = repo;
    }

    // GET all sessions for today
    public List<TimeSession> getTodaySessions() {
        return repo.findByDate(LocalDate.now());
    }

    // ---------------------------
    // CHECK-IN (Always Allowed)
    // ---------------------------
    @Transactional
    public TimeSession checkIn() {
        TimeSession session = new TimeSession();
        session.setDate(LocalDate.now());
        session.setCheckIn(LocalTime.now());
        session.setDurationSeconds(0L);
        return repo.save(session);
    }

    // ---------------------------
    // CHECK-OUT (Only last open session)
    // ---------------------------
    @Transactional
    public TimeSession checkOut() {
        List<TimeSession> today = repo.findByDate(LocalDate.now());

        if (today.isEmpty())
            throw new IllegalStateException("No active check-in found!");

        TimeSession last = today.get(today.size() - 1);

        if (last.getCheckOut() != null)
            throw new IllegalStateException("You must check-in again before another check-out.");

        LocalTime outTime = LocalTime.now();
        last.setCheckOut(outTime);

        long seconds = Duration.between(last.getCheckIn(), outTime).getSeconds();
        last.setDurationSeconds(seconds);

        return repo.save(last);
    }

    // ---------------------------
    // Monthly Total
    // ---------------------------
    public long getMonthlySeconds(int year, int month) {
        List<TimeSession> sessions = repo.findAll();

        return sessions.stream()
                .filter(s -> s.getDate().getYear() == year &&
                        s.getDate().getMonthValue() == month)
                .mapToLong(TimeSession::getDurationSeconds)
                .sum();
    }

    // Helper for formatting
    public static String format(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;

        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
