package com.smartattendance;

import com.smartattendance.service.ApprovalRequestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartAttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartAttendanceApplication.class, args);
        System.out.println("✅ Smart Attendance Web Server started on http://localhost:8080");
    }

    // Load dummy approval requests at startup (safe, runs only once)
    @Bean
    public CommandLineRunner loadDummyRequests(ApprovalRequestService service) {
        return args -> {
            try {
                service.addDummyRequests();
                System.out.println("📌 Dummy approval requests loaded successfully.");
            } catch (Exception e) {
                System.out.println("⚠️ Could not load dummy requests: " + e.getMessage());
            }
        };
    }
}
