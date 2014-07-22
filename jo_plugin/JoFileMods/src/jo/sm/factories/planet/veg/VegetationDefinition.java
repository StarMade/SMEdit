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
package jo.sm.factories.planet.veg;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class VegetationDefinition {

    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private int mPriority;
    private float mDensity;
    private List<VegetationEntry> mVegetation;

    public VegetationDefinition() {
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mPriority = 50;
        mDensity = 1 / 1000f;
        mVegetation = new ArrayList<>();
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public float getDensity() {
        return mDensity;
    }

    public void setDensity(float density) {
        mDensity = density;
    }

    public List<VegetationEntry> getVegetation() {
        return mVegetation;
    }

    public void setVegetation(List<VegetationEntry> vegetation) {
        mVegetation = vegetation;
    }
}
