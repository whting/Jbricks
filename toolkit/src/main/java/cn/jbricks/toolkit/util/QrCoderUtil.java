package cn.jbricks.toolkit.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;

/**
 * User: kuiyuexiang
 * Date: 2012-08-15
 * Time: 下午9:10
 */
public class QrCoderUtil {
    /**
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void encode(String contents, int width, int height, String imgPath) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.QR_CODE, width, height, hints);

            MatrixToImageWriter
                    .writeToFile(bitMatrix, "png", new File(imgPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encode(String contents, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param imgPath
     * @return String
     */
    public String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");

            result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PerformUtil performUtil = new PerformUtil("QrCoderUtil");
        for (int i = 0; i < 100; i++) {
            int width = 300, height = 300;
            QrCoderUtil handler = new QrCoderUtil();
            String contents = i + "http://" + i +
                    "192.168.0.29:" + i +
                    "8080/examin" + i +
                    "ingReport.html" + i;
            byte[] bytes = handler.encode(contents, width, height);
            performUtil.testRecord("encode i=" + i);
        }
        performUtil.testRecord("encode");

        String imgPath = "/Users/kuiyuexiang/logs/zxing/michael_zxing.png";
        String contents = "Hello Michael(大大),welcome to Zxing!"
                + "\nMichael’s blog [ http://sjsky.iteye.com ]"
                + "\nEMail [ sjsky007@gmail.com ]" + "\nTwitter [ @suncto ]";
        int width = 300, height = 300;
        QrCoderUtil handler = new QrCoderUtil();
        handler.encode(contents, width, height, imgPath);

        System.out.println("Michael ,you have finished zxing encode.");

        String decodeContent = handler.decode(imgPath);
        System.out.println("decodeContent：");
        System.out.println(decodeContent);
        System.out.println("Michael ,you have finished zxing decode.");
    }
}
