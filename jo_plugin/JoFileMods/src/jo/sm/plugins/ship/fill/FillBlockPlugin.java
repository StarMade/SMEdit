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
import java.util.List;
import java.util.Set;
import jo.sm.data.BooleanMatrix3D;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FillBlockPlugin implements IBlocksPlugin {

    public static final String NAME = "Fill with Block";
    public static final String DESC = "Fill ship interior with specific block";
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
        return new FillBlockParameters();
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
        FillBlockParameters params;
        params = (FillBlockParameters) p;
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>();
        List<Point3i> interior;
        interior = new ArrayList<>();
        BooleanMatrix3D exterior = HullLogic.findExteriorMatrix(original, cb);
        //Set<Point3i> exterior;
        //exterior = HullLogic.findExterior(original, cb);
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        FillPlugin.scopeInterior(original, modified, interior, exterior, lower, upper, cb);
        int todo = params.getTotal();
        if (todo <= 0) {
            todo = interior.size();
        }
        FillStrategy strategy;
        strategy = new FillStrategy(params.getStrategy(), params.getAxis(), lower, upper);
        cb.setStatus("Filling");
        cb.startTask(todo);
        FillPlugin.fill(modified, interior, todo, (short) -1, params.getBlockID(), strategy, cb);
        cb.endTask();
        return modified;
    }
}
