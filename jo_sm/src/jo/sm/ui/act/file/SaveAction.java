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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.BlueprintLogic;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Data;
import jo.sm.ship.logic.DataLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class SaveAction extends GenericAction {

    private final RenderFrame mFrame;

    public SaveAction(RenderFrame frame) {
        mFrame = frame;
        setName("Save");
        setToolTipText("Save object back to source");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        if (spec == null) {
            return;
        }
        if (spec.getType() == ShipSpec.FILE) {
            doSaveFile();
        } else if (spec.getType() == ShipSpec.BLUEPRINT) {
            doSaveBlueprint(false);
        } else if (spec.getType() == ShipSpec.DEFAULT_BLUEPRINT) {
            doSaveBlueprint(true);
        } else if (spec.getType() == ShipSpec.ENTITY) {
            doSaveEntity();
        }
    }

    private void doSaveFile() {
        final ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        final File dataFile = spec.getFile();
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Map<Point3i, Data> data = ShipLogic.getData(grid);
        final Point3i p = new Point3i();
        final Data d = data.get(p);
        if (d == null) {
            throw new IllegalArgumentException("No core element to ship!");
        }
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                try {
                    DataLogic.writeFile(p, d, new FileOutputStream(dataFile), true, cb);
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot save to '" + spec.getFile() + "'", e);
                }
            }
        };
        RunnableLogic.run(mFrame, spec.getName(), t);
    }

    private void doSaveBlueprint(final boolean def) {
        final ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                BlueprintLogic.saveBlueprint(StarMadeLogic.getModel(), spec, def, cb);
            }
        ;
        };
        RunnableLogic.run(mFrame, spec.getName(), t);
    }

    private void doSaveEntity() {
        ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        final Map<Point3i, Data> data = ShipLogic.getData(grid);
        final File baseDir = new File(spec.getEntity().getFile().getParentFile(), "DATA");
        String fName = spec.getEntity().getFile().getName();
        final String baseName = fName.substring(0, fName.length() - 4); // remove .ent
        IRunnableWithProgress t = new IRunnableWithProgress() {
            @Override
            public void run(IPluginCallback cb) {
                try {
                    DataLogic.writeFiles(data, baseDir, baseName, cb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        RunnableLogic.run(mFrame, baseName, t);
    }

}
