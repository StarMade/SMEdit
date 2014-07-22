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
public class CornerLogic {

    private static final short[] CLOCKWISE_X = {
        4, 0, 3, 7, 5, 1, 2, 6,
        -1, -1, -1, -1, -1, -1, -1, -1,};
    private static final short[] CLOCKWISE_Y = {
        3, 0, 1, 2, 7, 4, 5, 6,
        -1, -1, -1, -1, -1, -1, -1, -1,};
    private static final short[] CLOCKWISE_Z = {
        3, 2, 6, 7, 0, 1, 5, 4,
        -1, -1, -1, -1, -1, -1, -1, -1,};

    private static final short[] REFLECT_X = {
        3, 2, 1, 0, 7, 6, 5, 4,
        -1, -1, -1, -1, -1, -1, -1, -1,};
    private static final short[] REFLECT_Y = {
        4, 5, 6, 7, 0, 1, 2, 3,
        -1, -1, -1, -1, -1, -1, -1, -1,};
    private static final short[] REFLECT_Z = {
        1, 0, 3, 2, 5, 4, 7, 6,
        -1, -1, -1, -1, -1, -1, -1, -1,};

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
