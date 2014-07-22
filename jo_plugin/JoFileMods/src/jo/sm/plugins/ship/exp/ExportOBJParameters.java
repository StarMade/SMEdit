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
package jo.sm.plugins.ship.exp;

import jo.sm.ui.act.plugin.Description;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@Description(displayName = "Export object as OBJ", shortDescription = "Exports model plus imagemap in Wavefront OBJ file format")
public class ExportOBJParameters {

    @Description(displayName = "", shortDescription = "OBJ file (including extension) to export to")
    private String mFile;

    public ExportOBJParameters() {
    }

    public String getFile() {
        return mFile;
    }

    public void setFile(String file) {
        mFile = file;
    }
}
