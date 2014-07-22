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
package jo.sm.ui.logic;

import java.io.File;

import jo.sm.data.Entity;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ShipSpec {

    public static final int BLUEPRINT = 0;
    public static final int DEFAULT_BLUEPRINT = 1;
    public static final int ENTITY = 2;
    public static final int FILE = 3;

    private int mClassification;
    private int mType;
    private String mName;
    private Entity mEntity;
    private File mFile;

    @Override
    public String toString() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Entity getEntity() {
        return mEntity;
    }

    public void setEntity(Entity entity) {
        mEntity = entity;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public int getClassification() {
        return mClassification;
    }

    public void setClassification(int classification) {
        mClassification = classification;
    }
}
