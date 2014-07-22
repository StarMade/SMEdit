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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.utils.FloatUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Color3f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;


public class ImportVRMLPlugin implements IBlocksPlugin {

    public static final String NAME = "Import/VRML";
    public static final String DESC = "Import VRML file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_FILE, 25},
                {TYPE_STATION, SUBTYPE_FILE, 25},
                {TYPE_SHOP, SUBTYPE_FILE, 25},};
    private static final Logger log = Logger.getLogger(ImportVRMLPlugin.class.getName());

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
        return new ImportVRMLParameters();
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
        ImportVRMLParameters params;
        params = (ImportVRMLParameters) p;
        try {
            Hull3f hull;
            hull = readFile(params.getFile());
            log.log(Level.INFO, "Read " + hull.getTriangles().size() + " triangles");
            Point3i lowerGrid;
            lowerGrid = new Point3i();
            Point3i upperGrid;
            upperGrid = new Point3i();
            Point3i offset;
            offset = new Point3i();
            float scale;
            scale = PlotLogic.getScale(hull, params.getLongestDimension(), lowerGrid, upperGrid, offset);
            SparseMatrix<Block> modified;
            modified = new SparseMatrix<>();
            PlotLogic.mapHull(modified, hull, new Point3f(scale, scale, scale), lowerGrid, upperGrid, cb);
            return modified;
        } catch (IOException e) {
            cb.setError(e);
            return null;
        }
    }

    private Hull3f readFile(String wrlFile) throws IOException {
        List<?> vrml;
        try (BufferedReader rdr = new BufferedReader(new FileReader(new File(wrlFile)))) {
            vrml = VRMLLogic.read(rdr);
        }
        Hull3f hull;
        hull = new Hull3f();
        List<Point3f> verts;
        verts = new ArrayList<>();
        Color3f color;
        color = new Color3f();
        readList(vrml, hull, verts, color);
        return hull;
    }

    private void readList(Collection<?> vrml, Hull3f hull, List<Point3f> verts, Color3f color) {
        for (Object n : vrml) {
            if (n instanceof VRMLNode) {
                VRMLNode node;
                node = (VRMLNode) n;
                log.log(Level.INFO, "Scanning " + node.getName());
                if ("point".equalsIgnoreCase(node.getName()) && (node.getValue() instanceof Collection<?>)) {
                    verts.clear();
                    Collection<?> array;
                    array = (Collection<?>) node.getValue();
                    for (Iterator<?> i = array.iterator(); i.hasNext();) {
                        Point3f p;
                        p = new Point3f();
                        p.x = FloatUtils.parseFloat(i.next());
                        p.y = FloatUtils.parseFloat(i.next());
                        p.z = FloatUtils.parseFloat(i.next());
                        verts.add(p);
                    }
                    log.log(Level.INFO, "Read "+verts.size()+" verticies");
                } else if ("diffuseColor".equalsIgnoreCase(node.getName()) && (node.getValue() instanceof Collection<?>)) {
                    Collection<?> array;
                    array = (Collection<?>) node.getValue();
                    Iterator<?> i;
                    i = array.iterator();
                    color.x = FloatUtils.parseFloat(i.next());
                    color.y = FloatUtils.parseFloat(i.next());
                    color.z = FloatUtils.parseFloat(i.next());
                    log.log(Level.INFO, "Color: "+color);
                } else if ("coordIndex".equalsIgnoreCase(node.getName()) && (node.getValue() instanceof Collection<?>)) {
                    Collection<?> array;
                    array = (Collection<?>) node.getValue();
                    List<Integer> idx;
                    idx = new ArrayList<>();
                    for (Iterator<?> i = array.iterator(); i.hasNext();) {
                        int p;
                        p = IntegerUtils.parseInt((String) i.next());
                        if (p == -1) {
                            for (int o = 1; o < idx.size() - 1; o++) {
                                int p1;
                                p1 = idx.get(0);
                                int p2;
                                p2 = idx.get(o);
                                int p3;
                                p3 = idx.get(o + 1);
                                log.log(Level.INFO, "Triangle " + p1 + ", " + p2 + ", " + p3);
                                Triangle3f tri;
                                tri = new Triangle3f(verts.get(p1), verts.get(p2), verts.get(p3));
                                tri.setColor(new Color3f(color));
                                hull.getTriangles().add(tri);
                            }
                            idx.clear();
                        } else {
                            idx.add(p);
                        }
                    }
                } else if (node.getValue() instanceof Collection<?>) {
                    Collection<?> array;
                    array = (Collection<?>) node.getValue();
                    readList(array, hull, verts, color);
                }
            }
        }
    }
}
