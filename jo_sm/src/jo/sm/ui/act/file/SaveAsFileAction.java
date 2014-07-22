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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showConfirmDialog;
import javax.swing.filechooser.FileNameExtensionFilter;

import jo.sm.data.SparseMatrix;
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
public class SaveAsFileAction extends GenericAction {

    private final RenderFrame mFrame;

    public SaveAsFileAction(RenderFrame frame) {
        mFrame = frame;
        setName("File...");
        setToolTipText("Save as smb2 file");
    }
    
    
    
    
    
    

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (StarMadeLogic.getInstance().getCurrentModel() == null) {
            return;
        }
        File dir;
        if (StarMadeLogic.getProps().contains("open.file.dir")) {
            dir = new File(StarMadeLogic.getProps().getProperty("open.file.dir"));
        } else {
            dir = StarMadeLogic.getInstance().getBaseDir();
        }
        JFileChooser chooser = new JFileChooser(StarMadeLogic.getInstance().getBaseDir());
        chooser.setSelectedFile(dir);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
                "Ship File", "smd2");
        //FileNameExtensionFilter filter2 = new FileNameExtensionFilter(
        //        "Starmade Exported Ship File", "sment");
        chooser.addChoosableFileFilter(filter1);
        //chooser.addChoosableFileFilter(filter2);
        int returnVal = chooser.showSaveDialog(mFrame);
        File selFile = chooser.getSelectedFile();
        if (returnVal != JFileChooser.APPROVE_OPTION) {
        } else 
            if (returnVal == JFileChooser.APPROVE_OPTION && selFile!=null) {
                if (!selFile.getName().endsWith("smd2")) selFile = new File(selFile.getPath()+".smd2");
                if (selFile.exists()) {
                    int n;
                    n = showConfirmDialog(mFrame,
                            "File " + selFile.getPath() + " already exists. Overwrite?",
                            "Question",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (n==1) {actionPerformed(ev); return;}
                }
            final File smb2 = chooser.getSelectedFile();
        StarMadeLogic.getProps().setProperty("open.file.dir", smb2.getParent());
        StarMadeLogic.saveProps();
        String name = smb2.getName();
        final ShipSpec spec = new ShipSpec();
        spec.setType(ShipSpec.FILE);
        spec.setName(name);
        spec.setFile(smb2);
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
                    DataLogic.writeFile(p, d, new FileOutputStream(smb2), true, cb);
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot save to '" + spec.getFile() + "'", e);
                }
                StarMadeLogic.getInstance().setCurrentModel(spec);
            }
        };
        RunnableLogic.run(mFrame, name, t);
            
        }
        
        
    }
}
