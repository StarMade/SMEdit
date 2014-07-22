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
@Description(displayName = "Delete Macro", shortDescription = "Delete a recorded macro")
public class MacroDeleteParameters {

    @Description(displayName = "Name", shortDescription = "Name of macro", priority = 1)
    private String mName;

    public MacroDeleteParameters() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
