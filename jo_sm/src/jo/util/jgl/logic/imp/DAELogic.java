/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl.logic.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import jo.sm.logic.utils.FloatUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;
import jo.vecmath.Point3f;
import jo.vecmath.Point4f;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DAELogic {

    private static final Map<String, Point4f> mGeometryMaterials = new HashMap<>();
    private static final Map<String, Point4f> mLibraryMaterials = new HashMap<>();
    private static final Map<String, Point4f> mLibraryEffects = new HashMap<>();

    public static JGLNode readDAE(InputStream is) {
        JGLGroup dae = new JGLGroup();
        Document doc = XMLUtils.readStream(is);
        System.out.println("Indexing");
        indexLibraryEffects(doc);
        indexLibraryMaterials(doc);
        indexGeometryMaterials(doc);
        System.out.println("Looking for geometries");
        for (Node g : XMLUtils.findNodes(doc, "COLLADA/library_geometries/geometry")) {
            System.out.println("Geometry " + XMLUtils.getAttribute(g, "id"));
            for (Node t : XMLUtils.findNodes(g, "mesh/triangles")) {
                JGLObj o = new JGLObj();
                //System.out.println("Triangles");
                int triCount = IntegerUtils.parseInt(XMLUtils.getAttribute(t, "count"));
                Point4f color = getColor(t);
                if (color == null) {
                    System.out.println("no color");
                    color = new Point4f(.9f, .9f, .9f, .9f);
                }
                Map<Integer, String> offsetToSemantic;
                offsetToSemantic = new HashMap<>();
                Map<String, List<Short>> semanticToTriangles;
                semanticToTriangles = new HashMap<>();
                Map<String, List<Point3f>> semanticToFloats;
                semanticToFloats = new HashMap<>();
                for (Node input : XMLUtils.findNodes(t, "input")) {
                    String semantic = XMLUtils.getAttribute(input, "semantic");
                    int offset = IntegerUtils.parseInt(XMLUtils.getAttribute(input, "offset"));
                    String source = XMLUtils.getAttribute(input, "source").substring(1);
                    Node src = findNodeWithValue(t.getParentNode(), "vertices", "id", source);
                    if (src != null) {
                        Node i = XMLUtils.findFirstNode(src, "input");
                        source = XMLUtils.getAttribute(i, "source").substring(1);
                    }
                    List<Point3f> positions = findFloatArray(t.getParentNode(), source);
                    if (positions == null) {
                        System.out.println("  no position source, sematic=" + semantic + ", offset=" + offset + ", source=" + source);
                        continue;
                    }
                    offsetToSemantic.put(offset, semantic);
                    semanticToTriangles.put(semantic, new ArrayList<Short>());
                    semanticToFloats.put(semantic, positions);
                }
                Node p = XMLUtils.findFirstNode(t, "p");
                if (p == null) {
                    System.out.println("  no p node");
                    continue;
                }
                //List<Short> triangles = new ArrayList<Short>();
                String triTxt = XMLUtils.getText(p);
                StringTokenizer st = new StringTokenizer(triTxt, " \r\n\t");
                int count = st.countTokens();
                if (count != triCount * 3 * offsetToSemantic.size()) {
                    System.err.println("Odd count of triangles! Expected " + triCount + " -> " + triCount * 3 * offsetToSemantic.size() + ", got " + count);
                    System.err.println(triTxt);
                    System.err.println("--------------------------------------------------");
                    continue;
                }
                for (int i = 0; i < triCount * 3; i++) {
                    for (int offset = 0; offset < offsetToSemantic.size(); offset++) {
                        String sem = offsetToSemantic.get(offset);
                        semanticToTriangles.get(sem).add(Short.parseShort(st.nextToken()));
                    }
                }
                List<Point3f> oVerts = new ArrayList<>();
                List<Point3f> oNorms = new ArrayList<>();
                List<Short> oTris = new ArrayList<>();
                Map<String, Integer> combos = new HashMap<>();
                for (int i = 0; i < triCount * 3; i++) {
                    int vv = semanticToTriangles.get("VERTEX").get(i);
                    int nn = vv;
                    if (semanticToTriangles.containsKey("NORMAL")) {
                        nn = semanticToTriangles.get("NORMAL").get(i);
                    }
                    String id = vv + "-" + nn;
                    //System.out.println("  "+id);
                    if (combos.containsKey(id)) {
                        oTris.add(combos.get(id).shortValue());
                    } else {
                        if (vv >= semanticToFloats.get("VERTEX").size()) {
                            System.out.println("Bad position index " + vv + " (out of " + semanticToFloats.get("VERTEX").size() + ")");
                            continue;
                        }
                        Point3f vvp = semanticToFloats.get("VERTEX").get(vv);
                        oVerts.add(vvp);
                        if (semanticToTriangles.containsKey("NORMAL")) {
                            if (nn >= semanticToFloats.get("NORMAL").size()) {
                                System.out.println("Bad normal index " + nn + " (out of " + semanticToFloats.get("NORMALS").size() + ")");
                                continue;
                            }
                            Point3f nnp = semanticToFloats.get("NORMAL").get(nn);
                            oNorms.add(nnp);
                        }
                        int idx = oVerts.size();
                        combos.put(id, idx);
                        oTris.add((short) idx);
                    }
                }
                o.setVertices(oVerts);
                if (semanticToTriangles.containsKey("NORMAL")) {
                    o.setNormals(oNorms);
                }
                o.setIndices(oTris);
                //System.out.println("Converted to verts="+oVerts.size()+", norms="+oNorms.size()+", tris="+oTris.size());
                if (color == null) {
                } else {
                    List<Point4f> colors = new ArrayList<>();
                    for (Point3f oVert : oVerts) {
                        colors.add(color);
                    }
                    o.setColors(colors);
                }
                System.out.println("  added");
                dae.add(o);
            }
        }
        System.out.println("Created " + dae.getChildren().size() + " objects");
        return dae;
    }

    private static Point4f getColor(Node t) {
        String geomID = XMLUtils.getAttribute(t.getParentNode().getParentNode(), "id");
        if (StringUtils.isTrivial(geomID)) {
            return null;
        }
        String material = XMLUtils.getAttribute(t, "material");
        if (StringUtils.isTrivial(material)) {
            return null;
        }
        return mGeometryMaterials.get(geomID + "$" + material);
    }

    private static List<Point3f> findFloatArray(Node parent, String id) {
        Node src = findNodeWithValue(parent, "source", "id", id);
        if (src == null) {
            return null;
        }
        Node fsrc = XMLUtils.findFirstNode(src, "float_array");
        if (fsrc == null) {
            return null;
        }
        Node asrc = XMLUtils.findFirstNode(src, "technique_common/accessor");
        if (asrc == null) {
            return null;
        }
        int stride = IntegerUtils.parseInt(XMLUtils.getAttribute(asrc, "stride"));
        List<Point3f> floats = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(XMLUtils.getText(fsrc), " \r\n\t");
        while (st.countTokens() > 0) {
            Point3f p = new Point3f();
            p.x = FloatUtils.parseFloat(st.nextToken());
            p.y = FloatUtils.parseFloat(st.nextToken());
            if (stride == 3) {
                p.z = FloatUtils.parseFloat(st.nextToken());
            }
            floats.add(p);
        }
        if (st.countTokens() != 0) {
            System.err.println("Odd numer of tokens for " + XMLUtils.getText(fsrc));
        }
        return floats;
    }

//    private static String findInputSource(Node parent, String semantic)
//    {
//        Node i = findNodeWithValue(parent, "input", "semantic", semantic);
//        if (i != null)
//        {
//            String source = XMLUtils.getAttribute(i, "source");
//            if (source.startsWith("#"))
//                source = source.substring(1);
//            return source;
//        }
//        return null;
//    }
    private static Node findNodeWithValue(Node parent, String nodeName, String attrName, String attrValue) {
        for (Node i : XMLUtils.findAllNodesRecursive(parent, nodeName)) {
            String s = XMLUtils.getAttribute(i, attrName);
            if (attrValue.equals(s)) {
                return i;
            }
        }
        return null;
    }

    private static void indexGeometryMaterials(Document doc) {
        mGeometryMaterials.clear();
        for (Node ig : XMLUtils.findNodes(doc, "COLLADA/library_visual_scenes/visual_scene/node/instance_geometry")) {
            String geomID = XMLUtils.getAttribute(ig, "url");
            if (StringUtils.isTrivial(geomID)) {
                continue;
            }
            if (geomID.startsWith("#")) {
                geomID = geomID.substring(1);
            }
            for (Node im : XMLUtils.findAllNodesRecursive(ig, "instance_material")) {
                String symbol = XMLUtils.getAttribute(im, "symbol");
                String target = XMLUtils.getAttribute(im, "target");
                if (target.startsWith("#")) {
                    target = target.substring(1);
                }
                if (!mLibraryMaterials.containsKey(target)) {
                    continue;
                }
                Point4f effect = mLibraryMaterials.get(target);
                if (effect == null) {
                    continue;
                }
                mGeometryMaterials.put(geomID + "$" + symbol, effect);
            }
        }
    }

    private static void indexLibraryMaterials(Document doc) {
        mLibraryMaterials.clear();
        for (Node e : XMLUtils.findNodes(doc, "COLLADA/library_materials/material")) {
            String id = XMLUtils.getAttribute(e, "id");
            if (StringUtils.isTrivial(id)) {
                continue;
            }
            Node ie = XMLUtils.findFirstNode(e, "instance_effect");
            if (ie == null) {
                continue;
            }
            String effectID = XMLUtils.getAttribute(ie, "url");
            if (StringUtils.isTrivial(effectID)) {
                continue;
            }
            if (effectID.startsWith("#")) {
                effectID = effectID.substring(1);
            }
            Point4f effect = mLibraryEffects.get(effectID);
            if (effect == null) {
                continue;
            }
            mLibraryMaterials.put(id, effect);
        }
    }

    private static void indexLibraryEffects(Document doc) {
        mLibraryEffects.clear();
        for (Node e : XMLUtils.findNodes(doc, "COLLADA/library_effects/effect")) {
            String id = XMLUtils.getAttribute(e, "id");
            if (StringUtils.isTrivial(id)) {
                continue;
            }
            Node d = XMLUtils.findFirstNodeRecursive(e, "diffuse");
            if (d == null) {
                continue;
            }
            Node c = XMLUtils.findFirstNodeRecursive(d, "color");
            if (c == null) {
                continue;
            }
            StringTokenizer st = new StringTokenizer(XMLUtils.getText(c).trim(), " \t\r\n");
            if (st.countTokens() != 4) {
                continue;
            }
            Point4f color = new Point4f();
            color.x = FloatUtils.parseFloat(st.nextToken());
            color.y = FloatUtils.parseFloat(st.nextToken());
            color.z = FloatUtils.parseFloat(st.nextToken());
            color.w = FloatUtils.parseFloat(st.nextToken());
            mLibraryEffects.put(id, color);
        }
    }
}
