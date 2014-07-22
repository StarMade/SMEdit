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
package jo.sm.plugins.ship.fill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3i;


public class FillPlugin implements IBlocksPlugin {

    public static final String NAME = "Fill";
    public static final String DESC = "Autofill Ship Interior";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_MODIFY},};
    private static final Logger log = Logger.getLogger(FillPlugin.class.getName());

    public static void fill(SparseMatrix<Block> modified, List<Point3i> interior, int numBlocks, short controllerID, short blockID, FillStrategy fillStrategy) {
        fill(modified, interior, numBlocks, controllerID, blockID, fillStrategy, null);
    }

    public static void fill(SparseMatrix<Block> modified, List<Point3i> interior, int numBlocks, short controllerID, short blockID, FillStrategy fillStrategy, IPluginCallback cb) {
        if (numBlocks <= 0) {
            return;
        }
        Collections.sort(interior, fillStrategy);
        log.log(Level.WARNING, "Sorting for " + BlockTypes.BLOCK_NAMES.get(blockID) + ": " + interior.get(0) + ", " + interior.get(1) + ", " + interior.get(2) + ", ...");
        if ((controllerID > 0) && (interior.size() > 0)) {
            place(modified, interior, controllerID);
        }
        while ((numBlocks-- > 0) && (interior.size() > 0)) {
            place(modified, interior, blockID);
            if (cb != null) {
                cb.workTask(1);
            }
        }
    }

    private static void place(SparseMatrix<Block> modified, List<Point3i> interior, short blockID) {
        Block b = new Block();
        b.setBlockID(blockID);
        Point3i p = interior.get(0);
        interior.remove(0);
        modified.set(p, b);
    }

    public static void scopeInterior(SparseMatrix<Block> original, SparseMatrix<Block> modified, List<Point3i> interior, Set<Point3i> exterior, Point3i lower, Point3i upper, IPluginCallback cb) {
        cb.setStatus("Calculating interior");
        cb.startTask(original.size());
        for (Iterator<Point3i> i = original.iterator(); i.hasNext();) {
            Point3i p = i.next();
            if (exterior.contains(p)) {
                continue;
            }
            Block b = original.get(p);
            if (b != null) {
                modified.set(p, b);
                cb.workTask(1);
            } else {
                interior.add(p);
            }
        }
        cb.endTask();
        Point3i core = ShipLogic.findCore(modified);
        if (core == null) {
            core = new Point3i(8, 8, 8);
            modified.set(core, new Block(BlockTypes.CORE_ID));
        }
        int accessRadius = (int) (Math.pow(interior.size(), .333) / 200);
        for (Iterator<Point3i> i = interior.iterator(); i.hasNext();) {
            Point3i p = i.next();
            if ((Math.abs(p.x - core.x) <= accessRadius) || (Math.abs(p.y - core.y) <= accessRadius) || (Math.abs(p.z - core.z) <= accessRadius)) {
                i.remove();
            }
        }
    }

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
        return new FillParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object p, StarMade sm, IPluginCallback cb) {
        FillParameters params = (FillParameters) p;
        SparseMatrix<Block> modified = new SparseMatrix<>();
        List<Point3i> interior = new ArrayList<>();
        Set<Point3i> exterior = HullLogic.findExterior(original, cb);
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        original.getBounds(lower, upper);
        scopeInterior(original, modified, interior, exterior, lower, upper, cb);
        int interiorSize = interior.size();
        int oneHundredPercent = params.getEmpty() + params.getMissileDumb() + params.getMissileFafo()
                + params.getMissileHeat() + params.getPower() + params.getSalvage()
                + params.getShield() + params.getThrusters() + params.getWeapon();
        cb.setStatus("Filling");
        cb.startTask(oneHundredPercent);
        fill(modified, interior, params.getThrusters() * interiorSize / oneHundredPercent, (short) -1, BlockTypes.THRUSTER_ID,
                new FillStrategy(FillStrategy.MINUS, FillStrategy.Z, lower, upper));
        cb.workTask(params.getThrusters());
        fill(modified, interior, params.getMissileDumb() * interiorSize / oneHundredPercent, BlockTypes.MISSILE_DUMB_CONTROLLER_ID, BlockTypes.MISSILE_DUMB_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        cb.workTask(params.getMissileDumb());
        fill(modified, interior, params.getMissileHeat() * interiorSize / oneHundredPercent, BlockTypes.MISSILE_HEAT_CONTROLLER_ID, BlockTypes.MISSILE_HEAT_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        cb.workTask(params.getMissileHeat());
        fill(modified, interior, params.getMissileFafo() * interiorSize / oneHundredPercent, BlockTypes.MISSILE_FAFO_CONTROLLER_ID, BlockTypes.MISSILE_FAFO_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        cb.workTask(params.getMissileFafo());
        fill(modified, interior, params.getWeapon() * interiorSize / oneHundredPercent, BlockTypes.WEAPON_CONTROLLER_ID, BlockTypes.WEAPON_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.X, lower, upper));
        cb.workTask(params.getWeapon());
        fill(modified, interior, params.getSalvage() * interiorSize / oneHundredPercent, BlockTypes.SALVAGE_CONTROLLER_ID, BlockTypes.SALVAGE_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.Y, lower, upper));
        cb.workTask(params.getSalvage());
        fill(modified, interior, params.getPower() * interiorSize / oneHundredPercent, BlockTypes.POWER_COIL_ID, BlockTypes.POWER_ID,
                new FillStrategy(FillStrategy.CENTER, FillStrategy.X | FillStrategy.Y | FillStrategy.Z, lower, upper));
        cb.workTask(params.getPower());
        fill(modified, interior, params.getShield() * interiorSize / oneHundredPercent, (short) -1, BlockTypes.SHIELD_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.X | FillStrategy.Y | FillStrategy.Z, lower, upper));
        cb.workTask(params.getShield());
        cb.endTask();
        return modified;
    }
}
