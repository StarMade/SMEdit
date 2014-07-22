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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderTile;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.vecmath.Matrix3f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;


public class RenderLogic {
    private static final Logger log = Logger.getLogger(RenderLogic.class.getName());

    public static List<RenderTile> getRender(SparseMatrix<Block> blocks) {
        List<RenderTile> polys = new ArrayList<>();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        blocks.getBounds(lower, upper);
        getBasicPolys(blocks, upper, lower, polys);
        return polys;
    }

    private static void getBasicPolys(SparseMatrix<Block> blocks,
            Point3i upper, Point3i lower, List<RenderTile> polys) {
        for (CubeIterator i = new CubeIterator(lower, upper); i.hasNext();) {
            Point3i p = i.next();
            if (!blocks.contains(p)) {
                continue;
            }
            Block b = blocks.get(p);
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID())) {
                doCorner(blocks, p, polys);
            } else if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID())) {
                doWedge(blocks, p, polys);
            } else if (BlockTypes.isPenta(b.getBlockID()) || BlockTypes.isPowerPenta(b.getBlockID())) {
                doPenta(blocks, p, polys);
            } else if (BlockTypes.isTetra(b.getBlockID()) || BlockTypes.isPowerTetra(b.getBlockID())) {
                doTetra(blocks, p, polys);
            } else {
                doCube(blocks, p, polys);
            }
        }
    }

    private static void doPenta(SparseMatrix<Block> blocks, Point3i p, List<RenderTile> polys) {

    }

    private static void doTetra(SparseMatrix<Block> blocks, Point3i p, List<RenderTile> polys) {

    }

    private static void doCorner(SparseMatrix<Block> blocks, Point3i p, List<RenderTile> polys) {
        log.log(Level.INFO, "Corner, ori="+blocks.get(p).getOrientation());
        switch (blocks.get(p).getOrientation()) {
            case 0: // spire: xp,zm>yp
                doYMSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderTile.TRI4); // back
                doZPSquare(blocks, p, polys, RenderTile.TRI1); // back
                doRect(blocks, p, polys, RenderTile.XPYP);
                doRect(blocks, p, polys, RenderTile.YPZM);
                break;
            case 1: // spire: xp,zp>yp
                doYMSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderTile.TRI1); // back
                doZMSquare(blocks, p, polys, RenderTile.TRI1); // back
                doRect(blocks, p, polys, RenderTile.XPYP);
                doRect(blocks, p, polys, RenderTile.YPZP);
                break;
            case 2: // spire: xm,zp>yp
                doYMSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderTile.TRI1); // back
                doZMSquare(blocks, p, polys, RenderTile.TRI2); // back
                doRect(blocks, p, polys, RenderTile.XMYP);
                doRect(blocks, p, polys, RenderTile.YPZP);
                break;
            case 3: // spire: xm,zp>yp
                doYMSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderTile.TRI4); // back
                doZPSquare(blocks, p, polys, RenderTile.TRI2); // back
                doRect(blocks, p, polys, RenderTile.XMYP);
                doRect(blocks, p, polys, RenderTile.YPZM);
                break;
            case 4: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderTile.TRI3); // back
                doZPSquare(blocks, p, polys, RenderTile.TRI4); // back
                doRect(blocks, p, polys, RenderTile.XPYM);
                doRect(blocks, p, polys, RenderTile.YMZM);
                break;
            case 5: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXMSquare(blocks, p, polys, RenderTile.TRI2); // back
                doZMSquare(blocks, p, polys, RenderTile.TRI4); // back
                doRect(blocks, p, polys, RenderTile.XPYM);
                doRect(blocks, p, polys, RenderTile.YMZP);
                break;
            case 6: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderTile.TRI2); // back
                doZMSquare(blocks, p, polys, RenderTile.TRI3); // back
                doRect(blocks, p, polys, RenderTile.XMYM);
                doRect(blocks, p, polys, RenderTile.YMZP);
                break;
            case 7: // spire: xm,zp>yp
                doYPSquare(blocks, p, polys, RenderTile.SQUARE); // bottom
                doXPSquare(blocks, p, polys, RenderTile.TRI3); // back
                doZPSquare(blocks, p, polys, RenderTile.TRI3); // back
                doRect(blocks, p, polys, RenderTile.XMYM);
                doRect(blocks, p, polys, RenderTile.YMZM);
                break;
        }
    }

    private static void doWedge(SparseMatrix<Block> blocks, Point3i p, List<RenderTile> polys) {
        switch (blocks.get(p).getOrientation()) {
            case 0: // YPZM
                doXMSquare(blocks, p, polys, RenderTile.TRI4);
                doXPSquare(blocks, p, polys, RenderTile.TRI4);
                // no YP face
                doYMSquare(blocks, p, polys, RenderTile.SQUARE);
                doZPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderTile.YPZM);
                break;
            case 1: // XMYP
                doXPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no XM face
                // no YP face
                doYMSquare(blocks, p, polys, RenderTile.SQUARE);
                doZMSquare(blocks, p, polys, RenderTile.TRI2);
                doZPSquare(blocks, p, polys, RenderTile.TRI2);
                doRect(blocks, p, polys, RenderTile.XMYP);
                break;
            case 2: // YPZP
                doXMSquare(blocks, p, polys, RenderTile.TRI1);
                doXPSquare(blocks, p, polys, RenderTile.TRI1);
                // no YP face
                doYMSquare(blocks, p, polys, RenderTile.SQUARE);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderTile.SQUARE);
                doRect(blocks, p, polys, RenderTile.YPZP);
                break;
            case 3: // XPYP
                // no XP face
                doXMSquare(blocks, p, polys, RenderTile.SQUARE);
                // no YP face
                doYMSquare(blocks, p, polys, RenderTile.SQUARE);
                doZMSquare(blocks, p, polys, RenderTile.TRI1);
                doZPSquare(blocks, p, polys, RenderTile.TRI1);
                doRect(blocks, p, polys, RenderTile.XPYP);
                break;
            case 4: // YMZM
                doXMSquare(blocks, p, polys, RenderTile.TRI3);
                doXPSquare(blocks, p, polys, RenderTile.TRI3);
                doYPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no YM face
                doZPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderTile.YMZM);
                break;
            case 5: // XPYM
                // no XP face
                doXMSquare(blocks, p, polys, RenderTile.SQUARE);
                doYPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderTile.TRI4);
                doZPSquare(blocks, p, polys, RenderTile.TRI4);
                doRect(blocks, p, polys, RenderTile.XPYM);
                break;
            case 6: // YMZP
                doXMSquare(blocks, p, polys, RenderTile.TRI2);
                doXPSquare(blocks, p, polys, RenderTile.TRI2);
                doYPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no YM face
                // no ZP face
                doZMSquare(blocks, p, polys, RenderTile.SQUARE);
                doRect(blocks, p, polys, RenderTile.YMZP);
                break;
            case 7: // XMYM
                doXPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderTile.TRI3);
                doZPSquare(blocks, p, polys, RenderTile.TRI3);
                doRect(blocks, p, polys, RenderTile.XMYM);
                break;
            case 8: // XPZM
            case 12: // ???
                // no XP face
                doXMSquare(blocks, p, polys, RenderTile.SQUARE);
                doYPSquare(blocks, p, polys, RenderTile.TRI2);
                doYMSquare(blocks, p, polys, RenderTile.TRI2);
                doZPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderTile.ZMXP);
                break;
            case 10: // XMZM
                doXPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderTile.TRI3);
                doYMSquare(blocks, p, polys, RenderTile.TRI3);
                doZPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderTile.ZMXM);
                break;
            case 11: // XMZP
                doXPSquare(blocks, p, polys, RenderTile.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderTile.TRI4);
                doYMSquare(blocks, p, polys, RenderTile.TRI4);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderTile.SQUARE);
                doRect(blocks, p, polys, RenderTile.ZPXM);
                break;
            case 13: // XPZP
                // no XP face
                doXMSquare(blocks, p, polys, RenderTile.SQUARE);
                doYPSquare(blocks, p, polys, RenderTile.TRI1);
                doYMSquare(blocks, p, polys, RenderTile.TRI1);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderTile.SQUARE);
                doRect(blocks, p, polys, RenderTile.ZPXP);
                break;
            default:
                log.log(Level.INFO, "Wedge with unknown ori="+blocks.get(p).getOrientation());
                break;
        }
    }

    private static void doCube(SparseMatrix<Block> blocks, Point3i p, List<RenderTile> polys) {
        doXPSquare(blocks, p, polys, RenderTile.SQUARE);
        doXMSquare(blocks, p, polys, RenderTile.SQUARE);
        doYPSquare(blocks, p, polys, RenderTile.SQUARE);
        doYMSquare(blocks, p, polys, RenderTile.SQUARE);
        doZPSquare(blocks, p, polys, RenderTile.SQUARE);
        doZMSquare(blocks, p, polys, RenderTile.SQUARE);
    }

    private static void doRect(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int facing) {
        RenderTile rp = new RenderTile();
        rp.setBlock(blocks.get(p));
        rp.setFacing(facing);
        rp.setCenter(new Point3i(p));
        rp.setType(RenderTile.RECTANGLE);
        polys.add(rp);
    }

    private static void doZMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x, p.y, p.z - 1))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.ZM);
            rp.setCenter(new Point3i(p));
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doZPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x, p.y, p.z + 1))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.ZP);
            rp.setCenter(new Point3i(p.x, p.y, p.z + 1));
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x, p.y - 1, p.z))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.YM);
            rp.setCenter(new Point3i(p));
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x, p.y + 1, p.z))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.YP);
            rp.setCenter(new Point3i(p.x, p.y + 1, p.z));
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x - 1, p.y, p.z))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.XM);
            rp.setCenter(new Point3i(p));
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderTile> polys, int type) {
        if (!blocks.contains(new Point3i(p.x + 1, p.y, p.z))) {
            RenderTile rp = new RenderTile();
            rp.setBlock(blocks.get(p));
            rp.setFacing(RenderTile.XP);
            rp.setCenter(new Point3i(p.x + 1, p.y, p.z));
            rp.setType(type);
            polys.add(rp);
        }
    }

    public static void transformAndSort(List<RenderTile> tiles, Matrix4f transform) {
        Matrix3f rot = new Matrix3f();
        transform.get(rot);

        boolean[] showing = new boolean[6 + 12];
        calcShowing(showing, rot, 1, 0, 0, RenderTile.XP, RenderTile.XM);
        calcShowing(showing, rot, 0, 1, 0, RenderTile.YP, RenderTile.YM);
        calcShowing(showing, rot, 0, 0, 1, RenderTile.ZP, RenderTile.ZM);
        calcShowing(showing, rot, 1, 1, 0, RenderTile.XPYP, RenderTile.XMYM);
        calcShowing(showing, rot, -1, 1, 0, RenderTile.XMYP, RenderTile.XPYM);
        calcShowing(showing, rot, 0, 1, 1, RenderTile.YPZP, RenderTile.YMZM);
        calcShowing(showing, rot, 0, -1, 1, RenderTile.YMZP, RenderTile.YPZM);
        calcShowing(showing, rot, 1, 0, 1, RenderTile.ZPXP, RenderTile.ZMXM);
        calcShowing(showing, rot, 1, 0, -1, RenderTile.ZMXP, RenderTile.ZPXM);
        log.log(Level.INFO, "Showing +x="+showing[0]+", -x="+showing[1]+", +y="+showing[2]+", -y="+showing[3]+", +z="+showing[4]+", -z="+showing[5]);
        for (RenderTile tile : tiles) {
            if (!showing[tile.getFacing()]) {
                tile.setVisual(null);
                continue;
            }
            Point3i center = tile.getCenter();
            Point3f visual = new Point3f(center.x, center.y, center.z);
            transform.transform(visual);
            tile.setVisual(visual);
        }
        Collections.sort(tiles, new Comparator<RenderTile>() {
            @Override
            public int compare(RenderTile tile1, RenderTile tile2) {
                if (tile1.getVisual() == null) {
                    if (tile2.getVisual() == null) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (tile2.getVisual() == null) {
                    return -1;
                } else {
                    return (int) Math.signum(tile2.getVisual().z - tile1.getVisual().z);
                }
            }
        });
    }

    private static void calcShowing(boolean[] showing, Matrix3f rot, int dx, int dy, int dz, int axis, int naxis) {
        Point3f xp = new Point3f(dx, dy, dz);
        rot.transform(xp);
        showing[axis] = xp.z < 0;
        showing[naxis] = !showing[axis];
    }

    public static void draw(Graphics2D g2, List<RenderTile> tiles, Point3f unitX, Point3f unitY, Point3f unitZ,
            boolean fancyGraphics) {
        float[][] corners = new float[4][2];
        for (RenderTile tile : tiles) {
            Point3f corner = tile.getVisual();
            if (corner == null) {
                break;
            }
            if (!getCorners(tile, corner, corners, unitX, unitY, unitZ)) {
                continue;
            }
            ImageIcon icon = null;
            if (fancyGraphics) {
                icon = BlockTypeColors.getBlockImage(tile.getBlock().getBlockID());
            }
            if (tile.getType() == RenderTile.SQUARE) {
                renderSquare(g2, corners, tile, icon);
            } else if ((tile.getType() >= RenderTile.TRI1) && (tile.getType() <= RenderTile.TRI4)) {
                renderTriangle(g2, corners, tile, icon);
            } else if ((tile.getType() == RenderTile.RECTANGLE)) {
                renderSquare(g2, corners, tile, icon);
            }
        }

    }

    private static void renderTriangle(Graphics2D g2, float[][] corners,
            RenderTile tile, ImageIcon icon) {
        log.log(Level.INFO, "Render triangle "+tile.getType());
        int pCenter = (tile.getType() - RenderTile.TRI1);
        int pLeft = (pCenter + 1) % 4;
        int pRight = (pCenter + 3) % 4;
        if (icon != null) {
            float m00 = (corners[pRight][0] - corners[pCenter][0]) / 64f;
            float m10 = (corners[pRight][1] - corners[pCenter][1]) / 64f;
            float m01 = (corners[pLeft][0] - corners[pCenter][0]) / 64f;
            float m11 = (corners[pLeft][1] - corners[pCenter][1]) / 64f;
            float m02 = corners[pCenter][0];
            float m12 = corners[pCenter][1];
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            g2.drawImage(icon.getImage(), t, null);
        } else {
            Path2D p = new Path2D.Float();
            p.moveTo(corners[pCenter][0], corners[pCenter][1]);
            p.lineTo(corners[pLeft][0], corners[pLeft][1]);
            p.lineTo(corners[pRight][0], corners[pRight][1]);
            p.lineTo(corners[pCenter][0], corners[pCenter][1]);
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

    private static void renderSquare(Graphics2D g2, float[][] corners,
            RenderTile tile, ImageIcon icon) {
        if (icon != null) {
            float m00 = (corners[1][0] - corners[0][0]) / 64f;
            float m10 = (corners[1][1] - corners[0][1]) / 64f;
            float m01 = (corners[3][0] - corners[0][0]) / 64f;
            float m11 = (corners[3][1] - corners[0][1]) / 64f;
            float m02 = corners[0][0];
            float m12 = corners[0][1];
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            g2.drawImage(icon.getImage(), t, null);
        } else {
            Path2D p = new Path2D.Float();
            p.moveTo(corners[0][0], corners[0][1]);
            p.lineTo(corners[1][0], corners[1][1]);
            p.lineTo(corners[2][0], corners[2][1]);
            p.lineTo(corners[3][0], corners[3][1]);
            p.lineTo(corners[0][0], corners[0][1]);
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

    public static boolean getCorners(RenderTile tile, Point3f corner, float[][] corners,
            Point3f unitX, Point3f unitY, Point3f unitZ) {
        switch (tile.getFacing()) {
            case RenderTile.XP:
            case RenderTile.XM:
                setCorner(corners, corner, 0, false, false, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 1, false, true, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 2, false, true, true, unitX, unitY, unitZ);
                setCorner(corners, corner, 3, false, false, true, unitX, unitY, unitZ);
                break;
            case RenderTile.YP:
            case RenderTile.YM:
                setCorner(corners, corner, 0, false, false, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 1, false, false, true, unitX, unitY, unitZ);
                setCorner(corners, corner, 2, true, false, true, unitX, unitY, unitZ);
                setCorner(corners, corner, 3, true, false, false, unitX, unitY, unitZ);
                break;
            case RenderTile.ZP:
            case RenderTile.ZM:
                setCorner(corners, corner, 0, false, false, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 1, true, false, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 2, true, true, false, unitX, unitY, unitZ);
                setCorner(corners, corner, 3, false, true, false, unitX, unitY, unitZ);
                break;
            case RenderTile.XPYP:
            case RenderTile.XMYM:
                setDiagonalCorners(corners, corner, false, true, false, false, true, true, unitX, unitY, unitZ);
                break;
            case RenderTile.XPYM:
            case RenderTile.XMYP:
                setDiagonalCorners(corners, corner, true, true, false, true, true, true, unitX, unitY, unitZ);
                break;
            case RenderTile.YPZM:
            case RenderTile.YMZP:
                setDiagonalCorners(corners, corner, false, false, false, true, false, false, unitX, unitY, unitZ);
                break;
            case RenderTile.YPZP:
            case RenderTile.YMZM:
                setDiagonalCorners(corners, corner, false, false, true, true, false, true, unitX, unitY, unitZ);
                break;
            case RenderTile.ZPXP:
            case RenderTile.ZMXM:
                setDiagonalCorners(corners, corner, false, false, true, true, false, false, unitX, unitY, unitZ);
                break;
            case RenderTile.ZPXM:
            case RenderTile.ZMXP:
                setDiagonalCorners(corners, corner, true, true, true, true, false, true, unitX, unitY, unitZ);
                break;
            default:
                return false;
        }
        return true;
    }

    private static void setDiagonalCorners(float[][] corners, Point3f corner,
            boolean x0, boolean y0, boolean z0,
            boolean x1, boolean y1, boolean z1,
            Point3f unitX, Point3f unitY, Point3f unitZ) {
        boolean x2 = (x0 == x1) ? !x1 : x1;
        boolean x3 = (x1 == x2) ? !x2 : x2;
        boolean y2 = (y0 == y1) ? !y1 : y1;
        boolean y3 = (y1 == y2) ? !y2 : y2;
        boolean z2 = (z0 == z1) ? !z1 : z1;
        boolean z3 = (z1 == z2) ? !z2 : z2;
        setCorner(corners, corner, 0, x0, y0, z0, unitX, unitY, unitZ);
        setCorner(corners, corner, 1, x1, y1, z1, unitX, unitY, unitZ);
        setCorner(corners, corner, 2, x2, y2, z2, unitX, unitY, unitZ);
        setCorner(corners, corner, 3, x3, y3, z3, unitX, unitY, unitZ);
    }

    private static void setCorner(float[][] corners, Point3f corner, int off, boolean x, boolean y, boolean z, Point3f unitX, Point3f unitY, Point3f unitZ) {
        corners[off][0] = corner.x;
        corners[off][1] = corner.y;
        if (x) {
            corners[off][0] += unitX.x;
            corners[off][1] += unitX.y;
        }
        if (y) {
            corners[off][0] += unitY.x;
            corners[off][1] += unitY.y;
        }
        if (z) {
            corners[off][0] += unitZ.x;
            corners[off][1] += unitZ.y;
        }
    }
}
