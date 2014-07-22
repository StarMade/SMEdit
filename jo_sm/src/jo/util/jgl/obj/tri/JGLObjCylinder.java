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

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;

/**
 * Unit cylinder.
 */
public class JGLObjCylinder extends JGLObj {

    static private Map<Integer, FloatBuffer> mVertexBuffers = new HashMap<>();
    static private Map<Integer, ShortBuffer> mIndexBuffers = new HashMap<>();
    private int mResolution;

    public JGLObjCylinder() {
        this(16);
    }

    public JGLObjCylinder(Point3f size, Point3f center) {
        this();
        Matrix4fLogic.translate(getTransform(), center);
        Matrix4fLogic.scale(getTransform(), size);
    }

    public JGLObjCylinder(int resolution) {
        mResolution = resolution;
        if (mVertexBuffers.containsKey(mResolution)) {
            setVertexBuffer(mVertexBuffers.get(mResolution));
        } else {
            setupVerticies();
        }
        setupColors();
        if (mIndexBuffers.containsKey(mResolution)) {
            setIndexShortBuffer(mIndexBuffers.get(mResolution));
        } else {
            setupIndicies();
        }
    }

    @Override
    public void recycle() {
        mVertexBuffer = null; // these are cached, don't recycle
        mIndexShortBuffer = null;
        super.recycle();
    }

    private void setupVerticies() {
        float vertices[] = new float[mResolution * 3 * 2 + 2 * 3];
        for (int i = 0; i < mResolution; i++) {
            float a = (float) (Math.PI * 2 * i / mResolution);
            float z = .5f * (float) Math.sin(a);
            float y = .5f * (float) Math.cos(a);
            vertices[i * 3 + 0] = y;
            vertices[i * 3 + 1] = z;
            vertices[i * 3 + 2] = -.5f;
            vertices[i * 3 + 0 + mResolution * 3] = y;
            vertices[i * 3 + 1 + mResolution * 3] = z;
            vertices[i * 3 + 2 + mResolution * 3] = .5f;
        }
        vertices[mResolution * 3 * 2 + 0] = 0f;
        vertices[mResolution * 3 * 2 + 1] = 0f;
        vertices[mResolution * 3 * 2 + 2] = -.5f;
        vertices[mResolution * 3 * 2 + 3] = 0f;
        vertices[mResolution * 3 * 2 + 4] = 0f;
        vertices[mResolution * 3 * 2 + 5] = .5f;
        setVertices(vertices);
        mVertexBuffers.put(mResolution, mVertexBuffer);
    }

    private void setupColors() {
        float colors[] = new float[mResolution * 4 * 2 + 2 * 4];
        int idx = 0;
        for (int i = 0; i < mResolution; i++) {
            colors[idx++] = 1;
            colors[idx++] = 0;
            colors[idx++] = 0;
            colors[idx++] = 1;
        }
        for (int i = 0; i < mResolution; i++) {
            colors[idx++] = 0;
            colors[idx++] = 1;
            colors[idx++] = 0;
            colors[idx++] = 1;
        }
        colors[idx++] = 1;
        colors[idx++] = 1;
        colors[idx++] = 1;
        colors[idx++] = 1;
        colors[idx++] = 0;
        colors[idx++] = 0;
        colors[idx++] = 0;
        colors[idx++] = 1;
        setColors(colors);
    }

    private void setupIndicies() {
        Collection<Short> indices = new ArrayList<>();
        short top = (short) (mResolution * 2 + 0);
        short bottom = (short) (mResolution * 2 + 1);
        for (short i = 0; i < mResolution; i++) {
            short topLeft = i;
            short topRight = (short) ((topLeft + 1) % mResolution);
            short botLeft = (short) (topLeft + mResolution);
            short botRight = (short) (topRight + mResolution);
            indices.add(topRight);
            indices.add(topLeft);
            indices.add(top);
            indices.add(botLeft);
            indices.add(botRight);
            indices.add(bottom);
            indices.add(topLeft);
            indices.add(topRight);
            indices.add(botLeft);
            indices.add(botLeft);
            indices.add(topRight);
            indices.add(botRight);
        }
        setIndices(indices);
        mIndexBuffers.put(mResolution, mIndexShortBuffer);
    }

    @Override
    public void setFadeColor(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        float colors1[] = {
            r1, g1, b1, a1,};
        float colors2[] = {
            r2, g2, b2, a2,};
        mColorBuffer.position(0);
        for (int i = 0; i < mResolution; i++) {
            mColorBuffer.put(colors1);
        }
        for (int i = 0; i < mResolution; i++) {
            mColorBuffer.put(colors2);
        }
        mColorBuffer.put(colors1);
        mColorBuffer.put(colors2);
        mColorBuffer.position(0);
    }
}
