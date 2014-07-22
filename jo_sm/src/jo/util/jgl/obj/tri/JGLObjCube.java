/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jo.util.jgl.obj.tri;

import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;

/**
 * A vertex shaded cube.
 */
public class JGLObjCube extends JGLObj {

    public JGLObjCube() {
        float radius = .5f;
        float vertices[] = {
            -radius, -radius, -radius,
            radius, -radius, -radius,
            radius, radius, -radius,
            -radius, radius, -radius,
            -radius, -radius, radius,
            radius, -radius, radius,
            radius, radius, radius,
            -radius, radius, radius,};

        float colors[] = {
            0, 0, 0, 1,
            1, 0, 0, 1,
            1, 1, 0, 1,
            0, 1, 0, 1,
            0, 0, 1, 1,
            1, 0, 1, 1,
            1, 1, 1, 1,
            0, 1, 1, 1,};

        short indices[] = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
        };

        setVertices(vertices);
        setColors(colors);
        setIndices(indices);
    }

    public JGLObjCube(Point3f size, Point3f center) {
        this();
        Matrix4fLogic.translate(getTransform(), center);
        Matrix4fLogic.scale(getTransform(), size);
    }

    @Override
    public void setFadeColor(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        float colors1[] = {
            r1, g1, b1, a1,};
        float colors2[] = {
            r2, g2, b2, a2,};
        mColorBuffer.position(0);
        mColorBuffer.put(colors1);
        mColorBuffer.put(colors2);
        mColorBuffer.put(colors2);
        mColorBuffer.put(colors1);
        mColorBuffer.put(colors1);
        mColorBuffer.put(colors2);
        mColorBuffer.put(colors2);
        mColorBuffer.put(colors1);
        mColorBuffer.position(0);
    }
}
