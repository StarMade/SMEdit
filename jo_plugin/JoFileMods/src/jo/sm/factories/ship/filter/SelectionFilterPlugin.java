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
package jo.sm.factories.ship.filter;

import java.util.Iterator;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class SelectionFilterPlugin implements IBlocksPlugin {

    public static final String NAME = "Selection Only";
    public static final String DESC = "View only selection";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_VIEW, 15},
                {TYPE_STATION, SUBTYPE_VIEW, 15},
                {TYPE_SHOP, SUBTYPE_VIEW, 15},
                {TYPE_FLOATINGROCK, SUBTYPE_VIEW, 15},
                {TYPE_PLANET, SUBTYPE_VIEW, 15},};

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
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
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
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object params, StarMade sm, IPluginCallback cb) {
        SparseMatrix<Block> modified;
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            modified = new SparseMatrix<>();
            for (Iterator<Point3i> i = PluginUtils.getEffectiveSelectionIterator(sm, original); i.hasNext();) {
                Point3i p = i.next();
                Block b = original.get(p);
                if (b != null) {
                    modified.set(p, b);
                }
            }
        } else {
            modified = new SparseMatrix<>(original);
        }
        return modified;
    }

}
