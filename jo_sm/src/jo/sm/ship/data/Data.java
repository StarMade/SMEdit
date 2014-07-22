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

public class Data {

    private int mUnknown1;
    private byte[] mOffsetSizeTable;
    private byte[] mTimestampTable;
    private Chunk[] mChunks;

    public int getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(int unknown1) {
        mUnknown1 = unknown1;
    }

    public byte[] getOffsetSizeTable() {
        return mOffsetSizeTable;
    }

    public void setOffsetSizeTable(byte[] unknown2) {
        mOffsetSizeTable = unknown2;
    }

    public byte[] getTimestampTable() {
        return mTimestampTable;
    }

    public void setTimestampTable(byte[] unknown3) {
        mTimestampTable = unknown3;
    }

    public Chunk[] getChunks() {
        return mChunks;
    }

    public void setChunks(Chunk[] chunks) {
        mChunks = chunks;
    }
}
