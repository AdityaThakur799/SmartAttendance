package com.smartattendance.util;

public class Main {
    public static void main(String[] args) {
        System.out.println("Smart Attendance System starting up...");

        // Example test 1: Generate QR Code
        try {
            QRCodeGenerator generator = new QRCodeGenerator();
            generator.generateQRCode("TestQRCode", "test_qr.png", 200, 200);
            System.out.println("✅ QR Code generated successfully!");
        } catch (Exception e) {
            System.out.println("⚠️ Error while generating QR code: " + e.getMessage());
        }

        // Example test 2: Check database connection (optional)
        try {
            if (com.smartattendance.database.DatabaseConnection.getConnection() != null) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("⚠️ Database connection failed!");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Database connection error: " + e.getMessage());
        }

        System.out.println("Smart Attendance backend is running fine!");
    }
}
