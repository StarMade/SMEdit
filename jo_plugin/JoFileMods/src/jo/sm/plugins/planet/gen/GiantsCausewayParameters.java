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
package jo.sm.plugins.planet.gen;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Giant's Causeway", shortDescription = "Create a hexagonal slab like surface.")
public class GiantsCausewayParameters {

    @Description(displayName = "", shortDescription = "Planetary radius")
    private int mPlanetRadius;
    @Description(displayName = "", shortDescription = "Tallest point above baseline (-ve for below)")
    private int mPlanetHeight;
    @Description(displayName = "Hex", shortDescription = "Radius of hex slab")
    private int mHexRadius;
    @Description(displayName = "", shortDescription = "Block type to fill with")
    private short mFillWith;

    public GiantsCausewayParameters() {
        mPlanetRadius = 250;
        mPlanetHeight = 32;
        mHexRadius = 5;
        mFillWith = BlockTypes.TERRAIN_ROCK_ID;
    }

    public int getPlanetRadius() {
        return mPlanetRadius;
    }

    public void setPlanetRadius(int planetRadius) {
        mPlanetRadius = planetRadius;
    }

    public int getPlanetHeight() {
        return mPlanetHeight;
    }

    public void setPlanetHeight(int planetHeight) {
        mPlanetHeight = planetHeight;
    }

    public int getHexRadius() {
        return mHexRadius;
    }

    public void setHexRadius(int hexRadius) {
        mHexRadius = hexRadius;
    }

    public short getFillWith() {
        return mFillWith;
    }

    public void setFillWith(short fillWith) {
        mFillWith = fillWith;
    }
}
