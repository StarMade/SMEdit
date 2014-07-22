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
package jo.sm.logic;

import java.util.Iterator;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class PluginUtils {

    public static void getEffectiveSelection(StarMade sm, SparseMatrix<Block> grid, Point3i lower, Point3i upper) {
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
        } else {
            grid.getBounds(lower, upper);
        }
    }

    public static Iterator<Point3i> getEffectiveSelectionIterator(StarMade sm, SparseMatrix<Block> grid) {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        getEffectiveSelection(sm, grid, lower, upper);
        return new CubeIterator(lower, upper);
    }

    public static int getVolume(Point3i lower, Point3i upper) {
        return (upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z + 1);
    }

    public static void fill(SparseMatrix<Block> grid, int x1, int y1, int z1,
            int x2, int y2, int z2, int blockID, int orientation) {
        fill(grid, new Point3i(x1, y1, z1), new Point3i(x2, y2, z2), blockID, orientation);
    }

    public static void fill(SparseMatrix<Block> grid, Point3i lower,
            Point3i upper, int blockID, int orientation) {
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            Block b = new Block((short) blockID);
            b.setOrientation((short) orientation);
            grid.set(i.next(), b);
        }
    }
}
