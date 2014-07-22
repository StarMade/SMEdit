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
package jo.sm.data;

import java.io.File;
import java.util.Map;

import jo.sm.ent.data.Tag;
import jo.sm.ship.data.Data;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class Entity {

    private File mFile;
    private String mName;
    private String mUNID;
    private String mType;
    private Point3i mLocation;
    private Tag mTag;
    private Map<Point3i, Data> mData;

    @Override
    public String toString() {
        if ("SPACESTATION".equals(mType)) {
            return "Station " + mUNID;
        }
        if ("SHOP".equals(mType)) {
            return "Shop " + mUNID;
        }
        if ("PLANET".equals(mType)) {
            return "Planet " + mLocation;
        }
        if ("SHIP".equals(mType)) {
            if (mName.equals("AITURRET")) {
                return "AI Turret " + mUNID;
            }
            if (mName.startsWith("MOB_SIM_")) {
                return "Enemy " + mName.substring(7);
            }
            if (mName.startsWith("MOB_")) {
                return "Enemy " + mName.substring(4);
            }
            return "Ship " + mName;
        }
        return mType + " " + mUNID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUNID() {
        return mUNID;
    }

    public void setUNID(String uNID) {
        mUNID = uNID;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Point3i getLocation() {
        return mLocation;
    }

    public void setLocation(Point3i location) {
        mLocation = location;
    }

    public Tag getTag() {
        return mTag;
    }

    public void setTag(Tag tag) {
        mTag = tag;
    }

    public Map<Point3i, Data> getData() {
        return mData;
    }

    public void setData(Map<Point3i, Data> data) {
        mData = data;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }
}
