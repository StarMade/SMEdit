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
package jo.sm.plugins.ship.reflect;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Force Symmetry", shortDescription = "The model, or selection, will be reflected and duplicated along the axis specified above.")
public class DuplicateParameters {

    @Description(displayName = "Port/Starboard", shortDescription = "Reflect along the X axis")
    private int mXReflect;
    @Description(displayName = "Dorsal/Ventral", shortDescription = "Reflect along the Y axis")
    private int mYReflect;
    @Description(displayName = "Fore/Aft", shortDescription = "Reflect along the Z axis")
    private int mZReflect;

    public DuplicateParameters() {
    }

    public int getXReflect() {
        return mXReflect;
    }

    public void setXReflect(int xReflect) {
        mXReflect = xReflect;
    }

    public int getYReflect() {
        return mYReflect;
    }

    public void setYReflect(int yReflect) {
        mYReflect = yReflect;
    }

    public int getZReflect() {
        return mZReflect;
    }

    public void setZReflect(int zReflect) {
        mZReflect = zReflect;
    }
}
