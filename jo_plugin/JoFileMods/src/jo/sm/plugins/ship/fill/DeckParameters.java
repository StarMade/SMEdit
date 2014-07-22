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
@Description(displayName = "Place Decks", shortDescription = "Fills an interior with decks.")
public class DeckParameters {

    @Description(displayName = "Thickness", shortDescription = "Thinkness of decking")
    private int mThickness;
    @Description(displayName = "Gap", shortDescription = "Gap between decks")
    private int mSpace;
    @Description(displayName = "Material", shortDescription = "Material to use for decks")
    private short mMaterial;
    @Description(displayName = "Stair width", shortDescription = "How wide to make the stairs")
    private int mStairwellWidth;
    @Description(displayName = "Width Gap", shortDescription = "Gap between stairs port/starboard")
    private int mStairwellXGap;
    @Description(displayName = "Length Gap", shortDescription = "Gap between stairs fore/aft")
    private int mStairwellZGap;

    public DeckParameters() {
        mThickness = 1;
        mSpace = 3;
        mMaterial = BlockTypes.HULL_COLOR_GREY_ID;
        mStairwellWidth = 3;
        mStairwellXGap = 12;
        mStairwellZGap = 27;
    }

    public int getThickness() {
        return mThickness;
    }

    public void setThickness(int thickness) {
        mThickness = thickness;
    }

    public int getSpace() {
        return mSpace;
    }

    public void setSpace(int space) {
        mSpace = space;
    }

    public short getMaterial() {
        return mMaterial;
    }

    public void setMaterial(short material) {
        mMaterial = material;
    }

    public int getStairwellWidth() {
        return mStairwellWidth;
    }

    public void setStairwellWidth(int stairwellWidth) {
        mStairwellWidth = stairwellWidth;
    }

    public int getStairwellXGap() {
        return mStairwellXGap;
    }

    public void setStairwellXGap(int stairwellXGap) {
        mStairwellXGap = stairwellXGap;
    }

    public int getStairwellZGap() {
        return mStairwellZGap;
    }

    public void setStairwellZGap(int stairwellZGap) {
        mStairwellZGap = stairwellZGap;
    }
}
