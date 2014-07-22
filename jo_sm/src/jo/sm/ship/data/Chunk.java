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
package jo.sm.ship.data;

import jo.vecmath.Point3i;

public class Chunk {

    private long mTimestamp;
    private Point3i mPosition;
    private int mType;
    private Block[][][] mBlocks;

    //
    private int mTypeIndexStart; //0
    private int mTypeIndexEnd; //11 bits -> 2048 (0 incl)
    private int mHitpointsIndexStart; //11 - 8 bits -> 256
    private int mHitpointsIndexEnd; //19
    private int mActiveIndexStart; //19 - 1 bit -> 1
    private int mActiveIndexEnd; //20
    private int mOrientationStart; //20 - 4 bit -> 16
    private int mOrientationEnd; //24 

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public Point3i getPosition() {
        return mPosition;
    }

    public void setPosition(Point3i position) {
        mPosition = position;
    }

    public int getType() {
        return mType;
    }

    //
    public int getTypeIndexStart() {
        return mTypeIndexStart;
    }

    public int getTypeIndexEnd() {
        return mTypeIndexEnd;
    }

    //
    public void setType(int type) {
        mType = type;
    }

    //
    public void setTypeIndexStart(int typeIndexStart) {
        mTypeIndexStart = typeIndexStart;
    }

    public void setTypeIndexEnd(int typeIndexEnd) {
        mTypeIndexEnd = typeIndexEnd;
    }

    //
    public Block[][][] getBlocks() {
        return mBlocks;
    }

    public void setBlocks(Block[][][] blocks) {
        mBlocks = blocks;
    }

}
