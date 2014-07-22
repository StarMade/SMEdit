package jo.util.lwjgl.win;

import java.nio.FloatBuffer;

import jo.vecmath.Matrix4f;

import jo.util.jgl.obj.JGLNode;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ParticleDrawHandler extends ObjDrawHandler {

    @Override
    protected Matrix4f preDraw(long tick, JGLNode obj) {
        Matrix4f m = super.preDraw(tick, obj);
        FloatBuffer mm = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, mm);
        mm.put(0, 1);
        mm.put(1, 0);
        mm.put(2, 0);
        mm.put(4, 0);
        mm.put(5, 1);
        mm.put(6, 0);
        mm.put(8, 0);
        mm.put(9, 0);
        mm.put(10, 1);
        GL11.glLoadMatrix(mm);
        return m;
    }
}
