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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.macro.MacroLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroRunPlugin implements IBlocksPlugin {

    public static final String NAME = "Macro/Run...";
    public static final String DESC = "Run a macro from file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_ALL, SUBTYPE_EDIT, 90},};

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
        return new MacroRunParameters();
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
        MacroRunParameters params = (MacroRunParameters) p;
        return run(original, params, sm, cb);
    }

    @SuppressWarnings("unchecked")
    public static SparseMatrix<Block> run(SparseMatrix<Block> original,
            MacroRunParameters params, StarMade sm, IPluginCallback cb) {
        try {
            Map<String, Object> props = new HashMap<>();
            props.put("grid", original);
            props.put("sm", sm);
            props.put("cb", cb);
            Object ret = MacroLogic.eval(new File(params.getFile()), props);
            if (ret instanceof SparseMatrix) {
                return (SparseMatrix<Block>) ret;
            }
            return null;
        } catch (Exception e) {
            cb.setError(e);
            return null;
        }
    }
}
