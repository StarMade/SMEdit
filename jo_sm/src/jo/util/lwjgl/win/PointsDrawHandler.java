package jo.util.lwjgl.win;

import jo.vecmath.Color4f;
import jo.vecmath.Point2f;

import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.flat.JGLFlatPoints;

import org.lwjgl.opengl.GL11;

public class PointsDrawHandler extends NodeDrawHandler {

    @Override
    public void draw(long tick, JGLNode node) {
        preDraw(tick, node);
        JGLFlatPoints p = (JGLFlatPoints) node;
        if (p.getColor() != null) {
            Color4f c = p.getColor();
            GL11.glColor4f(c.x, c.y, c.z, c.w);
        }
        GL11.glPointSize(p.getRadius());
        if (p.isAntiAlias()) {
            GL11.glEnable(GL11.GL_POINT_SMOOTH);// antialiasing
        } else {
            GL11.glDisable(GL11.GL_POINT_SMOOTH);// antialiasing
        }
        GL11.glBegin(GL11.GL_POINTS);
        for (Point2f l : p.getLocations()) {
            GL11.glVertex2f(l.x, l.y);
        }
        GL11.glEnd();
        postDraw(tick, node);
    }
}
