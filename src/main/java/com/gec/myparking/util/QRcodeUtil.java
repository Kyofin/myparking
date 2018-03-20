package com.gec.myparking.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public class QRcodeUtil {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int width = 400; // 二维码图片宽度
    private static final int height = 400; // 二维码图片高度
    private static final String format = "gif";// 二维码的图片格式

    private QRcodeUtil() {
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    //写出到文件
    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    //写出到输出流
    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 写出一个二维码图片文件
     * @param text
     * @throws WriterException
     * @throws IOException
     */
    public  static void   writeQRcodeToFile(String text,String fileName) throws WriterException, IOException {
        //设置参数
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();//存储配置的字典
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   // 内容所使用字符集编码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码
        File outputFile = new File("D:\\学习\\毕设\\车位二维码" + File.separator + fileName+".jpg");
        writeToFile(bitMatrix, format, outputFile);
        System.out.println("生成成功");
    }

    /**
     * 写出一个二维码图片流
     * @param text
     * @throws WriterException
     * @throws IOException
     */
    public  static void   writeQRcodeToStream(String text, HttpServletResponse response) throws WriterException, IOException {
        //设置参数
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();//存储配置的字典
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   // 内容所使用字符集编码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码
        writeToStream(bitMatrix,format,response.getOutputStream());

    }



}

