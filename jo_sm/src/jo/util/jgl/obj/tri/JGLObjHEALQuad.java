package jo.util.jgl.obj.tri;

import jo.vecmath.Point3f;

public final class JGLObjHEALQuad extends JGLObj {

    private Point3f mTop;
    private Point3f mRight;
    private Point3f mBottom;
    private Point3f mLeft;
    private boolean mInterior;

    /**
     * Serialization only. Do not use.
     */
    public JGLObjHEALQuad() {
        setMode(QUADS);
    }

    /**
     * Create a quad with the given width and height. The quad is always created
     * in the XY plane.
     *
     * @param top
     * @param right
     * @param bottom
     * @param left
     */
    public JGLObjHEALQuad(Point3f top, Point3f right, Point3f bottom, Point3f left) {
        this();
        updateGeometry(top, right, bottom, left);
    }

    /**
     * Create a quad with the given width and height. The quad is always created
     * in the XY plane.
     *
     * @param top
     * @param right
     * @param bottom
     * @param left
     * @param flipCoords If true, the texture coordinates will be flipped along
     * the Y axis.
     */
    public JGLObjHEALQuad(Point3f top, Point3f right, Point3f bottom, Point3f left, boolean flipCoords) {
        this();
        updateGeometry(top, right, bottom, left, flipCoords);
    }

    public void updateGeometry(Point3f top, Point3f right, Point3f bottom, Point3f left) {
        updateGeometry(top, right, bottom, left, false);
    }

    public void updateGeometry(Point3f top, Point3f right, Point3f bottom, Point3f left, boolean flipCoords) {
        mTop = top;
        mRight = right;
        mBottom = bottom;
        mLeft = left;
        setVertices(new float[]{(float) mLeft.x, (float) mLeft.y, (float) mLeft.z,
            (float) mBottom.x, (float) mBottom.y, (float) mBottom.z,
            (float) mRight.x, (float) mRight.y, (float) mRight.z,
            (float) mTop.x, (float) mTop.y, (float) mTop.z
        });
        if (flipCoords) {
            setTextures(new float[]{0, 1,
                1, 1,
                1, 0,
                0, 0});
        } else {
            setTextures(new float[]{0, 0,
                1, 0,
                1, 1,
                0, 1});
        }
        setNormals(new float[]{0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1});
        if (mInterior) {
            setIndices(new short[]{0, 2, 1,
                0, 3, 2});
        } else {
            setIndices(new short[]{0, 1, 2,
                0, 2, 3});
        }
    }
}
