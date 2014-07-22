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

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Fill Blocks", shortDescription = "Fill selected region/interior of the ship with a specific block.")
public class FillBlockParameters {

    @Description(displayName = "Block", shortDescription = "Type of block to fill the region with")
    private short mBlockID;
    @Description(displayName = "Quantity", shortDescription = "How many blocks to fill (0 for all)")
    private int mTotal;
    @Description(displayName = "Strategy", shortDescription = "Approach to deciding which area to fill first")
    private int mStrategy;
    @Description(displayName = "Axis", shortDescription = "Which access to apply the strategy along")
    private int mAxis;

    public FillBlockParameters() {
        mBlockID = BlockTypes.THRUSTER_ID;
        mTotal = 200;
        mStrategy = FillStrategy.CENTER;
        mAxis = FillStrategy.Z;
    }

    public short getBlockID() {
        return mBlockID;
    }

    public void setBlockID(short blockID) {
        mBlockID = blockID;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public int getStrategy() {
        return mStrategy;
    }

    public void setStrategy(int strategy) {
        mStrategy = strategy;
    }

    public int getAxis() {
        return mAxis;
    }

    public void setAxis(int axis) {
        mAxis = axis;
    }
}
