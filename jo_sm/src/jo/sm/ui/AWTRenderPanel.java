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
package jo.sm.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jo.sm.data.BlockTypes;
import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.data.UndoBuffer;
import jo.sm.logic.RenderPolyLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.Vector3f;
import jo.vecmath.logic.Matrix4fLogic;
import jo.vecmath.logic.Point3iLogic;

@SuppressWarnings("serial")
public class AWTRenderPanel extends RenderPanel {

    private static final float PIXEL_TO_RADIANS = (1f / 3.14159f / 16f);
    private static final float ROLL_SCALE = 1.1f;

    private static final int MOUSE_MODE_NULL = 0;
    private static final int MOUSE_MODE_PIVOT = 1;
    private static final int MOUSE_MODE_SELECT = 2;

    private Point mMouseDownAt;
    private int mMouseMode;
    private boolean mPlainGraphics;
    private boolean mAxis;
    private boolean mDontDraw;
    private UndoBuffer mUndoer;

    private SparseMatrix<Block> mFilteredGrid;
    private final RenderSet mTiles;
    private final Matrix4f mTransform;
    private final Vector3f mPreTranslate;
    private float mScale;
    private float mRotX;
    private float mRotY;
    Vector3f mPOVTranslate;
    private final Vector3f mPostTranslate;

    public AWTRenderPanel() {
        mUndoer = new UndoBuffer();
        mTiles = new RenderSet();
        mTransform = new Matrix4f();
        mPreTranslate = new Vector3f();
        mPOVTranslate = new Vector3f();
        mScale = 1f;
        mRotX = (float) Math.PI;
        mRotY = 0;
        mPostTranslate = new Vector3f();
        mPlainGraphics = false;
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON1) {
                    doMouseDown(ev.getPoint(), ev.getModifiers());
                }
            }

            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON1) {
                    doMouseUp(ev.getPoint(), ev.getModifiers());
                }
            }

            @Override
            public void mouseDragged(MouseEvent ev) {
                if (mMouseDownAt != null) {
                    doMouseMove(ev.getPoint(), ev.getModifiers());
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                doMouseWheel(e.getWheelRotation());
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
                new RenderPanelKeyEventDispatcher(this));
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(ma);
        StarMadeLogic.getInstance().addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doNewGrid();
            }
        });
    }

    @Override
    public synchronized void updateTransform() {
        Dimension s = getSize();
        mPostTranslate.x = s.width / 2;
        mPostTranslate.y = s.height / 2;

        mTransform.setIdentity();
        Matrix4fLogic.translate(mTransform, mPreTranslate);
        Matrix4fLogic.rotX(mTransform, mRotX);
        Matrix4fLogic.rotY(mTransform, mRotY);
        Matrix4fLogic.translate(mTransform, mPOVTranslate);
        Matrix4fLogic.scale(mTransform, mScale);
        Matrix4fLogic.translate(mTransform, mPostTranslate);
        RenderPolyLogic.transformAndSort(mTiles, mTransform);
        repaint();
    }

    private void doMouseDown(Point p, int modifiers) {
        mMouseDownAt = p;
        //System.out.println("MouseMod="+Integer.toHexString(modifiers));
        if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
            RenderPoly tile = getTileAt(p.x, p.y);
            if (tile == null) {
                return;
            }
            mMouseMode = MOUSE_MODE_SELECT;
            StarMadeLogic.getInstance().setSelectedLower(null);
            StarMadeLogic.getInstance().setSelectedUpper(null);
            extendSelection(tile);
        } else {
            mMouseMode = MOUSE_MODE_PIVOT;
        }
    }

    private void doMouseMove(Point p, int modifiers) {
        if (mMouseMode == MOUSE_MODE_PIVOT) {
            int dx = p.x - mMouseDownAt.x;
            int dy = p.y - mMouseDownAt.y;
            mMouseDownAt = p;
            if (StarMadeLogic.isProperty(StarMadeLogic.INVERT_Y_AXIS)) {
                mRotX -= dy * PIXEL_TO_RADIANS;
            } else {
                mRotX += dy * PIXEL_TO_RADIANS;
            }
            if (StarMadeLogic.isProperty(StarMadeLogic.INVERT_X_AXIS)) {
                mRotY -= dx * PIXEL_TO_RADIANS;
            } else {
                mRotY += dx * PIXEL_TO_RADIANS;
            }
            updateTransform();
        } else if (mMouseMode == MOUSE_MODE_SELECT) {
            RenderPoly tile = getTileAt(p.x, p.y);
            if (tile != null) {
                extendSelection(tile);
            }
        }
    }

    private void doMouseUp(Point p, int modifiers) {
        if (mMouseMode == MOUSE_MODE_PIVOT) {
            doMouseMove(p, modifiers);
            mMouseDownAt = null;
        } else if (mMouseMode == MOUSE_MODE_PIVOT) {
            doMouseMove(p, modifiers);
        }
        mMouseMode = MOUSE_MODE_NULL;
    }

    private void doMouseWheel(int roll) {
        if (roll > 0) {
            while (roll-- > 0) {
                mScale /= ROLL_SCALE;
            }
        } else if (roll < 0) {
            while (roll++ < 0) {
                mScale *= ROLL_SCALE;
            }
        }
        updateTransform();
    }

    private void extendSelection(RenderPoly tile) {
        Point3i lowest = new Point3i();
        Point3i highest = new Point3i();
        RenderPolyLogic.getBounds(tile, lowest, highest);
        if (highest.x > lowest.x) {
            highest.x--;
        }
        if (highest.y > lowest.y) {
            highest.y--;
        }
        if (highest.z > lowest.z) {
            highest.z--;
        }
        Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
        lower = Point3iLogic.min(lower, lowest);
        StarMadeLogic.getInstance().setSelectedLower(lower);
        Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
        upper = Point3iLogic.max(upper, highest);
        StarMadeLogic.getInstance().setSelectedUpper(upper);
        updateTiles();
    }

    @Override
    public void paint(Graphics g) {
        if (mTiles == null) {
            return;
        }
        Dimension s = getSize();
        g.setColor(Color.black);
        g.fillRect(0, 0, s.width, s.height);
        Graphics2D g2 = (Graphics2D) g;
        RenderPolyLogic.draw(g2, mTiles, !mPlainGraphics);
    }

    private void doNewGrid() {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        StarMadeLogic.getModel().getBounds(lower, upper);
        mPreTranslate.x = -(lower.x + upper.x) / 2;
        mPreTranslate.y = -(lower.y + upper.y) / 2;
        mPreTranslate.z = -(lower.z + upper.z) / 2;
        float maxModel = Math.max(Math.max(upper.x - lower.x, upper.y - lower.y), upper.z - lower.z);
        Dimension s = getSize();
        float maxScreen = Math.max(s.width, s.height);
        mScale = maxScreen / maxModel / 2f;
        //System.out.println("Scale="+mScale+", preTrans="+mPreTranslate);
        //mTransform.setTranslation(new Vector3f(s.width/2f, s.height/2f, 0));
        updateTiles();
    }

    @Override
    public void updateTiles() {
        if (mDontDraw) {
            mFilteredGrid = new SparseMatrix<>();
        } else {
            if (StarMadeLogic.getInstance().getViewFilter() == null) {
                mFilteredGrid = StarMadeLogic.getModel();
            } else {
                mFilteredGrid = StarMadeLogic.getInstance().getViewFilter().modify(StarMadeLogic.getModel(), null, StarMadeLogic.getInstance(), null);
            }
        }
        RenderPolyLogic.fillPolys(mFilteredGrid, mTiles);
        Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
        Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
        if ((lower != null) && (upper != null)) {
            addBox(lower, upper, new short[]{BlockTypes.SPECIAL_SELECT_XP, BlockTypes.SPECIAL_SELECT_XM,
                BlockTypes.SPECIAL_SELECT_YP, BlockTypes.SPECIAL_SELECT_YM,
                BlockTypes.SPECIAL_SELECT_ZP, BlockTypes.SPECIAL_SELECT_ZM,});
        }
        if (mAxis) {
            lower = new Point3i();
            upper = new Point3i();
            mFilteredGrid.getBounds(lower, upper);
            int r = Math.max(256, Math.abs(lower.x));
            r = Math.max(r, Math.abs(lower.y));
            r = Math.max(r, Math.abs(lower.z));
            r = Math.max(r, Math.abs(upper.x));
            r = Math.max(r, Math.abs(upper.y));
            r = Math.max(r, Math.abs(upper.z));
            r += 16;
            addBox(new Point3i(9, 8, 8), new Point3i(r + 8, 8, 8), new short[]{BlockTypes.SPECIAL_SELECT_XP});
            addBox(new Point3i(8 - r, 8, 8), new Point3i(7, 8, 8), new short[]{BlockTypes.SPECIAL_SELECT_XM});
            addBox(new Point3i(8, 9, 8), new Point3i(8, r + 8, 8), new short[]{BlockTypes.SPECIAL_SELECT_YP});
            addBox(new Point3i(8, 8 - r, 8), new Point3i(8, 7, 8), new short[]{BlockTypes.SPECIAL_SELECT_YM});
            addBox(new Point3i(8, 8, 9), new Point3i(8, 8, r + 8), new short[]{BlockTypes.SPECIAL_SELECT_ZP});
            addBox(new Point3i(8, 8, 8 - r), new Point3i(8, 8, 7), new short[]{BlockTypes.SPECIAL_SELECT_ZM});
        }
        updateTransform();
    }

    private void addBox(Point3i lower, Point3i upper, short[] colors) {
        if ((lower == null) || (upper == null)) {
            return;
        }
        upper = new Point3i(upper.x + 1, upper.y + 1, upper.z + 1); // only place where bounds are at +1
        addSelectFace(upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.XP, colors[0 % colors.length]);
        addSelectFace(lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
                RenderPoly.XM, colors[1 % colors.length]);
        addSelectFace(lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
                RenderPoly.YP, colors[2 % colors.length]);
        addSelectFace(lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
                RenderPoly.YM, colors[3 % colors.length]);
        addSelectFace(lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
                RenderPoly.ZP, colors[4 % colors.length]);
        addSelectFace(lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
                RenderPoly.ZM, colors[5 % colors.length]);
    }

    private void addSelectFace(int x1, int y1, int z1, int x2, int y2, int z2,
            int face, short type) {
        if (x1 == x2) {
            for (int y = y1; y < y2; y++) {
                for (int z = z1; z < z2; z++) {
                    addSelectTile(x1, y, z, x2, y + 1, z + 1, face, type);
                }
            }
        } else if (y1 == y2) {
            for (int x = x1; x < x2; x++) {
                for (int z = z1; z < z2; z++) {
                    addSelectTile(x, y1, z, x + 1, y2, z + 1, face, type);
                }
            }
        } else if (z1 == z2) {
            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    addSelectTile(x, y, z1, x + 1, y + 1, z2, face, type);
                }
            }
        }
    }

    private void addSelectTile(int x1, int y1, int z1, int x2, int y2, int z2,
            int face, short type) {
        RenderPoly tile = new RenderPoly();
        if (x1 == x2) {
            tile.setModelPoints(new Point3i[]{
                new Point3i(x1, y1, z1),
                new Point3i(x1, y1, z2),
                new Point3i(x1, y2, z2),
                new Point3i(x1, y2, z1),});
        } else if (y1 == y2) {
            tile.setModelPoints(new Point3i[]{
                new Point3i(x1, y1, z1),
                new Point3i(x1, y1, z2),
                new Point3i(x2, y1, z2),
                new Point3i(x2, y1, z1),});
        } else if (z1 == z2) {
            tile.setModelPoints(new Point3i[]{
                new Point3i(x1, y1, z1),
                new Point3i(x1, y2, z1),
                new Point3i(x2, y2, z1),
                new Point3i(x2, y1, z1),});
        }
        tile.setNormal(face);
        tile.setType(RenderPoly.SQUARE);
        tile.setBlock(new Block(type));
        mTiles.getAllPolys().add(tile);
    }

    @Override
    public RenderPoly getTileAt(double x, double y) {
        for (int i = mTiles.getVisiblePolys().size() - 1; i >= 0; i--) {
            RenderPoly tile = mTiles.getVisiblePolys().get(i);
            Point3f[] corners = RenderPolyLogic.getCorners(tile, mTiles);
            Path2D p = new Path2D.Float();
            p.moveTo(corners[0].x, corners[0].y);
            p.lineTo(corners[1].x, corners[1].y);
            p.lineTo(corners[2].x, corners[2].y);
            p.lineTo(corners[3].x, corners[3].y);
            p.lineTo(corners[0].x, corners[0].y);
            if (p.contains(x, y)) {
                return tile;
            }
        }
        return null;
    }

    @Override
    public Block getBlockAt(double x, double y) {
        RenderPoly tile = getTileAt(x, y);
        if (tile != null) {
            return tile.getBlock();
        }
        return null;
    }

    @Override
    public boolean isPlainGraphics() {
        return mPlainGraphics;
    }

    @Override
    public void setPlainGraphics(boolean plainGraphics) {
        mPlainGraphics = plainGraphics;
        repaint();
    }

    @Override
    public boolean isAxis() {
        return mAxis;
    }

    @Override
    public void setAxis(boolean axis) {
        mAxis = axis;
        updateTiles();
    }

    @Override
    public UndoBuffer getUndoer() {
        return mUndoer;
    }

    @Override
    public void setUndoer(UndoBuffer undoer) {
        mUndoer = undoer;
    }

    @Override
    public void undo() {
        SparseMatrix<Block> grid = mUndoer.undo();
        if (grid != null) {
            StarMadeLogic.setModel(grid);
        }
    }

    @Override
    public void redo() {
        SparseMatrix<Block> grid = mUndoer.redo();
        if (grid != null) {
            StarMadeLogic.setModel(grid);
        }
    }

    @Override
    public void setCloseRequested(boolean pleaseClose) {
        // ignore
    }

    @Override
    public boolean isDontDraw() {
        return mDontDraw;
    }

    @Override
    public void setDontDraw(boolean dontDraw) {
        mDontDraw = dontDraw;
        updateTiles();
    }

}
