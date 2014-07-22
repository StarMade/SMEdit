/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jo.vecmath.Color3f;
import jo.vecmath.Point3f;

public class JGLUtils {

    public static final int one = 0x10000;

    public static List<Color3f> rndColors(int vertexCount, Color3f baseColor, Color3f deltaColor) {
        List<Color3f> colorArray = new ArrayList<>(vertexCount);
        Random r = new Random();
        for (int i = 0; i < vertexCount; i++) {
            colorArray.add(JGLUtils.rndColor(r, baseColor, deltaColor));
        }
        return colorArray;
    }

    public static Color3f rndColor(Random r, Color3f baseColor, Color3f deltaColor) {
        Color3f color = new Color3f();
        color.x = JGLUtils.rndColor(r, baseColor.x, deltaColor.x);
        color.y = JGLUtils.rndColor(r, baseColor.y, deltaColor.y);
        color.z = JGLUtils.rndColor(r, baseColor.z, deltaColor.z);
        return color;
    }

    public static float rndColor(Random r, float base, float variance) {
        if (variance > 0) {
            base += r.nextFloat() * 2 * variance - variance;
        }
        if (base < 0) {
            base = 0;
        } else if (base > 1.0f) {
            base = 1.0f;
        }
        return base;
    }

    public static float[] rndColorsf(int vertexCount, Point3f baseColor, Point3f deltaColor) {
        float[] colorArray = new float[vertexCount * 4];
        Random r = new Random();
        for (int i = 0; i < colorArray.length; i += 4) {
            colorArray[i + 0] = rndColorf(r, baseColor.x, deltaColor.x);
            colorArray[i + 1] = rndColorf(r, baseColor.y, deltaColor.y);
            colorArray[i + 2] = rndColorf(r, baseColor.z, deltaColor.z);
            colorArray[i + 3] = 1;
        }
        return colorArray;
    }

    public static float rndColorf(Random r, float base, float variance) {
        if (variance > 0) {
            base += r.nextDouble() * 2 * variance - variance;
        }
        if (base < 0) {
            base = 0;
        } else if (base > 1) {
            base = 1;
        }
        return base;
    }

    public static FloatBuffer makeFloatBuffer(List<Float> arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        for (Float arr1 : arr) {
            fb.put(arr1);
        }
        fb.position(0);
        return fb;
    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    public static ShortBuffer makeShortBuffer(List<Short> arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        for (Short arr1 : arr) {
            ib.put(arr1);
        }
        ib.position(0);
        return ib;
    }

    public static ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public static IntBuffer makeIntBuffer(List<Integer> arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        for (Integer arr1 : arr) {
            ib.put(arr1);
        }
        ib.position(0);
        return ib;
    }

    public static IntBuffer makeIntBuffer(int[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }
}
