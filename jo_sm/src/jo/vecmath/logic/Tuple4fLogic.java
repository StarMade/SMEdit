package jo.vecmath.logic;

import java.nio.FloatBuffer;

import jo.sm.logic.utils.BufferLogic;
import jo.vecmath.Tuple4f;

public class Tuple4fLogic extends MathUtils {

    public static float[] toFloatArray(Tuple4f v) {
        return new float[]{v.x, v.y, v.z, v.w};
    }

    public static FloatBuffer toFloatBuffer(Tuple4f v) {
        return BufferLogic.create(toFloatArray(v));
    }
}
