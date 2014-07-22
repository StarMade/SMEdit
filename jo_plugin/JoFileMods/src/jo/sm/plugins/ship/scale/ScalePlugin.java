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
package jo.sm.plugins.ship.scale;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.logic.Point3fLogic;


public class ScalePlugin implements IBlocksPlugin {

    public static final String NAME = "Scale";
    public static final String DESC = "Scale ship's size.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},
                {TYPE_STATION, SUBTYPE_MODIFY},
                {TYPE_SHOP, SUBTYPE_MODIFY},};
    private static final Logger log = Logger.getLogger(ScalePlugin.class.getName());

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
        return new ScaleParameters();
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
        ScaleParameters params;
        params = (ScaleParameters) p;
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        Point3i core;
        core = findCore(original);
        log.log(Level.INFO, "  Core at ", core);
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>();
        Point3f size;
        size = new Point3f(1, 1, 1);
        size.x *= params.getXScale();
        size.y *= params.getYScale();
        size.z *= params.getZScale();
        size = Point3fLogic.abs(size);
        size = Point3fLogic.ceil(size);
        log.log(Level.INFO, "Block size", size);
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext();) {
            Point3i xyz;
            xyz = i.next();
            Block b;
            b = original.get(xyz);
            Point3f fPoint;
            fPoint = transform(xyz, core, params);
            log.log(Level.INFO, "  \"+xyz+\" -> ", fPoint);
            if (b.getBlockID() != BlockTypes.CORE_ID) {
                for (int x = 0; x < size.x; x++) {
                    for (int y = 0; y < size.y; y++) {
                        for (int z = 0; z < size.z; z++) {
                            Block newB;
                            newB = new Block(b);
                            if (BlockTypes.isController(newB.getBlockID())) {
                                newB.setBlockID(BlockTypes.CONTROLLER_IDS.get(newB.getBlockID()));
                            }
                            set(fPoint, x, y, z, modified, newB);
                        }
                    }
                }
            }
        }
        ShipLogic.ensureCore(modified);
        return modified;
    }

    private void set(Point3f iPoint, int x, int y, int z,
            SparseMatrix<Block> grid, Block b) {
        x = (int) (iPoint.x + x + .5);
        y = (int) (iPoint.y + y + .5);
        z = (int) (iPoint.z + z + .5);
        grid.set(x, y, z, b);
    }

    private Point3f transform(Point3i ori, Point3i core, ScaleParameters params) {
        Point3f trans;
        trans = new Point3f();
        trans.x = transform(ori.x, core.x, params.getXScale());
        trans.y = transform(ori.y, core.y, params.getYScale());
        trans.z = transform(ori.z, core.z, params.getZScale());
        return trans;
    }

    private float transform(int ori, int core, float scale) {
        ori -= core;
        ori *= scale;
        ori += core;
        return ori;
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
