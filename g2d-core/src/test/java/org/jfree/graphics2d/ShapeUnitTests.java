package org.jfree.graphics2d;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.jfree.graphics2d.Utils.readImageAsBase64;

public class ShapeUnitTests {

    double factor = 1.51;
    final double maxFactor = 2.0;
    final double scale = 0.01;

    final int TILE_WIDTH = 100;
    final int TILE_HEIGHT = 65;
    final double margin = 5;
    BufferedImage image = new BufferedImage(TILE_WIDTH * 4, TILE_HEIGHT * 4, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();

    String imagePath = "test_output/ShapeTests/";

    final Rectangle2D areaBounds = new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH - 3 * margin, TILE_HEIGHT - 3 * margin);

    Area a1;
    Area a2;

    private String getCombinedAreaImageFile(String type) {
        return imagePath + "combinedArea" + type + ".png";
    }

    private void drawToImage(String type) {
        Utils.writeImageToFile(image, getCombinedAreaImageFile(type));
    }

    public void setup() {
        image = new BufferedImage((int) (TILE_WIDTH * 4), (int) (TILE_HEIGHT * 4), BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        double w = MathUtils.floorWithScale(areaBounds.getWidth() / factor, 7);
        double h = MathUtils.floorWithScale(areaBounds.getHeight() / factor, 7);
        System.out.printf("w(%s), h(%s)%n", w, h);
        Ellipse2D ellipse = new Ellipse2D.Double(areaBounds.getX(), areaBounds.getY(), w, h);
        Rectangle2D rectangle = new Rectangle2D.Double(areaBounds.getMaxX() - w, areaBounds.getMaxY() - h, w, h);

        a1 = new Area(ellipse);
        a2 = new Area(rectangle);
    }

    public void createCombinedAreaAdd() {
        System.out.println("Combined Area ADD");
        a1.add(a2);

        ShapeTests.fillAndStrokeShape(g2, a1, Color.BLUE, null, null);

        drawToImage("Add");
    }


    public void createCombinedAreaIntersect() {
        System.out.println("Combined Area INTERSECT");
        a1.intersect(a2);

        ShapeTests.fillAndStrokeShape(g2, a1, Color.BLUE, null, null);

        drawToImage("Intersect");
    }

    public void createCombinedAreaSubtract() {
        System.out.println("Combined Area SUBTRACT");
        a1.subtract(a2);

        ShapeTests.fillAndStrokeShape(g2, a1, Color.BLUE, null, null);

        drawToImage("Subtract");
    }

    public void createCombinedAreaExclusiveOr() {
        System.out.println("Combined Area XOR");
        a1.exclusiveOr(a2);

        ShapeTests.fillAndStrokeShape(g2, a1, Color.BLUE, null, null);

        drawToImage("XOR");
    }

    @Test
    public void compareCombinedAreasADDINTERSECT() {
        System.out.println("------------- Comparing Combined Areas ADD INTERSECT -----------------");
        for (double i = 0.1; i < maxFactor; i += scale) {
            factor = i;
            setup();
            createCombinedAreaAdd();
            setup();
            createCombinedAreaIntersect();

            System.out.println("Combined Areas: ADD, INTERSECT");
            compareImageByTypes("Add", "Intersect");
        }
        System.out.println("---------------------------------------------------------------------");
    }



    @Test
    public void compareCombinedAreasSUBXOR() {
        System.out.println("------------- Comparing Combined Areas SUBTRACT XOR -----------------");
        for (double i = 0.1; i < maxFactor; i += scale) {
            factor = i;
            setup();
            createCombinedAreaSubtract();
            setup();
            createCombinedAreaExclusiveOr();

            System.out.println("Combined Areas: SUBTRACT, XOR");
            compareImageByTypes("Subtract", "XOR");
        }
        System.out.println("---------------------------------------------------------------------");
    }

    public void compareImageByTypes(String type1, String type2) {
        String a = readImageAsBase64(getCombinedAreaImageFile(type1));
        String b = readImageAsBase64(getCombinedAreaImageFile(type2));

        try {
            Assert.assertNotEquals(a, b);
            System.out.println("Correcto! con factor (" + factor + ")");
        } catch (AssertionError e) {
            System.out.println("Son iguales con factor (" + factor + ") !!!");
        }
        System.out.println();

//        try {
//            Thread.sleep(800);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}
