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
package jo.sm.plugins.ship.replace;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ReplaceBlocksPlugin implements IBlocksPlugin {

    public static final String NAME = "Replace Blocks";
    public static final String DESC = "Replace one type of block on your ship with another.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_PAINT},
                {TYPE_STATION, SUBTYPE_PAINT},
                {TYPE_SHOP, SUBTYPE_PAINT},
                {TYPE_PLANET, SUBTYPE_PAINT},
                {TYPE_FLOATINGROCK, SUBTYPE_PAINT},};
    private static final Logger log = Logger.getLogger(ReplaceBlocksPlugin.class.getName());

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
        return new ReplaceBlocksParameters();
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
        ReplaceBlocksParameters params;
        params = (ReplaceBlocksParameters) p;
        Point3i upper;
        upper = new Point3i();
        Point3i lower;
        lower = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        log.log(Level.INFO, "Params: color1="+params.getColor1()+", color2="+params.getColor2());
        cb.setStatus("Replacing colors");
        cb.startTask(PluginUtils.getVolume(lower, upper));
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>(original);
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            cb.workTask(1);
            Point3i xyz;
            xyz = i.next();
            Block b;
            b = original.get(xyz);
            if (b == null) {
                continue;
            }
            if (b.getBlockID() == params.getColor1()) {
                if (params.getColor2() == 0) {
                    b = null;
                } else {
                    short oldOri;
                    oldOri = b.getOrientation();
                    b = new Block(params.getColor2());
                    b.setOrientation(oldOri);
                }
            }
            modified.set(xyz, b);
        }
        cb.endTask();
        return modified;
    }
}
