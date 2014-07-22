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

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import jo.sm.data.BlockTypes;
import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.data.UndoBuffer;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.sm.ui.RenderPanel;
import jo.util.jgl.obj.JGLCamera;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLScene;
import jo.util.jgl.obj.tri.JGLObj;
import jo.util.lwjgl.win.JGLCanvas;
import jo.vecmath.Color4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.Vector3f;

@SuppressWarnings("serial")
public class LWJGLRenderPanel extends RenderPanel {

    private final JGLCanvas mCanvas;
    private final JGLScene mScene;
    JGLCamera mUniverse;
    private final JGLGroup mBlocks;
    private final JGLGroup mSelection;
    private final JGLGroup mAxis;

    private SparseMatrix<Block> mFilteredGrid;
    private boolean mPlainGraphics;
    private boolean mDontDraw;
    private UndoBuffer mUndoer;

    Vector3f mPOVTranslate;

    public LWJGLRenderPanel() {
        mUndoer = new UndoBuffer();
        mPOVTranslate = new Vector3f();
        mScene = new JGLScene();
        mScene.setBackground(new Color4f());
        mUniverse = new JGLCamera();
        mScene.setNode(mUniverse);
        mBlocks = new JGLGroup();
        mUniverse.getChildren().add(mBlocks);
        mSelection = new JGLGroup();
        mUniverse.getChildren().add(mSelection);
        mAxis = new JGLGroup();
        mUniverse.getChildren().add(mAxis);
        mCanvas = new JGLCanvas();
        mCanvas.setScene(mScene);
        setLayout(new BorderLayout());
        add("Center", mCanvas);
        MouseAdapter ma = new LWJGLMouseAdapter(this);
        mCanvas.addMouseListener(ma);
        mCanvas.addMouseMotionListener(ma);
        mCanvas.addMouseWheelListener(ma);
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
//        		new LWJGLKeyEventDispatcher(this));
        mCanvas.addKeyListener(new LWJGLKeyEventDispatcher(this));
        StarMadeLogic.getInstance().addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent ev) {
                setLookAt(new Point3f(0, 0, -1));
            }
        });
    }

    @Override
    public void updateTransform() {
        // TODO Auto-generated method stub

    }

    public void setLookAt(Point3f axis) {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        StarMadeLogic.getModel().getBounds(lower, upper);
        Point3f lookAtThis = new Point3f((upper.x + lower.x) / 2, (upper.y + lower.y) / 2, (upper.z + lower.z) / 2);
        float maxModel = Math.max(Math.max(upper.x - lower.x, upper.y - lower.y), upper.z - lower.z) + 1;
        Point3f standHere = new Point3f(axis);
        standHere.scale(maxModel * 2);
        standHere.add(lookAtThis);
        //mUniverse.getCamera().setLocation(lookAtThis);
        mUniverse.getCamera().lookAt(standHere, lookAtThis);
        //mUniverse.getCamera().scale(mScale);
        System.out.println("Standing at " + standHere + ", looking at " + lookAtThis);
        //mUniverse.setTransformer(new SpinningTransformer(new Point3f(0, 20, 0)));
        updateTiles();
    }

    @Override
    public void updateTiles() {
        if (mDontDraw) {
            mFilteredGrid = new SparseMatrix<>();
        } else if (StarMadeLogic.getInstance().getViewFilter() == null) {
            mFilteredGrid = StarMadeLogic.getModel();
        } else {
            mFilteredGrid = StarMadeLogic.getInstance().getViewFilter().modify(StarMadeLogic.getModel(), null, StarMadeLogic.getInstance(), null);
        }
        updateAxis();
        mBlocks.getChildren().clear();
        LWJGLRenderLogic.addBlocks(mBlocks, mFilteredGrid, mPlainGraphics);
        System.out.println("Quads:" + mBlocks.getChildren().size());
        updateSelectionBox();
    }

    public void updateSelectionBox() {
        /*
         mSelection.getChildren().clear();
         Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
         Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
         if ((lower != null) && (upper != null))
         LWJGLRenderLogic.addBox(mSelection, new Point3f(lower), new Point3f(upper), new short[] { BlockTypes.SPECIAL_SELECT_XP, BlockTypes.SPECIAL_SELECT_XM,
         BlockTypes.SPECIAL_SELECT_YP, BlockTypes.SPECIAL_SELECT_YM,
         BlockTypes.SPECIAL_SELECT_ZP, BlockTypes.SPECIAL_SELECT_ZM,});
         */
    }

    public void updateAxis() {
        mAxis.getChildren().clear();
        MeshInfo info = new MeshInfo();
        info.verts = new ArrayList<>();
        info.indexes = new ArrayList<>();
        //info.colors = new ArrayList<Color3f>();
        info.uv = new ArrayList<>();
        System.out.println("Adding axis");
//        LWJGLRenderLogic.addBox(info, new Point3f(9,8,8), new Point3f(256+8,8,8), new short[] { BlockTypes.SPECIAL_SELECT_XP });
//        LWJGLRenderLogic.addBox(info, new Point3f(8-256,8,8), new Point3f(7,8,8), new short[] { BlockTypes.SPECIAL_SELECT_XM });
//        LWJGLRenderLogic.addBox(info, new Point3f(8,9,8), new Point3f(8,256+8,8), new short[] { BlockTypes.SPECIAL_SELECT_YP });
//        LWJGLRenderLogic.addBox(info, new Point3f(8,8-256,8), new Point3f(8,7,8), new short[] { BlockTypes.SPECIAL_SELECT_YM });
//        LWJGLRenderLogic.addBox(info, new Point3f(8,8,9), new Point3f(8,8,256+8), new short[] { BlockTypes.SPECIAL_SELECT_ZP });
//        LWJGLRenderLogic.addBox(info, new Point3f(8,8,8-256), new Point3f(8,8,7), new short[] { BlockTypes.SPECIAL_SELECT_ZM });
        LWJGLRenderLogic.addBox(info, new Point3f(9, 8, 8), new Point3f(256 + 8, 8, 8), new short[]{BlockTypes.LIGHT_RED});
        LWJGLRenderLogic.addBox(info, new Point3f(8 - 256, 8, 8), new Point3f(7, 8, 8), new short[]{BlockTypes.LIGHT_RED});
        LWJGLRenderLogic.addBox(info, new Point3f(8, 9, 8), new Point3f(8, 256 + 8, 8), new short[]{BlockTypes.LIGHT_GREEN});
        LWJGLRenderLogic.addBox(info, new Point3f(8, 8 - 256, 8), new Point3f(8, 7, 8), new short[]{BlockTypes.LIGHT_GREEN});
        LWJGLRenderLogic.addBox(info, new Point3f(8, 8, 9), new Point3f(8, 8, 256 + 8), new short[]{BlockTypes.LIGHT_BLUE});
        LWJGLRenderLogic.addBox(info, new Point3f(8, 8, 8 - 256), new Point3f(8, 8, 7), new short[]{BlockTypes.LIGHT_BLUE});
        JGLObj obj = LWJGLRenderLogic.infoToObj(info);
//        for (int i = 0; i < obj.getColorBuffer().limit(); i++)
//            System.out.print(" "+obj.getColorBuffer().get(i));
//        System.out.println();
        mAxis.add(obj);
    }

    @Override
    public RenderPoly getTileAt(double x, double y) {
        Point3i p = getPointAt(x, y);
        if (p == null) {
            return null;
        }
        Block b = StarMadeLogic.getModel().get(p);
        if (b == null) {
            return null;
        }
        RenderPoly tile = new RenderPoly();
        tile.setBlock(b);
        tile.setPosition(p);
        return tile;
    }

    @Override
    public Block getBlockAt(double x, double y) {
        Point3i p = getPointAt(x, y);
        if (p == null) {
            return null;
        }
        return StarMadeLogic.getModel().get(p);
    }

    public Point3i getPointAt(double x, double y) {
        Point3f pointMap = new Point3f((float) x, (float) y, 0);
        System.out.print(pointMap + " ->");
        mBlocks.setData("pointMap", pointMap);
        Point3f pointMapped = null;
        for (;;) {
            pointMapped = (Point3f) mBlocks.getData("pointMapped");
            if (pointMapped != null) {
                break;
            }
            try {
                Thread.sleep(1000 / 24);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println(" " + pointMapped);
        return null;
        /*
         Matrix3f rot = new Matrix3f();
         mUniverse.getCamera().get(rot);
         Point3f trans = new Point3f(mUniverse.getCamera().getLocation());
         rot.invert();
         System.out.println("Trans: "+trans+"\nRot^-1:\n"+rot);
         Point3f eye = mCanvas.getEyeRay();
         System.out.print(eye);
         Point3f model = new Point3f(eye);
         model.x += trans.x; model.y -= trans.y; model.z -= trans.z;
         System.out.print(" -> "+model);
         rot.transform(model);
         Point3i p = new Point3i(model);
         System.out.println(" -> "+model+" -> "+p);
         if (!StarMadeLogic.getModel().contains(p))
         return null;
         return p;
         */
    }

    @Override
    public boolean isPlainGraphics() {
        return mPlainGraphics;
    }

    @Override
    public void setPlainGraphics(boolean plainGraphics) {
        mPlainGraphics = plainGraphics;
    }

    @Override
    public boolean isAxis() {
        return !mAxis.isCull();
    }

    @Override
    public void setAxis(boolean axis) {
        mAxis.setCull(!axis);
        updateAxis();
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
        mCanvas.setCloseRequested(pleaseClose);
    }

    public void moveCamera(Point3i delta) {
        mUniverse.getCamera().moveRight(delta.x);
        mUniverse.getCamera().moveUp(delta.y);
        mUniverse.getCamera().moveForward(delta.z);
        //System.out.println("After moveCamera=\n"+mUniverse.getCamera());
    }

    public void rotateCamera(Point3i delta) {
        //System.out.println("Before rotate:\n"+mUniverse.getCamera());
        mUniverse.getCamera().yaw(delta.x * LWJGLMouseAdapter.PIXEL_TO_RADIANS);
        mUniverse.getCamera().pitch(delta.y * LWJGLMouseAdapter.PIXEL_TO_RADIANS);
        mUniverse.getCamera().roll(delta.z * LWJGLMouseAdapter.PIXEL_TO_RADIANS);
        //System.out.println("After rotate "+delta+":\n"+mUniverse.getCamera());
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

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        mCanvas.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        mCanvas.removeMouseListener(l);
    }
}
