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

import jo.vecmath.Vector3f;

/*
 * +        0         int                       unknown int
 +        4         int                       unknown int
 +        8         float[3]                 3d float vector (bounding box of ship)
 +        20        float[3]                 3d float fector (bounding box of ship)
 +        32        int                       number of block table entries (N)
 +        36        blockEntry[N]             block entry
 +        
 +        blockEntry is a 6 byte value
 +            0   short       blockID
 +            2   int         blockQuantity 
 */
public class Header {

    private int mUnknown1;
    private int mUnknown2;
    private Vector3f mUpperBound;
    private Vector3f mLowerBound;
    private BlockEntry[] mManifest;

    public int getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(int unknown1) {
        mUnknown1 = unknown1;
    }

    public int getUnknown2() {
        return mUnknown2;
    }

    public void setUnknown2(int unknown2) {
        mUnknown2 = unknown2;
    }

    public Vector3f getUpperBound() {
        return mUpperBound;
    }

    public void setUpperBound(Vector3f upperBound) {
        mUpperBound = upperBound;
    }

    public Vector3f getLowerBound() {
        return mLowerBound;
    }

    public void setLowerBound(Vector3f lowerBound) {
        mLowerBound = lowerBound;
    }

    public BlockEntry[] getManifest() {
        return mManifest;
    }

    public void setManifest(BlockEntry[] manifest) {
        mManifest = manifest;
    }
}
