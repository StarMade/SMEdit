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
package jo.sm.data;

import jo.sm.ship.data.Block;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class RenderTile {

    public static final int XP = 0;
    public static final int XM = 1;
    public static final int YP = 2;
    public static final int YM = 3;
    public static final int ZP = 4;
    public static final int ZM = 5;
    public static final int XPYP = 6;
    public static final int XPYM = 7;
    public static final int XMYP = 8;
    public static final int XMYM = 9;
    public static final int YPZP = 10;
    public static final int YPZM = 11;
    public static final int YMZP = 12;
    public static final int YMZM = 13;
    public static final int ZPXP = 14;
    public static final int ZPXM = 15;
    public static final int ZMXP = 16;
    public static final int ZMXM = 17;

    public static final int SQUARE = 0;
    public static final int TRI1 = 1;
    public static final int TRI2 = 2;
    public static final int TRI3 = 3;
    public static final int TRI4 = 4;
    public static final int RECTANGLE = 5;

    private int mFacing;
    private int mType;
    private Block mBlock;
    private Point3i mCenter;
    private Point3f mVisual;

    public int getFacing() {
        return mFacing;
    }

    public void setFacing(int facing) {
        mFacing = facing;
    }

    public Point3i getCenter() {
        return mCenter;
    }

    public void setCenter(Point3i center) {
        mCenter = center;
    }

    public Point3f getVisual() {
        return mVisual;
    }

    public void setVisual(Point3f visual) {
        mVisual = visual;
    }

    public Block getBlock() {
        return mBlock;
    }

    public void setBlock(Block block) {
        mBlock = block;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
