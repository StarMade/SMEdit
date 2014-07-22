/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl.obj;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import jo.vecmath.Matrix3f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Tuple3f;
import jo.vecmath.Vector3f;
import jo.vecmath.logic.ITransformer;

public class JGLNode {
    private static final Logger LOG = Logger.getLogger(JGLNode.class.getName());

    private String mID;
    Matrix4f mTransform;
    private Matrix3f mRotation;
    protected Vector3f mTranslation;
    protected float mScale;
    private ITransformer mTransformer;
    private JGLNode mParent;
    private boolean mCull;
    private boolean mInitialized;
    private Point3f mScreen;
    private Point3f mScreenLowBounds;
    private Point3f mScreenHighBounds;
    protected Point3f mLowBounds;
    protected Point3f mHighBounds;
    private Map<Object, Object> mData;

    public JGLNode() {
        mTransform = new Matrix4f();
        mTransform.setIdentity();
        mRotation = new Matrix3f();
        mTranslation = new Vector3f();
        decomposeTransform();
    }

    public void recycle() {
    }

    public void decomposeTransform() {
        mScale = getmTransform().get(getmRotation(), mTranslation);
    }

    public void composeTransform() {
        getmTransform().set(getmRotation(), mTranslation, mScale);
    }

    public Vector3f getLeftDir() {
        getRotation();
        Vector3f v = new Vector3f();
        getmRotation().getColumn(0, v);
        return v;
    }

    public void setLeftDir(Tuple3f v) {
        getRotation();
        getmRotation().setColumn(0, v.x, v.y, v.z);
        setRotation(getmRotation());
    }

    public Vector3f getUpDir() {
        getRotation();
        Vector3f v = new Vector3f();
        getmRotation().getColumn(1, v);
        return v;
    }

    public void setUpDir(Tuple3f v) {
        getRotation();
        getmRotation().setColumn(1, v.x, v.y, v.z);
        setRotation(getmRotation());
    }

    public Vector3f getForwardDir() {
        getRotation();
        Vector3f v = new Vector3f();
        getmRotation().getColumn(2, v);
        return v;
    }

    public void setForwardDir(Tuple3f v) {
        getRotation();
        getmRotation().setColumn(2, v.x, v.y, v.z);
        setRotation(getmRotation());
    }

    public void setData(Object key, Object val) {
        if (mData == null) {
            mData = new HashMap<>();
        }
        mData.put(key, val);
    }

    public Object getData(Object key) {
        if (mData == null) {
            return null;
        }
        return mData.get(key);
    }

    @Override
    public String toString() {
        return mID;
    }

    public void init() {
        mInitialized = true;
    }

    public Matrix4f calcTransform(long tick) {
        Matrix4f t = new Matrix4f(getTransform());
        if (mTransformer != null) {
            t = mTransformer.calcTransform(t);
        }
        //if (mParent != null)
        //    t.mult(mParent.calcTransform(tick));
        return t;
    }

    public String getID() {
        return mID;
    }

    public void setID(String iD) {
        mID = iD;
    }

    public Matrix4f getTransform() {
        return getmTransform();
    }

    public void setTransform(Matrix4f transform) {
        setmTransform(transform);
    }

    public JGLNode getParent() {
        return mParent;
    }

    public void setParent(JGLNode parent) {
        mParent = parent;
    }

    public boolean isCull() {
        return mCull;
    }

    public void setCull(boolean cull) {
        mCull = cull;
    }

    public ITransformer getTransformer() {
        return mTransformer;
    }

    public void setTransformer(ITransformer transforms) {
        mTransformer = transforms;
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public void setInitialized(boolean initialized) {
        mInitialized = initialized;
    }

    public Point3f getScreenLowBounds() {
        return mScreenLowBounds;
    }

    public void setScreenLowBounds(Point3f screenLowBounds) {
        mScreenLowBounds = screenLowBounds;
    }

    public Point3f getScreenHighBounds() {
        return mScreenHighBounds;
    }

    public void setScreenHighBounds(Point3f screenHighBounds) {
        mScreenHighBounds = screenHighBounds;
    }

    public Point3f getLowBounds() {
        return mLowBounds;
    }

    public void setLowBounds(Point3f lowBounds) {
        mLowBounds = lowBounds;
    }

    public Point3f getHighBounds() {
        return mHighBounds;
    }

    public void setHighBounds(Point3f highBounds) {
        mHighBounds = highBounds;
    }

    public Point3f getScreen() {
        return mScreen;
    }

    public void setScreen(Point3f screen) {
        mScreen = screen;
    }

    public Matrix3f getRotation() {
        decomposeTransform();
        return getmRotation();
    }

    public void setRotation(Matrix3f rotation) {
        setmRotation(rotation);
        composeTransform();
    }

    public Vector3f getTranslation() {
        decomposeTransform();
        return mTranslation;
    }

    public void setTranslation(Vector3f translation) {
        mTranslation = translation;
        composeTransform();
    }

    public float getScale() {
        decomposeTransform();
        return mScale;
    }

    public void setScale(float scale) {
        mScale = scale;
        composeTransform();
    }

    /**
     * @return the mTransform
     */
    public Matrix4f getmTransform() {
        return mTransform;
    }

    /**
     * @param mTransform the mTransform to set
     */
    public void setmTransform(Matrix4f mTransform) {
        this.mTransform = mTransform;
    }

    /**
     * @return the mRotation
     */
    public Matrix3f getmRotation() {
        return mRotation;
    }

    /**
     * @param mRotation the mRotation to set
     */
    public void setmRotation(Matrix3f mRotation) {
        this.mRotation = mRotation;
    }
}
