import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath, int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);

        System.out.println("QR Code generated at: " + filePath);
    }

    // Test main method - commented out to prevent generating student101.png
    /*
    public static void main(String[] args) {
        try {
            // Example data for a student
            String studentData = "ID:101,Name:Aditya Thakur,RollNo:101";
            String filePath = "qrcodes/student101.png";

            // Generate the QR code image
            generateQRCode(studentData, filePath, 200, 200);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
    */
}
