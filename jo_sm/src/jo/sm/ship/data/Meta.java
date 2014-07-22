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

import java.util.ArrayList;
import java.util.List;

import jo.sm.ent.data.Tag;


/*
 start   type
 0       int             unknown int
 4       byte            unknown byte. Currently expecting a 0x03 here.
 5       int             number of dockEntry (docked ship/turrets)
 9       dockEntry[N]    data about each docked ship/turret
 vary    byte            unknown byte
 vary    short           specifies if GZIP compression is used on the tagStruct
 vary    tagStruct[]     additional metadata in a tag structure



 */
public class Meta {

    private int mUnknown1;
    private byte mUnknown2;
    private List<DockEntry> mDocks;
    private byte mUnknown3;
    private Tag mData;

    public Meta() {
        mDocks = new ArrayList<>();
    }

    public int getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(int unknown1) {
        mUnknown1 = unknown1;
    }

    public byte getUnknown2() {
        return mUnknown2;
    }

    public void setUnknown2(byte unknown2) {
        mUnknown2 = unknown2;
    }

    public List<DockEntry> getDocks() {
        return mDocks;
    }

    public void setDocks(List<DockEntry> docks) {
        mDocks = docks;
    }

    public byte getUnknown3() {
        return mUnknown3;
    }

    public void setUnknown3(byte unknown3) {
        mUnknown3 = unknown3;
    }

    public Tag getData() {
        return mData;
    }

    public void setData(Tag data) {
        mData = data;
    }
}
