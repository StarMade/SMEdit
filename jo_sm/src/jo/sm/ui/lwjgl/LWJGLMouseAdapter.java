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

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import jo.sm.data.RenderPoly;
import jo.sm.logic.RenderPolyLogic;
import jo.sm.logic.StarMadeLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;

public class LWJGLMouseAdapter extends MouseAdapter {

    public static final float PIXEL_TO_RADIANS = (1f / 3.14159f / 16f);

    private static final int MOUSE_MODE_NULL = 0;
    private static final int MOUSE_MODE_PIVOT = 1;
    private static final int MOUSE_MODE_SELECT = 2;

    private final LWJGLRenderPanel mPanel;

    private Point mMouseDownAt;
    private Point3f mMousePivotAround;
    private int mMouseMode;

    public LWJGLMouseAdapter(LWJGLRenderPanel panel) {
        mPanel = panel;
    }

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

    private void doMouseDown(Point p, int modifiers) {
        mMouseDownAt = p;
        //System.out.println("MouseMod="+Integer.toHexString(modifiers));
        if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
            RenderPoly tile = mPanel.getTileAt(p.x, p.y);
            if (tile == null) {
                return;
            }
            mMouseMode = MOUSE_MODE_SELECT;
            StarMadeLogic.getInstance().setSelectedLower(null);
            StarMadeLogic.getInstance().setSelectedUpper(null);
            extendSelection(tile);
        } else {
            mMouseMode = MOUSE_MODE_PIVOT;
//            Point3i pivot = getPointAt(p.x, p.y);
//            if (pivot != null)
//                mMousePivotAround = new Point3f(pivot.x, pivot.y, pivot.z);
//            else
            mMousePivotAround = null;
        }
    }

    private void doMouseMove(Point p, int modifiers) {
        if (mMouseMode == MOUSE_MODE_PIVOT) {
            int dx = p.x - mMouseDownAt.x;
            int dy = p.y - mMouseDownAt.y;
            mMouseDownAt = p;
            if ((dx != 0) || (dy != 0)) {
                if (mMousePivotAround == null) {
                    //System.out.println("Pivot around ourselves");
                    if (dx != 0) {
                        mPanel.mUniverse.getCamera().yaw(dx * PIXEL_TO_RADIANS);
                    }
                    if (dy != 0) {
                        mPanel.mUniverse.getCamera().pitch(dy * PIXEL_TO_RADIANS);
                    }
                } else {
                    Point3f rot = new Point3f(dx * PIXEL_TO_RADIANS, dy * PIXEL_TO_RADIANS, 0);
                    System.out.println("Pivot around " + mMousePivotAround + ", location=" + mPanel.mUniverse.getCamera().getLocation() + " by " + rot);
                    mPanel.mUniverse.getCamera().rotateAround(mMousePivotAround, rot);
                    System.out.println("After pivot=\n" + mPanel.mUniverse.getCamera());
                }
                mPanel.updateTransform();
            }
        } else if (mMouseMode == MOUSE_MODE_SELECT) {
            RenderPoly tile = mPanel.getTileAt(p.x, p.y);
            if (tile != null) {
                extendSelection(tile);
            }
        }
    }

    private void doMouseUp(Point p, int modifiers) {
        if (mMouseMode == MOUSE_MODE_PIVOT) {
            doMouseMove(p, modifiers);
            mMouseDownAt = null;
        } else if (mMouseMode == MOUSE_MODE_SELECT) {
            doMouseMove(p, modifiers);
        }
        mMouseMode = MOUSE_MODE_NULL;
    }

    private void doMouseWheel(int roll) {
        if (roll == 0) {
            return;
        }
        mPanel.mUniverse.getCamera().moveForward(-roll * 1.0f);
        mPanel.updateTransform();
    }

    private void extendSelection(RenderPoly tile) {
        Point3i lowest = new Point3i();
        Point3i highest = new Point3i();
        RenderPolyLogic.getBounds(tile, lowest, highest);
        Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
        if (lower == null) {
            lower = lowest;
            StarMadeLogic.getInstance().setSelectedLower(lower);
        } else {
            lower.x = Math.min(lower.x, lowest.x);
            lower.y = Math.min(lower.y, lowest.y);
            lower.z = Math.min(lower.z, lowest.z);
        }
        Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
        if (upper == null) {
            upper = highest;
            StarMadeLogic.getInstance().setSelectedUpper(upper);
        } else {
            upper.x = Math.min(upper.x, highest.x);
            upper.y = Math.min(upper.y, highest.y);
            upper.z = Math.min(upper.z, highest.z);
        }
        mPanel.updateSelectionBox();
    }
}
