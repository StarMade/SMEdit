package jo.vecmath.logic;

import java.nio.FloatBuffer;

import jo.sm.logic.utils.BufferLogic;
import jo.vecmath.Tuple3f;

public class Tuple3fLogic extends MathUtils {

    public static float mag(Tuple3f v) {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static void normalize(Tuple3f v) {
        v.scale(1 / mag(v));
    }

    public static float[] toFloatArray(Tuple3f v) {
        return new float[]{v.x, v.y, v.z};
    }

    public static FloatBuffer toFloatBuffer(Tuple3f v) {
        return BufferLogic.create(toFloatArray(v));
    }

    public static String toIntString(Tuple3f v) {
        return "[" + (int) v.x + ", " + (int) v.y + ", " + (int) v.z + "]";
    }

    public static float distance(Tuple3f v, float x, float y, float z) {
        return sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) + (v.z - z) * (v.z - z));
    }

    public static float dot(Tuple3f a, Tuple3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
}
