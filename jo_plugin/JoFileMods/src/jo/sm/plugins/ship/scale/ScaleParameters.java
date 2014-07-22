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
package jo.sm.plugins.ship.scale;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Scale model", shortDescription = "Cange the size of the object")
public class ScaleParameters {

    @Description(displayName = "Width", shortDescription = "Scale along X axis, port/starboard")
    private float mXScale;
    @Description(displayName = "Height", shortDescription = "Scale along Y axis, dorsal/ventral")
    private float mYScale;
    @Description(displayName = "Length", shortDescription = "Scale along Z axis, fore/aft")
    private float mZScale;

    public ScaleParameters() {
        mXScale = 1;
        mYScale = 1;
        mZScale = 1;
    }

    public float getXScale() {
        return mXScale;
    }

    public void setXScale(float xScale) {
        mXScale = xScale;
    }

    public float getYScale() {
        return mYScale;
    }

    public void setYScale(float yScale) {
        mYScale = yScale;
    }

    public float getZScale() {
        return mZScale;
    }

    public void setZScale(float zScale) {
        mZScale = zScale;
    }

}
