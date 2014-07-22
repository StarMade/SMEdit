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

import java.util.ArrayList;
import java.util.List;

/*
 +        start   type
 +        0       int                         unknown int
 +        4       int                         numControllers (N)
 +        8       controllerEntry[N]
 +        
 +        controllerEntry is a variable length struct
 +            0   short[3]    Position of the controller block, for example the core is defined at (8, 8, 8)
 +            12  int         Number of groups of controlled blocks.  (M)
 +            16  groupEntry[M]
 +        
 +        groupEntry is a variable length struct
 +            0   short       Block ID for all blocks in this group
 +            2   int         Number of blocks in the group (I)
 +            6   short[3][I] Array of blocks positions for each of the I blocks 
 */
public class Logic {

    private int mUnknown1;
    private List<ControllerEntry> mControllers;

    public Logic() {
        mControllers = new ArrayList<ControllerEntry>();
    }

    public int getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(int unknown1) {
        mUnknown1 = unknown1;
    }

    public List<ControllerEntry> getControllers() {
        return mControllers;
    }

    public void setControllers(List<ControllerEntry> controllers) {
        mControllers = controllers;
    }
}
