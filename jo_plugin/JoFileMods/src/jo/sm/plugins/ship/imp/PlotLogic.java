/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.plugins.ship.imp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ResourceUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.BlockTypeColors;
import jo.util.Paths;
import jo.vecmath.Color3f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point2fLogic;
import jo.vecmath.logic.Point3fLogic;
import jo.vecmath.logic.Point3iLogic;
import jo.vecmath.logic.ext.Hull3fLogic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class PlotLogic {
    private static short[] HULL_IDS = null;
    private static int[] HULL_RGBS = null;
    private static long mLastRead = 0;
    private static final Logger log = Logger.getLogger(PlotLogic.class.getName());

    public static void mapHull(SparseMatrix<Block> modified, Hull3f hull,
            Point3f scale, Point3i lowerGrid, Point3i upperGrid, IPluginCallback cb) {
        Point3i center;
        center = Point3iLogic.interpolate(lowerGrid, upperGrid, .5f);
        cb.startTask(hull.getTriangles().size());
        for (Triangle3f t : hull.getTriangles()) {
            Point3i iA = mapPoint(t.getA(), scale, center);
            Point3i iB = mapPoint(t.getB(), scale, center);
            Point3i iC = mapPoint(t.getC(), scale, center);
            if ((t.getAUV() != null) && (t.getBUV() != null) && (t.getCUV() != null) && (t.getTexture() != null)) {
                drawTriangle(modified, iA, iB, iC, t.getAUV(), t.getBUV(), t.getCUV(), t.getTexture());
            } else if (t.getColor() != null) {
                short color;
                color = mapColor(t.getColor());
                drawTriangle(modified, iA, iB, iC, color);
            } else {
                drawTriangle(modified, iA, iB, iC, BlockTypes.HULL_COLOR_GREY_ID);
            }
            cb.workTask(1);
        }
        ShipLogic.ensureCore(modified);
        cb.endTask();
    }

    private static short uvToColor(Point2f uv, BufferedImage img) {
        uv.x -= Math.floor(uv.x);
        uv.y -= Math.floor(uv.y);
        int x;
        x = (int) MathUtils.interpolate(uv.x, 0, 1, 0, img.getWidth() - 1);
        int y;
        y = (int) MathUtils.interpolate(uv.y, 0, 1, 0, img.getHeight() - 1);
        int rgb;
        rgb = img.getRGB(x, y);
        return mapColor(rgb);
    }

    private static Point3i mapPoint(Point3f f, Point3f scale, Point3i center) {
        Point3f fA;
        fA = new Point3f(f);
        fA = Point3fLogic.scale(fA, scale);
        Point3i iA;
        iA = new Point3i(fA);
        iA.sub(center);
        iA.x += 8;
        iA.y += 8;
        iA.z += 8;
        return iA;
    }

    public static void drawTriangle(SparseMatrix<Block> grid, Point3i a, Point3i b, Point3i c, short color) {
        // Brute force and ignorance method
        // Anything smarter seems to leave holes
        doDrawTriangle(grid, a, b, c, color);
        doDrawTriangle(grid, b, c, a, color);
        doDrawTriangle(grid, c, a, b, color);
    }

    private static void doDrawTriangle(SparseMatrix<Block> grid,
            Point3i fulcrum, Point3i target1, Point3i target2, short color) {
        List<Point3i> iterpolate;
        iterpolate = new ArrayList<>();
        plotLine(target1, target2, iterpolate);
        Set<Point3i> area;
        area = new HashSet<>();
        log.log(Level.INFO, "interpolate over");
        for (Point3i i : iterpolate) {
            log.log(Level.INFO, " "+i);
            plotLine(i, fulcrum, area);
        }
        plotArea(grid, area, color);
    }

    public static void drawTriangle(SparseMatrix<Block> grid, Point3i a, Point3i b, Point3i c, Point2f auv, Point2f buv, Point2f cuv, BufferedImage img) {
        // Brute force and ignorance method
        // Anything smarter seems to leave holes
        doDrawTriangle(grid, a, b, c, auv, buv, cuv, img);
        doDrawTriangle(grid, b, c, a, buv, cuv, auv, img);
        doDrawTriangle(grid, c, a, b, cuv, auv, buv, img);
    }

    private static void doDrawTriangle(SparseMatrix<Block> grid,
            Point3i fulcrum, Point3i target1, Point3i target2,
            Point2f fulcrumUV, Point2f target1UV, Point2f target2UV,
            BufferedImage img) {
        List<Point3i> iterpolate;
        iterpolate = new ArrayList<>();
        plotLine(target1, target2, iterpolate);
        for (int i = 0; i < iterpolate.size(); i++) {
            Point3i p = iterpolate.get(i);
            Point2f puv = Point2fLogic.interpolate(target1UV, target2UV, i / (float) (iterpolate.size() - 1));
            drawLine(grid, p, fulcrum, puv, fulcrumUV, img);
        }
    }

    private static void plotArea(SparseMatrix<Block> grid, Collection<Point3i> area,
            short color) {
        for (Point3i p : area) {
            grid.set(p, new Block(color));
        }
    }

    public static void drawLine(SparseMatrix<Block> grid, Point3i a, Point3i b, short color) {
        List<Point3i> plot;
        plot = new ArrayList<>();
        plotLine(a, b, plot);
        plotArea(grid, plot, color);
    }

    public static void drawLine(SparseMatrix<Block> grid, Point3i a, Point3i b, Point2f auv, Point2f buv, BufferedImage img) {
        List<Point3i> plot;
        plot = new ArrayList<>();
        plotLine(a, b, plot);
        for (int i = 0; i < plot.size(); i++) {
            Point3i p = plot.get(i);
            Point2f uv = Point2fLogic.interpolate(auv, buv, i / (float) (plot.size() - 1));
            short color;
            color = uvToColor(uv, img);
            grid.set(p, new Block(color));
        }
    }

    private static void plotLine(Point3i a, Point3i b, Collection<Point3i> plot) {
        Point3f vector;
        vector = new Point3f(b);
        Point3f p;
        p = new Point3f(a);
        vector.sub(p);
        float mag;
        mag = Point3fLogic.mag(vector);
        int steps;
        steps = (int) mag;
        vector.scale(1 / mag);
        Point3i last;
        last = null;
        for (int i = 0; i < steps; i++) {
            last = new Point3i(p);
            plot.add(last);
            p.add(vector);
        }
        if (!b.equals(last)) {
            plot.add(new Point3i(b));
        }
    }

    public static float getScale(Hull3f hull, int longestDimension, Point3i lowerGrid, Point3i upperGrid, Point3i offset) {
        Point3f lowerModel;
        lowerModel = new Point3f();
        Point3f upperModel;
        upperModel = new Point3f();
        Hull3fLogic.getBounds(hull, lowerModel, upperModel);
        log.log(Level.INFO, "Model Bounds: " + lowerModel + " -- " + upperModel);
        float longestModel;
        longestModel = Math.max(upperModel.x - lowerModel.x, Math.max(upperModel.y - lowerModel.y, upperModel.z - lowerModel.z));
        float scale;
        scale = longestDimension / longestModel;
        log.log(Level.INFO, "Scale: " + scale + " (" + longestDimension + "/" + longestModel);
        lowerGrid.x = (int) Math.floor(lowerModel.x * scale);
        lowerGrid.y = (int) Math.floor(lowerModel.y * scale);
        lowerGrid.z = (int) Math.floor(lowerModel.z * scale);
        upperGrid.x = (int) Math.ceil(upperModel.x * scale);
        upperGrid.y = (int) Math.ceil(upperModel.y * scale);
        upperGrid.z = (int) Math.ceil(upperModel.z * scale);
        log.log(Level.INFO, "Grid Bounds: " + lowerGrid + " -- " + upperGrid);
        return scale;
    }


    public static int distance(int rgb1, int rgb2) {
        float [] buffer1 = new float[3];
        float [] buffer2 = new float[3];
        Color c1 = new Color(rgb1);
        Color c2 = new Color(rgb2);
        c1.getRGBColorComponents(buffer1);
        c2.getRGBColorComponents(buffer2);
        int distSquare = 0;
        for (int i = 0; i < 3; i++) {
            distSquare += (buffer1[i] - buffer2[i]) * (buffer1[i] - buffer2[i]);
        }
        return distSquare;
    }

    private static void loadColors() {
        File plugins = new File(Paths.getPluginsDirectory());
        File colorMap;
        colorMap = new File(plugins, "color_map.xml");
        if (colorMap.exists()) {
            if (colorMap.lastModified() <= mLastRead) {
                return;
            }
            try {
                readColorFile(new FileInputStream(colorMap));
                mLastRead = colorMap.lastModified();
                return;
            } catch (FileNotFoundException e) {
                log.log(Level.WARNING, "File Input Stream failed!", e);
            }
        }
        if (HULL_IDS == null) {
            readColorFile(ResourceUtils.loadSystemResourceStream("color_map.xml", PlotLogic.class));
        }
    }

    private static void readColorFile(InputStream is) {
        Document doc;
        doc = XMLUtils.readStream(is);
        if (doc == null) {
            return;
        }
        List<Short> hullIDs;
        hullIDs = new ArrayList<>();
        List<Integer> hullRGBs;
        hullRGBs = new ArrayList<>();
        for (Node n : XMLUtils.findNodes(doc, "colors/color")) {
            int rgb;
            rgb = Integer.parseInt(XMLUtils.getAttribute(n, "rgb"), 16);
            short block;
            String smBlock;
            smBlock = XMLUtils.getAttribute(n, "block");
            if (BlockTypeColors.mBlockTypes.containsKey(smBlock)) {
                block = ShortUtils.parseShort(BlockTypeColors.mBlockTypes.getProperty(smBlock));
            } else {
                block = ShortUtils.parseShort(smBlock);
            }
            hullIDs.add(block);
            hullRGBs.add(rgb);
        }
        HULL_IDS = ShortUtils.toShortArray(hullIDs.toArray());
        HULL_RGBS = IntegerUtils.toArray(hullRGBs.toArray());
    }

    public static short mapColor(int rgb) {
        loadColors();
        int best;
        best = 0;
        int value;
        value = distance(rgb, HULL_RGBS[best]);
        for (int i = 1; i < HULL_RGBS.length; i++) {
            int v;
            v = distance(rgb, HULL_RGBS[i]);
            if (v < value) {
                best = i;
                value = v;
            }
        }
        return HULL_IDS[best];
    }

    public static short mapColor(Color3f c) {
        int r;
        r = (int) (c.x * 255);
        int g;
        g = (int) (c.y * 255);
        int b;
        b = (int) (c.z * 255);
        int rgb;
        rgb = (r << 16) | (g << 8) | (b);
        short color;
        color = mapColor(rgb);
        log.log(Level.INFO, c+" -> "+r+","+g+","+b+" -> "+Integer.toHexString(rgb)+" -> "+color);
        return color;
    }

    public static Hull3f makeTorus(float resolution) {
        Hull3f torus;
        torus = new Hull3f();
        int longSteps;
        longSteps = (int) (2 * Math.PI * 1 / resolution);
        float longIncrement;
        longIncrement = (float) (2 * Math.PI / longSteps);
        int shortSteps;
        shortSteps = (int) (2 * Math.PI * .25 / resolution);
        float shortIncrement;
        shortIncrement = (float) (2 * Math.PI / shortSteps);
        for (int longSegment = 0; longSegment < longSteps; longSegment++) {
            Point3f longCenter1;
            longCenter1 = new Point3f(.75f, 0, 0);
            Point3fLogic.rotateBy(longCenter1, 0, longSegment * longIncrement, 0);
            Point3f longCenter2;
            longCenter2 = new Point3f(.75f, 0, 0);
            Point3fLogic.rotateBy(longCenter2, 0, (longSegment + 1) * longIncrement, 0);
            for (int shortSegment = 0; shortSegment < shortSteps; shortSegment++) {
                Point3f shortSpur11;
                shortSpur11 = new Point3f(.25f, 0, 0);
                Point3fLogic.rotateBy(shortSpur11, 0, 0, shortSegment * shortIncrement);
                Point3fLogic.rotateBy(shortSpur11, 0, longSegment * longIncrement, 0);
                shortSpur11.add(longCenter1);
                Point3f shortSpur12;
                shortSpur12 = new Point3f(.25f, 0, 0);
                Point3fLogic.rotateBy(shortSpur12, 0, 0, (shortSegment + 1) * shortIncrement);
                Point3fLogic.rotateBy(shortSpur12, 0, longSegment * longIncrement, 0);
                shortSpur12.add(longCenter1);
                Point3f shortSpur21;
                shortSpur21 = new Point3f(.25f, 0, 0);
                Point3fLogic.rotateBy(shortSpur21, 0, 0, shortSegment * shortIncrement);
                Point3fLogic.rotateBy(shortSpur21, 0, longSegment * longIncrement, 0);
                shortSpur21.add(longCenter2);
                Point3f shortSpur22;
                shortSpur22 = new Point3f(.25f, 0, 0);
                Point3fLogic.rotateBy(shortSpur22, 0, 0, (shortSegment + 1) * shortIncrement);
                Point3fLogic.rotateBy(shortSpur22, 0, longSegment * longIncrement, 0);
                shortSpur22.add(longCenter2);
                addTriangle(torus, shortSpur11, shortSpur12, shortSpur22, resolution);
                addTriangle(torus, shortSpur11, shortSpur21, shortSpur22, resolution);
            }
        }
        return torus;
    }

    public static Hull3f makeSphere(float resolution, boolean half) {
        Hull3f sphere;
        sphere = new Hull3f();
        Point3f fore;
        fore = new Point3f(0, 0, 1);
        Point3f aft;
        aft = new Point3f(0, 0, -1);
        Point3f dorsal;
        dorsal = new Point3f(0, 1, 0);
        Point3f ventral;
        ventral = new Point3f(0, -1, 0);
        Point3f starboard;
        starboard = new Point3f(1, 0, 0);
        Point3f port;
        port = new Point3f(-1, 0, 0);
        if (!half) {
            addTriangle(sphere, fore, port, dorsal, resolution);
            addTriangle(sphere, port, aft, dorsal, resolution);
            addTriangle(sphere, aft, starboard, dorsal, resolution);
            addTriangle(sphere, starboard, fore, dorsal, resolution);
        }
        addTriangle(sphere, fore, port, ventral, resolution);
        addTriangle(sphere, port, aft, ventral, resolution);
        addTriangle(sphere, aft, starboard, ventral, resolution);
        addTriangle(sphere, starboard, fore, ventral, resolution);
        return sphere;
    }

    private static void addTriangle(Hull3f sphere, Point3f p1, Point3f p2,
            Point3f p3, float resolution) {
        log.log(Level.INFO, "Adding "+p1+", "+p2+", "+p3);
        if ((p1.distance(p2) < resolution) || (p2.distance(p3) < resolution) || (p3.distance(p1) < resolution)) {
            Triangle3f t;
            t = new Triangle3f(p1, p2, p3);
            sphere.getTriangles().add(t);
        } else {
            Point3f p12;
            p12 = new Point3f(p1);
            p12.add(p2);
            Point3fLogic.normalize(p12);
            Point3f p23;
            p23 = new Point3f(p2);
            p23.add(p3);
            Point3fLogic.normalize(p23);
            Point3f p31;
            p31 = new Point3f(p3);
            p31.add(p1);
            Point3fLogic.normalize(p31);
            addTriangle(sphere, p1, p12, p31, resolution);
            addTriangle(sphere, p2, p23, p12, resolution);
            addTriangle(sphere, p3, p31, p23, resolution);
            addTriangle(sphere, p12, p23, p31, resolution);
        }
    }
}
