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
package jo.sm.plugins.ship.rotate;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.GridLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.CornerLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ship.logic.WedgeLogic;
import jo.vecmath.Point3i;
import jo.vecmath.Point4i;
import jo.vecmath.logic.TransformInteger;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 * @Auther Robert Barefoot for SMEdit - version 1.1
 **/
public class RotatePlugin implements IBlocksPlugin {

    public static final String NAME = "Rotate";
    public static final String DESC = "Rotate ship around the core.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},
                {TYPE_STATION, SUBTYPE_MODIFY},};
    private static final Logger log = Logger.getLogger(RotatePlugin.class.getName());

    public static SparseMatrix<Block> rotateAround(SparseMatrix<Block> original, RotateParameters params, Point3i around) {
        Point4i inPoint;
        inPoint = new Point4i();
        Point4i outPoint;
        outPoint = new Point4i();
        TransformInteger t;
        t = new TransformInteger();
        t.setIdentity();
        t.translate(-around.x, -around.y, -around.z);
        t.rotateEuler(params.getXRotate(), params.getYRotate(), params.getZRotate());
        t.translate(around.x, around.y, around.z);
        log.log(Level.INFO, "Matrix: ", t);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext();) {
            Point3i xyz;
            xyz = i.next();
            inPoint.x = xyz.x;
            inPoint.y = xyz.y;
            inPoint.z = xyz.z;
            inPoint.w = 1;
            Block b;
            b = original.get(xyz);
            t.transform(inPoint, outPoint);
            log.log(Level.INFO, "  "+inPoint+" -> "+outPoint);
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID)) {
                short ori;
                ori = b.getOrientation();
                ori = WedgeLogic.rotate(ori, params.getXRotate() / 90, params.getYRotate() / 90, params.getZRotate() / 90);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.INFO, "Could not rotate wedge ori: " + b.getOrientation());
                }
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID)) {
                short ori;
                ori = b.getOrientation();
                ori = CornerLogic.rotate(ori, params.getXRotate() / 90, params.getYRotate() / 90, params.getZRotate() / 90);
                if (ori >= 0) {
                    b.setOrientation(ori);
                } else {
                    log.log(Level.INFO, "Could not rotate corner ori: " + b.getOrientation());
                }
            }
            modified.set(outPoint.x, outPoint.y, outPoint.z, b);
        }
        return modified;
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
        return new RotateParameters();
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
        RotateParameters params;
        params = (RotateParameters) p;
        SparseMatrix<Block> modified;
        if ((sm.getSelectedLower() == null) || (sm.getSelectedUpper() == null)) {
            Point3i core;
            core = findCore(original);
            log.log(Level.INFO, "Core at: ", core);
            modified = rotateAround(original, params, core);
        } else {
            Point3i lower;
            lower = sm.getSelectedLower();
            Point3i upper;
            upper = sm.getSelectedUpper();
            Point3i center;
            center = new Point3i(lower);
            center.add(upper);
            center.scale(1, 2);
            modified = new SparseMatrix<>(original);
            SparseMatrix<Block> grid;
            grid = GridLogic.extract(modified, lower, upper);
            GridLogic.delete(modified, lower, upper);
            grid = rotateAround(grid, params, center);
            GridLogic.insert(modified, grid, lower);
        }
        ShipLogic.ensureCore(modified);
        return modified;
    }

    private Point3i findCore(SparseMatrix<Block> grid) {
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i xyz;
            xyz = i.next();
            if (grid.get(xyz).getBlockID() == BlockTypes.CORE_ID) {
                return xyz;
            }
        }
        return null;
    }
}
