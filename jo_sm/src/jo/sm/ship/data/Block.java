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

import jo.sm.ui.BlockTypeColors;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public final class Block {

    private short mBlockID;
    private byte mOrientation;

    public Block() {
    }

    public Block(short id) {
        this();
        setBlockID(id);
    }

    public Block(Block b) {
        mBlockID = b.mBlockID;
        mOrientation = b.mOrientation;
    }

    public short getBlockID() {
        return mBlockID;
    }

    public void setBlockID(short blockID) {
        mBlockID = blockID;
    }

    public boolean isActive() {
        return false;
    }

    public void setActive(boolean active) {
    }

    public short getHitPoints() {
        if (BlockTypeColors.BLOCK_HITPOINTS.containsKey(mBlockID)) {
            return BlockTypeColors.BLOCK_HITPOINTS.get(mBlockID);
        } else {
            return 100;
        }
    }

    public void setHitPoints(short hitPoints) {
    }

    public short getOrientation() {
        return mOrientation;
    }

    public void setOrientation(short orientation) {
        mOrientation = (byte) orientation;
    }
}
