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
@Description(displayName = "Reflect Model", shortDescription = "The model, or selection, will be reflected along the axis specified above.")
public class ReflectParameters {

    @Description(displayName = "Port/Starboard", shortDescription = "Flip the X coordinate on all blocks")
    private boolean mXReflect;
    @Description(displayName = "Dorsal/Ventral", shortDescription = "Flip the Y coordinate on all blocks")
    private boolean mYReflect;
    @Description(displayName = "Fore/Aft", shortDescription = "Flip the Z coordinate on all blocks")
    private boolean mZReflect;

    public ReflectParameters() {
    }

    public boolean isXReflect() {
        return mXReflect;
    }

    public void setXReflect(boolean xReflect) {
        mXReflect = xReflect;
    }

    public boolean isYReflect() {
        return mYReflect;
    }

    public void setYReflect(boolean yReflect) {
        mYReflect = yReflect;
    }

    public boolean isZReflect() {
        return mZReflect;
    }

    public void setZReflect(boolean zReflect) {
        mZReflect = zReflect;
    }
}
