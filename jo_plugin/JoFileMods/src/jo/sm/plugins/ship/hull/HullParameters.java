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
package jo.sm.plugins.ship.hull;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Generate Hull", shortDescription = "Create an empty hull of a regular shape")
public class HullParameters {

    public static final int OPEN_FRAME = 0;
    public static final int NEEDLE = 1;
    public static final int CONE = 2;
    public static final int CYLINDER = 3;
    public static final int BOX = 4;
    public static final int SPHERE = 5;
    public static final int DISC = 6;
    public static final int IRREGULAR = 7;
    public static final int HEMISPHERE = 8;
    public static final int TORUS = 9;

    @Description(displayName = "Hull Type", shortDescription = "Overall shape of hull")
    private int mType;
    @Description(displayName = "Width", shortDescription = "Port/Starboard dimension")
    private int mSizeX;
    @Description(displayName = "Height", shortDescription = "Dorsal/Ventral dimension")
    private int mSizeY;
    @Description(displayName = "Length", shortDescription = "Fore/Aft dimension")
    private int mSizeZ;
    @Description(displayName = "Center X", shortDescription = "Core Position")
    private int mCenterX;
    @Description(displayName = "Center Y")
    private int mCenterY;
    @Description(displayName = "Center Z")
    private int mCenterZ;

    public HullParameters() {
        mType = SPHERE;
        mSizeX = 20;
        mSizeY = 10;
        mSizeZ = 40;
        mCenterX = 8;
        mCenterY = 8;
        mCenterZ = 8;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public void setCenterX(int centerX) {
        mCenterX = centerX;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public void setCenterY(int centerY) {
        mCenterY = centerY;
    }

    public int getCenterZ() {
        return mCenterZ;
    }

    public void setCenterZ(int centerZ) {
        mCenterZ = centerZ;
    }

    public int getSizeX() {
        return mSizeX;
    }

    public void setSizeX(int sizeX) {
        mSizeX = sizeX;
    }

    public int getSizeY() {
        return mSizeY;
    }

    public void setSizeY(int sizeY) {
        mSizeY = sizeY;
    }

    public int getSizeZ() {
        return mSizeZ;
    }

    public void setSizeZ(int sizeZ) {
        mSizeZ = sizeZ;
    }
}
