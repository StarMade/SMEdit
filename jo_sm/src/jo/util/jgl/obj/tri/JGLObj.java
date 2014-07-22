package jo.util.jgl.obj.tri;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.Iterator;

import jo.sm.logic.utils.FloatUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.util.jgl.obj.JGLNode;
import jo.vecmath.Color3f;
import jo.vecmath.Color4f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.Tuple3f;
import jo.vecmath.Tuple4f;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Matrix4fLogic;

/*
 * Triangle Based Mesh object
 */
public class JGLObj extends JGLNode {

    public static final int TRIANGLES = 0;
    public static final int QUADS = 1;

    protected FloatBuffer mVertexBuffer;
    protected FloatBuffer mNormalBuffer;
    protected FloatBuffer mTexturesBuffer;
    protected FloatBuffer mColorBuffer;
    protected ShortBuffer mIndexShortBuffer;
    protected IntBuffer mIndexIntBuffer;

    protected int mTextureID;
    protected int mMode; // TRIANGLES, QUADS, ...
    private Color4f mTextureColor;

    protected boolean mWireframe;
    protected Color3f mWireColor;
    private boolean mAnyAlpha;
    protected int mVertices;
    protected int mIndices;
    protected int mColors;

    public JGLObj() {
        setInitialized(false);
        mAnyAlpha = false;
    }

    @Override
    public void recycle() {
        synchronized (this) {
            if (mVertexBuffer != null) {
                mVertexBuffer.clear();
            }
            if (mNormalBuffer != null) {
                mNormalBuffer.clear();
            }
            if (mTexturesBuffer != null) {
                mTexturesBuffer.clear();
            }
            if (mColorBuffer != null) {
                mColorBuffer.clear();
            }
            if (mIndexShortBuffer != null) {
                mIndexShortBuffer.clear();
            }
            if (mIndexIntBuffer != null) {
                mIndexIntBuffer.clear();
            }
        }
    }

    public boolean isTextured() {
        return ((mTextureID != 0) && (mTexturesBuffer != null));
    }

    // Buffers to be passed to gl*Pointer() functions
    // must be direct, i.e., they must be placed on the
    // native heap where the garbage collector cannot
    // move them.
    //
    // Buffers with multi-byte datatypes (e.g., short, int, float)
    // must have their byte order set to native order
    public void setVertices(float[] vertices) {
        synchronized (this) {
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVertexBuffer = vbb.asFloatBuffer();
            mVertexBuffer.put(vertices);
            mVertexBuffer.position(0);
            mLowBounds = new Point3f(vertices[0], vertices[1], vertices[2]);
            mHighBounds = new Point3f(mLowBounds);
            for (int i = 3; i < vertices.length; i += 3) {
                mLowBounds.x = Math.min(mLowBounds.x, vertices[i + 0]);
                mLowBounds.y = Math.min(mLowBounds.y, vertices[i + 1]);
                mLowBounds.z = Math.min(mLowBounds.z, vertices[i + 2]);
                mHighBounds.x = Math.max(mHighBounds.x, vertices[i + 0]);
                mHighBounds.y = Math.max(mHighBounds.y, vertices[i + 1]);
                mHighBounds.z = Math.max(mHighBounds.z, vertices[i + 2]);
            }
            mVertices = vertices.length / 3;
        }
    }

    public void setVertices(Collection<?> vertices) {
        Object first = vertices.iterator().next();
        if (first instanceof Point3f) {
            float[] cs = new float[vertices.size() * 3];
            int idx = 0;
            for (Iterator<?> i = vertices.iterator(); i.hasNext();) {
                Point3f p = (Point3f) i.next();
                cs[idx++] = (float) p.x;
                cs[idx++] = (float) p.y;
                cs[idx++] = (float) p.z;
            }
            setVertices(cs);
        } else {
            setVertices(FloatUtils.toFloatArray(vertices.toArray()));
        }
    }

    public void setNormals(float[] normals) {
        synchronized (this) {
            ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
            nbb.order(ByteOrder.nativeOrder());
            mNormalBuffer = nbb.asFloatBuffer();
            mNormalBuffer.put(normals);
            mNormalBuffer.position(0);
        }
    }

    public void setNormals(Collection<?> normals) {
        Object first = normals.iterator().next();
        if (first instanceof Point3f) {
            float[] cs = new float[normals.size() * 3];
            int idx = 0;
            for (Iterator<?> i = normals.iterator(); i.hasNext();) {
                Point3f p = (Point3f) i.next();
                cs[idx++] = (float) p.x;
                cs[idx++] = (float) p.y;
                cs[idx++] = (float) p.z;
            }
            setNormals(cs);
        } else {
            setNormals(FloatUtils.toFloatArray(normals.toArray()));
        }
    }

    public void setTextures(float[] textures) {
        synchronized (this) {
            ByteBuffer nbb = ByteBuffer.allocateDirect(textures.length * 4);
            nbb.order(ByteOrder.nativeOrder());
            mTexturesBuffer = nbb.asFloatBuffer();
            mTexturesBuffer.put(textures);
            mTexturesBuffer.position(0);
        }
    }

    public void setTextures(Collection<?> textures) {
        Object first = textures.iterator().next();
        if (first instanceof Point2f) {
            float[] cs = new float[textures.size() * 2];
            int idx = 0;
            for (Iterator<?> i = textures.iterator(); i.hasNext();) {
                Point2f p = (Point2f) i.next();
                cs[idx++] = (float) p.x;
                cs[idx++] = (float) p.y;
            }
            setTextures(cs);
        } else {
            setTextures(FloatUtils.toFloatArray(textures.toArray()));
        }
    }

    public void setColors(float[] colors) {
        synchronized (this) {
            ByteBuffer c2bb = ByteBuffer.allocateDirect(colors.length * 4);
            c2bb.order(ByteOrder.nativeOrder());
            mColorBuffer = c2bb.asFloatBuffer();
            mColorBuffer.put(colors);
            mColorBuffer.position(0);
            mColors = colors.length / 4;
            for (int i = 0; i < colors.length; i += 4) {
                if (!MathUtils.epsilonEquals(colors[i + 3], 1)) {
                    mAnyAlpha = true;
                    break;
                }
            }
        }
    }

    public void setColors(Collection<?> colors) {
        Object first = colors.iterator().next();
        if (first instanceof Tuple4f) {
            float[] cs = new float[colors.size() * 4];
            int idx = 0;
            for (Iterator<?> i = colors.iterator(); i.hasNext();) {
                Tuple4f p = (Tuple4f) i.next();
                cs[idx++] = (float) p.x;
                cs[idx++] = (float) p.y;
                cs[idx++] = (float) p.z;
                cs[idx++] = (float) p.w;
            }
            setColors(cs);
        } else if (first instanceof Tuple3f) {
            float[] cs = new float[colors.size() * 4];
            int idx = 0;
            for (Iterator<?> i = colors.iterator(); i.hasNext();) {
                Tuple3f p = (Tuple3f) i.next();
                cs[idx++] = (float) p.x;
                cs[idx++] = (float) p.y;
                cs[idx++] = (float) p.z;
                cs[idx++] = 1;
            }
            setColors(cs);
        } else {
            setColors(FloatUtils.toFloatArray(colors.toArray()));
        }
    }

    public void setIndices(short[] indices) {
        synchronized (this) {
            ByteBuffer i2bb = ByteBuffer.allocateDirect(indices.length * 4);
            i2bb.order(ByteOrder.nativeOrder());
            mIndexShortBuffer = i2bb.asShortBuffer();
            for (int i = 0; i < indices.length; i++) {
                mIndexShortBuffer.put(indices[i]);
            }
            mIndexShortBuffer.position(0);
            if (mMode == TRIANGLES) {
                mIndices = indices.length / 3;
            } else {
                mIndices = indices.length / 4;
            }
        }
    }

    public void setIndices(int[] indices) {
        synchronized (this) {
            ByteBuffer i2bb = ByteBuffer.allocateDirect(indices.length * 4);
            i2bb.order(ByteOrder.nativeOrder());
            mIndexIntBuffer = i2bb.asIntBuffer();
            for (int i = 0; i < indices.length; i++) {
                mIndexIntBuffer.put(indices[i]);
            }
            mIndexIntBuffer.position(0);
            if (mMode == TRIANGLES) {
                mIndices = indices.length / 3;
            } else {
                mIndices = indices.length / 4;
            }
        }
    }

    public void setIndices(Collection<?> normals) {
        Object[] arr = normals.toArray();
        if (arr[0] instanceof Short) {
            setIndices(ShortUtils.toShortArray(arr));
        } else if (arr[0] instanceof Integer) {
            setIndices(IntegerUtils.toArray(arr));
        }
    }

    public void setSolidColor(Point3f c) {
        setSolidColor((float) c.x, (float) c.y, (float) c.z, 1);
    }

    public void setSolidColor(Color4f c) {
        setSolidColor((float) c.x, (float) c.y, (float) c.z, (float) c.w);
    }

    public void setSolidColor(float r, float g, float b) {
        setSolidColor(r, g, b, 1);
    }

    public void setSolidColor(float r, float g, float b, float a) {
        float colors[] = new float[mVertices * 4];
        for (int i = 0; i < colors.length; i += 4) {
            colors[i + 0] = r;
            colors[i + 1] = g;
            colors[i + 2] = b;
            colors[i + 3] = a;
        }
        setColors(colors);
    }

    public void setFadeColor(Color4f c1, Color4f c2) {
        setFadeColor((float) c1.x, (float) c1.y, (float) c1.z, (float) c1.w, (float) c2.x, (float) c2.y, (float) c2.z, (float) c2.w);
    }

    public void setFadeColor(Point3f c1, Point3f c2) {
        setFadeColor((float) c1.x, (float) c1.y, (float) c1.z, 1, (float) c2.x, (float) c2.y, (float) c2.z, 1);
    }

    public void setFadeColor(float r1, float g1, float b1, float r2, float g2, float b2) {
        setFadeColor(r1, g1, b1, 1, r2, g2, b2, 1);
    }

    // assumes all -x indicies are first and all +x indicies are second
    public void setFadeColor(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        float colors1[] = {
            r1, g1, b1, a1,};
        float colors2[] = {
            r2, g2, b2, a2,};
        mColorBuffer.position(0);
        for (int i = 0; i < mColors / 2; i++) {
            mColorBuffer.put(colors1);
        }
        for (int i = 0; i < mColors / 2; i++) {
            mColorBuffer.put(colors2);
        }
        mColorBuffer.position(0);
    }

    // assumes object's central axis goes from -.5z to +.5z
    public void connect(Point3f top, Point3f bottom, float diameter) {
        Point3f at = new Point3f(top);
        at.add(bottom);
        at.scale(.5f);
        Matrix4f lookAt = new Matrix4f();
        Matrix4fLogic.lookAt(lookAt, at, top);
        Matrix4f scale = Matrix4fLogic.makeScaleMatrix(diameter, diameter, top.distance(bottom));
        getTransform().set(lookAt);
        getTransform().mul(scale);
    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public void setVertexBuffer(FloatBuffer vertexBuffer) {
        mVertexBuffer = vertexBuffer;
        mVertices = mVertexBuffer.limit() / 3;
    }

    public FloatBuffer getNormalBuffer() {
        return mNormalBuffer;
    }

    public void setNormalBuffer(FloatBuffer normalBuffer) {
        mNormalBuffer = normalBuffer;
    }

    public FloatBuffer getTexturesBuffer() {
        return mTexturesBuffer;
    }

    public void setTexturesBuffer(FloatBuffer texturesBuffer) {
        mTexturesBuffer = texturesBuffer;
    }

    public FloatBuffer getColorBuffer() {
        return mColorBuffer;
    }

    public void setColorBuffer(FloatBuffer colorBuffer) {
        mColorBuffer = colorBuffer;
        mColors = mColorBuffer.limit() / 4;
    }

    public ShortBuffer getIndexShortBuffer() {
        return mIndexShortBuffer;
    }

    public void setIndexShortBuffer(ShortBuffer indexBuffer) {
        mIndexShortBuffer = indexBuffer;
        if (mMode == TRIANGLES) {
            mIndices = mIndexShortBuffer.limit() / 3;
        } else {
            mIndices = mIndexShortBuffer.limit() / 4;
        }
    }

    public IntBuffer getIndexIntBuffer() {
        return mIndexIntBuffer;
    }

    public void setIndexIntBuffer(IntBuffer indexBuffer) {
        mIndexIntBuffer = indexBuffer;
        if (mMode == TRIANGLES) {
            mIndices = mIndexIntBuffer.limit() / 3;
        } else {
            mIndices = mIndexIntBuffer.limit() / 4;
        }
    }

    public int getIndices() {
        return mIndices;
    }

    public void setIndices(int indices) {
        mIndices = indices;
    }

    public int getColors() {
        return mColors;
    }

    public void setColors(int colors) {
        mColors = colors;
    }

    public int getVertices() {
        return mVertices;
    }

    public void setVertices(int vertices) {
        mVertices = vertices;
    }

    public int getTextureID() {
        return mTextureID;
    }

    public void setTextureID(int textureID) {
        mTextureID = textureID;
    }

    public boolean isWireframe() {
        return mWireframe;
    }

    public void setWireframe(boolean wireframe) {
        mWireframe = wireframe;
    }

    public Color3f getWireColor() {
        return mWireColor;
    }

    public void setWireColor(Color3f wireColor) {
        mWireColor = wireColor;
    }

    public boolean isAnyAlpha() {
        return mAnyAlpha;
    }

    public void setAnyAlpha(boolean anyAlpha) {
        mAnyAlpha = anyAlpha;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public Color4f getTextureColor() {
        return mTextureColor;
    }

    public void setTextureColor(Color4f textureColor) {
        mTextureColor = textureColor;
    }
}
