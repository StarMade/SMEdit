package jo.sm.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jo.vecmath.Point2i;

public class BooleanMatrix2D implements Iterable<Point2i> {

    private Map<Integer, RLEBooleanArray> mValues;

    public BooleanMatrix2D() {
        mValues = new HashMap<>();
    }

    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    @Override
    public Iterator<Point2i> iterator() {
        return new BooleanMatrix2DIterator();
    }

    public void set(int i, int j, boolean v) {
        RLEBooleanArray arr = mValues.get(i);
        if (arr == null) {
            if (v) {
                arr = new RLEBooleanArray();
                mValues.put(i, arr);
                arr.set(j, true);
            }
        } else {
            arr.set(j, v);
            if (arr.isEmpty()) {
                mValues.remove(i);
            }
        }
    }

    public boolean get(int i, int j) {
        RLEBooleanArray arr = mValues.get(i);
        if (arr == null) {
            return false;
        } else {
            return arr.get(j);
        }
    }

    class BooleanMatrix2DIterator implements Iterator<Point2i> {

        private int mIIdx;
        private Integer[] mIs;
        private Iterator<Integer> mJs;

        public BooleanMatrix2DIterator() {
            mIs = mValues.keySet().toArray(new Integer[0]);
            Arrays.sort(mIs);
            mIIdx = 0;
            if (mIs.length == 0) {
                mJs = null;
            } else {
                mJs = mValues.get(mIs[mIIdx]).iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return mJs != null;
        }

        @Override
        public Point2i next() {
            Point2i ret = new Point2i(mIs[mIIdx], mJs.next());
            if (!mJs.hasNext()) {
                mIIdx++;
                if (mIIdx >= mIs.length) {
                    mJs = null;
                } else {
                    mJs = mValues.get(mIs[mIIdx]).iterator();
                }
            }
            return ret;
        }

        @Override
        public void remove() {
            throw new IllegalStateException("Not implemented");
        }

    }
}
