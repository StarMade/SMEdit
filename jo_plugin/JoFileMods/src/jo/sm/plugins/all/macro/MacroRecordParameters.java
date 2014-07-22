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
package jo.sm.plugins.all.macro;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Record Macro", shortDescription = "Record actions as a macro")
public class MacroRecordParameters {

    @Description(displayName = "Name", shortDescription = "Name of macro", priority = 1)
    private String mName;
    @Description(displayName = "Author", shortDescription = "Who are you?", priority = 2)
    private String mAuthor;
    @Description(displayName = "Placement", shortDescription = "Where the macro should appear", priority = 3)
    private String mPlacement;
    @Description(displayName = "Enablement", shortDescription = "Type of a object it should appear for", priority = 4)
    private String mEnablement;
    @Description(displayName = "Priority", shortDescription = "How high up or down the menu to appear", priority = 5)
    private int mPriority;
    @Description(displayName = "Description", shortDescription = "Long description", priority = 6)
    private String mDescription;

    public MacroRecordParameters() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getPlacement() {
        return mPlacement;
    }

    public void setPlacement(String placement) {
        mPlacement = placement;
    }

    public String getEnablement() {
        return mEnablement;
    }

    public void setEnablement(String enablement) {
        mEnablement = enablement;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
