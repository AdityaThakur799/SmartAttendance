package com.smartattendance.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// The class name must match the filename: QRCodeGenerator.java
public class QRCodeGenerator {

    /**
     * Generates a QR Code image and saves it to a specified file path.
     * This method is called by StudentManagerGUI.java.
     */
    public static void generateQRCode(String text, String filePath, int width, int height) throws WriterException, IOException {

        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hintMap.put(EncodeHintType.MARGIN, 1);

        // 1. Encode the text into a BitMatrix
        BitMatrix matrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hintMap
        );

        // 2. Prepare the file path
        Path path = FileSystems.getDefault().getPath(filePath);

        // Ensure the directory ('qrcodes' folder) exists
        File outputFile = path.toFile();
        File directory = outputFile.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        // 3. Write the matrix to the file as a PNG image
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }
}