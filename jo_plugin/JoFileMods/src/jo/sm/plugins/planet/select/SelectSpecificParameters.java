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
package jo.sm.plugins.planet.select;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Select Specific Region", shortDescription = "")
public class SelectSpecificParameters {

    @Description(displayName = "Start X", priority = 1)
    private int mLowX;
    @Description(displayName = "Start Y", priority = 2)
    private int mLowY;
    @Description(displayName = "Start Z", priority = 3)
    private int mLowZ;
    @Description(displayName = "End X", priority = 4)
    private int mHighX;
    @Description(displayName = "End Y", priority = 5)
    private int mHighY;
    @Description(displayName = "End Z", priority = 6)
    private int mHighZ;

    public SelectSpecificParameters() {
        mLowX = 0;
        mLowY = 0;
        mLowZ = 0;
        mHighX = 16;
        mHighY = 16;
        mHighZ = 16;
    }

    public int getLowX() {
        return mLowX;
    }

    public void setLowX(int lowX) {
        mLowX = lowX;
    }

    public int getLowY() {
        return mLowY;
    }

    public void setLowY(int lowY) {
        mLowY = lowY;
    }

    public int getLowZ() {
        return mLowZ;
    }

    public void setLowZ(int lowZ) {
        mLowZ = lowZ;
    }

    public int getHighX() {
        return mHighX;
    }

    public void setHighX(int highX) {
        mHighX = highX;
    }

    public int getHighY() {
        return mHighY;
    }

    public void setHighY(int highY) {
        mHighY = highY;
    }

    public int getHighZ() {
        return mHighZ;
    }

    public void setHighZ(int highZ) {
        mHighZ = highZ;
    }
}
