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
package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ship.data.Block;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.ShipChooser;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;
import jo.sm.ui.logic.ShipTreeLogic;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class OpenExistingAction extends GenericAction {

    private final RenderFrame mFrame;

    public OpenExistingAction(RenderFrame frame) {
        mFrame = frame;
        setName("Open...");
        setToolTipText("Open an existing data object");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        ShipChooser chooser = new ShipChooser(mFrame);
        chooser.setVisible(true);
        final ShipSpec spec = chooser.getSelected();
        if (spec == null) {
            return;
        }
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                SparseMatrix<Block> grid = ShipTreeLogic.loadShip(spec, cb);
                if (grid != null) {
                    StarMadeLogic.getInstance().setCurrentModel(spec);
                    StarMadeLogic.setModel(grid);
                    mFrame.getClient().getUndoer().clear();
                }
            }
        };
        RunnableLogic.run(mFrame, "Open " + spec.getName(), t);
    }

}
