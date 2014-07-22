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

import jo.vecmath.Point3f;
import jo.vecmath.Point3i;

/*
 dockEntry is a variable length struct
 start   type
 0       int         length of the string giving attached ship's subfolder
 4       wchar[N]    ship subfolder string given in modified UTF-8 encoding
 vary    int[3]      q vector, the location of the dock block
 vary    float[3]    a vector, ???
 vary    short       block ID of the dock block
 */
public class DockEntry {

    private String mSubFolder;
    private Point3i mPosition;
    private Point3f mA;
    private short mBlockID;
    private byte mUnknown1;

    public short getBlockID() {
        return mBlockID;
    }

    public void setBlockID(short blockID) {
        mBlockID = blockID;
    }

    public String getSubFolder() {
        return mSubFolder;
    }

    public void setSubFolder(String subFolder) {
        mSubFolder = subFolder;
    }

    public Point3i getPosition() {
        return mPosition;
    }

    public void setPosition(Point3i position) {
        mPosition = position;
    }

    public Point3f getA() {
        return mA;
    }

    public void setA(Point3f a) {
        mA = a;
    }

    public byte getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(byte unknown1) {
        mUnknown1 = unknown1;
    }
}
