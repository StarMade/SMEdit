/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/

package jo.sm.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jo.vecmath.Point2i;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class BooleanMatrix3D implements Iterable<Point3i> {

    private Map<Integer, BooleanMatrix2D> mValues;

    public BooleanMatrix3D() {
        mValues = new HashMap<>();
    }

    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    @Override
    public Iterator<Point3i> iterator() {
        return new BooleanMatrix3DIterator();
    }

    public void set(Point3i v) {
        set(v.x, v.y, v.z, true);
    }

    public boolean contains(Point3i v) {
        return get(v.x, v.y, v.z);
    }

    public void set(int i, int j, int k, boolean v) {
        BooleanMatrix2D arr = mValues.get(i);
        if (arr == null) {
            if (v) {
                arr = new BooleanMatrix2D();
                mValues.put(i, arr);
                arr.set(j, k, true);
            }
        } else {
            arr.set(j, k, v);
            if (arr.isEmpty()) {
                mValues.remove(i);
            }
        }
    }

    public boolean get(int i, int j, int k) {
        BooleanMatrix2D arr = mValues.get(i);
        if (arr == null) {
            return false;
        } else {
            return arr.get(j, k);
        }
    }

    class BooleanMatrix3DIterator implements Iterator<Point3i> {

        private int mIIdx;
        private Integer[] mIs;
        private Iterator<Point2i> mJs;

        public BooleanMatrix3DIterator() {
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
        public Point3i next() {
            Point2i next2 = mJs.next();
            Point3i ret = new Point3i(mIs[mIIdx], next2.x, next2.y);
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
