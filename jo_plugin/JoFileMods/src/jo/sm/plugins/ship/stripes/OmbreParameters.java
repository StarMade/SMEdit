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
package jo.sm.plugins.ship.stripes;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 * @Auther Robert Barefoot for SMEdit - version 1.1
 **/
@Description(displayName = "Paint With Onbre Pattern", shortDescription = "Paint a pattern fading from one color to another.")
public class OmbreParameters {

    @Description(displayName = "First Color")
    private short mColor1;
    @Description(displayName = "Second Color")
    private short mColor2;
    @Description(displayName = "Left to Right")
    private boolean mXAxis;
    @Description(displayName = "Top to Bottom")
    private boolean mYAxis;
    @Description(displayName = "Fore to Aft")
    private boolean mZAxis;

    public OmbreParameters() {
        mColor1 = BlockTypes.HULL_COLOR_BLACK_ID;
        mColor2 = BlockTypes.HULL_COLOR_WHITE_ID;
        mXAxis = true;
        mYAxis = false;
        mZAxis = false;
    }

    public short getColor1() {
        return mColor1;
    }

    public void setColor1(short color1) {
        mColor1 = color1;
    }

    public short getColor2() {
        return mColor2;
    }

    public void setColor2(short color2) {
        mColor2 = color2;
    }

    public boolean isXAxis() {
        return mXAxis;
    }

    public void setXAxis(boolean xAxis) {
        mXAxis = xAxis;
    }

    public boolean isYAxis() {
        return mYAxis;
    }

    public void setYAxis(boolean yAxis) {
        mYAxis = yAxis;
    }

    public boolean isZAxis() {
        return mZAxis;
    }

    public void setZAxis(boolean zAxis) {
        mZAxis = zAxis;
    }
}
