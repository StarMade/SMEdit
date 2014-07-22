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
package jo.sm.factories.planet.comp;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MaterialDefinition {

    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private int mPriority;
    private List<MaterialEntry> mOldBlocks;
    private List<MaterialEntry> mNewBlocks;

    public MaterialDefinition() {
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mPriority = 50;
        mOldBlocks = new ArrayList<>();
        mNewBlocks = new ArrayList<>();
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

    public List<MaterialEntry> getOldBlocks() {
        return mOldBlocks;
    }

    public void setOldBlocks(List<MaterialEntry> oldBlocks) {
        mOldBlocks = oldBlocks;
    }

    public List<MaterialEntry> getNewBlocks() {
        return mNewBlocks;
    }

    public void setNewBlocks(List<MaterialEntry> newBlocks) {
        mNewBlocks = newBlocks;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }
}
