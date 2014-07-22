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

import java.util.ArrayList;
import java.util.List;

import jo.util.jgl.JGLColorValues;
import jo.vecmath.Color3f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;
import jo.vecmath.logic.Point2fLogic;
import jo.vecmath.logic.Point3fLogic;

/**
 * A vertex shaded cube.
 */
public class JGLObjHEALPixSphere extends JGLObj {

    protected int mVertCount;
    protected int mTriCount;
    protected int mEdgeSize;
    protected boolean mInterior;

    /**
     * the distance from the center point each point falls on
     */
    public float mRadius;

    private static final Point3f NORTH_POLE = new Point3f(0, 0, 1);
    private static final Point3f NORTH_TROPIC1 = Point3fLogic.rotateNew(Point3fLogic.rotateNew(NORTH_POLE, (float) Math.PI / 4, 0, 0), 0, 0, (float) Math.PI / 4);
    private static final Point3f NORTH_TROPIC2 = Point3fLogic.rotateNew(NORTH_TROPIC1, 0, 0, (float) Math.PI / 2);
    private static final Point3f NORTH_TROPIC3 = Point3fLogic.rotateNew(NORTH_TROPIC2, 0, 0, (float) Math.PI / 2);
    private static final Point3f NORTH_TROPIC4 = Point3fLogic.rotateNew(NORTH_TROPIC3, 0, 0, (float) Math.PI / 2);
    private static final Point3f EQUATOR1 = Point3fLogic.rotateNew(NORTH_POLE, (float) Math.PI / 2, 0, 0);
    private static final Point3f EQUATOR2 = Point3fLogic.rotateNew(EQUATOR1, 0, 0, (float) Math.PI / 2);
    private static final Point3f EQUATOR3 = Point3fLogic.rotateNew(EQUATOR2, 0, 0, (float) Math.PI / 2);
    private static final Point3f EQUATOR4 = Point3fLogic.rotateNew(EQUATOR3, 0, 0, (float) Math.PI / 2);
    private static final Point3f SOUTH_TROPIC1 = Point3fLogic.rotateNew(Point3fLogic.rotateNew(NORTH_POLE, (float) Math.PI * 3 / 4, 0, 0), 0, 0, (float) Math.PI / 4);
    private static final Point3f SOUTH_TROPIC2 = Point3fLogic.rotateNew(SOUTH_TROPIC1, 0, 0, (float) Math.PI / 2);
    private static final Point3f SOUTH_TROPIC3 = Point3fLogic.rotateNew(SOUTH_TROPIC2, 0, 0, (float) Math.PI / 2);
    private static final Point3f SOUTH_TROPIC4 = Point3fLogic.rotateNew(SOUTH_TROPIC3, 0, 0, (float) Math.PI / 2);
    private static final Point3f SOUTH_POLE = new Point3f(0, 0, -1);

    private static final float UV_X_0 = 0.0f;
    private static final float UV_X_1 = 0.25f;
    private static final float UV_X_2 = 0.5f;
    private static final float UV_X_3 = 0.75f;
    private static final float UV_X_4 = 1.0f;
    private static final float UV_Y_0 = 1.0f;
    private static final float UV_Y_1 = 2.0f / 3.0f;
    private static final float UV_Y_2 = 1.0f / 3.0f;
    private static final float UV_Y_3 = 0.0f;

    private static final Point2f[][] UV_GRID = {
        {new Point2f(UV_X_0, UV_Y_0), new Point2f(UV_X_1, UV_Y_0), new Point2f(UV_X_2, UV_Y_0), new Point2f(UV_X_3, UV_Y_0), new Point2f(UV_X_4, UV_Y_0),},
        {new Point2f(UV_X_0, UV_Y_1), new Point2f(UV_X_1, UV_Y_1), new Point2f(UV_X_2, UV_Y_1), new Point2f(UV_X_3, UV_Y_1), new Point2f(UV_X_4, UV_Y_1),},
        {new Point2f(UV_X_0, UV_Y_2), new Point2f(UV_X_1, UV_Y_2), new Point2f(UV_X_2, UV_Y_2), new Point2f(UV_X_3, UV_Y_2), new Point2f(UV_X_4, UV_Y_2),},
        {new Point2f(UV_X_0, UV_Y_3), new Point2f(UV_X_1, UV_Y_3), new Point2f(UV_X_2, UV_Y_3), new Point2f(UV_X_3, UV_Y_3), new Point2f(UV_X_4, UV_Y_3),},};

    private static final Point3f[][] V_FACES = {
        {NORTH_POLE, NORTH_TROPIC1, EQUATOR1, NORTH_TROPIC4,}, // 0
        {NORTH_POLE, NORTH_TROPIC2, EQUATOR2, NORTH_TROPIC1,}, // 3
        {NORTH_POLE, NORTH_TROPIC3, EQUATOR3, NORTH_TROPIC2,}, // 2
        {NORTH_POLE, NORTH_TROPIC4, EQUATOR4, NORTH_TROPIC3,}, // 1
        {NORTH_TROPIC1, EQUATOR2, SOUTH_TROPIC1, EQUATOR1,}, // 7
        {NORTH_TROPIC2, EQUATOR3, SOUTH_TROPIC2, EQUATOR2,}, // 6
        {NORTH_TROPIC3, EQUATOR4, SOUTH_TROPIC3, EQUATOR3,}, // 5
        {NORTH_TROPIC4, EQUATOR1, SOUTH_TROPIC4, EQUATOR4,}, // 4
        {EQUATOR2, SOUTH_TROPIC2, SOUTH_POLE, SOUTH_TROPIC1,}, // 10
        {EQUATOR3, SOUTH_TROPIC3, SOUTH_POLE, SOUTH_TROPIC2,}, // 9
        {EQUATOR4, SOUTH_TROPIC4, SOUTH_POLE, SOUTH_TROPIC3,}, // 8
        {EQUATOR1, SOUTH_TROPIC1, SOUTH_POLE, SOUTH_TROPIC4,}, // 11
    };

    private static final Point2f[][] UV_FACES = {
        {UV_GRID[0][3], UV_GRID[0][4], UV_GRID[1][4], UV_GRID[1][3],}, // 0
        {UV_GRID[0][0], UV_GRID[0][1], UV_GRID[1][1], UV_GRID[1][0],}, // 3
        {UV_GRID[0][1], UV_GRID[0][2], UV_GRID[1][2], UV_GRID[1][1],}, // 2
        {UV_GRID[0][2], UV_GRID[0][3], UV_GRID[1][3], UV_GRID[1][2],}, // 1
        {UV_GRID[1][3], UV_GRID[1][4], UV_GRID[2][4], UV_GRID[2][3],}, // 4       
        {UV_GRID[1][0], UV_GRID[1][1], UV_GRID[2][1], UV_GRID[2][0],}, // 7
        {UV_GRID[1][1], UV_GRID[1][2], UV_GRID[2][2], UV_GRID[2][1],}, // 6
        {UV_GRID[1][2], UV_GRID[1][3], UV_GRID[2][3], UV_GRID[2][2],}, // 5
        {UV_GRID[2][3], UV_GRID[2][4], UV_GRID[3][4], UV_GRID[3][3],}, // 8
        {UV_GRID[2][0], UV_GRID[2][1], UV_GRID[3][1], UV_GRID[3][0],}, // 11
        {UV_GRID[2][1], UV_GRID[2][2], UV_GRID[3][2], UV_GRID[3][1],}, // 10
        {UV_GRID[2][2], UV_GRID[2][3], UV_GRID[3][3], UV_GRID[3][2],}, // 9
    };

    public JGLObjHEALPixSphere(Point3f size, Point3f center) {
        this(2);
        Matrix4fLogic.translate(getTransform(), center);
        Matrix4fLogic.scale(getTransform(), size);
    }

    public JGLObjHEALPixSphere(int depth) {
        init(depth);
    }

    private void init(int zSamples) {
        mEdgeSize = zSamples;
        // allocate vertices
        mVertCount = mEdgeSize * mEdgeSize * 12 * 4;
        List<Point3f> posBuf = new ArrayList<>();
        List<Point3f> normBuf = new ArrayList<>();
        List<Point2f> texBuf = new ArrayList<>();
        List<Short> idxBuf = new ArrayList<>();
        // generate geometry
        for (int face = 0; face < 12; face++) {
            //System.out.println("Square "+face);
            makeSquare(posBuf, normBuf, texBuf,
                    V_FACES[face][0], V_FACES[face][1], V_FACES[face][2], V_FACES[face][3],
                    UV_FACES[face][0], UV_FACES[face][1], UV_FACES[face][2], UV_FACES[face][3]
            );
        }
        setIndexData(idxBuf);
        setVertices(posBuf);
        setNormals(normBuf);
        setTextures(texBuf);
        setIndices(idxBuf);
        List<Color3f> colorBuf = new ArrayList<>();
        int vertsPerFace = posBuf.size() / 12;
        for (int face = 0; face < 12; face++) {
            for (int v = 0; v < vertsPerFace; v++) {
                colorBuf.add(JGLColorValues.POINT_COLORS[face]);
            }
        }
        setColors(colorBuf);
        //setColors(JGLUtils.rndColors(mVertices, new Point3f(.5f, .5f, .5f), new Point3f(.5f, .5f, .5f)));
//        System.out.println("Verts: "+posBuf.size()+" (bytes="+mVertexBuffer.limit()+")");
//        System.out.println("Triangles: "+(idxBuf.size()/3));
//        for (int i = 0; i < idxBuf.size(); i += 3)
//        {
//            int i1 = idxBuf.get(i);
//            int i2 = idxBuf.get(i+1);
//            int i3 = idxBuf.get(i+2);
//            System.out.print("  "+i1+"-"+i2+"-"+i3);
//            Point3f p1 = posBuf.get(i1);
//            Point3f p2 = posBuf.get(i2);
//            Point3f p3 = posBuf.get(i3);
//            System.out.println(" -> "+p1+"-"+p2+"-"+p3);
//        }
    }

    private static final float E = .0001f;

    private void makeSquare(List<Point3f> posBuf, List<Point3f> normBuf, List<Point2f> texBuf,
            Point3f top, Point3f right, Point3f bottom, Point3f left,
            Point2f uvTop, Point2f uvRight, Point2f uvBottom, Point2f uvLeft) {
        //System.out.println("UV: "+uvTop+", "+uvRight+", "+uvBottom+", "+uvLeft);
        uvTop = new Point2f(uvTop.x + E, uvTop.y + E);
        uvRight = new Point2f(uvRight.x - E, uvRight.y + E);
        uvBottom = new Point2f(uvBottom.x - E, uvBottom.y - E);
        uvLeft = new Point2f(uvLeft.x + E, uvLeft.y - E);
        float pc = 1.0f / mEdgeSize;
        for (int j = 0; j < mEdgeSize; j++) {
            Point3f rowTopLeft = Point3fLogic.interpolate(top, left, j * pc);
            Point3f rowBotLeft = Point3fLogic.interpolate(top, left, (j + 1) * pc);
            Point3f rowTopRight = Point3fLogic.interpolate(right, bottom, j * pc);
            Point3f rowBotRight = Point3fLogic.interpolate(right, bottom, (j + 1) * pc);
            Point2f rowUVTopLeft = Point2fLogic.interpolate(uvTop, uvLeft, j * pc);
            Point2f rowUVBotLeft = Point2fLogic.interpolate(uvTop, uvLeft, (j + 1) * pc);
            Point2f rowUVTopRight = Point2fLogic.interpolate(uvRight, uvBottom, j * pc);
            Point2f rowUVBotRight = Point2fLogic.interpolate(uvRight, uvBottom, (j + 1) * pc);
            for (int i = 0; i < mEdgeSize; i++) {
                // top
                makeVertex(posBuf, normBuf, texBuf,
                        rowTopLeft, rowTopRight, rowUVTopLeft, rowUVTopRight,
                        i * pc);
                // left
                makeVertex(posBuf, normBuf, texBuf,
                        rowBotLeft, rowBotRight, rowUVBotLeft, rowUVBotRight,
                        i * pc);
                // bottom
                makeVertex(posBuf, normBuf, texBuf,
                        rowBotLeft, rowBotRight, rowUVBotLeft, rowUVBotRight,
                        (i + 1) * pc);
                // right
                makeVertex(posBuf, normBuf, texBuf,
                        rowTopLeft, rowTopRight, rowUVTopLeft, rowUVTopRight,
                        (i + 1) * pc);
            }
        }
    }

    private void makeVertex(List<Point3f> posBuf, List<Point3f> normBuf, List<Point2f> texBuf,
            Point3f rowTopLeft, Point3f rowTopRight,
            Point2f rowUVTopLeft, Point2f rowUVTopRight,
            float pc) {
        Point3f pixTopV = Point3fLogic.interpolate(rowTopLeft, rowTopRight, pc);
        Point3fLogic.normalize(pixTopV);
        Point2f pixTopUV = Point2fLogic.interpolate(rowUVTopLeft, rowUVTopRight, pc);
        normBuf.add(pixTopV);
        pixTopV.scale(mRadius);
        posBuf.add(pixTopV);
        texBuf.add(pixTopUV);
        //System.out.println("pos="+pixTopV.x+","+pixTopV.y+","+pixTopV.z+"; uv="+pixTopUV.x+","+pixTopUV.y);
    }

    /**
     * sets the indices for rendering the sphere.
     */
    private void setIndexData(List<Short> idxBuf) {
        // allocate connectivity
        mTriCount = mEdgeSize * mEdgeSize * 12 * 2;

        // generate connectivity
        for (int i = 0; i < mTriCount; i += 2) {
            int base = (i / 2) * 4;
            idxBuf.add((short) (base + 0));
            idxBuf.add((short) (base + 1));
            idxBuf.add((short) (base + 2));
            idxBuf.add((short) (base + 2));
            idxBuf.add((short) (base + 3));
            idxBuf.add((short) (base + 0));
        }
    }
}
