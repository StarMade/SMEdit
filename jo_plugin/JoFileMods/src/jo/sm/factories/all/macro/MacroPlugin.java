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
package jo.sm.factories.all.macro;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.plugins.all.macro.MacroRunParameters;
import jo.sm.plugins.all.macro.MacroRunPlugin;
import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroPlugin implements IBlocksPlugin {

    private final MacroDefinition mDef;

    public MacroPlugin(MacroDefinition def) {
        mDef = def;
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
        return mDef.getClassifications();
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        MacroRunParameters params = new MacroRunParameters();
        params.setFile(mDef.getScript().toString());
        return MacroRunPlugin.run(original, params, sm, cb);
    }

    public MacroDefinition getDef() {
        return mDef;
    }
}
