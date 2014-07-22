package jo.util.lwjgl.win;

import java.util.HashMap;
import java.util.Map;

import jo.vecmath.Color4f;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLLight;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.JGLScene;
import jo.util.jgl.obj.flat.JGLFlatPoints;
import jo.util.jgl.obj.flat.JGLFlatRect;
import jo.util.jgl.obj.part.JGLObjParticle;
import jo.util.jgl.obj.tri.JGLObj;
import jo.util.jgl.obj.txt.JGLTextGroup;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class DrawLogic {

    private static final Map<Class<?>, IDrawHandler> mHandlers = new HashMap<>();

    static {
        mHandlers.put(JGLNode.class, new NodeDrawHandler());
        mHandlers.put(JGLObj.class, new ObjDrawHandler());
        mHandlers.put(JGLGroup.class, new GroupDrawHandler());
        mHandlers.put(JGLFlatRect.class, new RectDrawHandler());
        mHandlers.put(JGLFlatPoints.class, new PointsDrawHandler());
        mHandlers.put(JGLLight.class, new LightDrawHandler());
        mHandlers.put(JGLObjParticle.class, new ParticleDrawHandler());
    }

    /**
     * Text Renderer
     */
    //private static Map<String,TextRenderer> mRenderers = new HashMap<String, TextRenderer>();
    private static IDrawHandler getHandler(JGLNode obj) {
        Class<?> c = obj.getClass();
        while (c != Object.class) {
            if (mHandlers.containsKey(c)) {
                return mHandlers.get(c);
            } else {
                c = c.getSuperclass();
            }
        }
        return null;
    }

    public static void draw(long tick, JGLNode node) {
        if (node == null) {
            return;
        }
        if (node.isCull()) {
            return;
        }
        IDrawHandler h = getHandler(node);
        if (h == null) {
            throw new IllegalArgumentException("No handler for " + node.getClass().getName());
        }
        h.draw(tick, node);
    }

    public static void draw(int x, int y, long tick,
            JGLScene scene) {
        synchronized (scene) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            if (scene.getBackground() != null) {
                Color4f bg = scene.getBackground();
                GL11.glClearColor(bg.x, bg.y, bg.z, bg.w);
            }
            GL11.glCullFace(GL11.GL_FRONT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glFrontFace(GL11.GL_CCW);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            float aspect;
            aspect = x / (float) y;
            GLU.gluPerspective(scene.getFieldOfView(), aspect, scene.getMinZ(), scene.getMaxZ());

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            draw(tick, scene.getNode());
            for (JGLTextGroup textGroup : scene.getTexts()) {
                if (!textGroup.isCull()) {
                    draw(textGroup, x, y);
                }
            }
        }
    }

    public static void draw(JGLTextGroup group, int width, int height) {
        /*
         String sig = group.getFace()+"-"+group.getSize()+"-"+group.getStyle();
         TextRenderer renderer = mRenderers.get(sig);
         if (renderer == null)
         {
         renderer = new TextRenderer(new Font(group.getFace(), group.getStyle(), group.getSize()));
         renderer.setUseVertexArrays(false);
         mRenderers.put(sig, renderer);
         }
         renderer.beginRendering(width, height);
         setColor(renderer, group.getColor());
         JGLText2D[] texts;
         synchronized (group)
         {
         texts = group.getTexts().toArray(new JGLText2D[0]);
         }
         for (JGLText2D text : texts)
         if (!text.isCull() && (text.getText() != null))
         {
         setColor(renderer, text.getColor());
         double x = text.getX();
         double y = text.getY();
         if (text.getReference() != null)
         {
         JGLNode ref = text.getReference();
         if (ref.isCull())
         continue;
         if ((ref.getScreenLowBounds() == null) || (ref.getScreenHighBounds() == null))
         continue;
         if (((int)ref.getScreenLowBounds().z != 0) || ((int)ref.getScreenHighBounds().z != 0))
         continue;
         //x += MathUtils.interpolate(text.getReferenceXInterpolate(), 0, 1, ref.getScreenLowBounds().x, ref.getScreenHighBounds().x);
         //y += MathUtils.interpolate(text.getReferenceYInterpolate(), 0, 1, ref.getScreenLowBounds().y, ref.getScreenHighBounds().y);
         x += ref.getScreen().x;
         y += ref.getScreen().y;
         //                    System.out.println(text.getText()+" - "+ref.getScreen().toIntString()
         //                            +" low="+ref.getLowBounds().toIntString()+"/"+ref.getScreenLowBounds().toIntString()
         //                            +", high="+ref.getHighBounds().toIntString()+"/"+ref.getScreenHighBounds().toIntString());
         }
         Rectangle2D bounds = renderer.getBounds(text.getText());
         if ((text.getTextAlign()&JGLText2D.H_CENTER) != 0)
         x -= bounds.getWidth()/2;
         else if ((text.getTextAlign()&JGLText2D.H_RIGHT) != 0)
         x -= bounds.getWidth();
         if ((text.getTextAlign()&JGLText2D.V_CENTER) != 0)
         y += bounds.getHeight()/2;
         else if ((text.getTextAlign()&JGLText2D.V_TOP) != 0)
         y += bounds.getHeight();
         text.setScreenX((int)x);
         text.setScreenY((int)y);
         text.setScreenWidth((int)bounds.getWidth());
         text.setScreenHeight((int)bounds.getHeight());
         if ((x + bounds.getWidth() < 0) || (y + bounds.getHeight() < 0) || (x > width) && (y > height))
         continue;
         renderer.draw(text.getText(), text.getScreenX(), text.getScreenY());
         }
         renderer.endRendering();
         */
    }

    /*
     private static void setColor(TextRenderer renderer, Color4f c)
     {
     if (c != null)
     renderer.setColor((float)c.x, (float)c.y, (float)c.z, (float)c.w);
     }
     */
    /*

     private static void draw(long tick, JGLObjCamera obj)
     {
     Transform3D t = obj.calcTransform(tick);
     Point3D pos = new Point3D();
     Point3D lookDir = new Point3D(obj.getLookDir());
     Point3D upDir = new Point3D(obj.getUpDir());
     t.mult(pos);
     t.mult(lookDir);
     t.mult(upDir);
     Point3D center = new Point3D(pos);
     center.incr(lookDir);
     GL11.glLoadIdentity();
     GLU.gluPerspective(gl, mFieldOfViewY, (mWidth * 1f) / mHeight, mClipNear, mClipFar);
     GLU.gluLookAt(gl, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ(), 
     (float)center.getX(), (float)center.getY(), (float)center.getZ(), 
     (float)upDir.getX(), (float)upDir.getY(), (float)upDir.getZ());
     }
    
    
     @Override
     public void draw(long tick, JGLObjMeshColors obj)
     {
     JGLMeshCache.useMesh(obj.getMesh());
     super.draw(gl, tick);
     }
     @Override
     public void draw(long tick, JGLObjMeshTexture obj)
     {
     JGLMeshCache.useMesh(mMesh);
     super.draw(gl, tick);
     }
    
     public void draw(long tick, JGLGroup obj)
     {
     preDraw(gl, tick, obj);
     for (JGLNode child : obj.getChildren())
     {
     if (child.isCull())
     continue;
     draw(gl, tick, child);
     }
     postDraw(gl, tick, obj);
     }
    
     @Override
     public void draw(long tick, JGLObjMesh obj)
     {
     JGLMeshCache.useMesh(mMesh);
     super.draw(gl, tick);
     }

    
     @Override
     protected void preDraw(long tick, JGLObjBitmap bm)
     {
     if (mTexture == null)
     {
     Bitmap bmp = mBitmap;
     if (mBitmap == null)
     bmp = BitmapFactory.decodeResource(mResources, mImageID, null);
     Matrix flip = new Matrix();
     flip.postScale(1f, -1f);
     bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flip, true);
     mTexture = JGLTextureCache.getTexture(gl, bmp);
     bmp.recycle();
     // Use Nearest for performance.
     GL11.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
     GL11.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
     GL11.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
     GL11.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
     }
     JGLTextureCache.useTexture(mTexture);
     //System.out.println("Using texture "+mTexture.getTexture()+"/"+mTexture.getResourceID());
     GL11.glBindTexture(GL2.GL_TEXTURE_2D, mTexture.getTexture());
     GL11.glShadeModel(GL2.GL_FLAT);
     GL11.glEnable(GL2.GL_BLEND);
     GL11.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
     GL11.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
     GL11.glMatrixMode(GL2.GL_PROJECTION);
     GL11.glPushMatrix();
     GL11.glLoadIdentity();
     GL11.glOrthof(0.0f, ((MatrixTrackingGL)gl).getWidth(), 0.0f, ((MatrixTrackingGL)gl).getHeight(), 0.0f, 1.0f);
     GL11.glMatrixMode(GL2.GL_MODELVIEW);
     GL11.glPushMatrix();
     GL11.glLoadIdentity();
     // Magic offsets to promote consistent rasterization.
     GL11.glTranslatef(0.375f, 0.375f, 0.0f);
     }
    
     @Override
     public void draw(long tick, JGLObjBitmap bm)
     {
     preDraw(gl, tick);
     GL11.glEnable(GL2.GL_TEXTURE_2D);
     ((GL11)gl).glTexParameteriv(GL2.GL_TEXTURE_2D,
     GL11Ext.GL_TEXTURE_CROP_RECT_OES, new int[] { 0, 0, mTexture.getSize(), mTexture.getSize() }, 0);
     ((GL11Ext)gl).glDrawTexiOES(mLeft, mTop, 0, mWidth, mHeight);
     postDraw(gl, tick);
     }
    
     @Override
     protected void postDraw(long tick, JGLObjBitmap bm)
     {
     GL11.glDisable(GL2.GL_BLEND);
     GL11.glMatrixMode(GL2.GL_PROJECTION);
     GL11.glPopMatrix();
     GL11.glMatrixMode(GL2.GL_MODELVIEW);
     GL11.glPopMatrix();
     }
     */
}
