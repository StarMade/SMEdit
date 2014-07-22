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

import jo.vecmath.Point3s;

public class ControllerEntry {

    private Point3s mPosition;
    private List<GroupEntry> mGroups;

    public ControllerEntry() {
        mGroups = new ArrayList<>();
    }

    public Point3s getPosition() {
        return mPosition;
    }

    public void setPosition(Point3s position) {
        mPosition = position;
    }

    public List<GroupEntry> getGroups() {
        return mGroups;
    }

    public void setGroups(List<GroupEntry> groups) {
        mGroups = groups;
    }
}
