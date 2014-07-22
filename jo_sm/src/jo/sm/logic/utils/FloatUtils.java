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
package jo.sm.logic.utils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FloatUtils {

    public static final float EPSILON = 0.0001f;
    public static final float PI = (float) Math.PI;

    public static float parseFloat(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        return parseFloat(o.toString());
    }

    public static float parseFloat(String str) {
        try {
            int o = StringUtils.indexNotOf(str, "-+0123456789.");
            if (o >= 0) {
                str = str.substring(0, o);
            }
            return Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static Object[] toArray(float[] floatArray) {
        if (floatArray == null) {
            return null;
        }
        Float[] objArray = new Float[floatArray.length];
        for (int i = 0; i < floatArray.length; i++) {
            objArray[i] = floatArray[i];
        }
        return objArray;
    }

    public static boolean greaterThan(float a, float b) {
        return a - b > EPSILON;
    }

    public static boolean lessThan(float a, float b) {
        return b - a > EPSILON;
    }

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static float min(float v1, float v2) {
        if (v1 < v2) {
            return v1;
        } else {
            return v2;
        }
    }

    public static float max(float v1, float v2) {
        if (v1 < v2) {
            return v2;
        } else {
            return v1;
        }
    }

    public static float tan(float f) {
        return (float) Math.tan(f);
    }

    public static String toString(float[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static float[] toFloatArray(Object[] array) {
        if (array == null) {
            return null;
        }
        float[] ret = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = parseFloat(array[i]);
        }
        return ret;
    }
}
