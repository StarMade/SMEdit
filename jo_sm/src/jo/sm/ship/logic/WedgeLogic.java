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
package jo.sm.ship.logic;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class WedgeLogic {

    private static final short[] CLOCKWISE_X = {
        4, 10, 0, 8, 6, 13, 2, 11,
        5, -1, 7, 1, 5, 3, 7, -1,};
    private static final short[] CLOCKWISE_Y = {
        1, 2, 3, 0, 7, 4, 5, 6,
        10, -1, 11, 13, 10, 8, 11, -1,};
    private static final short[] CLOCKWISE_Z = {
        10, 7, 11, 1, 8, 3, 13, 5,
        0, -1, 4, 6, 0, 2, 4, -1,};

    private static final short[] REFLECT_X = {
        0, 3, 2, 1, 4, 7, 6, 5,
        10, 9, 8, 13, 12, 11, 14, 15,};
    private static final short[] REFLECT_Y = {
        4, 7, 6, 5, 0, 3, 2, 1,
        8, 9, 10, 11, 12, 13, 14, 15,};
    private static final short[] REFLECT_Z = {
        2, 1, 0, 3, 6, 5, 4, 7,
        13, 8, 11, 10, 12, 8, 14, 15,};

    public static short rotate(short ori, int rx, int ry, int rz) {
        rx = -rx;
        //ry = -ry;
        //rz = -rz;
        rx %= 4;
        ry %= 4;
        rz %= 4;
        if (rx < 0) {
            rx = 4 + rx;
        }
        if (ry < 0) {
            ry = 4 + ry;
        }
        if (rz < 0) {
            rz = 4 + rz;
        }
        ori = rotate(ori, CLOCKWISE_X, rx);
        ori = rotate(ori, CLOCKWISE_Y, ry);
        ori = rotate(ori, CLOCKWISE_Z, rz);
        return ori;
    }

    private static short rotate(short ori, short[] turns, int num) {
        while (num-- > 0) {
            if (ori < 0) {
                return ori;
            }
            ori = turns[ori];
        }
        return ori;
    }

    public static short reflect(short ori, boolean xReflect, boolean yReflect,
            boolean zReflect) {
        if (xReflect) {
            ori = REFLECT_X[ori];
        }
        if (yReflect) {
            ori = REFLECT_Y[ori];
        }
        if (zReflect) {
            ori = REFLECT_Z[ori];
        }
        return ori;
    }
}
