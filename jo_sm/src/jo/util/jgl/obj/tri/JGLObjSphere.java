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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.util.jgl.JGLUtils;
import jo.vecmath.Color3f;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;
import jo.vecmath.logic.Point3fLogic;

/**
 * A vertex shaded cube.
 */
public class JGLObjSphere extends JGLObj {

    private static Map<Integer, FloatBuffer> mVertexBuffers;
    private static Map<Integer, FloatBuffer> mNormalBuffers;
    private static Map<Integer, ShortBuffer> mIndexBuffers;
    private static Map<Integer, Point3f> mHBounds;
    private static Map<Integer, Point3f> mLBounds;

    static {
        mVertexBuffers = new HashMap<>();
        mNormalBuffers = new HashMap<>();
        mIndexBuffers = new HashMap<>();
        mHBounds = new HashMap<>();
        mLBounds = new HashMap<>();
    }

    private static float c = 1 / (float) Math.sqrt(5);
    private static float s = 2 / (float) Math.sqrt(5);
    private static float c1 = (float) Math.cos(2 * (float) Math.PI / 5);
    private static float s1 = (float) Math.sin(2 * (float) Math.PI / 5);
    private static float c2 = (float) Math.cos((float) Math.PI / 5);
    private static float s2 = (float) Math.sin((float) Math.PI / 5);

    private static final float[] vertices
            = {
                0, 0, 1,
                // upper ring
                s, 0, c,
                s * c1, s * s1, c,
                -s * c2, s * s2, c,
                -s * c2, -s * s2, c,
                s * c1, -s * s1, c,
                // lower ring
                s * c2, s * s2, -c,
                -s * c1, s * s1, -c,
                -s, 0, -c,
                -s * c1, -s * s1, -c,
                s * c2, -s * s2, -c,
                0, 0, -1,};
    private static final short[] indices = {
        // top
        0, 1, 2,
        0, 2, 3,
        0, 3, 4,
        0, 4, 5,
        0, 5, 1,
        // top middle
        2, 1, 6,
        3, 2, 7,
        4, 3, 8,
        5, 4, 9,
        1, 5, 10,
        // bottom middle
        6, 7, 2,
        7, 8, 3,
        8, 9, 4,
        9, 10, 5,
        10, 6, 1,
        // bottom
        7, 6, 11,
        8, 7, 11,
        9, 8, 11,
        10, 9, 11,
        6, 10, 11,};

    protected int mVertexCount = 0;
    protected int mFaceCount = 0;

    public JGLObjSphere(int depth, Color3f baseColor, Color3f deltaColor) {
        int numColors;
        if (depth == 0) {
            numColors = 12;
        } else if (depth == 1) {
            numColors = 42;
        } else if (depth == 2) {
            numColors = 162;
        } else {
            numColors = 642;
        }
        List<Color3f> colorArray = JGLUtils.rndColors(numColors, baseColor, deltaColor);
        init(depth, colorArray);
    }

    public JGLObjSphere(Point3f size, Point3f center) {
        this(2, new Color3f(.5f, .5f, .5f), new Color3f(.25f, .25f, .25f));
        Matrix4fLogic.translate(getTransform(), center);
        Matrix4fLogic.scale(getTransform(), size);
    }

    public JGLObjSphere(int depth, List<Color3f> colors) {
        init(depth, colors);
    }

    @Override
    public void recycle() {
        if (mVertexBuffers != null) {
            mVertexBuffer = null; // these are cached, don't recycle
        }
        if (mIndexBuffers != null) {
            mIndexShortBuffer = null;
        }
        if (mNormalBuffers != null) {
            mNormalBuffer = null;
        }
        super.recycle();
    }

    private void init(int depth, List<Color3f> colors) {
        List<Float> vertex = new ArrayList<>();
        List<Short> index = new ArrayList<>();
        List<Float> normal = new ArrayList<>();
        List<Color3f> color = new ArrayList<>();
        // initialize with zero-order
        for (float f : vertices) {
            vertex.add(f);
            normal.add(f);
        }
        mVertexCount = vertex.size() / 3;
        for (short i : indices) {
            index.add(i);
        }
        mFaceCount = index.size() / 3;
        for (Color3f c : colors) {
            color.add(c);
        }
        Map<String, Short> alreadySubdivided = new HashMap<>();
        // subdivide
        while (depth-- > 0) {
            //System.out.println("Subdivision pass");
            List<Short> oldIndex = index;
            index = new ArrayList<Short>();
            for (int face = 0; face < oldIndex.size(); face += 3) {
                // get primary corners
                short i1 = oldIndex.get(face + 0);
                short i2 = oldIndex.get(face + 1);
                short i3 = oldIndex.get(face + 2);
                //System.out.println("subdividing "+i1+","+i2+","+i3);
                // create inbetweens
                Short i12 = calcInbetween(i1, i2, alreadySubdivided, vertex, normal, color);
                Short i23 = calcInbetween(i2, i3, alreadySubdivided, vertex, normal, color);
                Short i31 = calcInbetween(i3, i1, alreadySubdivided, vertex, normal, color);
                index.add(i1);
                index.add(i12);
                index.add(i31);
                index.add(i12);
                index.add(i2);
                index.add(i23);
                index.add(i31);
                index.add(i23);
                index.add(i3);
                index.add(i23);
                index.add(i31);
                index.add(i12);
            }
        }
        mVertexCount = vertex.size() / 3;
        mFaceCount = index.size() / 3;

        if ((mVertexBuffers != null) && mVertexBuffers.containsKey(depth)) {
            setVertexBuffer(mVertexBuffers.get(depth));
            setHighBounds(mHBounds.get(depth));
            setLowBounds(mLBounds.get(depth));
        } else {
            setVertices(vertex);
            if (mVertexBuffers != null) {
                mVertexBuffers.put(depth, getVertexBuffer());
                mHBounds.put(depth, getHighBounds());
                mLBounds.put(depth, getLowBounds());
            }
        }
        if ((mNormalBuffers != null) && mNormalBuffers.containsKey(depth)) {
            setNormalBuffer(mNormalBuffers.get(depth));
        } else {
            setNormals(normal);
            if (mNormalBuffers != null) {
                mNormalBuffers.put(depth, getNormalBuffer());
            }
        }
        if ((mIndexBuffers != null) && mIndexBuffers.containsKey(depth)) {
            setIndexShortBuffer(mIndexBuffers.get(depth));
        } else {
            setIndices(index);
            if (mIndexBuffers != null) {
                mIndexBuffers.put(depth, getIndexShortBuffer());
            }
        }
        setColors(color);
    }

    private Short calcInbetween(short i1, short i2, Map<String, Short> alreadySubdivided, List<Float> vertex, List<Float> normal, List<Color3f> color) {
        Point3f v1 = new Point3f(vertex.get(i1 * 3 + 0), vertex.get(i1 * 3 + 1), vertex.get(i1 * 3 + 2));
        Point3f v2 = new Point3f(vertex.get(i2 * 3 + 0), vertex.get(i2 * 3 + 1), vertex.get(i2 * 3 + 2));
        String k;
        if (i1 < i2) {
            k = i1 + "." + i2;
        } else {
            k = i2 + "." + i1;
        }
        Short i12 = alreadySubdivided.get(k);
        if (i12 == null) {
            i12 = (short) (vertex.size() / 3);
            alreadySubdivided.put(k, i12);
            //System.out.println("creating "+k);
            Point3f v12 = new Point3f(v1);
            v12.add(v2);
            v12.scale(.5f);
            Point3fLogic.normalize(v12);
            vertex.add((float) v12.getX());
            vertex.add((float) v12.getY());
            vertex.add((float) v12.getZ());
            normal.add((float) v12.getX());
            normal.add((float) v12.getY());
            normal.add((float) v12.getZ());
            Color3f c_1 = color.get(i1);
            Color3f c_2 = color.get(i2);
            Color3f c12 = new Color3f(c_1);
            c12.add(c_2);
            c12.scale(.5f);
            color.add(c12);
        }
        //else
//            System.out.println("reusing "+k);
        return i12;
    }
}
