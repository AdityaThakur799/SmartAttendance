import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;

public class QRCodeScanner {

    public static String scanQRCode(Webcam webcam) {
        // The webcam is already opened by the AttendanceGUI class.

        BufferedImage image = webcam.getImage();
        if (image == null) {
            return null;
        }

        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Collection<com.google.zxing.BarcodeFormat> possibleFormats = new ArrayList<>();
            possibleFormats.add(com.google.zxing.BarcodeFormat.QR_CODE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, possibleFormats);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}