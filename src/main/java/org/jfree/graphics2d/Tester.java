package org.jfree.graphics2d;

import org.jetbrains.skija.Data;
import org.jetbrains.skija.EncodedImageFormat;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.skija.SkijaGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Runs a visual testing setup against various Graphics2D implementations.  The idea
 * is that you can compare the output of a custom Graphics2D implementation against
 * the reference implementation (Java2D).
 */
public class Tester {

    private static int TILE_COUNT_H = 12;

    private static int TILE_COUNT_V = 25;

    private static int TILE_WIDTH = 100;

    private static int TILE_HEIGHT = 65;

    private static int MARGIN = 5;

    private static Stroke OUTLINE = new BasicStroke(1.0f);

    private static Stroke DASHED = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 4f, new float[] { 2f, 2f }, 0f);

    private static Stroke DASHED_3 = new BasicStroke(3.0f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND, 4f, new float[] { 4f, 8f }, 0f);

    private static Color[] RAINBOW_COLORS = new Color[] {
            new Color(255, 0, 0), // RED
            new Color(255, 165, 0), // ORANGE
            new Color(255, 255, 0), // YELLOW
            new Color(0, 128, 0), // GREEN
            new Color(0, 0, 255), // BLUE
            new Color(75, 0, 130), // INDIGO
            new Color(238, 130, 238) // VIOLET
    };

    private static void moveTo(int tileX, int tileY, Graphics2D g2) {
        AffineTransform t = AffineTransform.getTranslateInstance(tileX * TILE_WIDTH, tileY * TILE_HEIGHT);
        g2.setTransform(t);
    }

    private static void drawTilePolygon(Graphics2D g2) {
        g2.setPaint(Color.BLUE);
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawPolygon(xCoordsForCross(TILE_WIDTH / 2, 36, 18), yCoordsForCross(TILE_HEIGHT / 2, 36, 18), 12);
    }

    private static int[] xCoordsForCross(int centerX, int radius, int thickness) {
        int[] xcoords = new int[12];
        xcoords[0] = centerX + thickness / 2;
        xcoords[1] = xcoords[0];
        xcoords[2] = centerX + radius;
        xcoords[3] = xcoords[2];
        xcoords[4] = xcoords[0];
        xcoords[5] = xcoords[0];
        xcoords[6] = centerX - thickness / 2;
        xcoords[7] = xcoords[6];
        xcoords[8] = centerX - radius;
        xcoords[9] = xcoords[8];
        xcoords[10] = xcoords[6];
        xcoords[11] = xcoords[6];
        return xcoords;
    }

    private static int[] yCoordsForCross(int centerY, int radius, int thickness) {
        int[] ycoords = new int[12];
        ycoords[0] = centerY + radius;
        ycoords[1] = centerY + thickness / 2;
        ycoords[2] = ycoords[1];
        ycoords[3] = centerY - thickness / 2;
        ycoords[4] = ycoords[3];
        ycoords[5] = centerY - radius;
        ycoords[6] = ycoords[5];
        ycoords[7] = ycoords[3];
        ycoords[8] = ycoords[3];
        ycoords[9] = ycoords[1];
        ycoords[10] = ycoords[1];
        ycoords[11] = ycoords[0];
        return ycoords;
    }

    /**
     * Draws a test sheet consisting of a number of tiles, each one testing one or
     * more features of Java2D.
     *
     * @param g2  the graphics target.
     */
    private static void drawTestSheet(Graphics2D g2) {
        int row = -1;
        Rectangle2D bounds = new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT);

        row++; // ***** RECTANGLE2D
        Rectangle2D rect = new Rectangle2D.Double(5, 5, TILE_WIDTH - 10, TILE_HEIGHT - 10);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, rect, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, rect, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, rect, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, rect, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, rect, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // ***** ROUNDRECTANGLE2D
        RoundRectangle2D roundRect = new RoundRectangle2D.Double(5, 5, TILE_WIDTH - 10, TILE_HEIGHT - 10, 8.0, 12.0);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, roundRect, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, roundRect, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, roundRect, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, roundRect, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, roundRect, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++; // ***** LINE2D
        moveTo(0, row, g2);
        ShapeTests.drawLines(g2, bounds, 5.0, new BasicStroke(1.0f));
        moveTo(1, row, g2);
        ShapeTests.drawLines(g2, bounds, 5.0, new BasicStroke(0.0f));
        moveTo(2, row, g2);
        ShapeTests.drawLines(g2, bounds, 5.0, new BasicStroke(3.0f));
        moveTo(3, row, g2);
        ShapeTests.drawLines(g2, bounds, 5.0, DASHED);
        moveTo(4, row, g2);
        ShapeTests.drawLines(g2, bounds, 5.0, DASHED_3);

        row++; // ***** QUADCURVE2D
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeQuadCurve2D(g2, bounds, 5.0, Color.RED, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeQuadCurve2D(g2, bounds, 5.0, null, OUTLINE, Color.RED);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeQuadCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, OUTLINE, Color.RED);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeQuadCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeQuadCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++; // ***** CUBICCURVE2D
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeCubicCurve2D(g2, bounds, 5.0, Color.RED, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeCubicCurve2D(g2, bounds, 5.0, null, OUTLINE, Color.RED);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeCubicCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, OUTLINE, Color.RED);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeCubicCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeCubicCurve2D(g2, bounds, 5.0, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // ***** ELLIPSE2D
        Ellipse2D ellipse = new Ellipse2D.Double(MARGIN, MARGIN, bounds.getWidth() - 2 * MARGIN, bounds.getHeight() - 2 * MARGIN);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, ellipse, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, ellipse, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, ellipse, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, ellipse, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, ellipse, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // ***** ARC2D PIE
        Arc2D arc = ShapeTests.createArc2D(Arc2D.PIE, 45, 270, bounds, 5);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // ***** ARC2D CHORD
        Arc2D arc2 = ShapeTests.createArc2D(Arc2D.CHORD, 210, 300, bounds, 5);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc2, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc2, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc2, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc2, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc2, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // ***** ARC2D OPEN
        Arc2D arc3 = ShapeTests.createArc2D(Arc2D.OPEN, -45, 270, bounds, 5);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc3, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc3, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc3, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc3, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, arc3, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++; // ***** GeneralPATH
        Path2D path = ShapeTests.createPath2D(bounds, MARGIN);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++; // ***** GeneralPATH WIND_NON_ZERO FILL
        path.setWindingRule(Path2D.WIND_NON_ZERO);
        moveTo(0, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.BLUE, null, null);
        moveTo(1, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, null, OUTLINE, Color.BLUE);
        moveTo(2, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, OUTLINE, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, DASHED, Color.BLACK);
        moveTo(4, row, g2);
        ShapeTests.fillAndStrokeShape(g2, path, Color.LIGHT_GRAY, DASHED_3, Color.BLACK);

        row++;  // *****
        moveTo(0, row, g2);
        ShapeTests.drawAndFillArea(g2, ShapeTests.createCombinedArea("add", new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0));
        moveTo(1, row, g2);
        ShapeTests.drawAndFillArea(g2, ShapeTests.createCombinedArea("intersect", new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0));
        moveTo(2, row, g2);
        ShapeTests.drawAndFillArea(g2, ShapeTests.createCombinedArea("subtract", new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0));
        moveTo(3, row, g2);
        ShapeTests.drawAndFillArea(g2, ShapeTests.createCombinedArea("exclusiveOr", new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0));

        row++;  // ***** LINES SPECIAL
        moveTo(0, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, Color.BLACK);
        moveTo(1, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,1.0f, Color.BLACK);
        moveTo(2, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0f,0.0f, Color.BLACK);
        moveTo(3, row, g2);
        ShapeTests.drawLineCapAndDash(g2, bounds, 5.0f, 5.0);
        moveTo(4, row, g2);
        ShapeTests.drawLineCapAndDash(g2, bounds, 1.0f, 5.0);
        moveTo(5, row, g2);
        ShapeTests.drawLineCapAndDash(g2, bounds, 0.0f, 5.0);

        row++;  // ***** ALPHACOMPOSITE
        // show a set of tiles with standard AlphaComposite settings
        moveTo(0, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.Clear, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(1, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.Src, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(2, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.SrcOver, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(3, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.DstOver, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(4, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.SrcIn, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(5, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.DstIn, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(6, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.SrcOut, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(7, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.DstOut, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(8, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.Dst, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(9, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.SrcAtop, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(10, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.DstAtop, new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);

        row++;  // ***** ALPHACOMPOSITE
        // show a set of tiles with standard AlphaComposite settings
        moveTo(0, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(1, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.SRC, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(2, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(3, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(4, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(5, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST_IN, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(6, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(7, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(8, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(9, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);
        moveTo(10, row, g2);
        ShapeTests.drawShapesWithAlphaComposite(g2, AlphaComposite.getInstance(AlphaComposite.DST_ATOP, 0.6f), new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH, TILE_HEIGHT), 5.0);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        row++;  // ***** GRADIENTPAINT
        moveTo(0, row, g2);
        GradientPaint gp = new GradientPaint(0f, 0f, Color.YELLOW, TILE_WIDTH, 0f, Color.RED);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp, null, null);

        // here we change the gradient to start one quarter of the way across the shape
        // and finish at the three quarter mark - the default should be non-cyclic
        moveTo(1, row, g2);
        float p = TILE_WIDTH / 4.0f;
        GradientPaint gp2 = new GradientPaint(p, 0f, Color.YELLOW, p * 3, 0f, Color.RED);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp2, null, null);

        moveTo(2, row, g2);
        GradientPaint gp3 = new GradientPaint(p, 0f, Color.YELLOW, p * 3, 0f, Color.RED, true);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp3, null, null);

        moveTo(3, row, g2);
        float delta = (TILE_WIDTH - 10) / 6f;
        LinearGradientPaint lgp1 = new LinearGradientPaint(10f, 0f, TILE_WIDTH - 10 , 0f, new float[] { 0f, 1/6f, 2/6f, 3/6f, 4/6f, 5/6f, 1f}, RAINBOW_COLORS);
        ShapeTests.fillAndStrokeShape(g2, roundRect, lgp1, null, null);

        moveTo(4, row, g2);
        GradientPaint gp4 = new GradientPaint(0f, 0f, Color.YELLOW, TILE_WIDTH, TILE_HEIGHT, Color.RED);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp4, null, null);

        // here we change the gradient to start one quarter of the way across the shape
        // and finish at the three quarter mark - the default should be non-cyclic
        moveTo(5, row, g2);
        GradientPaint gp5 = new GradientPaint(p, 0f, Color.YELLOW, p * 3, TILE_HEIGHT, Color.RED);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp5, null, null);

        moveTo(6, row, g2);
        GradientPaint gp6 = new GradientPaint(p, 0f, Color.YELLOW, p * 3, TILE_HEIGHT, Color.RED, true);
        ShapeTests.fillAndStrokeShape(g2, roundRect, gp6, null, null);

        moveTo(7, row, g2);
        LinearGradientPaint lgp2 = new LinearGradientPaint(10f, 0f, TILE_WIDTH - 10 , TILE_HEIGHT, new float[] { 0f, 1/6f, 2/6f, 3/6f, 4/6f, 5/6f, 1f}, RAINBOW_COLORS);
        ShapeTests.fillAndStrokeShape(g2, roundRect, lgp2, null, null);

        row++;  // ***** LINES WITH GRADIENT PAINT
        moveTo(0, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp);
        moveTo(1, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp2);
        moveTo(2, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp3);
        moveTo(3, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, lgp1);
        moveTo(4, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp4);
        moveTo(5, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp5);
        moveTo(6, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, gp6);
        moveTo(7, row, g2);
        ShapeTests.drawLineCaps(g2, bounds, 5.0,5.0f, lgp2);

        row++;  // ***** RADIAL GRADIENT PAINT
        moveTo(0, row, g2);
        Point2D center = new Point2D.Double(TILE_WIDTH / 2, TILE_HEIGHT / 2);
        RadialGradientPaint rgp = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY});
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp, null, null);

        moveTo(1, row, g2);
        RadialGradientPaint rgp2 = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY}, MultipleGradientPaint.CycleMethod.REPEAT);
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp2, null, null);

        moveTo(2, row, g2);
        RadialGradientPaint rgp3 = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY}, MultipleGradientPaint.CycleMethod.REFLECT);
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp3, null, null);

        moveTo(3, row, g2);
        Point2D focus = new Point2D.Double(TILE_WIDTH / 3, TILE_HEIGHT / 3);
        RadialGradientPaint rgp4 = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), focus, new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY}, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp4, null, null);

        moveTo(4, row, g2);
        RadialGradientPaint rgp5 = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), focus, new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY}, MultipleGradientPaint.CycleMethod.REPEAT);
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp5, null, null);

        moveTo(5, row, g2);
        RadialGradientPaint rgp6 = new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), focus, new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.LIGHT_GRAY}, MultipleGradientPaint.CycleMethod.REFLECT);
        ShapeTests.fillAndStrokeShape(g2, roundRect, rgp6, null, null);

        row++; // ***** TRANSLATION
        moveTo(0, row, g2);
        Rectangle2D rectToTranslate = new Rectangle2D.Double(5, 5, TILE_WIDTH / 2 - 10, TILE_HEIGHT / 2 - 10);
        TransformTests.translateShape(g2, bounds, rectToTranslate, Color.YELLOW, new BasicStroke(1.0f), Color.BLACK);
        moveTo(1, row, g2);
        RoundRectangle2D roundRectToTranslate = new RoundRectangle2D.Double(5, 5, TILE_WIDTH / 2 - 10, TILE_HEIGHT / 2 - 10, 8, 8);
        TransformTests.translateShape(g2, bounds, roundRectToTranslate, Color.ORANGE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(2, row, g2);
        QuadCurve2D quadCurveToTranslate = ShapeTests.createQuadCurve2D(new Rectangle2D.Double(0, 0, TILE_WIDTH / 2, TILE_HEIGHT / 2), 3);
        TransformTests.translateShape(g2, bounds, quadCurveToTranslate, Color.YELLOW, new BasicStroke(1.0f), Color.BLACK);
        moveTo(3, row, g2);
        CubicCurve2D cubicCurveToTranslate = ShapeTests.createCubicCurve2D(new Rectangle2D.Double(0, 0, TILE_WIDTH / 2, TILE_HEIGHT / 2), 3);
        TransformTests.translateShape(g2, bounds, cubicCurveToTranslate, Color.ORANGE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(4, row, g2);
        Ellipse2D ellipseToTranslate = new Ellipse2D.Double(5, 5, TILE_WIDTH / 2 - 10, TILE_HEIGHT / 2 - 10);
        TransformTests.translateShape(g2, bounds, ellipseToTranslate, Color.YELLOW, new BasicStroke(1.0f), Color.BLACK);
        moveTo(5, row, g2);
        Arc2D arcToTranslate = new Arc2D.Double(new Rectangle2D.Double(5, 5, TILE_WIDTH / 2 - 10, TILE_HEIGHT / 2 - 10), 45, 290, Arc2D.PIE);
        TransformTests.translateShape(g2, bounds, arcToTranslate, Color.ORANGE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(6, row, g2);
        Area areaToTranslate = ShapeTests.createCombinedArea("exclusiveOr", new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH / 2, TILE_HEIGHT / 2), 2.5);
        TransformTests.translateShape(g2, bounds, areaToTranslate, Color.YELLOW, new BasicStroke(1.0f), Color.BLACK);
        moveTo(7, row, g2);
        Path2D pathToTranslate = ShapeTests.createPath2D(new Rectangle2D.Double(0.0, 0.0, TILE_WIDTH / 2, TILE_HEIGHT / 2), 2.5);
        TransformTests.translateShape(g2, bounds, pathToTranslate, Color.ORANGE, new BasicStroke(1.0f), Color.BLACK);

        row++; // ***** ROTATION
        moveTo(0, row, g2);
        double m = 0.33 * TILE_HEIGHT;
        Rectangle2D rectToRotate = new Rectangle2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2);
        TransformTests.rotateShape(g2, bounds, rectToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(1, row, g2);
        RoundRectangle2D roundRectToRotate = new RoundRectangle2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2, 8, 8);
        TransformTests.rotateShape(g2, bounds, roundRectToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(2, row, g2);
        QuadCurve2D quadCurveToRotate = ShapeTests.createQuadCurve2D(new Rectangle2D.Double(0, 0, TILE_WIDTH, TILE_HEIGHT),15);
        TransformTests.rotateShape(g2, bounds, quadCurveToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(3, row, g2);
        CubicCurve2D cubicCurveToRotate = ShapeTests.createCubicCurve2D(new Rectangle2D.Double(0, 0, TILE_WIDTH, TILE_HEIGHT), 15);
        TransformTests.rotateShape(g2, bounds, cubicCurveToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(4, row, g2);
        Ellipse2D ellipseToRotate = new Ellipse2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2);
        TransformTests.rotateShape(g2, bounds, ellipseToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
        moveTo(5, row, g2);
        Arc2D arcToRotate = new Arc2D.Double(new Rectangle2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2), 45, 290, Arc2D.PIE);
        TransformTests.rotateShape(g2, bounds, arcToRotate, Math.PI / 4, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);

//        row++; // ***** SHEAR
//        moveTo(0, row, g2);
//        double mm = 0.33 * TILE_HEIGHT;
//        Rectangle2D rectToSkew = new Rectangle2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2);
//        TransformTests.shearShape(g2, bounds, rectToSkew, 0.5, -0.5, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
//        moveTo(4, row, g2);
//        Ellipse2D ellipseToSkew = new Ellipse2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2);
//        TransformTests.shearShape(g2, bounds, ellipseToSkew, 0.5, -0.5, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);
//        moveTo(5, row, g2);
//        Arc2D arcToSkew = new Arc2D.Double(new Rectangle2D.Double(m, m, TILE_WIDTH - m*2, TILE_HEIGHT - m*2), 45, 290, Arc2D.PIE);
//        TransformTests.shearShape(g2, bounds, arcToSkew, 0.5, -0.5, Color.BLUE, new BasicStroke(1.0f), Color.BLACK);

        row++;  // ***** IMAGE
        moveTo(0, row, g2);
        ImageTests.drawImage(g2, bounds, 5);

        row++;  // ***** STRINGS & FONTS
        moveTo(0, row, g2);
        FontTests.drawString(g2);
        moveTo(3, row, g2);
        FontTests.drawStringBounds(g2, bounds);

        row++;  // ***** CLIPPING
        moveTo(0, row, g2);
        ClippingTests.fillRectangularClippingRegions(g2, bounds);

        moveTo(1, row, g2);
        ClippingTests.drawArc2DWithRectangularClip(g2, bounds, 5);

    }

    /**
     * Draws a single tile - useful for testing just one feature.
     *
     * @param g2  the graphics target.
     */
    private static void drawTestSingle(Graphics2D g2) {
        moveTo(0, 0, g2);
        Rectangle2D bounds = new Rectangle2D.Double(0, 0, TILE_WIDTH, TILE_HEIGHT);
        Point2D center = new Point2D.Double(TILE_WIDTH / 2, TILE_HEIGHT / 2);
        //fillRectangle(g2, new RadialGradientPaint(center, (float) (TILE_HEIGHT / 2.0 - 5), new float[] {0.0f, 0.75f, 1.0f}, new Color[] {Color.YELLOW, Color.RED, Color.GRAY}));
        //drawAxes(g2, center, 40.0, new BasicStroke(1f), Color.GRAY);
        drawTilePolygon(g2);
    }

    /**
     * Renders the test output (checks whether generating the whole test
     * sheet or just one single test).
     *
     * @param g2  the graphics target.
     * @param single  set to true if just generating a single test
     */
    private static void drawTestOutput(Graphics2D g2, boolean single) {
        if (single) {
            drawTestSingle(g2);
        } else {
            drawTestSheet(g2);
        }
    }

    /**
     * Run the tests with SkijaGraphics2D.
     *
     * @param fileName  the base filename.
     * @param single  run the current single test?
     */
    public static void testSkijaGraphics2D(String fileName, boolean single) {
        SkijaGraphics2D g2 = new SkijaGraphics2D(TILE_COUNT_H * TILE_WIDTH, TILE_COUNT_V * TILE_HEIGHT);
        drawTestOutput(g2, single);
        org.jetbrains.skija.Image image = g2.getSurface().makeImageSnapshot();
        Data pngData = image.encodeToData(EncodedImageFormat.PNG);
        byte [] pngBytes = pngData.getBytes();
        try {
            if (single) {
                fileName += "-single.png";
            } else {
                fileName += ".png";
            }
            java.nio.file.Path path = java.nio.file.Path.of(fileName);
            java.nio.file.Files.write(path, pngBytes);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Run the tests with a Graphics2D from a Java2D BufferedImage and save
     * the results to the specified file.
     *
     * @param fileName  the PNG file name.
     *
     * @throws IOException
     */
    public static void testJava2D(String fileName, boolean single) throws IOException {
        BufferedImage image = new BufferedImage(TILE_WIDTH * TILE_COUNT_H, TILE_HEIGHT * TILE_COUNT_V, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTestOutput(g2, single);
        if (single) {
            fileName += "-single.png";
        } else {
            fileName += ".png";
        }
        ImageIO.write(image, "png", new File(fileName));
    }

    public static void testJFreeSVG(String filename, boolean single) throws IOException {
        SVGGraphics2D g2 = new SVGGraphics2D(TILE_WIDTH * TILE_COUNT_H, TILE_HEIGHT * TILE_COUNT_V);
        drawTestOutput(g2, single);
        if (single) {
            filename += "-single.svg";
        } else {
            filename += ".svg";
        }
        SVGUtils.writeToSVG(new File(filename), g2.getSVGElement());
    }

    /**
     * Creates Java2D output that exercises many features of the API.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        boolean single = false;
        testSkijaGraphics2D("skija", single);
        //testJFreeSVG("jfreesvg", single);
        testJava2D("java2D", single);
    }

}