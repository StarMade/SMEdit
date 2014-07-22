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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegerUtils {
    private static final Logger log = Logger.getLogger(IntegerUtils.class.getName());

    public static String format(int v, int w) {
        if (w < 0) {
            return FormatUtils.rightJustify(String.valueOf(v), -w);
        } else {
            return FormatUtils.leftJustify(String.valueOf(v), w);
        }
    }

    public static int[] dup(int[] arr) {
        int[] ret = new int[arr.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    public static int parseInt(String str) {
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
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int[] toArray(Object[] objArray) {
        if (objArray == null) {
            return null;
        }
        int[] intArray = new int[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            if (objArray[i] == null) {
                intArray[i] = 0;
            } else if (objArray[i] instanceof Number) {
                intArray[i] = ((Number) objArray[i]).intValue();
            } else {
                intArray[i] = parseInt(objArray[i].toString());
            }
        }
        return intArray;
    }

    public static Object[] toArray(int[] intArray) {
        if (intArray == null) {
            return null;
        }
        Integer[] objArray = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            objArray[i] = intArray[i];
        }
        return objArray;
    }

    public static int[] fromBytes(byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            int len = ois.readInt();
            int[] ret = new int[len];
            for (int i = 0; i < len; i++) {
                ret[i] = ois.readInt();
            }
            return ret;
        } catch (IOException e) {
            log.log(Level.WARNING, "ObjectInputStream failed!", e);
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toBytes(int[] ints) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeInt(ints.length);
            for (int i = 0; i < ints.length; i++) {
                oos.writeInt(ints[i]);
            }
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            log.log(Level.WARNING, "ByteArrayOutputStream failed!", e);
            e.printStackTrace();
            return null;
        }
    }
}
