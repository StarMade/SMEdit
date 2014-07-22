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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.DebugLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Data;
import jo.sm.ship.data.Header;
import jo.sm.ship.data.Logic;
import jo.sm.ship.data.Meta;
import jo.sm.ship.logic.DataLogic;
import jo.sm.ship.logic.HeaderLogic;
import jo.sm.ship.logic.LogicLogic;
import jo.sm.ship.logic.MetaLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class OpenFileAction1 extends GenericAction {

    private final RenderFrame mFrame;

    public OpenFileAction1(RenderFrame frame) {
        mFrame = frame;

    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        File dir;
        if (StarMadeLogic.getProps().contains("open.file.dir")) {
            dir = new File(StarMadeLogic.getProps().getProperty("open.file.dir"));
        } else {
            dir = StarMadeLogic.getInstance().getBaseDir();
        }
        JFileChooser chooser = new JFileChooser(StarMadeLogic.getInstance().getBaseDir());
        chooser.setSelectedFile(dir);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
                "Starmade Ship File", "smd2");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter(
                "Starmade Exported Ship File", "sment");
        chooser.addChoosableFileFilter(filter1);
        chooser.addChoosableFileFilter(filter2);
        int returnVal = chooser.showOpenDialog(mFrame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File smb2 = chooser.getSelectedFile();
        StarMadeLogic.getProps().setProperty("open.file.dir", smb2.getParent());
        StarMadeLogic.saveProps();
        String name = smb2.getName();
        try {
            Header header = null;
            Meta meta = null;
            Logic logic = null;
            Map<Point3i, Data> data = new HashMap<>();
            InputStream is = new FileInputStream(smb2);;
            if (smb2.getName().endsWith(".smd2")) {
                Point3i p = new Point3i();
                Data datum = DataLogic.readFile(is, true);
                data.put(p, datum);
                name = name.substring(0, name.length() - 5);
            } else if (smb2.getName().endsWith(".sment")) {
                name = null;
                ZipInputStream zis = new ZipInputStream(new FileInputStream(smb2));
                for (;;) {
                    ZipEntry entry = zis.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    String ename = entry.getName();
                    if (name == null) {
                        int o = ename.indexOf('/');
                        if (o > 0) {
                            name = ename.substring(0, o);
                        }
                    }
                    if (name != null) {
                        if (ename.startsWith(name + "/DATA/") && ename.endsWith(".smd2")) {
                            String[] parts = entry.getName().split("\\.");
                            Point3i position = new Point3i(Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3]));
                            Data datum = DataLogic.readFile(zis, false);
                            data.put(position, datum);
                        } else if (ename.equals(name + "/header.smbph")) {
                            header = HeaderLogic.readFile(zis, false);
                        } else if (ename.equals(name + "/logic.smbpl")) {
                            logic = LogicLogic.readFile(zis, false);
                        } else if (ename.equals(name + "/meta.smbpm")) {
                            meta = MetaLogic.readFile(zis, false);
                        }
                    }
                }
                zis.close();
            } else {
                is.close();
                throw new IllegalArgumentException("Unsupported file type '" + smb2 + "'");
            }
            SparseMatrix<Block> grid = ShipLogic.getBlocks(data);
            ShipSpec spec = new ShipSpec();
            spec.setName(name);
            spec.setType(ShipSpec.FILE);
            spec.setClassification(IBlocksPlugin.TYPE_SHIP); // TODO: autodetect
            spec.setFile(smb2);
            StarMadeLogic.getInstance().setCurrentModel(spec);
            StarMadeLogic.setModel(grid);
            mFrame.getClient().getUndoer().clear();
            if (DebugLogic.DEBUG) {
                if (header != null) {
                    HeaderLogic.dump(header);
                }
                if (meta != null) {
                    MetaLogic.dump(meta);
                }
                if (logic != null) {
                    LogicLogic.dump(logic, grid);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
