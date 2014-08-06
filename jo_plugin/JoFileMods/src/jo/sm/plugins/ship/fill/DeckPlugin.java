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
package jo.sm.plugins.ship.fill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.BooleanMatrix3D;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class DeckPlugin implements IBlocksPlugin {

    public static final String NAME = "Decks";
    public static final String DESC = "Fill Ship Interior with Decks";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},
                {TYPE_STATION, SUBTYPE_MODIFY},};

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
        return new DeckParameters();
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
        DeckParameters params;
        params = (DeckParameters) p;
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>(original);
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        List<Point3i> interior;
        interior = new ArrayList<>();
        findInterior(original, sm, cb, modified, lower, upper, interior);
        Set<Integer> deckYs;
        deckYs = new HashSet<>();
        placeDecks(cb, params, modified, lower, upper, interior, deckYs);
        if (params.getStairwellWidth() > 0) {
            placeStairs(cb, params, modified, lower, upper, interior, deckYs);
        }
        return modified;
    }

    private void placeStairs(IPluginCallback cb, DeckParameters params,
            SparseMatrix<Block> modified, Point3i lower, Point3i upper,
            List<Point3i> interior, Set<Integer> deckYs) {
        int sx;
        sx = params.getStairwellWidth();
        int sy;
        sy = params.getSpace() + params.getThickness();
        int sz;
        sz = sy;
        Set<Integer> xs;
        xs = new HashSet<>();
        int firstX;
        firstX = 8 - params.getStairwellXGap() / 2 - sx;
        for (int x = firstX; x < upper.x; x += sx + params.getStairwellXGap()) {
            xs.add(x);
        }
        for (int x = firstX - sx - params.getStairwellXGap(); x >= lower.x; x -= sx + params.getStairwellXGap()) {
            xs.add(x);
        }
        Set<Integer> zs = new HashSet<>();
        int firstZ = 8 - params.getStairwellZGap() / 2 - sz;
        for (int z = firstZ; z < upper.z; z += sz + params.getStairwellZGap()) {
            zs.add(z);
        }
        for (int z = firstZ - sz - params.getStairwellZGap(); z >= lower.z; z -= sz + params.getStairwellZGap()) {
            zs.add(z);
        }
        cb.setStatus("Placing stairs");
        cb.startTask(deckYs.size());
        short tred = params.getMaterial();
        if (BlockTypes.isAnyHull(tred)) {
            tred = BlockTypes.getColoredBlock(BlockTypes.HULL_COLOR_WEDGE_GREY_ID, BlockTypes.getColor(tred));
        } else {
            tred = BlockTypes.HULL_COLOR_WEDGE_GREY_ID;
        }
        for (Integer y : deckYs) {
            if (y + sy >= upper.y) {
                continue;
            }
            for (Integer x : xs) {
                for (Integer z : zs) {
                    placeStair(modified, x, y + params.getThickness(), z, interior, tred, sx, sy, sz);
                }
            }
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void placeStair(SparseMatrix<Block> grid, int baseX, int baseY,
            int baseZ, List<Point3i> interior, short material, int sizeX, int sizeY,
            int sizeZ) {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Point3i p;
                p = new Point3i(baseX + x, baseY + y, baseZ + y);
                placeIfInterior(grid, p, interior, material);
                for (int v = y + 1; v < sizeY; v++) {
                    p.y = baseY + v;
                    removeIfInterior(grid, p, interior);
                }
            }
        }
    }

    private void placeIfInterior(SparseMatrix<Block> grid, Point3i p,
            List<Point3i> interior, short material) {
        if (interior.contains(p)) {
            grid.set(p, new Block(material));
        }
    }

    private void removeIfInterior(SparseMatrix<Block> grid, Point3i p,
            List<Point3i> interior) {
        if (interior.contains(p)) {
            grid.set(p, null);
        }
    }

    private void findInterior(SparseMatrix<Block> original, StarMade sm,
            IPluginCallback cb, SparseMatrix<Block> modified, Point3i lower,
            Point3i upper, List<Point3i> interior) {
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
            for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
                Point3i xyz = i.next();
                if (!original.contains(xyz)) {
                    interior.add(xyz);
                }
            }
        } else {
            BooleanMatrix3D exterior = HullLogic.findExteriorMatrix(original, cb);
            //Set<Point3i> exterior;
            //exterior = HullLogic.findExterior(original, cb);
            original.getBounds(lower, upper);
            scopeInterior(modified, interior, exterior, lower, upper, cb);
        }
    }

    private void placeDecks(IPluginCallback cb, DeckParameters params,
            SparseMatrix<Block> modified, Point3i lower, Point3i upper,
            List<Point3i> interior, Set<Integer> ys) {
        int firstY;
        firstY = 8 - params.getSpace() / 2 - params.getThickness();
        // upper decks
        for (int y = firstY; y < upper.y; y += params.getThickness() + params.getSpace()) {
            ys.add(y);
        }
        // lower decks
        for (int y = firstY - params.getThickness() - params.getSpace(); y >= lower.y; y -= params.getThickness() + params.getSpace()) {
            ys.add(y);
        }
        cb.setStatus("Placing decks");
        cb.startTask(ys.size());
        for (Integer y : ys) {
            for (int t = 0; t < params.getThickness(); t++) {
                placeDeck(modified, y, interior, params.getMaterial());
            }
            cb.workTask(1);
        }
        cb.endTask();
    }

    private void placeDeck(SparseMatrix<Block> grid, int y,
            List<Point3i> interior, short material) {
        for (Point3i p : interior) {
            if (p.y == y) {
                grid.set(p, new Block(material));
            }
        }
    }

    private void scopeInterior(SparseMatrix<Block> grid, List<Point3i> interior,
            BooleanMatrix3D exterior, Point3i lower, Point3i upper,
            IPluginCallback cb) {
        cb.setStatus("Calculating interior");
        cb.startTask(grid.size());
        for (Iterator<Point3i> i = grid.iterator(); i.hasNext();) {
            Point3i p = i.next();
            if (exterior.contains(p)) {
                continue;
            }
            Block b = grid.get(p);
            if (b == null) {
                interior.add(p);
            }
        }
        cb.endTask();
    }
}
