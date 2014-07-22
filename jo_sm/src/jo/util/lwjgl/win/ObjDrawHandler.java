package jo.util.lwjgl.win;

import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;

import org.lwjgl.opengl.GL11;

public class ObjDrawHandler extends NodeDrawHandler {

    @Override
    public void draw(long tick, JGLNode node) {
        synchronized (node) {
            preDraw(tick, node);
            JGLObj obj = (JGLObj) node;
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            if (obj.getNormalBuffer() != null) {
                GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            }
            if (obj.isWireframe()) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                if (obj.getWireColor() != null) {
                    GL11.glColor3f(obj.getWireColor().x, obj.getWireColor().y, obj.getWireColor().z);
                }
            } else {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }
            if (obj.isTextured()) {
                if (obj.getTextureColor() != null) {
                    GL11.glColor4f(obj.getTextureColor().x, obj.getTextureColor().y, obj.getTextureColor().z, obj.getTextureColor().w);
                }
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, JGLTextureCache.getTexture(obj.getTextureID()));
            } else if (obj.getColorBuffer() != null) {
                if (obj.isAnyAlpha()) {
                    GL11.glEnable(GL11.GL_BLEND);
                }
                GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            }
            GL11.glFrontFace(GL11.GL_CW);
            GL11.glVertexPointer(3, 0, obj.getVertexBuffer());
            if (obj.getNormalBuffer() != null) {
                GL11.glNormalPointer(0, obj.getNormalBuffer());
            }
            if (obj.isTextured()) {
                GL11.glTexCoordPointer(2, 0, obj.getTexturesBuffer());
            } else if (obj.getColorBuffer() != null) {
                GL11.glColorPointer(4, 0, obj.getColorBuffer());
            }
            if (obj.getMode() == JGLObj.TRIANGLES) {
                if (obj.getIndexShortBuffer() != null) {
                    GL11.glDrawElements(GL11.GL_TRIANGLES, obj.getIndexShortBuffer());
                } else if (obj.getIndexIntBuffer() != null) {
                    GL11.glDrawElements(GL11.GL_TRIANGLES, obj.getIndexIntBuffer());
                }
            } else if (obj.getMode() == JGLObj.QUADS) {
                if (obj.getIndexShortBuffer() != null) {
                    GL11.glDrawElements(GL11.GL_QUADS, obj.getIndexShortBuffer());
                } else if (obj.getIndexIntBuffer() != null) {
                    GL11.glDrawElements(GL11.GL_QUADS, obj.getIndexIntBuffer());
                }
            } else {
                throw new IllegalArgumentException("Unknown mode: " + obj.getMode());
            }
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            if (obj.getNormalBuffer() != null) {
                GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            }
            if (obj.isTextured()) {
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL11.glDisable(GL11.GL_BLEND);
            } else if (obj.getColorBuffer() != null) {
                GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
                if (obj.isAnyAlpha()) {
                    GL11.glEnable(GL11.GL_BLEND);
                }
            }
            postDraw(tick, node);
        }
    }

    /*
     @Override
     protected void postDraw(GL2 gl, long tick, JGLNode node)
     {
     super.postDraw(tick, node);
     if (gl instanceof MatrixTrackingGL)
     {
     mScreenHighBounds = JGLUtils.gluProject(mHighBounds);
     mScreenLowBounds = JGLUtils.gluProject(mLowBounds);
     }
     }
     */
}
