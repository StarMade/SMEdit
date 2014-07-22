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
public class ConvertLogic {

    public static Object toObject(String sVal, Class<?> propertyType) {
        Object oVal = null;
        if (sVal != null) {
            if ((propertyType == boolean.class) || (propertyType == Boolean.class)) {
                try {
                    oVal = Boolean.parseBoolean(sVal);
                } catch (Exception e) {
                    oVal = Boolean.FALSE;
                }
            } else if ((propertyType == byte.class) || (propertyType == Byte.class)) {
                try {
                    oVal = Byte.parseByte(sVal);
                } catch (NumberFormatException e) {
                    oVal = (byte) 0;
                }
            } else if ((propertyType == short.class) || (propertyType == Short.class)) {
                try {
                    oVal = Short.parseShort(sVal);
                } catch (NumberFormatException e) {
                    oVal = (short) 0;
                }
            } else if ((propertyType == int.class) || (propertyType == Integer.class)) {
                try {
                    oVal = Integer.parseInt(sVal);
                } catch (NumberFormatException e) {
                    oVal = 0;
                }
            } else if ((propertyType == long.class) || (propertyType == Long.class)) {
                try {
                    oVal = Long.parseLong(sVal);
                } catch (NumberFormatException e) {
                    oVal = 0L;
                }
            } else if ((propertyType == float.class) || (propertyType == Float.class)) {
                try {
                    oVal = Float.parseFloat(sVal);
                } catch (NumberFormatException e) {
                    oVal = 0f;
                }
            } else if ((propertyType == double.class) || (propertyType == Double.class)) {
                try {
                    oVal = Double.parseDouble(sVal);
                } catch (NumberFormatException e) {
                    oVal = 0.0;
                }
            } else if ((propertyType == char.class) || (propertyType == Character.class)) {
                if (sVal.trim().length() > 0) {
                    oVal = sVal.trim().charAt(0);
                }
            } else if (propertyType == String.class) {
                oVal = sVal;
            } else {
                throw new IllegalArgumentException("Cannot handle converting type '" + propertyType.getName() + "'");
            }
        }
        return oVal;
    }
}
