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
@Description(displayName = "Export Images of Object to Disk", shortDescription = "This writes fore, aft, dorsal, ventral, port, starboard, and an isometric view"
        + " of the object, plus a contact sheet with everything on it to the given directory.")
public class ExportImagesParameters {

    @Description(displayName = "", shortDescription = "Directory to place images")
    private String mDirectory;
    @Description(displayName = "", shortDescription = "Prefix to use for image name")
    private String mName;
    @Description(displayName = "", shortDescription = "Width in pixels")
    private int mWidth;
    @Description(displayName = "", shortDescription = "Height in pixels")
    private int mHeight;

    public ExportImagesParameters() {
        mDirectory = System.getProperty("user.home");
        mWidth = 1024;
        mHeight = 768;
    }

    public String getDirectory() {
        return mDirectory;
    }

    public void setDirectory(String directory) {
        mDirectory = directory;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
}
