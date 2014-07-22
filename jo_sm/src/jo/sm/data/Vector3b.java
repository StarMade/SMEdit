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

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class Vector3b {

    public Vector3b() {
    }

    public Vector3b(byte byte0, byte byte1, byte byte2) {
        a = byte0;
        b = byte1;
        c = byte2;
    }

    public Vector3b(float f, float f1, float f2) {
        a = (byte) (int) f;
        b = (byte) (int) f1;
        c = (byte) (int) f2;
    }

    public Vector3b(Vector3b o1) {
        a = o1.a;
        b = o1.b;
        c = o1.c;
    }

    public final void a(byte byte0, byte byte1, byte byte2) {
        a += byte0;
        b += byte1;
        c += byte2;
    }

    public final void a(Vector3b o1) {
        a += o1.a;
        b += o1.b;
        c += o1.c;
    }

    public final void a() {
        a /= 2;
        b /= 2;
        c /= 2;
    }

    public boolean equals(Object obj) {
        try {
            obj = (Vector3b) obj;
            return a == ((Vector3b) (obj)).a && b == ((Vector3b) (obj)).b && c == ((Vector3b) (obj)).c;
        } catch (NullPointerException _ex) {
            return false;
        } catch (ClassCastException _ex) {
            return false;
        }
    }

    public int hashCode() {
        long l = 7L + (long) a;
        l = 7L * l + (long) b;
        long l1 = 7L * l + (long) c;
        return (byte) (int) (l1 ^ l1 >> 8);
    }

    public final void b(byte byte0, byte byte1, byte byte2) {
        a = byte0;
        b = byte1;
        c = byte2;
    }

    public final void b(Vector3b o1) {
        b(o1.a, o1.b, o1.c);
    }

    public final void c(Vector3b o1) {
        a -= o1.a;
        b -= o1.b;
        c -= o1.c;
    }

    public String toString() {
        return (new StringBuilder("(")).append(a).append(", ").append(b).append(", ").append(c).append(")").toString();
    }

    public byte a;
    public byte b;
    public byte c;
}
