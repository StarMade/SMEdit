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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import jo.sm.logic.utils.FloatUtils;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.Point3fLogic;
import jo.vecmath.logic.ext.Triangle3fLogic;


public class OBJLogic {

    private static final Logger log = Logger.getLogger(OBJLogic.class.getName());

    public static Hull3f readFile(String objFileName) throws IOException {
        File objFile;
        objFile = new File(objFileName);
        Hull3f hull;
        try (BufferedReader rdr = new BufferedReader(new FileReader(objFile))) {
            hull = new Hull3f();
            List<Point3f> verts;
            verts = new ArrayList<>();
            List<Point2f> textures;
            textures = new ArrayList<>();
            Map<String, OBJMaterial> materials;
            materials = new HashMap<>();
            OBJMaterial material;
            material = null;
            for (;;) {
                String inbuf;
                inbuf = rdr.readLine();
                if (inbuf == null) {
                    break;
                }
                if (inbuf.startsWith("v ")) {   // vertex
                    StringTokenizer st;
                    st = new StringTokenizer(inbuf, " ");
                    if (st.countTokens() != 4) {
                        log.log(Level.WARNING, "Unrecognized vertex line '" + inbuf + "'");
                        break;
                    }
                    st.nextToken(); // skip label
                    Point3f v;
                    v = new Point3f();
                    v.x = Float.parseFloat(st.nextToken());
                    v.y = Float.parseFloat(st.nextToken());
                    v.z = Float.parseFloat(st.nextToken());
                    verts.add(v);
                } else if (inbuf.startsWith("vt ")) {   // vertex texture
                    StringTokenizer st;
                    st = new StringTokenizer(inbuf, " ");
                    if (st.countTokens() != 3) {
                        log.log(Level.WARNING, "Unrecognized vertex texture line '" + inbuf + "'");
                        break;
                    }
                    st.nextToken(); // skip label
                    Point2f v;
                    v = new Point2f();
                    v.x = Float.parseFloat(st.nextToken());
                    v.y = Float.parseFloat(st.nextToken());
                    textures.add(v);
                } else if (inbuf.startsWith("f ")) {   // poly
                    List<Point3f> points;
                    points = new ArrayList<>();
                    List<Point2f> uvs;
                    uvs = new ArrayList<>();
                    StringTokenizer st;
                    st = new StringTokenizer(inbuf, " ");
                    st.nextToken(); // skip label
                    while (st.hasMoreTokens()) {
                        String v;
                        v = st.nextToken();
                        StringTokenizer st2;
                        st2 = new StringTokenizer(v, "/");
                        if (st2.hasMoreTokens()) {
                            int idx;
                            idx = Integer.parseInt(st2.nextToken());
                            points.add(verts.get(idx - 1));
                            if (st2.hasMoreTokens()) {
                                idx = Integer.parseInt(st2.nextToken());
                                uvs.add(textures.get(idx - 1));
                            }
                        }
                    }
                    for (int third = 2; third < points.size(); third++) {
                        Triangle3f tri;
                        tri = new Triangle3f();
                        tri.setA(points.get(0));
                        tri.setB(points.get(third - 1));
                        tri.setC(points.get(third));
                        if ((uvs.size() > 0) && (material != null)) {
                            tri.setAUV(uvs.get(0));
                            tri.setBUV(uvs.get(third - 1));
                            tri.setCUV(uvs.get(third));
                            ensureImage(material);
                            tri.setTexture(material.getMapKDImage());
                        }
                        if (!Triangle3fLogic.isDegenerate(tri)) {
                            hull.getTriangles().add(tri);
                        }
                    }
                } else if (inbuf.startsWith("mtllib ")) {
                    File mtlFile;
                    mtlFile = new File(objFile.getParentFile(), inbuf.substring(7).trim());
                    readMTLFile(mtlFile, materials);
                } else if (inbuf.startsWith("usemtl ")) {
                    material = materials.get(inbuf.substring(7).trim());
                }
            }
        }
        return hull;
    }

    private static void ensureImage(OBJMaterial material) {
        if (material.getMapKDImage() != null) {
            return;
        }
        if (material.getMapKD() == null) {
            return;
        }
        try {
            BufferedImage img;
            img = ImageIO.read(material.getMapKD());
            material.setMapKDImage(img);
        } catch (IOException e) {
            log.log(Level.WARNING, "Image IO failed!", e);
            material.setMapKD(null); // null out so we don't retry
        }
    }

    private static void readMTLFile(File mtlFile, Map<String, OBJMaterial> materials) {
        if (!mtlFile.exists()) {
            return; // gracefully degrade
        }
        try {
            OBJMaterial mtl = null;
            try (BufferedReader rdr = new BufferedReader(new FileReader(mtlFile))) {
                for (;;) {
                    String inbuf = rdr.readLine();
                    if (inbuf == null) {
                        break;
                    }
                    if (inbuf.startsWith("newmtl ")) {
                        mtl = new OBJMaterial();
                        mtl.setName(inbuf.substring(7).trim());
                        materials.put(mtl.getName(), mtl);
                    } else if (inbuf.startsWith("Ns ")) {
                        if (mtl != null) {
                            mtl.setNS(FloatUtils.parseFloat(inbuf.substring(3).trim()));
                        }
                    } else if (inbuf.startsWith("Ni ")) {
                        if (mtl != null) {
                            mtl.setNi(FloatUtils.parseFloat(inbuf.substring(3).trim()));
                        }
                    } else if (inbuf.startsWith("D ")) {
                        if (mtl != null) {
                            mtl.setD(FloatUtils.parseFloat(inbuf.substring(2).trim()));
                        }
                    } else if (inbuf.startsWith("illum ")) {
                        if (mtl != null) {
                            mtl.setIllum(FloatUtils.parseFloat(inbuf.substring(6).trim()));
                        }
                    } else if (inbuf.startsWith("Ka ")) {
                        if (mtl != null) {
                            mtl.setKA(Point3fLogic.fromString(inbuf.substring(3).trim()));
                        }
                    } else if (inbuf.startsWith("Kd ")) {
                        if (mtl != null) {
                            mtl.setKD(Point3fLogic.fromString(inbuf.substring(3).trim()));
                        }
                    } else if (inbuf.startsWith("Ks ")) {
                        if (mtl != null) {
                            mtl.setKS(Point3fLogic.fromString(inbuf.substring(3).trim()));
                        }
                    } else if (inbuf.startsWith("map_Kd ")) {
                        if (mtl != null) {
                            File mapFile = new File(mtlFile.getParentFile(), inbuf.substring(7).trim());
                            if (mapFile.exists()) {
                                mtl.setMapKD(mapFile);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Buffered Reader failed!", e);
        }
    }

}
