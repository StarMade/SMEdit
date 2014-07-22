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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ArrayUtils {

    public static boolean isTrivial(Object[] arr) {
        return (arr == null) || (arr.length == 0);
    }

    public static boolean isTrivial(Collection<?> arr) {
        return (arr == null) || (arr.isEmpty());
    }

    public static String[] toStringArray(Object[] arr) {
        if (arr == null) {
            return null;
        }
        String[] ret = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                ret[i] = null;
            } else {
                ret[i] = arr[i].toString();
            }
        }
        return ret;
    }

    public static String[] toStringArray(Collection<?> arr) {
        return toStringArray(arr.toArray());
    }

    public static String[] toStringArray(StringTokenizer st) {
        ArrayList<String> strings = new ArrayList<>();
        while (st.hasMoreTokens()) {
            strings.add(st.nextToken());
        }
        return strings.toArray(new String[0]);
    }

    public static int[] toIntArray(Object[] arr) {
        if (arr == null) {
            return null;
        }
        int[] ret = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                ret[i] = 0;
            } else {
                ret[i] = ((Number) arr[i]).intValue();
            }
        }
        return ret;
    }

    public static int[] toIntArray(Collection<?> arr) {
        return toIntArray(arr.toArray());
    }

    public static long[] toLongArray(Object[] arr) {
        if (arr == null) {
            return null;
        }
        long[] ret = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                ret[i] = 0L;
            } else {
                ret[i] = ((Number) arr[i]).longValue();
            }
        }
        return ret;
    }

    public static long[] toLongArray(Collection<?> arr) {
        return toLongArray(arr.toArray());
    }

    public static int indexOf(int[] arr, int val) {
        if (arr == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param arr
     * @param idx
     * @return
     */
    public static Object get(Collection<?> arr, int idx) {
        if (idx < 0) {
            return null;
        }
        for (Iterator<?> i = arr.iterator(); i.hasNext(); idx--) {
            Object b = i.next();
            if (idx <= 0) {
                return b;
            }
        }
        return null;
    }

    /**
     * @param ret
     * @param elements
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addAll(Collection ret, Object[] elements) {
        if (elements != null) {
            ret.addAll(Arrays.asList(elements));
        }
    }

    public static void addAll(Collection<String> ret, String[] elements) {
        if (elements != null) {
            ret.addAll(Arrays.asList(elements));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addAll(Collection ret, Iterator elements) {
        if (elements != null) {
            while (elements.hasNext()) {
                ret.add(elements.next());
            }
        }
    }

    public static void removeAll(Collection<Object> ret, Object[] elements) {
        if (elements != null) {
            for (Object element : elements) {
                ret.remove(element);
            }
        }
    }

    public static void removeAll(Collection<Object> ret, Iterator<Object> elements) {
        if (elements != null) {
            while (elements.hasNext()) {
                ret.remove(elements.next());
            }
        }
    }

    // takes an array of arrays and transposes them
    public static List<List<?>> transpose(List<List<?>> input) {
        Object[][] matrix = new Object[input.size()][];
        int max = 0;
        for (List<?> a : input) {
            max = Math.max(max, a.size());
        }
        for (int i = 0; i < input.size(); i++) {
            List<List<?>> a = new ArrayList<>();
            matrix[i] = new Object[max];
            a.toArray(matrix[i]);
        }
        List<List<?>> output = new ArrayList<>();
        for (int i = 0; i < matrix[0].length; i++) {
            List<Object> a = new ArrayList<>();
            for (Object[] matrix1 : matrix) {
                if (matrix1[i] != null) {
                    a.add(matrix1[i]);
                }
            }
            output.add(a);
        }
        return output;
    }

    /**
     * @param objs
     * @return
     */
    public static List<?> toList(Object[] objs) {
        ArrayList<Object> ret = new ArrayList<>();
        if (objs != null) {
            ret.addAll(Arrays.asList(objs));
        }
        return ret;
    }

    /**
     * @param objs
     * @return
     */
    public static List<Integer> toList(int[] objs) {
        List<Integer> ret = new ArrayList<>();
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                ret.add(objs[i]);
            }
        }
        return ret;
    }

    /**
     * @param objs
     * @return
     */
    public static Set<Object> toSet(Object[] objs) {
        Set<Object> ret = new HashSet<>();
        if (objs != null) {
            ret.addAll(Arrays.asList(objs));
        }
        return ret;
    }

    public static boolean equals(Object[] objs1, Object[] objs2) {
        if ((objs1 == null) && (objs2 == null)) {
            return true;
        }
        if ((objs1 == null) || (objs2 == null)) {
            return false;
        }
        if (objs1.length != objs2.length) {
            return false;
        }
        Set<Object> set1 = toSet(objs1);
        Set<Object> set2 = toSet(objs2);
        set1.removeAll(set2);
        return set1.isEmpty();
    }

    public static boolean isOrderedSubset(Object[] set1, Object[] set2) {
        int i1 = 0;
        int i2 = 0;
        while (i1 < set1.length) {
            if (i2 >= set2.length) {
                return false;
            }
            while ((set1[i1] != set2[i2]) && (i2 < set2.length)) {
                i2++;
            }
            if (i2 >= set2.length) {
                return false;
            }
            i1++;
            i2++;
        }
        return true;
    }

    public static int indexOf(Object[] objs, Object obj) {
        for (int i = 0; i < objs.length; i++) {
            if ((objs[i] == obj) || ((objs[i] != null) && objs[i].equals(obj))) {
                return i;
            }
        }
        return -1;
    }

    public static Object[] filter(Object[] objs, Class<?> type) {
        if (objs == null) {
            return null;
        }
        ArrayList<Object> ret = new ArrayList<>();
        for (int i = 0; i < objs.length; i++) {
            if (type.isAssignableFrom(objs[i].getClass())) {
                ret.add(objs[i]);
            }
        }
        return ret.toArray();
    }

    public static Object getRandom(List<Object> list, Random random) {
        int size = list.size();
        if (size <= 0) {
            return null;
        }
        return list.get(random.nextInt(size));
    }

    public static void shuffle(Object[] objs, Random random, int start, int len) {
        for (int i = 0; i < len; i++) {
            int j = random.nextInt(len);
            Object tmp = objs[i];
            objs[i] = objs[j];
            objs[j] = tmp;
        }
    }

    public static void shuffle(Object[] objs, Random random) {
        shuffle(objs, random, 0, objs.length);
    }

    public static long max(long[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        long ret = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > ret) {
                ret = arr[i];
            }
        }
        return ret;
    }

    public static long min(long[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        long ret = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < ret) {
                ret = arr[i];
            }
        }
        return ret;
    }

    public static int max(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int ret = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > ret) {
                ret = arr[i];
            }
        }
        return ret;
    }

    public static int min(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int ret = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < ret) {
                ret = arr[i];
            }
        }
        return ret;
    }

    public static List<?> toList(Iterator<?> iterator) {
        ArrayList<Object> ret = new ArrayList<>();
        while (iterator.hasNext()) {
            ret.add(iterator.next());
        }
        return ret;
    }

    public static void set(boolean[] arr, boolean v) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = v;
        }
    }

    public static boolean compareExactOrder(Collection<?> c1, Collection<?> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        Iterator<?> i1 = c1.iterator();
        Iterator<?> i2 = c2.iterator();
        while (i1.hasNext()) {
            if (i1.next() != i2.next()) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareAnyOrder(Collection<?> c1, Collection<?> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        for (Iterator<?> i1 = c1.iterator(); i1.hasNext();) {
            if (!c2.contains(i1.next())) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(Object[] children,
            Object child) {
        if (children == null) {
            return false;
        }
        for (Object children1 : children) {
            if ((children1 == child) || ((children1 != null) && children1.equals(child))) {
                return true;
            }
        }
        return false;
    }

    public static void reverse(Object[] list) {
        Object[] newList = new Object[list.length];
        for (int i = 0; i < list.length; i++) {
            newList[newList.length - i - 1] = list[i];
        }
        System.arraycopy(newList, 0, list, 0, list.length);
    }
}
