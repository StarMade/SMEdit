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

public class ShortUtils {

    public static short parseShort(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).shortValue();
        }
        return parseShort(o.toString());
    }

    public static short parseShort(String str) {
        try {
            if (str == null) {
                return 0;
            }
            str = str.trim();
            if (str.startsWith("+")) {
                str = str.substring(1);
            }
            int o = str.indexOf('.');
            if (o >= 0) {
                str = str.substring(0, o);
            }
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Object[] toArray(short[] shortArray) {
        if (shortArray == null) {
            return null;
        }
        Short[] objArray = new Short[shortArray.length];
        for (int i = 0; i < shortArray.length; i++) {
            objArray[i] = shortArray[i];
        }
        return objArray;
    }

    public static String toString(short[] arr) {
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

    public static short[] toShortArray(Object[] array) {
        if (array == null) {
            return null;
        }
        short[] ret = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = parseShort(array[i]);
        }
        return ret;
    }
}
