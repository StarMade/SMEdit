package jo.util.lwjgl.win;

import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.flat.JGLFlatRect;

import org.lwjgl.opengl.GL11;

public class RectDrawHandler extends NodeDrawHandler {

    @Override
    public void draw(long tick, JGLNode node) {
        preDraw(tick, node);
        JGLFlatRect rect = (JGLFlatRect) node;
        if (rect.getColor() != null) {
            GL11.glColor4f(rect.getColor().x, rect.getColor().y, rect.getColor().z, rect.getColor().w);
        }
//        gl.glEnable(GL11.GL_CULL_FACE);
//        gl.glCullFace(GL11.GL_FRONT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glRectf(rect.getUpperLeft().x, rect.getUpperLeft().y, rect.getLowerRight().x, rect.getLowerRight().y);
        postDraw(tick, node);
    }
}
