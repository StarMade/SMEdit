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
package jo.sm.factories.planet.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MaterialPlugin implements IBlocksPlugin {

    private final MaterialDefinition mDef;
    private final Random mRND;
    private final Map<Short, MaterialEntry> mOldBlockMap;

    public MaterialPlugin(MaterialDefinition def) {
        mDef = def;
        mOldBlockMap = makeBlockMap(mDef.getOldBlocks());
        mRND = new Random();
    }

    private Map<Short, MaterialEntry> makeBlockMap(List<MaterialEntry> blocks) {
        Map<Short, MaterialEntry> blockMap = new HashMap<>();
        for (MaterialEntry entry : blocks) {
            blockMap.put(entry.getBlockID(), entry);
        }
        return blockMap;
    }

    @Override
    public String getName() {
        return mDef.getTitle();
    }

    @Override
    public String getDescription() {
        return mDef.getDescription();
    }

    @Override
    public String getAuthor() {
        return mDef.getAuthor();
    }

    @Override
    public Object newParameterBean() {
        return null;
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        int[][] classifications = new int[][]{
            {TYPE_FLOATINGROCK, SUBTYPE_GENERATE, mDef.getPriority()},
            {TYPE_PLANET, SUBTYPE_GENERATE, mDef.getPriority()},};
        return classifications;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object params, StarMade sm, IPluginCallback cb) {
        SparseMatrix<Block> modified = new SparseMatrix<>(original);
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        original.getBounds(lower, upper);
        cb.setStatus("Altering Composition");
        cb.startTask(upper.x - lower.x + 1);
        for (int x = lower.x; x <= upper.x; x++) {
            for (int z = lower.z; z <= upper.z; z++) {
                int lowY = getLowY(original, x, z, lower.y, upper.y);
                if (lowY < lower.y) {
                    continue;
                }
                int highY = getHighY(original, x, z, lower.y, upper.y);
                if (highY > upper.y) {
                    continue;
                }
                for (int y = lowY; y <= highY; y++) {
                    Block b = original.get(x, y, z);
                    if (b == null) {
                        continue;
                    }
                    if (doWeReplace(y, lowY, highY, b)) {
                        modified.set(x, y, z, replaceWith(y, lowY, highY, b));
                    }
                }
            }
            cb.workTask(1);
        }
        cb.endTask();
        return modified;
    }

    private int getLowY(SparseMatrix<Block> grid, int x, int z, int y1,
            int y2) {
        for (int y = y1; y <= y2; y++) {
            if (grid.contains(x, y, z)) {
                return y;
            }
        }
        return y1 - 1;
    }

    private int getHighY(SparseMatrix<Block> grid, int x, int z, int y1,
            int y2) {
        for (int y = y2; y >= y1; y--) {
            if (grid.contains(x, y, z)) {
                return y;
            }
        }
        return y2 + 1;
    }

    private boolean doWeReplace(int y, int lowY, int highY, Block b) {
        MaterialEntry oldEntry = mOldBlockMap.get(b.getBlockID());
        if (oldEntry == null) {
            oldEntry = mOldBlockMap.get(-1);
            if (oldEntry == null) {
                return false;
            }
        }
        if (!isWithinBounds(y, lowY, highY, oldEntry)) {
            return false;
        }
        return mRND.nextInt(100) <= oldEntry.getPercent();
    }

    private Block replaceWith(int y, int lowY, int highY, Block b) {
        List<MaterialEntry> probs = new ArrayList<>();
        int total = 0;
        for (MaterialEntry entry : mDef.getNewBlocks()) {
            if (isWithinBounds(y, lowY, highY, entry)) {
                probs.add(entry);
                total += entry.getPercent();
            }
        }
        if (total == 0) {
            return null;
        }
        int roll = mRND.nextInt(total);
        for (MaterialEntry entry : probs) {
            roll -= entry.getPercent();
            if (roll < 0) {
                return new Block(entry.getBlockID());
            }
        }
        throw new IllegalStateException("We fell off the bottom!");
    }

    private boolean isWithinBounds(int y, int lowY, int highY, MaterialEntry entry) {
        int lowLimit = getLimit(lowY, highY, entry.getLowStrategy(), entry.getLowValue());
        int highLimit = getLimit(lowY, highY, entry.getHighStrategy(), entry.getHighValue());
        return (y >= lowLimit) && (y <= highLimit);
    }

    private int getLimit(int lowY, int highY, int strategy, int value) {
        if (strategy == MaterialEntry.RELATIVE) {
            if (value >= 0) {
                return lowY + value;    // offset from top
            } else {
                return highY + value;   // offset from bottom
            }
        } else {
            int delta = highY - lowY;
            int abs = delta * value / 100;
            return lowY + abs;
        }
    }
}
