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
package jo.sm.ui.lwjgl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.tri.JGLObj;
import jo.util.lwjgl.win.JGLTextureCache;
import jo.vecmath.Color3f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.logic.MathUtils;

public class LWJGLRenderLogic {

    private static int mTextureID = -1;

    public static void addBlocks(JGLGroup group, SparseMatrix<Block> grid, boolean plain) {
        if (mTextureID < 0) {
            BlockTypeColors.loadBlockIcons();
            JGLTextureCache.register(1, BlockTypeColors.mAllTextures);
            mTextureID = 1;
        }
        MeshInfo info = new MeshInfo();
        info.verts = new ArrayList<>();
        info.indexes = new ArrayList<>();
        if (plain) {
            info.colors = new ArrayList<>();
        } else {
            info.uv = new ArrayList<>();
        }
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            addBlock(info, grid, i.next());
        }
        JGLObj obj = infoToObj(info);
        group.add(obj);
    }

    /**
     *
     * @param info
     * @return
     */
    public static JGLObj infoToObj(MeshInfo info) {
        JGLObj obj = new JGLObj();
        obj.setMode(JGLObj.QUADS);
        obj.setVertices(info.verts);
        obj.setIndices(info.indexes);
        if (info.colors != null) {
            obj.setColors(info.colors);
        } else {
            obj.setTextures(info.uv);
            obj.setTextureID(mTextureID);
        }
        return obj;
    }

    /**
     *
     * @param group
     * @param grid
     * @param p
     */
    public static void addBlock(MeshInfo group, SparseMatrix<Block> grid, Point3i p) {
        Block b = grid.get(p);
        if (b == null) {
            return;
        }
        Point3f lower = new Point3f(p.x - .5f, p.y - .5f, p.z - .5f);
        Point3f upper = new Point3f(p.x + .5f, p.y + .5f, p.z + .5f);
        short[] colors = new short[]{b.getBlockID()};
        List<JGLObj> objs = new ArrayList<>();
        if (!grid.contains(p.x + 1, p.y, p.z)) {
            addSelectFace(group, upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
                    RenderPoly.XP, colors[0 % colors.length]);
        }
        if (!grid.contains(p.x - 1, p.y, p.z)) {
            addSelectFace(group, lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
                    RenderPoly.XM, colors[1 % colors.length]);
        }
        if (!grid.contains(p.x, p.y + 1, p.z)) {
            addSelectFace(group, lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
                    RenderPoly.YP, colors[2 % colors.length]);
        }
        if (!grid.contains(p.x, p.y - 1, p.z)) {
            addSelectFace(group, lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
                    RenderPoly.YM, colors[3 % colors.length]);
        }
        if (!grid.contains(p.x, p.y, p.z + 1)) {
            addSelectFace(group, lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
                    RenderPoly.ZP, colors[4 % colors.length]);
        }
        if (!grid.contains(p.x, p.y, p.z - 1)) {
            addSelectFace(group, lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
                    RenderPoly.ZM, colors[5 % colors.length]);
        }
        for (JGLObj obj : objs) {
            obj.setData("point", p);
            obj.setData("block", b);
        }
    }

    /**
     *
     * @param group
     * @param lower
     * @param upper
     * @param colors
     */
    public static void addBox(MeshInfo group, Point3f lower, Point3f upper, short[] colors) {
        if ((lower == null) || (upper == null)) {
            return;
        }
        lower = new Point3f(lower.x - .5f, lower.y - .5f, lower.z - .5f);
        upper = new Point3f(upper.x + .5f, upper.y + .5f, upper.z + .5f); // only place where bounds are at +1
        addSelectFace(group, upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.XP, colors[0 % colors.length]);
        addSelectFace(group, lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
                RenderPoly.XM, colors[1 % colors.length]);
        addSelectFace(group, lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.YP, colors[2 % colors.length]);
        addSelectFace(group, lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
                RenderPoly.YM, colors[3 % colors.length]);
        addSelectFace(group, lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
                RenderPoly.ZP, colors[4 % colors.length]);
        addSelectFace(group, lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
                RenderPoly.ZM, colors[5 % colors.length]);
    }

    /**
     *
     * @param group
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param face
     * @param type
     */
    public static void addSelectFace(MeshInfo group, float x1, float y1, float z1, float x2, float y2, float z2,
            int face, short type) {
        if (MathUtils.epsilonEquals(x1, x2)) {
            if (face == RenderPoly.XP) {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y1, z2),
                        new Point3f(x1, y2, z2),
                        new Point3f(x1, y2, z1),
                        type);
            } else {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y2, z1),
                        new Point3f(x1, y2, z2),
                        new Point3f(x1, y1, z2),
                        type);
            }
        } else if (MathUtils.epsilonEquals(y1, y2)) {
            if (face == RenderPoly.YP) {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x2, y1, z1),
                        new Point3f(x2, y1, z2),
                        new Point3f(x1, y1, z2),
                        type);
            } else {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y1, z2),
                        new Point3f(x2, y1, z2),
                        new Point3f(x2, y1, z1),
                        type);
            }
        } else if (MathUtils.epsilonEquals(z1, z2)) {
            if (face == RenderPoly.ZP) {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y2, z1),
                        new Point3f(x2, y2, z1),
                        new Point3f(x2, y1, z1),
                        type);
            } else {
                addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x2, y1, z1),
                        new Point3f(x2, y2, z1),
                        new Point3f(x1, y2, z1),
                        type);
            }
        }
    }

    /**
     *
     * @param info
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param type
     */
    public static void addSelectQuad(MeshInfo info, Point3f left, Point3f top, Point3f right, Point3f bottom,
            short type) {
        info.verts.add(left);
        info.verts.add(top);
        info.verts.add(right);
        info.verts.add(bottom);
        info.indexes.add(info.verts.size() - 1);
        info.indexes.add(info.verts.size() - 2);
        info.indexes.add(info.verts.size() - 3);
        info.indexes.add(info.verts.size() - 4);
        if (info.colors != null) {
            Color c = BlockTypeColors.getFillColor(type);
            Color3f color = new Color3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
            info.colors.add(color);
            info.colors.add(color);
            info.colors.add(color);
            info.colors.add(color);
        }
        if (info.uv != null) {
            Rectangle2D.Float rec = BlockTypeColors.getAllTextureLocation(type);
            info.uv.add(new Point2f(rec.x, rec.y));
            info.uv.add(new Point2f(rec.x + rec.width, rec.y));
            info.uv.add(new Point2f(rec.x + rec.width, rec.y + rec.height));
            info.uv.add(new Point2f(rec.x, rec.y + rec.height));
        }
    }
}

class MeshInfo {

    List<Point3f> verts;
    List<Integer> indexes;
    List<Color3f> colors;
    List<Point2f> uv;
}
