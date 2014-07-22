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

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Point3f;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class RenderSet {

    private List<RenderPoly> mAllPolys;
    private List<RenderPoly> mVisiblePolys;
    private Point3f mUnitX;
    private Point3f mUnitY;
    private Point3f mUnitZ;
    private Point3f mOrigin;

    public RenderSet() {
        mAllPolys = new ArrayList<>();
        mVisiblePolys = new ArrayList<>();
    }

    public List<RenderPoly> getAllPolys() {
        return mAllPolys;
    }

    public void setAllPolys(List<RenderPoly> polys) {
        mAllPolys = polys;
    }

    public List<RenderPoly> getVisiblePolys() {
        return mVisiblePolys;
    }

    public void setVisiblePolys(List<RenderPoly> visiblePolys) {
        mVisiblePolys = visiblePolys;
    }

    public Point3f getUnitX() {
        return mUnitX;
    }

    public void setUnitX(Point3f unitX) {
        mUnitX = unitX;
    }

    public Point3f getUnitY() {
        return mUnitY;
    }

    public void setUnitY(Point3f unitY) {
        mUnitY = unitY;
    }

    public Point3f getUnitZ() {
        return mUnitZ;
    }

    public void setUnitZ(Point3f unitZ) {
        mUnitZ = unitZ;
    }

    public Point3f getOrigin() {
        return mOrigin;
    }

    public void setOrigin(Point3f origin) {
        mOrigin = origin;
    }
}
