
package jo.sm.mods;
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
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public interface IBlocksPlugin extends IStarMadePlugin {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SHIP = 1;
    public static final int TYPE_STATION = 2;
    public static final int TYPE_SHOP = 3;
    public static final int TYPE_PLANET = 4;
    public static final int TYPE_FLOATINGROCK = 5;

    public static final int SUBTYPE_PAINT = 1;
    public static final int SUBTYPE_MODIFY = 2;
    public static final int SUBTYPE_GENERATE = 3;
    public static final int SUBTYPE_EDIT = 4;
    public static final int SUBTYPE_FILE = 5;
    public static final int SUBTYPE_VIEW = 6;

    public void initParameterBean(SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb);

    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb);
}
