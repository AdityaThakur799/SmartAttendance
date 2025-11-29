package com.smartattendance.service;

import com.smartattendance.model.AttendanceStatus;
import com.smartattendance.model.DailyAttendance;
import com.smartattendance.repository.DailyAttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Optional;

@Service
public class DailyAttendanceService {

    private final DailyAttendanceRepository repository;

    public DailyAttendanceService(DailyAttendanceRepository repository) {
        this.repository = repository;
    }

    // ---- TIMINGS (Your Rules) ----
    private static final LocalTime CLASS_START = LocalTime.of(9, 0);     // 9:00 AM
    private static final LocalTime LATE_AFTER = LocalTime.of(9, 10);     // 9:10 AM
    private static final LocalTime ABSENT_AFTER = LocalTime.of(9, 30);   // 9:30 AM

    private static final int MIN_CHECKOUT_MINUTES = 30; // Stay minimum 30 min


    // ---- MAIN SCAN FUNCTION ----
    public String scanQR(int studentId) {

        LocalDate today = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        LocalDateTime now = LocalDateTime.now();

        Optional<DailyAttendance> existing =
                repository.findByStudentIdAndDate(studentId, today);

        // FIRST SCAN = CHECK-IN
        if (existing.isEmpty()) {
            return doCheckIn(studentId, today, timeNow, now);
        }

        // RECORD EXISTS → maybe check-out?
        DailyAttendance record = existing.get();

        // Already completed?
        if (record.getCheckOut() != null) {
            return "❌ Already checked out. Attendance complete.";
        }

        // SECOND SCAN = CHECK-OUT
        return doCheckOut(record, now);
    }


    // ---- CHECK-IN LOGIC ----
    private String doCheckIn(int studentId, LocalDate date,
                             LocalTime timeNow, LocalDateTime now) {

        DailyAttendance data = new DailyAttendance();
        data.setStudentId(studentId);
        data.setDate(date);
        data.setCheckIn(now);
        data.setStatus(calculateStatus(timeNow)); // auto status

        repository.save(data);

        if (data.getStatus() == AttendanceStatus.PRESENT)
            return "✔ Check-in successful — Marked PRESENT.";

        if (data.getStatus() == AttendanceStatus.LATE)
            return "⚠️ Check-in successful — Marked LATE.";

        return "❌ Check-in successful — Marked ABSENT (after 9:30).";
    }


    // ---- CHECK-OUT LOGIC ----
    private String doCheckOut(DailyAttendance record, LocalDateTime now) {

        LocalDateTime earliestCheckout =
                record.getCheckIn().plusMinutes(MIN_CHECKOUT_MINUTES);

        // Not allowed to checkout early
        if (now.isBefore(earliestCheckout)) {
            long minutesLeft =
                    Duration.between(now, earliestCheckout).toMinutes();

            return "⏳ You must stay 30 mins. Wait " + minutesLeft + " more min.";
        }

        // Allowed to checkout
        record.setCheckOut(now);

        // If not absent → mark completed
        if (record.getStatus() != AttendanceStatus.ABSENT) {
            record.setStatus(AttendanceStatus.COMPLETED);
        }

        repository.save(record);

        return "✔ Successfully checked out — Attendance completed.";
    }


    // ---- STATUS DECIDER LOGIC ----
    private AttendanceStatus calculateStatus(LocalTime now) {

        if (now.isBefore(LATE_AFTER)) {
            return AttendanceStatus.PRESENT;  // before 9:10
        }
        else if (now.isBefore(ABSENT_AFTER)) {
            return AttendanceStatus.LATE;     // 9:10 – 9:29
        }
        else {
            return AttendanceStatus.ABSENT;   // after 9:30
        }
    }
}
