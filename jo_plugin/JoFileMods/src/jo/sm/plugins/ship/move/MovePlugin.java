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
package jo.sm.plugins.ship.move;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;


public class MovePlugin implements IBlocksPlugin {

    public static final String NAME = "Move";
    public static final String DESC = "Move ship's core.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},};
    private static final Logger log = Logger.getLogger(MovePlugin.class.getName());

    public static SparseMatrix<Block> shift(SparseMatrix<Block> original, int dx, int dy, int dz, IPluginCallback cb) {
        cb.startTask(original.size());
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext();) {
            cb.workTask(1);
            Point3i from;
            from = i.next();
            Point3i to;
            to = new Point3i(from.x - dx, from.y - dy, from.z - dz);
            Block b;
            b = original.get(from);
            if (b.getBlockID() == BlockTypes.CORE_ID) {
                short newID = getFiller(original, from);
                if (newID == -1) {
                    continue;
                }
                b = new Block(newID);
            }
            modified.set(to, b);
        }
        return modified;
    }

    private static short getFiller(SparseMatrix<Block> grid, Point3i p) {
        Map<Short, Integer> votes = new HashMap<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if ((dx != 0) || (dy != 0) || (dz != 0)) {
                        Block b = grid.get(p.x + dx, p.y + dy, p.z + dz);
                        if (b != null) {
                            Integer v = votes.get(b.getBlockID());
                            if (v == null) {
                                v = 1;
                            } else {
                                v = v + 1;
                            }
                            votes.put(b.getBlockID(), v);
                        }
                    }
                }
            }
        }
        short best = -1;
        int bestv;
        bestv = 0;
        for (Short test : votes.keySet()) {
            if (votes.get(test) > bestv) {
                best = test;
                bestv = votes.get(best);
            }
        }
        return best;
    }

    public static Point3i findCore(SparseMatrix<Block> grid) {
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i xyz = i.next();
            if (grid.get(xyz).getBlockID() == BlockTypes.CORE_ID) {
                return xyz;
            }
        }
        return null;
    }

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
        return new MoveParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object p, StarMade sm, IPluginCallback cb) {
        MoveParameters params;
        params = (MoveParameters) p;
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        Point3i core;
        core = findCore(original);
        log.log(Level.WARNING, "Core at: ", core);
        SparseMatrix<Block> modified = shift(original, params.getXMove(), params.getYMove(), params.getZMove(), cb);
        // copy core
        modified.set(core, original.get(core));
        return modified;
    }
}
