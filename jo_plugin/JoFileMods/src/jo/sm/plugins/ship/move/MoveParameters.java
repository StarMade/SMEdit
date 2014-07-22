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
package jo.sm.plugins.ship.move;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Move Ship's Core")
public class MoveParameters {

    @Description(displayName = "Starboard/Port", shortDescription = "How far along X axis")
    private int mXMove;
    @Description(displayName = "Dorsal/Ventral", shortDescription = "How far along Y axis")
    private int mYMove;
    @Description(displayName = "Fore/Aft", shortDescription = "How far along Z axis")
    private int mZMove;

    public MoveParameters() {
        mXMove = 0;
        mYMove = 0;
        mZMove = 0;
    }

    public int getXMove() {
        return mXMove;
    }

    public void setXMove(int xMove) {
        mXMove = xMove;
    }

    public int getYMove() {
        return mYMove;
    }

    public void setYMove(int yMove) {
        mYMove = yMove;
    }

    public int getZMove() {
        return mZMove;
    }

    public void setZMove(int zMove) {
        mZMove = zMove;
    }

}
