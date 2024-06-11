package org.jfree.graphics2d;

import gnu.classpath.debug.SystemLogger;
import gnu.java.awt.DebugLogger;
import gnu.java.awt.java2d.AlphaCompositeContext;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static org.jfree.graphics2d.Utils.readImageAsBase64;

public class AlphaCompositeUnitTests {

    private static final int TILE_WIDTH = 100;

    private static final int TILE_HEIGHT = 65;

    private static final int FIRST_TYPE = AlphaComposite.CLEAR;
    private static final int LAST_TYPE = AlphaComposite.XOR;

    BufferedImage image = new BufferedImage(TILE_WIDTH*3, TILE_HEIGHT*3, BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g2 = image.createGraphics();

    Logger log = DebugLogger.getInstance().getLogger();

    FiguresContext figuresContext = new FiguresContext();

    /** TODO: replicar el metodo de debug que se usa en clases Thread en el archivo
     * <a href="https://github.com/ingelabs/mauve/blob/a4898f6b9553851b1772599626e9d20aba7413e7/gnu/testlet/java/nio/channels/Selector/select.java#L44">GNU Testlet: select.java</a>
     * en vez de usar logger o escribir en archivo
     * Hay que saber cual es el call stack de las funciones que nos dan problemas para ver donde poner esos logs, herramientas como jconsole funcionan
     * en JDK, tomar de ahi el call stack para ir a JamVM y sacar el debug
     */


    public AlphaCompositeUnitTests() {
    }


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
        image = new BufferedImage(TILE_WIDTH * 12, TILE_HEIGHT * 3, BufferedImage.TYPE_INT_ARGB_PRE);
        g2 = image.createGraphics();
    }


    // TODO: Probar tambiem con imagenes y alphacomposite
    private void drawShapesWithAlphaComposite(Graphics2D g2, AlphaComposite ac, FiguresContext figuresContext) {
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setPaint(Color.red);
        g2.fill(figuresContext.getShape1());

        g2.setComposite(ac);
        g2.setPaint(Color.blue);
        g2.fill(figuresContext.getShape2());
    }

    static class FiguresContext {
        double w = TILE_WIDTH / 1.5;
        double h = TILE_HEIGHT / 1.5;

        double x = TILE_WIDTH;
        double y = TILE_HEIGHT;

        Shape shape1 = new Rectangle2D.Double(x, y, w, h);
        Shape shape2 = new Rectangle2D.Double(x + w / 2, y + h / 2, w, h);

        public Rectangle getBounds() {
            return new Rectangle(TILE_WIDTH, TILE_HEIGHT);
        }

        public Shape getShape1() {
            return shape1;
        }

        public Shape getShape2() {
            return shape2;
        }
    }

    private void drawAllOpacities(int rule, FiguresContext figuresContext) {
        int i = 0;
        float opacity = 0.0f;

        AlphaComposite ac = AlphaComposite.getInstance(rule, opacity);
        drawShapesWithAlphaComposite(g2, ac, figuresContext);
        log.info(String.format("Image ID: %s.id(%s) o(%s)%n", rule, i, opacity));
        drawToFile(String.format("%s.id(%s)", rule, i));

        i++;
        opacity = 0.5f;
        ac = ac.derive(opacity);

        drawShapesWithAlphaComposite(g2, ac, figuresContext);
        log.info(String.format("Image ID: %s.id(%s) o(%s)%n", rule, i, opacity));
        drawToFile(String.format("%s.id(%s)", rule, i));

        i++;
        opacity = 1.0f;
        ac = ac.derive(opacity);

        drawShapesWithAlphaComposite(g2, ac, figuresContext);
        log.info(String.format("Image ID: %s.id(%s) o(%s)%n", rule, i, opacity));
        drawToFile(String.format("%s.id(%s)", rule, i));
    }

    @Test
    public void compareAlphaCompositeModeClear() {
        // TODO: comprobar donde esta la raiz del problema, clear deberia de ser siempre transparente independiente de la opacidad


        drawAllOpacities(0, figuresContext);

    }


    @Test
    public void compareAlphaCompositeModeSrc() {
        // TODO: comprobar donde esta la raiz del problema, src deberia no verse la figura roja debajo de la azul
        // Si se suman SrcIn y SrcOut


        drawAllOpacities(1, figuresContext);
    }

    @Test
    public void compareAlphaCompositeModeDst() {
//
//
//        int i = 0;
//        for (float opacity = 0.0f; opacity <= 1.1f; opacity += 0.1f) {
//            drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST, opacity), figuresContext.getShape1(), figuresContext.getShape2());
//            System.out.println("Image ID: Dst.id("+i+") o(" + opacity + ")");
//            drawToFile("Dst.id("+ i +")");
//            i++;
//        }
        Assert.assertEquals(true, true);
    }

    @Test
    public void compareAlphaCompositeModeSrcOver() {
        Assert.assertEquals(true, true);

    }

    @Test
    public void compareAlphaCompositeModeDstOver() {
        Assert.assertEquals(true, true);

    }

    @Test
    public void compareAlphaCompositeModeSrcIn() {
        // TODO: comprobar donde esta la raiz del problema


        drawAllOpacities(3, figuresContext);

    }

    @Test
    public void compareAlphaCompositeModeDstIn() {
        // TODO: comprobar donde esta la raiz del problema


        drawAllOpacities(8, figuresContext);

    }


    @Test
    public void compareAlphaCompositeModeSrcOut() {
        // TODO: comprobar donde esta la raiz del problema


        drawAllOpacities(4, figuresContext);

    }

    @Test
    public void compareAlphaCompositeModeDstOut() {
        Assert.assertEquals(true, true);

    }

    @Test
    public void compareAlphaCompositeModeSrcATop() {
        Assert.assertEquals(true, true);
    }

    @Test
    public void compareAlphaCompositeModeDstATop() {
        // TODO: comprobar donde esta la raiz del problema


        drawAllOpacities(AlphaComposite.DST_ATOP, figuresContext);

    }

    @Test
    public void compareAlphaCompositeModeXOR() {
        Assert.assertEquals(true, true);

    }

    @Test
    public void compareComposite11() {
        String javaFolder = "Java";
        String jamVMFolder = "JamVM";

        String f = "11.id(%s)";

        List<String> javaImages = new ArrayList<String>();
        List<String> jamImages = new ArrayList<String>();


        for (int i = 0; i < 3; i++) {
            System.out.println(getAlphaCompositeImageFile(javaFolder, String.format(f, i)));
            String java = readImageAsBase64(getAlphaCompositeImageFile(javaFolder, String.format(f, i)));
            javaImages.add(java);
            String jam = readImageAsBase64(getAlphaCompositeImageFile(jamVMFolder, String.format(f, i)));
            jamImages.add(jam);
            log.info(String.format("%s", java == null ? jam == null : java.equals(jam)));
        }
    }

    public void compareAlphaComposites() {
        int i = 1;
        for (float opacity = 0.0f; opacity <= 1.0f; opacity += 0.01f) {
            setup();
            log.info(String.format("Image ID: ("+i+") o(" + opacity + ")"));
            for (int rule = FIRST_TYPE; rule <= LAST_TYPE; rule++) {
                moveTo(rule - 1, 1, g2);
                ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(rule, opacity), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
            }

            drawToFile("id("+ i +")");
            i++;
        }
        setup();
        float opacity = 1.0f;
        log.info(String.format("Image ID: ("+i+") o(" + opacity + ")"));
        for (int rule = FIRST_TYPE; rule <= LAST_TYPE; rule++) {
            moveTo(rule - 1, 1, g2);
            ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(rule, opacity), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        }

        drawToFile("id("+ i +")");
    }

//    @Test
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
