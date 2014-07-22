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
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import jo.util.jgl.JGLColorValues;
import jo.vecmath.Color3f;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Point3fLogic;

/**
 * A vertex shaded cube.
 */
public class JGLObjTorus extends JGLObj {

    private float mRadius1;
    private float mRadius2;
    private int mNSides;
    private int mNRings;
    private boolean mDirty;

    public JGLObjTorus(float r1, float r2, int nsides, int nrings) {
        mRadius1 = r1;
        mRadius2 = r2;
        mNSides = nsides;
        mNRings = nrings;
        mDirty = true;
        clean();
    }

    public JGLObjTorus(float r1, float r2) {
        this(r1, r2, 15, 15);
    }

    public JGLObjTorus() {
        this(1f, .5f, 15, 15);
    }

    private void clean() {
        if (!mDirty) {
            return;
        }
        List<Point3f> vertex = new ArrayList<>((mNRings + 1) * (mNSides + 1));
        List<Short> index = new ArrayList<>();
        List<Point3f> normal = new ArrayList<>((mNRings + 1) * (mNSides + 1));
        List<Color3f> colors = new ArrayList<>((mNRings + 1) * (mNSides + 1));
        float ringDelta = 2.0f * (float) Math.PI / mNRings;
        float sideDelta = 2.0f * (float) Math.PI / mNSides;
        float ringAngle = 0;
        for (int ring = 0; ring <= mNRings; ring++) {
            float sideAngle = 0;
            for (int side = 0; side <= mNSides; side++) {
                Point3f v = new Point3f(mRadius1, 0, 0);
                Point3fLogic.rotateBy(v, 0, sideAngle, 0);
                v.x += mRadius2;
                Point3fLogic.rotateBy(v, 0, 0, ringAngle);
                vertex.add(v);
                Point3f n = new Point3f(1, 0, 0);
                Point3fLogic.rotateBy(n, 0, sideAngle, 0);
                Point3fLogic.rotateBy(n, 0, 0, ringAngle);
                normal.add(v);
                colors.add(JGLColorValues.Red);
                sideAngle += sideDelta;
            }
            ringAngle += ringDelta;
        }
        for (int i = 0; i < mNRings; i++) {
            for (int j = 0; j < mNSides; j++) {
                index.add((short) ((i + 0) * (mNSides + 1) + (j + 0)));
                index.add((short) ((i + 1) * (mNSides + 1) + (j + 0)));
                index.add((short) ((i + 0) * (mNSides + 1) + (j + 1)));

                index.add((short) ((i + 1) * (mNSides + 1) + (j + 0)));
                index.add((short) ((i + 1) * (mNSides + 1) + (j + 1)));
                index.add((short) ((i + 0) * (mNSides + 1) + (j + 1)));
            }
        }

        setVertices(vertex);
        setNormals(normal);
        setIndices(index);
        setColors(colors);
        mDirty = false;
    }

    public float getRadius1() {
        return mRadius1;
    }

    public void setRadius1(float radius1) {
        mRadius1 = radius1;
        mDirty = true;
    }

    public float getRadius2() {
        return mRadius2;
    }

    public void setRadius2(float radius2) {
        mRadius2 = radius2;
        mDirty = true;
    }

    public int getNSides() {
        return mNSides;
    }

    public void setNSides(int nSides) {
        mNSides = nSides;
        mDirty = true;
    }

    public int getNRings() {
        return mNRings;
    }

    public void setNRings(int nRings) {
        mNRings = nRings;
        mDirty = true;
    }

    @Override
    public FloatBuffer getVertexBuffer() {
        clean();
        return super.getVertexBuffer();
    }

    @Override
    public FloatBuffer getNormalBuffer() {
        clean();
        return super.getNormalBuffer();
    }

    @Override
    public ShortBuffer getIndexShortBuffer() {
        clean();
        return super.getIndexShortBuffer();
    }

    @Override
    public IntBuffer getIndexIntBuffer() {
        clean();
        return super.getIndexIntBuffer();
    }
}
