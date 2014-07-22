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
package jo.sm.plugins.ship.imp;

import java.awt.image.BufferedImage;
import java.io.File;

import jo.vecmath.Point3f;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class OBJMaterial {

    private String mName;
    private float mNS;
    private Point3f mKA;
    private Point3f mKD;
    private Point3f mKS;
    private float mNi;
    private float mD;
    private float mIllum;
    private File mMapKD;
    private BufferedImage mMapKDImage;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getNS() {
        return mNS;
    }

    public void setNS(float nS) {
        mNS = nS;
    }

    public Point3f getKA() {
        return mKA;
    }

    public void setKA(Point3f kA) {
        mKA = kA;
    }

    public Point3f getKD() {
        return mKD;
    }

    public void setKD(Point3f kF) {
        mKD = kF;
    }

    public Point3f getKS() {
        return mKS;
    }

    public void setKS(Point3f kS) {
        mKS = kS;
    }

    public float getNi() {
        return mNi;
    }

    public void setNi(float ni) {
        mNi = ni;
    }

    public float getD() {
        return mD;
    }

    public void setD(float f) {
        mD = f;
    }

    public float getIllum() {
        return mIllum;
    }

    public void setIllum(float illum) {
        mIllum = illum;
    }

    public File getMapKD() {
        return mMapKD;
    }

    public void setMapKD(File mapKD) {
        mMapKD = mapKD;
    }

    public BufferedImage getMapKDImage() {
        return mMapKDImage;
    }

    public void setMapKDImage(BufferedImage mapKDImage) {
        mMapKDImage = mapKDImage;
    }
}
