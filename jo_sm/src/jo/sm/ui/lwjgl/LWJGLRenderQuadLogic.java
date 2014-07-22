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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.tri.JGLObj;
import jo.util.jgl.obj.tri.JGLObjHEALQuad;
import jo.util.lwjgl.win.JGLTextureCache;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.logic.MathUtils;

public class LWJGLRenderQuadLogic {

    public static void addBlocks(JGLGroup group, SparseMatrix<Block> grid) {
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            addBlock(group, grid, i.next());
        }
    }

    public static void addBlock(JGLGroup group, SparseMatrix<Block> grid, Point3i p) {
        Block b = grid.get(p);
        if (b == null) {
            return;
        }
        Point3f lower = new Point3f(p.x, p.y, p.z);
        Point3f upper = new Point3f(p.x + 1, p.y + 1, p.z + 1);
        short[] colors = new short[]{b.getBlockID()};
        List<JGLObj> objs = new ArrayList<>();
        if (!grid.contains(p.x + 1, p.y, p.z)) {
            objs.add(addSelectFace(group, upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
                    RenderPoly.XP, colors[0 % colors.length]));
        }
        if (!grid.contains(p.x - 1, p.y, p.z)) {
            objs.add(addSelectFace(group, lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
                    RenderPoly.XM, colors[1 % colors.length]));
        }
        if (!grid.contains(p.x, p.y + 1, p.z)) {
            objs.add(addSelectFace(group, lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
                    RenderPoly.YP, colors[2 % colors.length]));
        }
        if (!grid.contains(p.x, p.y - 1, p.z)) {
            objs.add(addSelectFace(group, lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
                    RenderPoly.YM, colors[3 % colors.length]));
        }
        if (!grid.contains(p.x, p.y, p.z + 1)) {
            objs.add(addSelectFace(group, lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
                    RenderPoly.ZP, colors[4 % colors.length]));
        }
        if (!grid.contains(p.x, p.y, p.z - 1)) {
            objs.add(addSelectFace(group, lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
                    RenderPoly.ZM, colors[5 % colors.length]));
        }
        for (JGLObj obj : objs) {
            obj.setData("point", p);
            obj.setData("block", b);
        }
    }

    public static List<JGLObj> addBox(JGLGroup group, Point3f lower, Point3f upper, short[] colors) {
        List<JGLObj> objs = new ArrayList<>();
        if ((lower == null) || (upper == null)) {
            return objs;
        }
        upper = new Point3f(upper.x + 1, upper.y + 1, upper.z + 1); // only place where bounds are at +1
        objs.add(addSelectFace(group, upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.XP, colors[0 % colors.length]));
        objs.add(addSelectFace(group, lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
                RenderPoly.XM, colors[1 % colors.length]));
        objs.add(addSelectFace(group, lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.YP, colors[2 % colors.length]));
        objs.add(addSelectFace(group, lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
                RenderPoly.YM, colors[3 % colors.length]));
        objs.add(addSelectFace(group, lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
                RenderPoly.ZP, colors[4 % colors.length]));
        objs.add(addSelectFace(group, lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
                RenderPoly.ZM, colors[5 % colors.length]));
        return objs;
    }

    public static JGLObj addSelectFace(JGLGroup group, float x1, float y1, float z1, float x2, float y2, float z2,
            int face, short type) {
        if (MathUtils.epsilonEquals(x1, x2)) {
            if (face == RenderPoly.XP) {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y1, z2),
                        new Point3f(x1, y2, z2),
                        new Point3f(x1, y2, z1),
                        type);
            } else {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y2, z1),
                        new Point3f(x1, y2, z2),
                        new Point3f(x1, y1, z2),
                        type);
            }
        } else if (MathUtils.epsilonEquals(y1, y2)) {
            if (face == RenderPoly.YP) {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x2, y1, z1),
                        new Point3f(x2, y1, z2),
                        new Point3f(x1, y1, z2),
                        type);
            } else {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y1, z2),
                        new Point3f(x2, y1, z2),
                        new Point3f(x2, y1, z1),
                        type);
            }
        } else if (MathUtils.epsilonEquals(z1, z2)) {
            if (face == RenderPoly.ZP) {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x1, y2, z1),
                        new Point3f(x2, y2, z1),
                        new Point3f(x2, y1, z1),
                        type);
            } else {
                return addSelectQuad(group, new Point3f(x1, y1, z1),
                        new Point3f(x2, y1, z1),
                        new Point3f(x2, y2, z1),
                        new Point3f(x1, y2, z1),
                        type);
            }
        }
        return null;
    }

    private static final List<Point2f> square = new ArrayList<>();

    static {
        square.add(new Point2f(0, 0));
        square.add(new Point2f(0, 1));
        square.add(new Point2f(1, 1));
        square.add(new Point2f(1, 0));
    }

    public static JGLObj addSelectQuad(JGLGroup group, Point3f left, Point3f top, Point3f right, Point3f bottom,
            short type) {
        JGLObjHEALQuad q = new JGLObjHEALQuad(left, top, right, bottom);
        if (JGLTextureCache.isRegistered(type)) {
            q.setTextureID(type);
            q.setTextures(square);
        } else {
            ImageIcon icon = BlockTypeColors.getBlockImage(type);
            if (icon != null) {
                JGLTextureCache.register(type, (BufferedImage) icon.getImage());
                q.setTextureID(type);
                q.setTextures(square);
            } else {
                Color c = BlockTypeColors.getFillColor(type);
                Point3f color = new Point3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
                q.setSolidColor(color);
            }
        }
        group.add(q);
        return q;
    }

}
