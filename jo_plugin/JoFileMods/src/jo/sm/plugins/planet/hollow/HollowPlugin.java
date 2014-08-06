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
package jo.sm.plugins.planet.hollow;

import java.util.Iterator;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.BooleanMatrix3D;
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
public class HollowPlugin implements IBlocksPlugin {

    public static final String NAME = "Hollow";
    public static final String DESC = "Remove all non-surface blocks.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},
                {TYPE_STATION, SUBTYPE_MODIFY},
                {TYPE_SHOP, SUBTYPE_MODIFY},
                {TYPE_FLOATINGROCK, SUBTYPE_MODIFY},
                {TYPE_PLANET, SUBTYPE_MODIFY},};

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
        return null;
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
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>(original);
        doHollow(modified, cb);
        return modified;
    }

    public static void doHollow(SparseMatrix<Block> grid, IPluginCallback cb) {
        //Set<Point3i> exterior;
        //exterior = HullLogic.findExterior(grid, cb);
        BooleanMatrix3D exterior = HullLogic.findExteriorMatrix(grid, cb);
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i xyz;
            xyz = i.next();
            Block b;
            b = grid.get(xyz);
            if (isEdge(exterior, xyz)) {
            } else {
                if (b.getBlockID() == BlockTypes.CORE_ID) {
                } else {
                    grid.set(xyz, null);
                }
            }
        }
    }

    private static boolean isEdge(BooleanMatrix3D exterior, Point3i xyz) {
        if (exterior.contains(new Point3i(xyz.x - 1, xyz.y, xyz.z))) {
            return true;
        }
        if (exterior.contains(new Point3i(xyz.x + 1, xyz.y, xyz.z))) {
            return true;
        }
        if (exterior.contains(new Point3i(xyz.x, xyz.y - 1, xyz.z))) {
            return true;
        }
        if (exterior.contains(new Point3i(xyz.x, xyz.y + 1, xyz.z))) {
            return true;
        }
        if (exterior.contains(new Point3i(xyz.x, xyz.y, xyz.z - 1))) {
            return true;
        }
        return exterior.contains(new Point3i(xyz.x, xyz.y, xyz.z + 1));
    }
}
