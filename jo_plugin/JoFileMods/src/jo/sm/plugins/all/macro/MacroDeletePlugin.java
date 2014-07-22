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
package jo.sm.plugins.all.macro;

import java.util.List;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.factories.all.macro.MacroPlugin;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.macro.MacroFunctionOpLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroDeletePlugin implements IBlocksPlugin {

    public static final String NAME = "Macro/Delete...";
    public static final String DESC = "Delete a recorded macro";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_ALL, SUBTYPE_EDIT, 94},};

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
        return new MacroDeleteParameters();
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
        try {
            String id = ((MacroDeleteParameters) p).getName();
            MacroPlugin plugin = findPlugin(id);
            if (plugin != null) {
                plugin.getDef().getScript().delete();
            }
            return null;
        } catch (Exception e) {
            cb.setError(e);
            return null;
        }
    }

    private MacroPlugin findPlugin(String id) {
        MacroPlugin p;
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_FILE));
        if (p != null) {
            return p;
        }
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_EDIT));
        if (p != null) {
            return p;
        }
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_VIEW));
        if (p != null) {
            return p;
        }
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_MODIFY));
        if (p != null) {
            return p;
        }
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_GENERATE));
        if (p != null) {
            return p;
        }
        p = findPlugins(id, StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_PAINT));
        return p;
    }

    private MacroPlugin findPlugins(String id, List<IBlocksPlugin> blocksPlugins) {
        for (IBlocksPlugin plugin : blocksPlugins) {
            if (plugin instanceof MacroPlugin) {
                if (id.equals(MacroFunctionOpLogic.getID(plugin))) {
                    return (MacroPlugin) plugin;
                }
            }
        }
        return null;
    }
}
