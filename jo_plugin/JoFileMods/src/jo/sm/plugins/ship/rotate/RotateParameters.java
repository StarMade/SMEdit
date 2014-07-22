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
package jo.sm.plugins.ship.rotate;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Rotate ship around core")
public class RotateParameters {

    @Description(displayName = "Pitch", shortDescription = "Around X axis")
    private int mXRotate;
    @Description(displayName = "Yaw", shortDescription = "Around Y axis")
    private int mYRotate;
    @Description(displayName = "Roll", shortDescription = "Around Z axis")
    private int mZRotate;

    public RotateParameters() {
        mXRotate = 0;
        mYRotate = 0;
        mZRotate = 0;
    }

    public int getXRotate() {
        return mXRotate;
    }

    public void setXRotate(int xRotate) {
        mXRotate = xRotate;
    }

    public int getYRotate() {
        return mYRotate;
    }

    public void setYRotate(int yRotate) {
        mYRotate = yRotate;
    }

    public int getZRotate() {
        return mZRotate;
    }

    public void setZRotate(int zRotate) {
        mZRotate = zRotate;
    }

}
