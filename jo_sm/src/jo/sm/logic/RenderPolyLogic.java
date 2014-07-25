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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import jo.sm.data.BlockTypes;
import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.vecmath.Matrix3f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.logic.Point3iLogic;


public class RenderPolyLogic {
    private static final Logger log = Logger.getLogger(RenderPolyLogic.class.getName());

    public static void fillPolys(SparseMatrix<Block> blocks, RenderSet set) {
        set.getAllPolys().clear();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        blocks.getBounds(lower, upper);
        getBasicPolys(blocks, upper, lower, set.getAllPolys());
        //log.log(Level.INFO, "FillPolys: count="+set.getAllPolys().size());
    }

    private static void getBasicPolys(SparseMatrix<Block> blocks,
            Point3i upper, Point3i lower, List<RenderPoly> polys) {
        /*
         for (int z = lower.z; z <= upper.z; z++)
         {
         System.out.println("Z="+z);
         for (int y = lower.y; y <= upper.y; y++)
         {
         if (y < 10)
         System.out.print(" ");
         System.out.print(y+": ");
         for (int x = lower.x; x <= upper.x; x++)
         {
         Block b = blocks.get(x, y, z);
         //                    if (b == null)
         //                        System.out.print(" ------------------------");
         //                    else
         //                        System.out.print(" "+b.getOrientation()+":"+StringUtils.zeroPrefix(Integer.toBinaryString(b.getBitfield()), 24));
         if (b == null)
         System.out.print(" --");
         else
         System.out.print(" "+StringUtils.spacePrefix(Integer.toString(b.getOrientation()), 2));
         }
         System.out.println();
         }
         }
         */
        for (Iterator<Point3i> i = blocks.iteratorNonNull(); i.hasNext();) {
            Point3i p = i.next();
            Block b = blocks.get(p);
            short blockID = b.getBlockID();
            if (BlockTypes.isCorner(blockID) || BlockTypes.isPowerCorner(blockID)) {
                doCorner(blocks, p, polys);
            } else if (BlockTypes.isWedge(blockID) || BlockTypes.isPowerWedge(blockID)) {
                doWedge(blocks, p, polys);
            } else if (BlockTypes.isPenta(blockID) || BlockTypes.isPowerPenta(blockID)) {
                doPenta(blocks, p, polys);
            } else if (BlockTypes.isTetra(blockID) || BlockTypes.isPowerTetra(blockID)) {
                doTetra(blocks, p, polys);
            } else {
                doCube(blocks, p, polys);
            }
        }
    }

    private static void doPenta(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys) {
        doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
    }

    private static void doTetra(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys) {
        doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
    }

    private static void doCorner(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys) {
        //System.out.println("Corner, ori="+blocks.get(p).getOrientation());
        switch (blocks.get(p).getOrientation()) {
            case 0: // spire: xp,zm>yp
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderPoly.TRI4); // back
                doZPSquare(blocks, p, polys, RenderPoly.TRI1); // back
                doRect(blocks, p, polys, RenderPoly.XPYP, RenderPoly.TRI2);
                doRect(blocks, p, polys, RenderPoly.YPZM, RenderPoly.TRI1);
                break;
            case 1: // spire: xp,zp>yp
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderPoly.TRI1); // back
                doZMSquare(blocks, p, polys, RenderPoly.TRI1); // back
                doRect(blocks, p, polys, RenderPoly.XPYP, RenderPoly.TRI1);
                doRect(blocks, p, polys, RenderPoly.YPZP, RenderPoly.TRI4);
                break;
            case 2: // spire: xm,zp>yp
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderPoly.TRI1); // back
                doZMSquare(blocks, p, polys, RenderPoly.TRI2); // back
                doRect(blocks, p, polys, RenderPoly.XMYP, RenderPoly.TRI1);
                doRect(blocks, p, polys, RenderPoly.YPZP, RenderPoly.TRI3);
                break;
            case 3: // spire: xm,zp>yp
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderPoly.TRI4); // back
                doZPSquare(blocks, p, polys, RenderPoly.TRI2); // back
                doRect(blocks, p, polys, RenderPoly.XMYP, RenderPoly.TRI4);
                doRect(blocks, p, polys, RenderPoly.YPZM, RenderPoly.TRI2);
                break;
            case 4: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderPoly.TRI3); // back
                doZPSquare(blocks, p, polys, RenderPoly.TRI4); // back
                doRect(blocks, p, polys, RenderPoly.XPYM, RenderPoly.TRI3);
                doRect(blocks, p, polys, RenderPoly.YMZM, RenderPoly.TRI1);
                break;
            case 5: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderPoly.TRI2); // back
                doZMSquare(blocks, p, polys, RenderPoly.TRI4); // back
                doRect(blocks, p, polys, RenderPoly.XPYM, RenderPoly.TRI2);
                doRect(blocks, p, polys, RenderPoly.YMZP, RenderPoly.TRI4);
                break;
            case 6: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderPoly.TRI2); // back
                doZMSquare(blocks, p, polys, RenderPoly.TRI3); // back
                doRect(blocks, p, polys, RenderPoly.XMYM, RenderPoly.TRI4);
                doRect(blocks, p, polys, RenderPoly.YMZP, RenderPoly.TRI3);
                break;
            case 7: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderPoly.TRI3); // back
                doZPSquare(blocks, p, polys, RenderPoly.TRI3); // back
                doRect(blocks, p, polys, RenderPoly.XMYM, RenderPoly.TRI3);
                doRect(blocks, p, polys, RenderPoly.YMZM, RenderPoly.TRI2);
                break;
        }
    }

    private static void doWedge(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys) {
        switch (blocks.get(p).getOrientation()) {
            case 0: // YPZM
                doXMSquare(blocks, p, polys, RenderPoly.TRI4);
                doXPSquare(blocks, p, polys, RenderPoly.TRI4);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.YPZM);
                break;
            case 1: // XMYP
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZMSquare(blocks, p, polys, RenderPoly.TRI2);
                doZPSquare(blocks, p, polys, RenderPoly.TRI2);
                doRect(blocks, p, polys, RenderPoly.XMYP);
                break;
            case 2: // YPZP
                doXMSquare(blocks, p, polys, RenderPoly.TRI1);
                doXPSquare(blocks, p, polys, RenderPoly.TRI1);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.YPZP);
                break;
            case 3: // XPYP
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZMSquare(blocks, p, polys, RenderPoly.TRI1);
                doZPSquare(blocks, p, polys, RenderPoly.TRI1);
                doRect(blocks, p, polys, RenderPoly.XPYP);
                break;
            case 4: // YMZM
                doXMSquare(blocks, p, polys, RenderPoly.TRI3);
                doXPSquare(blocks, p, polys, RenderPoly.TRI3);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.YMZM);
                break;
            case 5: // XPYM
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderPoly.TRI4);
                doZPSquare(blocks, p, polys, RenderPoly.TRI4);
                doRect(blocks, p, polys, RenderPoly.XPYM);
                break;
            case 6: // YMZP
                doXMSquare(blocks, p, polys, RenderPoly.TRI2);
                doXPSquare(blocks, p, polys, RenderPoly.TRI2);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.YMZP);
                break;
            case 7: // XMYM
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderPoly.TRI3);
                doZPSquare(blocks, p, polys, RenderPoly.TRI3);
                doRect(blocks, p, polys, RenderPoly.XMYM);
                break;
            case 8: // XPZM
            case 12: // ???
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.TRI4);
                doYMSquare(blocks, p, polys, RenderPoly.TRI4);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.ZMXP);
                break;
            case 10: // XMZM
            case 14:
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.TRI3);
                doYMSquare(blocks, p, polys, RenderPoly.TRI3);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.ZMXM);
                break;
            case 11: // XMZP
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.TRI2);
                doYMSquare(blocks, p, polys, RenderPoly.TRI2);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.ZPXM);
                break;
            case 13: // XPZP
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.TRI1);
                doYMSquare(blocks, p, polys, RenderPoly.TRI1);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.ZPXP);
                break;
            default:
                log.log(Level.INFO, "Wedge with unknown ori="+blocks.get(p).getOrientation());
                break;
        }
    }

    private static void doCube(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys) {
        doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
    }

    private static void doRect(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int facing) {
        doRect(blocks, p, polys, facing, RenderPoly.SQUARE);
    }

    private static void doRect(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int facing, int type) {
        RenderPoly rp = new RenderPoly();
        rp.setPosition(p);
        rp.setBlock(blocks.get(p));
        rp.setNormal(facing);
        switch (facing) {
            case RenderPoly.XPYP:
            case RenderPoly.XMYM:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),});
                break;
            case RenderPoly.XPYM:
            case RenderPoly.XMYP:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),});
                break;
            case RenderPoly.YPZP:
            case RenderPoly.YMZM:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),});
                break;
            case RenderPoly.YPZM:
            case RenderPoly.YMZP:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),});
                break;
            case RenderPoly.ZPXP:
            case RenderPoly.ZMXM:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),});
                break;
            case RenderPoly.ZPXM:
            case RenderPoly.ZMXP:
                rp.setModelPoints(new Point3i[]{
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),});
                break;
            default:
                return;
        }
        rp.setType(type);
        polys.add(rp);
    }

    private static void doZMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x, p.y, p.z - 1)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.ZM);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 0, p.y + 0, p.z + 0),
                new Point3i(p.x + 1, p.y + 0, p.z + 0),
                new Point3i(p.x + 1, p.y + 1, p.z + 0),
                new Point3i(p.x + 0, p.y + 1, p.z + 0),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doZPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x, p.y, p.z + 1)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.ZP);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 0, p.y + 0, p.z + 1),
                new Point3i(p.x + 1, p.y + 0, p.z + 1),
                new Point3i(p.x + 1, p.y + 1, p.z + 1),
                new Point3i(p.x + 0, p.y + 1, p.z + 1),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x, p.y - 1, p.z)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.YM);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 0, p.y + 0, p.z + 0),
                new Point3i(p.x + 1, p.y + 0, p.z + 0),
                new Point3i(p.x + 1, p.y + 0, p.z + 1),
                new Point3i(p.x + 0, p.y + 0, p.z + 1),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x, p.y + 1, p.z)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.YP);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 0, p.y + 1, p.z + 0),
                new Point3i(p.x + 1, p.y + 1, p.z + 0),
                new Point3i(p.x + 1, p.y + 1, p.z + 1),
                new Point3i(p.x + 0, p.y + 1, p.z + 1),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x - 1, p.y, p.z)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.XM);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 0, p.y + 0, p.z + 0),
                new Point3i(p.x + 0, p.y + 1, p.z + 0),
                new Point3i(p.x + 0, p.y + 1, p.z + 1),
                new Point3i(p.x + 0, p.y + 0, p.z + 1),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type) {
        if (!opaque(blocks, p.x + 1, p.y, p.z)) {
            RenderPoly rp = new RenderPoly();
            rp.setPosition(p);
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.XP);
            rp.setModelPoints(new Point3i[]{
                new Point3i(p.x + 1, p.y + 0, p.z + 0),
                new Point3i(p.x + 1, p.y + 1, p.z + 0),
                new Point3i(p.x + 1, p.y + 1, p.z + 1),
                new Point3i(p.x + 1, p.y + 0, p.z + 1),});
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static boolean opaque(SparseMatrix<Block> grid, int x, int y, int z) {
        Block b = grid.get(x, y, z);
        return (b != null) && !BlockTypes.isAnyCorner(b.getBlockID()) && !BlockTypes.isAnyWedge(b.getBlockID());
    }

    public static void transformAndSort(final RenderSet set, Matrix4f transform) {
        List<RenderPoly> tiles = set.getAllPolys();
        List<RenderPoly> visible = set.getVisiblePolys();
        visible.clear();

        Point3f o = new Point3f();
        transform.transform(o);
        set.setOrigin(o);

        Matrix3f rot = new Matrix3f();
        transform.get(rot);
        float scale = transform.getScale();
        boolean[] showing = new boolean[6 + 12];
        Point3f unitX = calcShowing(showing, rot, 1, 0, 0, RenderPoly.XP, RenderPoly.XM);
        Point3f unitY = calcShowing(showing, rot, 0, 1, 0, RenderPoly.YP, RenderPoly.YM);
        Point3f unitZ = calcShowing(showing, rot, 0, 0, 1, RenderPoly.ZP, RenderPoly.ZM);
        calcShowing(showing, rot, 1, 1, 0, RenderPoly.XPYP, RenderPoly.XMYM);
        calcShowing(showing, rot, -1, 1, 0, RenderPoly.XMYP, RenderPoly.XPYM);
        calcShowing(showing, rot, 0, 1, 1, RenderPoly.YPZP, RenderPoly.YMZM);
        calcShowing(showing, rot, 0, -1, 1, RenderPoly.YMZP, RenderPoly.YPZM);
        calcShowing(showing, rot, 1, 0, 1, RenderPoly.ZPXP, RenderPoly.ZMXM);
        calcShowing(showing, rot, 1, 0, -1, RenderPoly.ZMXP, RenderPoly.ZPXM);
        unitX.scale(scale);
        set.setUnitX(unitX);
        unitY.scale(scale);
        set.setUnitY(unitY);
        unitZ.scale(scale);
        set.setUnitZ(unitZ);
        //log.log(Level.INFO, "Unit X="+unitX+", Y="+unitY+", Z="+unitZ);
        //log.log(Level.INFO, "Showing +x="+showing[0]+", -x="+showing[1]+", +y="+showing[2]+", -y="+showing[3]+", +z="+showing[4]+", -z="+showing[5]);
        //log.log(Level.INFO, "Showing XPYP="+showing[RenderPoly.XPYP]);
        //System.out.println("Unit X="+unitX+", Y="+unitY+", Z="+unitZ);
        //System.out.println("Showing +x="+showing[0]+", -x="+showing[1]+", +y="+showing[2]+", -y="+showing[3]+", +z="+showing[4]+", -z="+showing[5]);
        //System.out.println("Showing XPYP="+showing[RenderPoly.XPYP]);
        for (RenderPoly tile : tiles) {
            if (showing[tile.getNormal()]) {
                visible.add(tile);
            }
        }
        Collections.sort(visible, new Comparator<RenderPoly>() {
            @Override
            public int compare(RenderPoly tile1, RenderPoly tile2) {
                float delta = getMidZ(tile2, set) - getMidZ(tile1, set);
                if (Math.abs(delta) < .001) {
                    return (int) Math.signum(tile1.getBlock().getBlockID() - tile2.getBlock().getBlockID());
                }
                return (int) Math.signum(delta);
            }
        });
        //log.log(Level.INFO, "TransformAndSort, visible="+set.getVisiblePolys().size());
        //System.out.println("TransformAndSort, visible="+set.getVisiblePolys().size());
    }

    private static float getX(Point3i p, RenderSet set) {
        float z = set.getOrigin().x + p.x * set.getUnitX().x + p.y * set.getUnitY().x + p.z * set.getUnitZ().x;
        return z;
    }

    private static float getY(Point3i p, RenderSet set) {
        float z = set.getOrigin().y + p.x * set.getUnitX().y + p.y * set.getUnitY().y + p.z * set.getUnitZ().y;
        return z;
    }

    private static float getZ(Point3i p, RenderSet set) {
        float z = set.getOrigin().z + p.x * set.getUnitX().z + p.y * set.getUnitY().z + p.z * set.getUnitZ().z;
        return z;
    }

    private static float getMidZ(RenderPoly tile, RenderSet set) {
        float z = 0;
        for (Point3i p : tile.getModelPoints()) {
            z += getZ(p, set);
        }
        z /= tile.getModelPoints().length;
        return z;
    }

    private static Point3f calcShowing(boolean[] showing, Matrix3f rot, int dx, int dy, int dz, int axis, int naxis) {
        Point3f xp = new Point3f(dx, dy, dz);
        rot.transform(xp);
        showing[axis] = xp.z < 0;
        showing[naxis] = !showing[axis];
        return xp;
    }

    public static void draw(Graphics2D g2, RenderSet set, boolean fancyGraphics) {
        for (RenderPoly tile : set.getVisiblePolys().toArray(new RenderPoly[0])) {
            ImageIcon icon = null;
            if (fancyGraphics) {
                icon = BlockTypeColors.getBlockImage(tile.getBlock().getBlockID());
            }
            if (tile.getType() == RenderPoly.SQUARE) {
                renderSquare(g2, set, tile, icon);
            } else if ((tile.getType() >= RenderPoly.TRI1) && (tile.getType() <= RenderPoly.TRI4)) {
                renderTriangle(g2, set, tile, icon);
            }
        }

    }

    private static void renderTriangle(Graphics2D g2, RenderSet set,
            RenderPoly tile, ImageIcon icon) {
        //log.log(Level.INFO, "Render triangle "+tile.getType());
        //System.out.println("Render triangle "+tile.getType());
        Point3f[] corners = getCorners(tile, set);
        int pCenter = (tile.getType() - RenderPoly.TRI1);
        int pLeft = (pCenter + 1) % 4;
        int pRight = (pCenter + 3) % 4;
        Path2D p = new Path2D.Float();
        p.moveTo(corners[pCenter].x, corners[pCenter].y);
        p.lineTo(corners[pLeft].x, corners[pLeft].y);
        p.lineTo(corners[pRight].x, corners[pRight].y);
        p.lineTo(corners[pCenter].x, corners[pCenter].y);
        if (icon != null) {
            float m00 = (corners[pRight].x - corners[pCenter].x) / 64f;
            float m10 = (corners[pRight].y - corners[pCenter].y) / 64f;
            float m01 = (corners[pLeft].x - corners[pCenter].x) / 64f;
            float m11 = (corners[pLeft].y - corners[pCenter].y) / 64f;
            float m02 = corners[pCenter].x;
            float m12 = corners[pCenter].y;
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            if ((icon.getIconWidth() != 64) || (icon.getIconHeight() != 64)) {
                t.scale(64.0 / icon.getIconWidth(), 64.0 / icon.getIconHeight());
            }
            Graphics2D g3 = (Graphics2D) g2.create();
            g3.clip(p);
            g3.drawImage(icon.getImage(), t, null);
        } else {
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

    private static void renderSquare(Graphics2D g2, RenderSet set,
            RenderPoly tile, ImageIcon icon) {
        Point3f[] corners = getCorners(tile, set);
        if (icon != null) {
            float m00 = (corners[1].x - corners[0].x) / 64f;
            float m10 = (corners[1].y - corners[0].y) / 64f;
            float m01 = (corners[3].x - corners[0].x) / 64f;
            float m11 = (corners[3].y - corners[0].y) / 64f;
            float m02 = corners[0].x;
            float m12 = corners[0].y;
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            if ((icon.getIconWidth() != 64) || (icon.getIconHeight() != 64)) {
                t.scale(64.0 / icon.getIconWidth(), 64.0 / icon.getIconHeight());
            }
            g2.drawImage(icon.getImage(), t, null);
        } else {
            Path2D p = new Path2D.Float();
            p.moveTo(corners[0].x, corners[0].y);
            p.lineTo(corners[1].x, corners[1].y);
            p.lineTo(corners[2].x, corners[2].y);
            p.lineTo(corners[3].x, corners[3].y);
            p.lineTo(corners[0].x, corners[0].y);
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

    public static Point3f[] getCorners(RenderPoly tile, RenderSet set) {
        Point3f[] corners = new Point3f[tile.getModelPoints().length];
        for (int i = 0; i < corners.length; i++) {
            Point3i p = tile.getModelPoints()[i];
            corners[i] = new Point3f();
            corners[i].x = getX(p, set);
            corners[i].y = getY(p, set);
        }
        return corners;
    }

    public static void getBounds(RenderPoly tile, Point3i lower, Point3i upper) {
        lower.set(tile.getModelPoints()[0]);
        upper.set(tile.getModelPoints()[0]);
        for (int i = 1; i < tile.getModelPoints().length; i++) {
            Point3iLogic.min(lower, lower, tile.getModelPoints()[i]);
            Point3iLogic.max(upper, upper, tile.getModelPoints()[i]);
        }
    }

}
