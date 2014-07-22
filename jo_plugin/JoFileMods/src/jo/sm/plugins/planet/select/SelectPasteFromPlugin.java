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
package jo.sm.plugins.planet.select;

import java.io.IOException;
import java.util.Map;

import jo.sm.data.ShapeLibraryEntry;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.GridLogic;
import jo.sm.logic.ShapeLibraryLogic;
import jo.sm.logic.utils.FileUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class SelectPasteFromPlugin implements IBlocksPlugin {

    public static final String NAME = "Paste from library";
    public static final String DESC = "Insert selection from shape library";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = {
        {TYPE_SHIP, SUBTYPE_EDIT, 14},
        {TYPE_STATION, SUBTYPE_EDIT, 14},
        {TYPE_SHOP, SUBTYPE_EDIT, 14},
        {TYPE_FLOATINGROCK, SUBTYPE_EDIT, 14},
        {TYPE_PLANET, SUBTYPE_EDIT, 14},};

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
        return new SelectPasteFromParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
        Map<String, Object> shapeMap = ShapeLibraryLogic.getEntryMap();
        ((SelectPasteFromParameters) params).setShape((Integer) shapeMap.values().iterator().next());
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object p,
            StarMade sm, IPluginCallback cb) {
        SelectPasteFromParameters params = (SelectPasteFromParameters) p;
        Point3i lower = sm.getSelectedLower();
        Point3i upper = sm.getSelectedUpper();
        if ((lower != null) && (upper != null)) {
            ShapeLibraryEntry entry = ShapeLibraryLogic.getEntry(params.getShape());
            if (entry != null) {
                try {
                    String xml = FileUtils.readFileAsString(entry.getShape().toString());
                    SparseMatrix<Block> insertion = GridLogic.fromString(xml);
                    GridLogic.insert(original, insertion, lower);
                    return original;
                } catch (IOException ex) {
                    cb.setError(ex);
                }
            }
        }
        return null;
    }
}
