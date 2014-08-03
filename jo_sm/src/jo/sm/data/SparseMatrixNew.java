
package jo.sm.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.vecmath.Point3i;
import jo.vecmath.Point3s;
import jo.vecmath.Tuple3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public final class SparseMatrixNew<T> {

    private Map<Long, T> mMatrix;
    private Point3i mLower;
    private Point3i mUpper;

    public SparseMatrixNew() {
        mMatrix = new HashMap<>();
        mLower = null;
        mUpper = null;
    }

    public SparseMatrixNew(SparseMatrixNew<T> original) {
        this();
        set(original);
        mLower = new Point3i();
        mUpper = new Point3i();
        original.getBounds(mLower, mUpper);
    }

    public void addAll(SparseMatrixNew<T> original) {
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext();) {
            Point3i p = i.next();
            set(p, original.get(p));
        }
    }

    public void set(SparseMatrixNew<T> original) {
        mMatrix.clear();
        addAll(original);
    }

    public void set(int x, int y, int z, T val) {
        long idx = Tuple3i.hashCode(x, y, z);
        if (val == null) {
            mMatrix.remove(idx);
        } else {
            mMatrix.put(idx, val);
        }
        if (mLower == null) {
            mLower = new Point3i(x, y, z);
        } else {
            mLower.x = Math.min(mLower.x, x);
            mLower.y = Math.min(mLower.y, y);
            mLower.z = Math.min(mLower.z, z);
        }
        if (mUpper == null) {
            mUpper = new Point3i(x, y, z);
        } else {
            mUpper.x = Math.max(mUpper.x, x);
            mUpper.y = Math.max(mUpper.y, y);
            mUpper.z = Math.max(mUpper.z, z);
        }
    }

    public T get(int x, int y, int z) {
        long idx = Tuple3i.hashCode(x, y, z);
        return mMatrix.get(idx);
    }

    public boolean contains(int x, int y, int z) {
        return get(x, y, z) != null;
    }

    public void set(Point3i v, T val) {
        set(v.x, v.y, v.z, val);
    }

    public T get(Point3i v) {
        return get(v.x, v.y, v.z);
    }

    public T get(Point3s v) {
        return get(v.x, v.y, v.z);
    }

    public boolean contains(Point3i v) {
        return get(v.x, v.y, v.z) != null;
    }

    public void getBounds(Point3i lower, Point3i upper) {
        lower.set(mLower);
        upper.set(mUpper);
    }

    public Iterator<Point3i> iterator() {
        return new CubeIterator(mLower, mUpper);
    }

    public Iterator<Point3i> iteratorNonNull() {
        List<Point3i> points = new ArrayList<>();
        for (Iterator<Point3i> i = iterator(); i.hasNext();) {
            Point3i p = i.next();
            if (contains(p)) {
                points.add(p);
            }
        }
        return points.iterator();
    }

    public int size() {
        return mMatrix.size();
    }
}
