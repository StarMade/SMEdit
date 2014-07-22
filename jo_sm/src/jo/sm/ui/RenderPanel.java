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

import javax.swing.JPanel;

import jo.sm.data.RenderPoly;
import jo.sm.data.UndoBuffer;
import jo.sm.ship.data.Block;

@SuppressWarnings("serial")
public abstract class RenderPanel extends JPanel {

    public abstract void updateTransform();

    public abstract void updateTiles();

    public abstract RenderPoly getTileAt(double x, double y);

    public abstract Block getBlockAt(double x, double y);

    public abstract boolean isPlainGraphics();

    public abstract void setPlainGraphics(boolean plainGraphics);

    public abstract boolean isAxis();

    public abstract void setAxis(boolean axis);

    public abstract boolean isDontDraw();

    public abstract void setDontDraw(boolean dontDraw);

    public abstract void setCloseRequested(boolean pleaseClose);

    public abstract UndoBuffer getUndoer();

    public abstract void setUndoer(UndoBuffer undoer);

    public abstract void undo();

    public abstract void redo();
}
