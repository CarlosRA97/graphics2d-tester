package org.jfree.graphics2d;

import gnu.java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Utils {
    public static void writeImageToFile(BufferedImage image, String file) {
        Toolkit.getDefaultToolkit().sync();

        try {
            ImageIO.write(image, "png", new File(file));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String readImageAsBase64(String file) {
        return Base64.encode(toByteArray(readImage(file)));
    }

    private static BufferedImage readImage(String file) {
        try {
            return ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static byte[] toByteArray(BufferedImage bi) {

        int[] data = null;

        try {
            PixelGrabber a = new PixelGrabber(bi, 0, 0, -1, -1, false);

            if (a.grabPixels()) {
                int width = a.getWidth();
                int height = a.getHeight();
                data = new int[width * height];
                data = (int[]) a.getPixels();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);

        byte[] array = byteBuffer.array();

        return array;
    }
}
