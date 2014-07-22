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
package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class HardenAction extends GenericAction {

    private final RenderFrame mFrame;

    public HardenAction(RenderFrame frame) {
        mFrame = frame;
        setName("Harden");
        setToolTipText("Convert all unhardened hull blocks to hardened hull blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        if (grid == null) {
            return;
        }
        mFrame.getClient().getUndoer().checkpoint(grid);
        HullLogic.power(grid);
        StarMadeLogic.setModel(grid);
    }

}
