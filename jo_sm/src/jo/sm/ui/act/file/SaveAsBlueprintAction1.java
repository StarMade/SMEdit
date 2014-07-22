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
import java.io.File;

import javax.swing.JOptionPane;

import jo.sm.logic.BlueprintLogic;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class SaveAsBlueprintAction1 extends GenericAction {

    private final boolean mDef;
    private final RenderFrame mFrame;

    public SaveAsBlueprintAction1(RenderFrame frame, boolean def) {
        mFrame = frame;
        mDef = def;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        ShipSpec oriSpec = StarMadeLogic.getInstance().getCurrentModel();
        if (oriSpec == null) {
            return;
        }
        String name = JOptionPane.showInputDialog(mFrame, "What do you want to name it?", oriSpec.getName());
        if (StringUtils.isTrivial(name)) {
            return;
        }
        File prints;
        if (mDef) {
            prints = new File(StarMadeLogic.getInstance().getBaseDir(), "blueprints-default");
        } else {
            prints = new File(StarMadeLogic.getInstance().getBaseDir(), "blueprints");
        }
        File dir = new File(prints, name);
        final ShipSpec spec = new ShipSpec();
        spec.setType(mDef ? ShipSpec.DEFAULT_BLUEPRINT : ShipSpec.BLUEPRINT);
        spec.setClassification(IBlocksPlugin.TYPE_SHIP); // TODO: autodetect
        spec.setName(name);
        spec.setFile(dir);
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                BlueprintLogic.saveBlueprint(StarMadeLogic.getModel(), spec, mDef, cb);
                StarMadeLogic.getInstance().setCurrentModel(spec);
            }
        };
        RunnableLogic.run(mFrame, name, t);
    }
}
