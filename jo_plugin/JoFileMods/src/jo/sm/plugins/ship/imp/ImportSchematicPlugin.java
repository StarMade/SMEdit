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
package jo.sm.plugins.ship.imp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ResourceUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.plugins.ship.imp.nbt.FormatException;
import jo.sm.plugins.ship.imp.nbt.IO;
import jo.sm.plugins.ship.imp.nbt.Tag;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.BlockTypeColors;
import jo.util.Paths;
import jo.vecmath.Point3i;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class ImportSchematicPlugin implements IBlocksPlugin {

    public static final String NAME = "Import/Schematic";
    public static final String DESC = "Import Schematic file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_FILE, 25},
                {TYPE_STATION, SUBTYPE_FILE, 25},
                {TYPE_SHOP, SUBTYPE_FILE, 25},
                {TYPE_PLANET, SUBTYPE_FILE, 25},
                {TYPE_FLOATINGROCK, SUBTYPE_FILE, 25},};
    private static final Map<Integer, MCMap> BLOCK_MAP = new HashMap<>();
    private static long mLastRead = 0;
    private static final Logger log = Logger.getLogger(ImportSchematicPlugin.class.getName());

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getAuthor() {
        return AUTH;
    }

    @Override
    public Object newParameterBean() {
        return new ImportSchematicParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        readData(sm);
        ImportSchematicParameters params;
        params = (ImportSchematicParameters) p;
        try {
            Point3i center;
            center = new Point3i(8, 8, 8);
            if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
                center.set(sm.getSelectedLower());
                center.add(sm.getSelectedUpper());
                center.scale(1, 2);
            }
            SparseMatrix<Block> modified;
            modified = new SparseMatrix<>();
            readFile(params.getFile(), modified, center, cb);
            ShipLogic.ensureCore(modified);
            return modified;
        } catch (Exception e) {
            cb.setError(e);
            return null;
        }
    }

    private void readFile(String objFile, SparseMatrix<Block> grid, Point3i center, IPluginCallback cb) throws Exception {
        Tag.Compound schematic;
        try {
            try (DataInputStream rdr = new DataInputStream(new FileInputStream(new File(objFile)))) {
                schematic = IO.Read(rdr);
            }
        } catch (IOException | FormatException e) {
            try (DataInputStream rdr = new DataInputStream(new FileInputStream(new File(objFile)))) {
                schematic = IO.ReadUncompressed(rdr);
            }
        }
        Tag.Short width = (Tag.Short) schematic.Get("Width");
        Tag.Short height = (Tag.Short) schematic.Get("Height");
        Tag.Short length = (Tag.Short) schematic.Get("Length");
        int xSize;
        xSize = width.v;
        int ySize;
        ySize = height.v;
        int zSize;
        zSize = length.v;
        center.x -= xSize / 2;
        center.y -= ySize / 2;
        center.z -= zSize / 2;
        cb.setStatus("Importing...");
        cb.startTask(xSize * ySize * zSize);
        Tag.ByteArray blocks = (Tag.ByteArray) schematic.Get("Blocks");
        Tag.ByteArray data = (Tag.ByteArray) schematic.Get("Data");
        int idx;
        idx = 0;
        for (int y = 0; y < ySize; y++) {
            for (int z = 0; z < zSize; z++) {
                for (int x = 0; x < xSize; x++) {
                    cb.workTask(1);
                    int blockID;
                    blockID = blocks.v[idx];
                    int dataID;
                    dataID = data.v[idx];
                    idx++;
                    MCMap map = BLOCK_MAP.get((dataID << 8) | blockID);
                    if (map == null) {
                        map = BLOCK_MAP.get(blockID);
                    }
                    if (map == null) {
                        continue;
                    }
                    if (map.mSMBlock > 0) {
                        grid.set(center.x + x, center.y + y, center.z + z, new Block((short) map.mSMBlock));
                    }
                }
            }
        }
        cb.endTask();
    }

    private void readData(StarMade sm) {
        File plugins = new File(Paths.getPluginsDirectory());
        File minecraftTypes;
        minecraftTypes = new File(plugins, "schematic_map.xml");
        if (minecraftTypes.exists()) {
            if (minecraftTypes.lastModified() <= mLastRead) {
                return;
            }
            try {
                readDataFile(new FileInputStream(minecraftTypes));
                mLastRead = minecraftTypes.lastModified();
                return;
            } catch (FileNotFoundException e) {
                log.log(Level.WARNING, "read Data File failed!", e);
            }
        }
        if (BLOCK_MAP.isEmpty()) {
            readDataFile(ResourceUtils.loadSystemResourceStream("schematic_map.xml", ImportSchematicPlugin.class));
        }
    }

    private void readDataFile(InputStream is) {
        Document doc = XMLUtils.readStream(is);
        if (doc == null) {
            return;
        }
        BLOCK_MAP.clear();
        for (Node b : XMLUtils.findNodes(doc, "blockMap/block")) {
            MCMap map;
            map = new MCMap();
            String mcBlock = XMLUtils.getAttribute(b, "mcBlock");
            if (MinecraftTypes.NAME_TO_ID.containsKey(mcBlock)) {
                map.mMCBlock = MinecraftTypes.NAME_TO_ID.get(mcBlock);
            } else {
                int mcBlockNum;
                mcBlockNum = IntegerUtils.parseInt(mcBlock);
                if (mcBlockNum != 0) {
                    map.mMCBlock = IntegerUtils.parseInt(mcBlock);
                } else {
                    log.log(Level.WARNING, "Unknown MC Block type: " + mcBlock);
                    continue;
                }
            }
            map.mMCData = IntegerUtils.parseInt(XMLUtils.getAttribute(b, "mcData"));
            String smBlock;
            smBlock = XMLUtils.getAttribute(b, "smBlock");
            if (BlockTypeColors.mBlockTypes.containsKey(smBlock)) {
                map.mSMBlock = IntegerUtils.parseInt(BlockTypeColors.mBlockTypes.getProperty(smBlock));
            } else {
                map.mSMBlock = IntegerUtils.parseInt(smBlock);
            }
            int idx = map.mMCBlock;
            if (map.mMCData != 0) {
                idx |= map.mMCData << 8;
            }
            BLOCK_MAP.put(idx, map);
        }
    }

    class MCMap {

        public int mMCBlock;
        public int mMCData;
        public int mSMBlock;
    }
}
