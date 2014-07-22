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
package jo.sm.plugins.planet.info;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ObjectReportPlugin implements IBlocksPlugin {

    public static final String NAME = "Object Report";
    public static final String DESC = "Report on object";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_EDIT, 95},
                {TYPE_STATION, SUBTYPE_EDIT, 95},
                {TYPE_SHOP, SUBTYPE_EDIT, 95},
                {TYPE_FLOATINGROCK, SUBTYPE_EDIT, 95},
                {TYPE_PLANET, SUBTYPE_EDIT, 95},};

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
        try {
            File repFile;
            repFile = File.createTempFile("smReport", ".txt");
            try (PrintWriter wtr = new PrintWriter(repFile)) {
                reportBasics(wtr, original);
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(repFile);
            }
        } catch (IOException e) {
            cb.setError(e);
        }
        return null;
    }

    private void reportBasics(PrintWriter wtr, SparseMatrix<Block> grid) {
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        grid.getBounds(lower, upper);
        int gridSize;
        gridSize = grid.size();
        wtr.println("Bounds: " + lower + " -- " + upper);
        wtr.println("Mass  : " + gridSize);
        List<Point3i> core;
        core = new ArrayList<>();
        Map<Short, Integer> quantities;
        quantities = new HashMap<>();
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i p;
            p = i.next();
            Block b;
            b = grid.get(p);
            Integer count;
            count = quantities.get(b.getBlockID());
            if (count == null) {
                quantities.put(b.getBlockID(), 1);
            } else {
                quantities.put(b.getBlockID(), count + 1);
            }
            if (b.getBlockID() == BlockTypes.CORE_ID) {
                core.add(p);
            }
        }
        if (core.isEmpty()) {
            wtr.println("Core  : none");
        } else {
            for (Point3i p : core) {
                wtr.println("Core  : " + p);
            }
        }
        wtr.println("Breakdown:");
        Short[] ids;
        ids = quantities.keySet().toArray(new Short[0]);
        Arrays.sort(ids);
        for (short id : ids) {
            int q;
            q = quantities.get(id);
            int pc;
            pc = q * 100 / gridSize;
            wtr.print(StringUtils.prefix(Integer.toString(q), ' ', 5));
            if (pc > 0) {
                wtr.print(" " + StringUtils.prefix(Integer.toString(pc), ' ', 3) + "%");
            } else {
                wtr.print("     ");
            }
            wtr.print(" " + BlockTypes.BLOCK_NAMES.get(id));
            wtr.println();
        }
    }
}
