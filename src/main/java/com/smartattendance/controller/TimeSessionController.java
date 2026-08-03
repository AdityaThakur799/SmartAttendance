package com.smartattendance.controller;

import com.smartattendance.model.TimeSession;
import com.smartattendance.service.TimeSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class TimeSessionController {

    private final TimeSessionService service;

    public TimeSessionController(TimeSessionService service) {
        this.service = service;
    }

    // ---------------------------------
    // GET Today's sessions
    // ---------------------------------
    @GetMapping("/today")
    public ResponseEntity<?> getToday() {
        return ResponseEntity.ok(
                Map.of(
                        "sessions", service.getTodaySessions()
                )
        );
    }

    // ---------------------------------
    // CHECK-IN
    // ---------------------------------
    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn() {
        try {
            TimeSession s = service.checkIn();
            return ResponseEntity.ok(Map.of("success", true, "session", s));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", e.getMessage())
            );
        }
    }

    // ---------------------------------
    // CHECK-OUT
    // ---------------------------------
    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut() {
        try {
            TimeSession s = service.checkOut();
            return ResponseEntity.ok(Map.of("success", true, "session", s));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", e.getMessage())
            );
        }
    }

    // ---------------------------------
    // ALL SESSIONS (History)
    // ---------------------------------
    @GetMapping("/history")
    public ResponseEntity<?> history() {
        return ResponseEntity.ok(service.getTodaySessions());
    }

    // ---------------------------------
    // Monthly total
    // ---------------------------------
    @GetMapping("/monthly/{year}/{month}/total")
    public ResponseEntity<?> getMonthTotal(@PathVariable int year, @PathVariable int month) {

        long totalSec = service.getMonthlySeconds(year, month);

        return ResponseEntity.ok(
                Map.of(
                        "totalSeconds", totalSec,
                        "formatted", TimeSessionService.format(totalSec)
                )
        );
    }
}
