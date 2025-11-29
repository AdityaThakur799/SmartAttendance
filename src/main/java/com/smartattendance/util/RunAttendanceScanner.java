package com.smartattendance.util;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.smartattendance.service.AttendanceService;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class RunAttendanceScanner {

    public static void main(String[] args) {

        System.out.println("📡 Smart Attendance Scanner starting...");
        System.out.println("Detected webcams: " + Webcam.getWebcams());

        // Run UI safely
        SwingUtilities.invokeLater(() -> {

            Webcam webcam = Webcam.getDefault();
            if (webcam == null) {
                System.out.println("❌ No webcam found.");
                return;
            }

            webcam.setViewSize(WebcamResolution.VGA.getSize());
            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);

            JFrame window = new JFrame("QR Code Scanner");
            window.add(panel);
            window.setResizable(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setVisible(true);

            System.out.println("✅ Webcam window opened. Point a QR code at the camera.");

            // Thread to scan QR continuously
            new Thread(() -> {

                AttendanceService service = new AttendanceService();

                while (webcam.isOpen()) {
                    try {
                        Thread.sleep(120);
                    } catch (InterruptedException ignored) {}

                    BufferedImage image = webcam.getImage();
                    if (image == null) continue;

                    BinaryBitmap bitmap = new BinaryBitmap(
                            new HybridBinarizer(new BufferedImageLuminanceSource(image))
                    );

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);

                        if (result != null) {
                            String qrText = result.getText();
                            System.out.println("📌 QR Code Detected: " + qrText);

                            // Call your NEW QR logic
                            String responseMessage = service.processQR(qrText);

                            System.out.println("🟢 RESULT → " + responseMessage);

                            break; // stop after first scan
                        }
                    } catch (NotFoundException ignored) {
                        // No QR detected — ignore
                    }
                }

                webcam.close();
                window.dispose();
                System.out.println("📷 Scanner closed.");
            }).start();
        });
    }
}
