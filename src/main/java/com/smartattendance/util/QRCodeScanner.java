package com.smartattendance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeScanner {

    /**
     * Scans a QR code from the image file path and returns the decoded text.
     *
     * @param imagePath absolute or relative path to the image file
     * @return decoded text from the QR code, or null if not found
     */
    public static String scanQRCodeImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (!file.exists() || !file.isFile()) {
                System.out.println("File not found: " + imagePath);
                return null;
            }
            var bufferedImage = ImageIO.read(new FileInputStream(file));
            if (bufferedImage == null) {
                System.out.println("Invalid image file: " + imagePath);
                return null;
            }

            var source = new BufferedImageLuminanceSource(bufferedImage);
            var bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();

        } catch (IOException e) {
            System.out.println("Error reading image file: " + e.getMessage());
        } catch (NotFoundException e) {
            System.out.println("No QR code found in the image.");
        }

        return null;
    }
}
