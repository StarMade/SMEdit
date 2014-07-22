package jo.util.jgl.obj.tri;

import jo.util.jgl.obj.JGLGroup;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;
import jo.vecmath.logic.Point3fLogic;

public class JGLObjHEALPixQuads extends JGLGroup {

    protected int mEdgeSize;
    protected JGLObjHEALQuad[][][] mQuads;

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

    public JGLObjHEALPixQuads(Point3f size, Point3f center) {
        this(2);
        Matrix4fLogic.translate(getTransform(), center);
        Matrix4fLogic.scale(getTransform(), size);
    }

    public JGLObjHEALPixQuads(int depth) {
        init(depth);
    }

    private void init(int zSamples) {
        mEdgeSize = zSamples;
        mQuads = new JGLObjHEALQuad[12][mEdgeSize][mEdgeSize];
        // generate geometry
        for (int face = 0; face < 12; face++) {
            //System.out.println("Square "+face);
            makeSquare(face,
                    V_FACES[face][0], V_FACES[face][3], V_FACES[face][2], V_FACES[face][1]
            );
        }
    }

    private void makeSquare(int square,
            Point3f top, Point3f right, Point3f bottom, Point3f left) {
        float pc = 1.0f / mEdgeSize;
        for (int j = 0; j < mEdgeSize; j++) {
            Point3f rowTopLeft = Point3fLogic.interpolate(top, left, j * pc);
            Point3f rowBotLeft = Point3fLogic.interpolate(top, left, (j + 1) * pc);
            Point3f rowTopRight = Point3fLogic.interpolate(right, bottom, j * pc);
            Point3f rowBotRight = Point3fLogic.interpolate(right, bottom, (j + 1) * pc);
            for (int i = 0; i < mEdgeSize; i++) {
                Point3f qtop = makeVertex(rowTopLeft, rowTopRight, i * pc);
                Point3f qright = makeVertex(rowTopLeft, rowTopRight, (i + 1) * pc);
                Point3f qbottom = makeVertex(rowBotLeft, rowBotRight, (i + 1) * pc);
                Point3f qleft = makeVertex(rowBotLeft, rowBotRight, i * pc);
                JGLObjHEALQuad quad = new JGLObjHEALQuad(qtop, qright, qbottom, qleft);
                quad.setID(square + ":" + j + ":" + i);
                mQuads[square][j][i] = quad;
                add(quad);
            }
        }
    }

    private Point3f makeVertex(Point3f rowTopLeft, Point3f rowTopRight, float pc) {
        Point3f pixTopV = Point3fLogic.interpolate(rowTopLeft, rowTopRight, pc);
        Point3fLogic.normalize(pixTopV);
        //System.out.println("pos="+pixTopV.x+","+pixTopV.y+","+pixTopV.z+"; uv="+pixTopUV.x+","+pixTopUV.y);
        return pixTopV;
    }

    public void setTextureID(int textureID) {
        for (JGLObjHEALQuad[][] mQuad : mQuads) {
            for (JGLObjHEALQuad[] mQuad1 : mQuad) {
                for (JGLObjHEALQuad mQuad11 : mQuad1) {
                    mQuad11.setTextureID(textureID);
                }
            }
        }
    }

    public JGLObjHEALQuad getQuad(int square, int i, int j) {
        return mQuads[square][i][j];
    }
}
