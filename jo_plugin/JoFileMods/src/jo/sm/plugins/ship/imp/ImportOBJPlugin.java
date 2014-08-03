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
package jo.sm.plugins.ship.imp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ImportOBJPlugin implements IBlocksPlugin {

    public static final String NAME = "Import/OBJ";
    public static final String DESC = "Import OBJ file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_FILE, 25},};
    private static final Logger log = Logger.getLogger(ImportOBJPlugin.class.getName());

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
        return new ImportOBJParameters();
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
        ImportOBJParameters params;
        params = (ImportOBJParameters) p;
        try {
            Hull3f hull;
            hull = OBJLogic.readFile(params.getFile());
            //log.log(Level.INFO, "Read " + hull.getTriangles().size() + " triangles");
            if (hull.getTriangles().isEmpty()) {
                throw new IllegalArgumentException("OBJ File " + params.getFile() + " has no triangles defined in it.");
            }
            Point3i lowerGrid;
            lowerGrid = new Point3i();
            Point3i upperGrid;
            upperGrid = new Point3i();
            Point3i offset;
            offset = new Point3i();
            float scale;
            scale = PlotLogic.getScale(hull, params.getLongestDimension(), lowerGrid, upperGrid, offset);
            SparseMatrix<Block> modified = new SparseMatrix<>();
            PlotLogic.mapHull(modified, hull, new Point3f(scale, scale, scale), lowerGrid, upperGrid, cb);
            ShipLogic.ensureCore(modified);
            return modified;
        } catch (IOException e) {
            cb.setError(e);
            return null;
        }
    }
}
