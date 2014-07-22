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
package jo.sm.plugins.ship.reflect;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.CornerLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ship.logic.WedgeLogic;
import jo.vecmath.Point3i;


public class DuplicatePlugin implements IBlocksPlugin {

    public static final String NAME = "Reflective Symmetry";
    public static final String DESC = "Make the ship symmetrical along one or more axis.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},
                {TYPE_SHOP, SUBTYPE_MODIFY},
                {TYPE_STATION, SUBTYPE_MODIFY},};
    private static final Logger log = Logger.getLogger(DuplicatePlugin.class.getName());

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
        return new DuplicateParameters();
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
        DuplicateParameters params;
        params = (DuplicateParameters) p;
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>(original);
        reflectX(params, modified, sm, cb);
        reflectY(params, modified, sm, cb);
        reflectZ(params, modified, sm, cb);
        ShipLogic.ensureCore(modified);
        return modified;
    }

    private void reflectX(DuplicateParameters params, SparseMatrix<Block> grid, StarMade sm, IPluginCallback cb) {
        if (params.getXReflect() == 0) {
            return;
        }
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
        } else {
            grid.getBounds(lower, upper);
        }
        cb.setStatus("Reflecting X");
        cb.startTask((upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z * 1));
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            cb.workTask(1);
            Point3i xyz;
            xyz = i.next();
            Block b;
            b = grid.get(xyz);
            if (b == null) {
                continue;
            }
            if (b.getBlockID() == BlockTypes.CORE_ID) {
                continue;
            }
            if (params.getXReflect() > 0) {
                int delta = upper.x - xyz.x;
                xyz.x = upper.x + 1 + delta;
            } else {
                int delta = xyz.x - lower.x;
                xyz.x = lower.x - 1 - delta;
            }
            short ori;
            ori = b.getOrientation();
            b = new Block(b.getBlockID());
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID)) {
                ori = WedgeLogic.reflect(ori, true, false, false);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate wedge ori=" + b.getOrientation());
                }
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID)) {
                ori = CornerLogic.reflect(ori, true, false, false);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate corner ori=" + b.getOrientation());
                }
            }
            grid.set(xyz, b);
        }
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            if (params.getXReflect() > 0) {
                int delta;
                delta = upper.x - lower.x;
                upper.x = upper.x + 1 + delta;
                sm.setSelectedUpper(upper);
            } else {
                int delta;
                delta = upper.x - lower.x;
                lower.x = lower.x - 1 - delta;
                sm.setSelectedLower(lower);
            }
        }
        cb.endTask();
    }

    private void reflectY(DuplicateParameters params, SparseMatrix<Block> grid, StarMade sm, IPluginCallback cb) {
        if (params.getYReflect() == 0) {
            return;
        }
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
        } else {
            grid.getBounds(lower, upper);
        }
        cb.setStatus("Reflecting Y");
        cb.startTask((upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z * 1));
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            cb.workTask(1);
            Point3i xyz;
            xyz = i.next();
            Block b;
            b = grid.get(xyz);
            if (b == null) {
                continue;
            }
            if (b.getBlockID() == BlockTypes.CORE_ID) {
                continue;
            }
            if (params.getYReflect() > 0) {
                int delta;
                delta = upper.y - xyz.y;
                xyz.y = upper.y + 1 + delta;
            } else {
                int delta;
                delta = xyz.y - lower.y;
                xyz.y = lower.y - 1 - delta;
            }
            short ori;
            ori = b.getOrientation();
            b = new Block(b.getBlockID());
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID)) {
                ori = WedgeLogic.reflect(ori, false, true, false);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate wedge ori=" + b.getOrientation());
                }
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID)) {
                ori = CornerLogic.reflect(ori, false, true, false);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate corner ori=" + b.getOrientation());
                }
            }
            grid.set(xyz, b);
        }
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            if (params.getYReflect() > 0) {
                int delta;
                delta = upper.y - lower.y;
                upper.y = upper.y + 1 + delta;
                sm.setSelectedUpper(upper);
            } else {
                int delta;
                delta = upper.y - lower.y;
                lower.y = lower.y - 1 - delta;
                sm.setSelectedLower(lower);
            }
        }
        cb.endTask();
    }

    private void reflectZ(DuplicateParameters params, SparseMatrix<Block> grid, StarMade sm, IPluginCallback cb) {
        if (params.getZReflect() == 0) {
            return;
        }
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
        } else {
            grid.getBounds(lower, upper);
        }
        cb.setStatus("Reflecting Z");
        cb.startTask((upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z * 1));
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            cb.workTask(1);
            Point3i xyz = i.next();
            Block b = grid.get(xyz);
            if (b == null) {
                continue;
            }
            if (b.getBlockID() == BlockTypes.CORE_ID) {
                continue;
            }
            if (params.getXReflect() > 0) {
                int delta;
                delta = upper.z - xyz.z;
                xyz.z = upper.z + 1 + delta;
            } else {
                int delta;
                delta = xyz.z - lower.z;
                xyz.z = lower.z - 1 - delta;
            }
            b = new Block(b.getBlockID());
            short ori;
            ori = b.getOrientation();
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID)) {
                ori = WedgeLogic.reflect(ori, false, false, true);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate wedge ori=" + b.getOrientation());
                }
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID)) {
                ori = CornerLogic.reflect(ori, false, false, true);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.WARNING, "Could not rotate corner ori=" + b.getOrientation());
                }
            }
            grid.set(xyz, b);
        }
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            if (params.getZReflect() > 0) {
                int delta;
                delta = upper.z - lower.z;
                upper.z = upper.z + 1 + delta;
                sm.setSelectedUpper(upper);
            } else {
                int delta;
                delta = upper.z - lower.z;
                lower.z = lower.z - 1 - delta;
                sm.setSelectedLower(lower);
            }
        }
        cb.endTask();
    }
}
