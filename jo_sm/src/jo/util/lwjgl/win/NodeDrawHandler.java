package jo.util.lwjgl.win;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jo.sm.logic.utils.BufferLogic;
import jo.util.jgl.obj.JGLNode;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Matrix4fLogic;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class NodeDrawHandler implements IDrawHandler {

    protected Matrix4f preDraw(long tick, JGLNode obj) {
        if (!obj.isInitialized()) {
            obj.init();
        }
        Matrix4f t = obj.calcTransform(tick);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glMultMatrix(Matrix4fLogic.toFloatBuffer(t));
        return t;
    }

    @Override
    public void draw(long tick, JGLNode node) {
        // NO-OP        
    }

    protected void postDraw(long tick, JGLNode obj) {
        projectToScreen(obj);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
    }

    private void projectToScreen(JGLNode obj) {
        if ((obj.getLowBounds() == null) && (obj.getHighBounds() == null) && (obj.getData("pointMap") == null)) {
            return;
        }
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        FloatBuffer dviewport = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetFloat(GL11.GL_VIEWPORT, dviewport);

        IntBuffer viewport = BufferLogic.create((int) dviewport.get(0), (int) dviewport.get(1), (int) dviewport.get(2), (int) dviewport.get(3));
        if ((obj.getLowBounds() != null) && (obj.getHighBounds() != null)) {
            FloatBuffer low = BufferLogic.createFloatBuffer(3);
            GLU.gluProject(obj.getLowBounds().x, obj.getLowBounds().y, obj.getLowBounds().z, modelView, projection, viewport, low);
            FloatBuffer high = BufferLogic.createFloatBuffer(3);
            GLU.gluProject(obj.getHighBounds().x, obj.getHighBounds().y, obj.getHighBounds().z, modelView, projection, viewport, high);
            FloatBuffer mid = BufferLogic.createFloatBuffer(3);
            GLU.gluProject(0, 0, 0, modelView, projection, viewport, mid);
            obj.setScreen(new Point3f(mid.get(0), mid.get(1), mid.get(2)));
            obj.setScreenLowBounds(new Point3f(Math.min(low.get(0), high.get(0)), Math.min(low.get(1), high.get(1)), Math.min(low.get(2), high.get(2))));
            obj.setScreenHighBounds(new Point3f(Math.max(low.get(0), high.get(0)), Math.max(low.get(1), high.get(1)), Math.max(low.get(2), high.get(2))));
//        System.out.println("modelView="+DoubleUtils.toString(modelView));
//        System.out.println("projection="+DoubleUtils.toString(projection));
//        System.out.println("dviewport="+DoubleUtils.toString(dviewport));
//        System.out.println("Screen="+DoubleUtils.toString(mid));
        }
        Point3f pointMap = (Point3f) obj.getData("pointMap");
        if (pointMap != null) {
            FloatBuffer pointMapped = BufferLogic.createFloatBuffer(3);
            GLU.gluUnProject(pointMap.x, pointMap.y, pointMap.z, modelView, projection, viewport, pointMapped);
            Point3f pMapped = new Point3f(pointMapped.get(0), pointMapped.get(1), pointMapped.get(2));
            obj.setData("pointMap", null);
            obj.setData("pointMapped", pMapped);
        }
    }
}
