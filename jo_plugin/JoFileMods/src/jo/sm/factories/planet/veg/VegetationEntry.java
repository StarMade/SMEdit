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
package jo.sm.factories.planet.veg;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class VegetationEntry {

    public static final int ABSOLUTE = 0;
    public static final int RELATIVE = 1;

    private short mBlockID;
    private int mPercent;
    private int mLowValue;
    private int mLowStrategy;
    private int mHighValue;
    private int mHighStrategy;

    public VegetationEntry() {
        mPercent = 1;
        mLowStrategy = ABSOLUTE;
        mLowValue = 0;
        mHighStrategy = ABSOLUTE;
        mHighValue = 100;
    }

    public short getBlockID() {
        return mBlockID;
    }

    public void setBlockID(short blockID) {
        mBlockID = blockID;
    }

    public int getPercent() {
        return mPercent;
    }

    public void setPercent(int percent) {
        mPercent = percent;
    }

    public int getLowValue() {
        return mLowValue;
    }

    public void setLowValue(int lowValue) {
        mLowValue = lowValue;
    }

    public int getLowStrategy() {
        return mLowStrategy;
    }

    public void setLowStrategy(int lowStrategy) {
        mLowStrategy = lowStrategy;
    }

    public int getHighValue() {
        return mHighValue;
    }

    public void setHighValue(int highValue) {
        mHighValue = highValue;
    }

    public int getHighStrategy() {
        return mHighStrategy;
    }

    public void setHighStrategy(int highStrategy) {
        mHighStrategy = highStrategy;
    }
}
