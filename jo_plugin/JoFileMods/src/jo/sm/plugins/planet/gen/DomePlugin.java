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
package jo.sm.plugins.planet.gen;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.logic.MathUtils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class DomePlugin implements IBlocksPlugin {

    public static final String NAME = "Shape/Dome";
    public static final String DESC = "Domed surface";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_PLANET, SUBTYPE_GENERATE, 26},};

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
        return new DomeParameters();
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
        DomeParameters params = (DomeParameters) p;
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        fillGrid(modified, params, cb);
        return modified;
    }

    private void fillGrid(SparseMatrix<Block> grid, DomeParameters params, IPluginCallback cb) {
        cb.setStatus("Filling in dome");
        cb.startTask(params.getPlanetRadius() * 2);
        for (int x = -params.getPlanetRadius(); x <= params.getPlanetRadius(); x++) {
            cb.workTask(1);
            for (int y = -params.getPlanetRadius(); y <= params.getPlanetRadius(); y++) {
                fillColumn(x, y, grid, params);
            }
        }
        cb.endTask();
    }

    private void fillColumn(int x, int y, SparseMatrix<Block> grid, DomeParameters params) {
        double r = Math.sqrt(x * x + y * y);
        if (r > params.getPlanetRadius()) {
            return; // out of radius        
        }
        int columnDepth;
        if (params.isConcave()) {
            columnDepth = (int) MathUtils.interpolateSin(r, params.getPlanetRadius(), 0, params.getPlanetHeight(), 0);
        } else {
            columnDepth = (int) MathUtils.interpolateCos(r, 0, params.getPlanetRadius(), params.getPlanetHeight(), 0);
        }
        PluginUtils.fill(grid, x, 0, y, x, columnDepth, y, params.getFillWith(), 0);
    }
}
