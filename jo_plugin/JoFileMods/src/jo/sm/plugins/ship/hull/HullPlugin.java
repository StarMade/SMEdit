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
package jo.sm.plugins.ship.hull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.plugins.planet.hollow.HollowPlugin;
import jo.sm.plugins.ship.imp.PlotLogic;
import jo.sm.plugins.ship.rotate.RotateParameters;
import jo.sm.plugins.ship.rotate.RotatePlugin;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point3iLogic;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class HullPlugin implements IBlocksPlugin {

    public static final String NAME = "Hull";
    public static final String DESC = "Generate Basic Hull";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_GENERATE},
                {TYPE_STATION, SUBTYPE_GENERATE},};

    private static final Random mRND = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getAuthor() {
        return AUTH;
    }

    @Override
    public Object newParameterBean() {
        return new HullParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        HullParameters params;
        params = (HullParameters) p;
        Point3i center;
        Point3i lower;
        Point3i upper;
        SparseMatrix<Block> modified;
        if ((sm.getSelectedLower() == null) || (sm.getSelectedUpper() == null)) {
            center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
            lower = new Point3i(center.x - params.getSizeX() / 2, center.y - params.getSizeY() / 2, center.z - params.getSizeZ() / 2);
            upper = new Point3i(center.x + params.getSizeX() / 2, center.y + params.getSizeY() / 2, center.z + params.getSizeZ() / 2);
            modified = new SparseMatrix<>();
        } else {
            lower = sm.getSelectedLower();
            upper = sm.getSelectedUpper();
            center = Point3iLogic.interpolate(lower, upper, .5f);
            modified = new SparseMatrix<>(original);
        }
        switch (params.getType()) {
            case HullParameters.OPEN_FRAME:
                generateOpenFrame(modified, center, lower, upper, cb);
                break;
            case HullParameters.NEEDLE:
                generateNeedle(modified, center, lower, upper, cb);
                break;
            case HullParameters.CONE:
                generateCone(modified, center, lower, upper, cb);
                break;
            case HullParameters.CYLINDER:
                generateCylinder(modified, center, lower, upper, cb);
                break;
            case HullParameters.BOX:
                generateBox(modified, center, lower, upper, cb);
                break;
            case HullParameters.SPHERE:
                generateSphere(modified, center, lower, upper, cb, false);
                break;
            case HullParameters.HEMISPHERE:
                generateSphere(modified, center, lower, upper, cb, true);
                break;
            case HullParameters.DISC:
                generateDisc(modified, center, lower, upper, cb);
                break;
            case HullParameters.IRREGULAR:
                generateIrregular(modified, center, lower, upper, cb);
                break;
            case HullParameters.TORUS:
                generateTorus(modified, center, lower, upper, cb);
                break;
        }
        HollowPlugin.doHollow(modified, cb);
        if (ShipLogic.findCore(modified) == null) {
            modified.set(params.getCenterX(), params.getCenterY(), params.getCenterZ(), new Block(BlockTypes.CORE_ID));
        }
        return modified;
    }

    private void generateBox(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling Box");
        cb.startTask(PluginUtils.getVolume(lower, upper));
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            Point3i p = i.next();
            addHull(grid, p);
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void generateTorus(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling torus");
        Point3f radius = new Point3f(upper.x - lower.x, upper.y - lower.y, upper.z - lower.z);
        radius.scale(.5f);
        float maxDiam = Math.max(Math.max(radius.x, radius.y), radius.z);
        float resolution = 2.5f / maxDiam;
        Hull3f torus = PlotLogic.makeTorus(resolution);
        PlotLogic.mapHull(grid, torus, radius, new Point3i(), new Point3i(), cb);
    }

    private void generateSphere(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb, boolean hemi) {
        cb.setStatus("Filling sphere");
        Point3f radius = new Point3f(upper.x - lower.x, upper.y - lower.y, upper.z - lower.z);
        radius.scale(0.50f);
        float maxDiam = Math.max(Math.max(radius.x, radius.y), radius.z);
        float resolution = 2.50f / maxDiam;
        Hull3f sphere = PlotLogic.makeSphere(resolution, hemi);
        PlotLogic.mapHull(grid, sphere, radius, new Point3i(), new Point3i(), cb);
    }

    private void generateCone(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling Cone");
        cb.startTask(upper.z - lower.z + 1);
        for (int z = lower.z; z <= upper.z; z++) {
            cb.workTask(1);
            int xRad = (int) MathUtils.interpolate(z, lower.z, upper.z, (upper.x - lower.x) / 2, 0);
            int yRad = (int) MathUtils.interpolate(z, lower.z, upper.z, (upper.y - lower.y) / 2, 0);
            drawElipse(grid, center.x, center.y, z, xRad, yRad);
        }
        cb.endTask();
    }

    private void generateNeedle(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling Cone");
        cb.startTask(upper.z - lower.z + 1);
        int xMidRad;
        xMidRad = center.x - lower.x;
        int yMidRad;
        yMidRad = center.y - lower.y;
        int xRad;
        int yRad;
        for (int z = lower.z; z <= upper.z; z++) {
            if (z < center.z) {
                xRad = (int) MathUtils.interpolate(z, lower.z, center.z, 0, xMidRad);
                yRad = (int) MathUtils.interpolate(z, lower.z, center.z, 0, yMidRad);
            } else {
                xRad = (int) MathUtils.interpolate(z, center.z, upper.z, xMidRad, 0);
                yRad = (int) MathUtils.interpolate(z, center.z, upper.z, yMidRad, 0);
            }
            drawElipse(grid, center.x, center.y, z, xRad, yRad);
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void generateCylinder(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling Cylinder");
        cb.startTask(upper.z - lower.z + 1);
        int xRad;
        xRad = (upper.x - lower.x) / 2;
        int yRad;
        yRad = (upper.y - lower.y) / 2;
        for (int z = lower.z; z <= upper.z; z++) {
            drawElipse(grid, center.x, center.y, z, xRad, yRad);
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void generateDisc(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Filling Disk");
        cb.startTask(upper.y - lower.y + 1);
        int xRad;
        xRad = (upper.x - lower.x) / 2;
        int zRad;
        zRad = (upper.z - lower.z) / 2;
        for (int y = lower.y; y <= upper.y; y++) {
            drawElipse(grid, center.x, center.z, y, xRad, zRad);
            cb.workTask(1);
        }
        cb.endTask();
        RotateParameters params = new RotateParameters();
        params.setXRotate(90);
        SparseMatrix<Block> modified;
        modified = RotatePlugin.rotateAround(grid, params, center);
        grid.set(modified);
    }

    private void generateIrregular(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        Set<Point3i> done;
        done = new HashSet<>();
        List<Point3i> todo;
        todo = new ArrayList<>();
        todo.add(center);
        int volume;
        volume = PluginUtils.getVolume(lower, upper);
        cb.setStatus("Filling Irregular");
        cb.startTask(volume);
        for (int i = 0; i < volume; i++) {
            int idx = mRND.nextInt(todo.size());
            Point3i p = todo.get(idx);
            todo.remove(idx);
            addHull(grid, p);
            done.add(p);
            add(done, todo, p, 1, 0, 0);
            add(done, todo, p, -1, 0, 0);
            add(done, todo, p, 0, 1, 0);
            add(done, todo, p, 0, -1, 0);
            add(done, todo, p, 0, 0, 1);
            add(done, todo, p, 0, 0, -1);
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void add(Set<Point3i> done, List<Point3i> todo, Point3i p, int dx,
            int dy, int dz) {
        Point3i next;
        next = new Point3i(p.x + dx, p.y + dy, p.z + dz);
        if (done.contains(next)) {
            return;
        }
        if (!todo.contains(next)) {
            todo.add(next);
        }
    }

    private void generateOpenFrame(SparseMatrix<Block> grid, Point3i center, Point3i lower, Point3i upper, IPluginCallback cb) {
        Set<Point3i> done;
        done = new HashSet<>();
        List<Point3i> todo;
        todo = new ArrayList<>();
        todo.add(new Point3i(0, 0, 0));
        int volume;
        volume = PluginUtils.getVolume(lower, upper);
        cb.setStatus("Filling Open frame");
        cb.startTask(volume);
        for (int i = volume / (8 * 8 * 8); i > 0; i--) {
            int idx = mRND.nextInt(todo.size());
            Point3i p = todo.get(idx);
            todo.remove(idx);
            addFrame(grid, p, center);
            done.add(p);
            addFrameLink(grid, done, todo, p, center, 1, 0, 0);
            addFrameLink(grid, done, todo, p, center, -1, 0, 0);
            addFrameLink(grid, done, todo, p, center, 0, 1, 0);
            addFrameLink(grid, done, todo, p, center, 0, -1, 0);
            addFrameLink(grid, done, todo, p, center, 0, 0, 1);
            addFrameLink(grid, done, todo, p, center, 0, 0, -1);
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void addFrameLink(SparseMatrix<Block> grid, Set<Point3i> done, List<Point3i> todo, Point3i p, Point3i center, int dx, int dy, int dz) {
        add(done, todo, p, dx, dy, dz);
        Point3i next = new Point3i(p.x + dx, p.y + dy, p.z + dz);
        if (!done.contains(next)) {
            return;
        }
        Point3i planeX = new Point3i();
        Point3i planeY = new Point3i();
        Point3i axisZ = new Point3i(dx, dy, dz);
        Point3i o = new Point3i(center.x + p.x * 12, center.y + p.y * 12, center.z + p.z * 12);
        if (dx == 0) {
            planeX.x = 1;
            if (dy == 0) {
                planeY.y = 1;
            } else {
                planeY.z = 1;
            }
        } else {
            planeX.y = 1;
            planeY.z = 1;
        }
        if (dx == 1) {
            o.x += 7;
        } else if (dy == 1) {
            o.y += 7;
        } else if (dz == 1) {
            o.z += 7;
        }
        for (int z = 0; z <= 5; z++) {
            int skip = ((z == 0) || (z == 5)) ? 1 : 3;
            int rad = ((z == 0) || (z == 5)) ? 6 : 2;
            short type = ((z == 0) || (z == 5)) ? BlockTypes.HULL_COLOR_BLUE_ID : BlockTypes.HULL_COLOR_WHITE_ID;
            Point3i squareO = new Point3i(o);
            for (int s = 0; s < skip; s++) {
                squareO.add(planeX);
                squareO.add(planeY);
            }
            for (int x = 0; x < rad; x++) {
                Point3i squareOO = new Point3i(squareO);
                for (int y = 0; y < rad; y++) {
                    addHull(grid, squareOO, type);
                    squareOO.add(planeY);
                }
                squareO.add(planeX);
            }
            o.add(axisZ);
        }
    }

    private void addFrame(SparseMatrix<Block> grid, Point3i p, Point3i center) {
        Point3i o;
        o = new Point3i(center.x + p.x * 12, center.y + p.y * 12, center.z + p.z * 12);
        for (int i = 0; i < 8; i++) {
            addHull(grid, o.x + i, o.y, o.z);
            addHull(grid, o.x + i, o.y + 7, o.z);
            addHull(grid, o.x + i, o.y + 7, o.z + 7);
            addHull(grid, o.x + i, o.y, o.z + 7);
            addHull(grid, o.x, o.y + i, o.z);
            addHull(grid, o.x + 7, o.y + i, o.z);
            addHull(grid, o.x + 7, o.y + i, o.z + 7);
            addHull(grid, o.x, o.y + i, o.z + 7);
            addHull(grid, o.x, o.y, o.z + i);
            addHull(grid, o.x + 7, o.y, o.z + i);
            addHull(grid, o.x + 7, o.y + 7, o.z + i);
            addHull(grid, o.x, o.y + 7, o.z + i);
        }
    }

    private void drawElipse(SparseMatrix<Block> grid, int xc, int yc, int zc, int xRad, int yRad) {
        if (xRad == 0) {
            if (yRad == 0) {
                addHull(grid, xc, yc, zc);
                return;
            } else {
                for (int y = -yRad; y <= yRad; y++) {
                    addHull(grid, xc, yc + y, zc);
                }
                return;
            }
        } else if (yRad == 0) {
            for (int x = -xRad; x <= xRad; x++) {
                addHull(grid, xc + x, yc, zc);
            }
            return;
        }
        int a2 = xRad * xRad;
        int b2 = yRad * yRad;
        int fa2 = 4 * a2;
        int x, y, sigma;
        for (x = 0, y = yRad, sigma = 2 * b2 + a2 * (1 - 2 * yRad); b2 * x <= a2 * y; x++) {
            drawXLine(grid, xc - x, yc + y, zc, xc + x);
            drawXLine(grid, xc - x, yc - y, zc, xc + x);
            drawYLine(grid, xc - x, yc - y, zc, yc + y);
            drawYLine(grid, xc + x, yc - y, zc, yc + y);
            if (sigma >= 0) {
                sigma += fa2 * (1 - y);
                y--;
            }
            sigma += b2 * (4 * x + 6);
        }
        int fb2 = 4 * b2;
        for (x = xRad, y = 0, sigma = 2 * a2 + b2 * (1 - 2 * xRad); a2 * y <= b2 * x; y++) {
            drawXLine(grid, xc - x, yc + y, zc, xc + x);
            drawXLine(grid, xc - x, yc - y, zc, xc + x);
            drawYLine(grid, xc - x, yc - y, zc, yc + y);
            drawYLine(grid, xc + x, yc - y, zc, yc + y);
            if (sigma >= 0) {
                sigma += fb2 * (1 - x);
                x--;
            }
            sigma += a2 * (4 * y + 6);
        }
    }

    private void drawXLine(SparseMatrix<Block> grid, int x1, int y, int z, int x2) {
        for (int x = x1; x <= x2; x++) {
            addHull(grid, x, y, z);
        }
    }

    private void drawYLine(SparseMatrix<Block> grid, int x, int y1, int z, int y2) {
        for (int y = y1; y <= y2; y++) {
            addHull(grid, x, y, z);
        }
    }

    private void addHull(SparseMatrix<Block> grid, Point3i p) {
        addHull(grid, p.x, p.y, p.z);
    }

    private void addHull(SparseMatrix<Block> grid, Point3i p, short type) {
        addHull(grid, p.x, p.y, p.z, type);
    }

    private void addHull(SparseMatrix<Block> grid, int x, int y, int z) {
        addHull(grid, x, y, z, BlockTypes.HULL_COLOR_GREY_ID);
    }

    private void addHull(SparseMatrix<Block> grid, int x, int y, int z, short type) {
        Block b;
        b = new Block();
        b.setBlockID(type);
        grid.set(x, y, z, b);
    }
}
