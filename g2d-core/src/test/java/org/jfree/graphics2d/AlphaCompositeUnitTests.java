package org.jfree.graphics2d;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jfree.graphics2d.Utils.readImageAsBase64;

public class AlphaCompositeUnitTests {

    private static final int TILE_WIDTH = 100;

    private static final int TILE_HEIGHT = 65;

    private static final int FIRST_TYPE = AlphaComposite.CLEAR;
    private static final int LAST_TYPE = AlphaComposite.XOR;

    BufferedImage image = new BufferedImage(TILE_WIDTH * 4, TILE_HEIGHT * 4, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();

    private static String getImagePath(String folder) {
        return "test_output/" + folder + "/AlphaCompositeTests/";
    }

    private static String getAlphaCompositeImageFile(String type) {
        return getAlphaCompositeImageFile(System.getProperty("java.vm.name").split(" ")[0], type);
    }

    private static String getAlphaCompositeImageFile(String folder, String type) {
        return getImagePath(folder) + "AlphaComposite." + type + ".png";
    }

    private static void moveTo(int tileX, int tileY, Graphics2D g2) {
        AffineTransform t = AffineTransform.getTranslateInstance(tileX * TILE_WIDTH, tileY * TILE_HEIGHT);
        g2.setTransform(t);
    }

    private void drawToFile(String type) {
        Utils.writeImageToFile(image, getAlphaCompositeImageFile(type));
    }

    private void setup() {
        image = new BufferedImage(TILE_WIDTH * 12, TILE_HEIGHT * 3, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
    }

    @Test
    public void compareAlphaComposites() {
        int i = 1;
        float temp_opacity = 0;
        for (float opacity = 0.0f; opacity <= 1.0f; opacity += 0.01f) {
            setup();
            System.out.println("Image ID: ("+i+") o(" + opacity + ")");
            for (int rule = FIRST_TYPE; rule <= LAST_TYPE; rule++) {
                moveTo(rule - 1, 1, g2);
                ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(rule, opacity), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
            }

            drawToFile("id("+ i +")");
            i++;
        }
        setup();
        float opacity = 1.0f;
        System.out.println("Image ID: ("+i+") o(" + opacity + ")");
        for (int rule = FIRST_TYPE; rule <= LAST_TYPE; rule++) {
            moveTo(rule - 1, 1, g2);
            ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(rule, opacity), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        }

        drawToFile("id("+ i +")");
    }

    @Test
    public void compareAlphaCompositeJamVMJavaImages() {
        String javaFolder = "Java";
        String jamVMFolder = "JamVM";

        String f = "id(%s)";

        List<String> javaImages = new ArrayList<String>();
        List<String> jamImages = new ArrayList<String>();


        for (int i = 1; i < 103; i++) {
            System.out.println(getAlphaCompositeImageFile(javaFolder, String.format(f, i)));
            String java = readImageAsBase64(getAlphaCompositeImageFile(javaFolder, String.format(f, i)));
            javaImages.add(java);
            String jam = readImageAsBase64(getAlphaCompositeImageFile(jamVMFolder, String.format(f, i)));
            jamImages.add(jam);
        }

//        for (int i = 0; i < javaImages.size(); i++) {
//            for (int j = 0; j < jamImages.size(); j++) {
//                boolean esIgual = jamImages.get(j).equals(javaImages.get(i));
//                System.out.printf("JamVM %s, Java %s are equal: %s%n", j+1, i+1, esIgual);
//            }
//        }
    }


}
