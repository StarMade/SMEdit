package jo.util.lwjgl.win;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import jo.util.jgl.enm.JGLColorMaterialFace;
import jo.util.jgl.enm.JGLColorMaterialMode;
import jo.util.jgl.enm.JGLFogMode;
import jo.util.jgl.obj.JGLScene;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Color4fLogic;
import jo.vecmath.logic.Matrix4fLogic;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SuppressWarnings("serial")
public class JGLCanvas extends Canvas {

    private JGLScene mScene;
    private Point3f mEyeRay;

    private final IntBuffer mIB16;
    //private int mViewportX;
    //private int mViewportBottom;
    private int mWidth;
    private int mHeight;

    private boolean mCloseRequested = false;
    private AtomicReference<Dimension> mNewCanvasSize;

    private boolean[] mMouseState;
    private final List<MouseListener> mMouseListeners;
    private final List<MouseMotionListener> mMouseMotionListeners;
    private final List<MouseWheelListener> mMouseWheelListeners;
    private final List<KeyListener> mKeyListeners;

    public JGLCanvas() {
        this.mNewCanvasSize = new AtomicReference<>();
        this.mMouseListeners = new ArrayList<>();
        this.mMouseWheelListeners = new ArrayList<>();
        this.mMouseMotionListeners = new ArrayList<>();
        this.mKeyListeners = new ArrayList<>();
        mIB16 = BufferUtils.createIntBuffer(16);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mNewCanvasSize.set(getSize());
            }
        });
    }

    /**
     * <p>
     * Queries the current view port size & position and updates all related
     * internal state.</p>
     *
     * <p>
     * It is important that the internal state matches the OpenGL viewport or
     * clipping won't work correctly.</p>
     *
     * <p>
     * This method should only be called when the viewport size has changed. It
     * can have negative impact on performance to call every frame.</p>
     *
     * @see #getWidth()
     * @see #getHeight()
     */
    public void syncViewportSize() {
        mIB16.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, mIB16);
        //mViewportX = mIB16.get(0);
        mWidth = mIB16.get(2);
        mHeight = mIB16.get(3);
        //mViewportBottom = mIB16.get(1) + mHeight;
    }

    private void init() {
        Thread t = new Thread("Render Thread") {
            @Override
            public void run() {
                doRenderLoop();
            }
        };
        t.start();
    }

    private void initFog() {
        GL11.glEnable(GL11.GL_FOG);
        GL11.glFogi(GL11.GL_FOG_MODE, conv(mScene.getFogMode()));
        if (!Matrix4fLogic.epsilonEquals(mScene.getFogDensity(), 1)) {
            GL11.glFogf(GL11.GL_FOG_DENSITY, mScene.getFogDensity());
        }
        if (!Matrix4fLogic.epsilonEquals(mScene.getFogStart(), 0)) {
            GL11.glFogf(GL11.GL_FOG_START, mScene.getFogStart());
        }
        if (!Matrix4fLogic.epsilonEquals(mScene.getFogEnd(), 1)) {
            GL11.glFogf(GL11.GL_FOG_END, mScene.getFogEnd());
        }
        if (!Matrix4fLogic.epsilonEquals(mScene.getFogIndex(), 0)) {
            GL11.glFogf(GL11.GL_FOG_INDEX, mScene.getFogIndex());
        }
        if (mScene.getFogColor() != null) {
            GL11.glFog(GL11.GL_FOG_COLOR, Color4fLogic.toFloatBuffer(mScene.getFogColor()));
        }
    }

    private void initMaterial() {
        int face = conv(mScene.getColorMaterialFace());
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        if (mScene.getColorMaterialFace() != JGLColorMaterialFace.UNSET) {
            GL11.glColorMaterial(face, conv(mScene.getColorMaterialMode()));
        }
        if (mScene.getMaterialAmbient() != null) {
            GL11.glMaterial(face, GL11.GL_AMBIENT, Color4fLogic.toFloatBuffer(mScene.getMaterialAmbient()));
        }
        if (mScene.getMaterialDiffuse() != null) {
            GL11.glMaterial(face, GL11.GL_DIFFUSE, Color4fLogic.toFloatBuffer(mScene.getMaterialDiffuse()));
        }
        if (mScene.getMaterialSpecular() != null) {
            GL11.glMaterial(face, GL11.GL_SPECULAR, Color4fLogic.toFloatBuffer(mScene.getMaterialSpecular()));
        }
        if (mScene.getMaterialEmission() != null) {
            GL11.glMaterial(face, GL11.GL_EMISSION, Color4fLogic.toFloatBuffer(mScene.getMaterialEmission()));
        }
        if (mScene.getMaterialShininess() >= 0) {
            GL11.glMaterialf(face, GL11.GL_SHININESS, mScene.getMaterialShininess());
        }
    }

    private int conv(JGLFogMode fogMode) {
        switch (fogMode) {
            case UNSET:
                return -1;
            case LINEAR:
                return GL11.GL_LINEAR;
            case EXP:
                return GL11.GL_EXP;
            case EXP2:
                return GL11.GL_EXP2;
        }
        return -1;
    }

    private int conv(JGLColorMaterialFace colorMaterialFace) {
        switch (colorMaterialFace) {
            case UNSET:
                return -1;
            case FRONT:
                return GL11.GL_FRONT;
            case BACK:
                return GL11.GL_BACK;
            case FRONT_AND_BACK:
                return GL11.GL_FRONT_AND_BACK;
        }
        return -1;
    }

    private int conv(JGLColorMaterialMode colorMaterialMode) {
        switch (colorMaterialMode) {
            case UNSET:
                return -1;
            case EMISSION:
                return GL11.GL_EMISSION;
            case AMBIENT:
                return GL11.GL_AMBIENT;
            case DIFFUSE:
                return GL11.GL_DIFFUSE;
            case SPECULAR:
                return GL11.GL_SPECULAR;
            case AMBIENT_AND_DIFFUSE:
                return GL11.GL_AMBIENT_AND_DIFFUSE;
        }
        return -1;
    }

    private void doRenderLoop() {
        try {
            while (!isDisplayable()) {
                Thread.sleep(50);
            }
            Display.setParent(this);
            Display.setVSyncEnabled(true);
            Display.create();
            mMouseState = new boolean[Mouse.getButtonCount()];
            for (int i = 0; i < mMouseState.length; i++) {
                mMouseState[i] = Mouse.isButtonDown(i);
            }

            //GL11.glsetSwapInterval(1);
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            //gl11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
            GL11.glClearDepth(1.0);
            GL11.glLineWidth(2);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (mScene.getAmbientLight() != null) {
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Color4fLogic.toFloatBuffer(mScene.getAmbientLight()));
            }
            if (mScene.getColorMaterialFace() != JGLColorMaterialFace.UNSET) {
                initMaterial();
            }
            if (mScene.getFogMode() != JGLFogMode.UNSET) {
                initFog();
            }

            Dimension newDim;

            while (!Display.isCloseRequested() && !mCloseRequested) {
                newDim = mNewCanvasSize.getAndSet(null);
                if (newDim != null) {
                    GL11.glViewport(0, 0, newDim.width, newDim.height);
                    syncViewportSize();
                }
                doRender();
                doMouse();
                doKeys();
                doEye();
                Display.update();
            }

            Display.destroy();
        } catch (InterruptedException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    private static final Map<Integer, Integer> KEY_LWJGL_TO_AWT = new HashMap<>();

    static {
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_0, KeyEvent.VK_0);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_1, KeyEvent.VK_1);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_2, KeyEvent.VK_2);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_3, KeyEvent.VK_3);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_4, KeyEvent.VK_4);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_5, KeyEvent.VK_5);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_6, KeyEvent.VK_6);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_7, KeyEvent.VK_7);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_8, KeyEvent.VK_8);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_9, KeyEvent.VK_9);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_A, KeyEvent.VK_A);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_B, KeyEvent.VK_B);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_C, KeyEvent.VK_C);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_D, KeyEvent.VK_D);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_E, KeyEvent.VK_E);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_F, KeyEvent.VK_F);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_G, KeyEvent.VK_G);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_H, KeyEvent.VK_H);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_I, KeyEvent.VK_I);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_J, KeyEvent.VK_J);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_K, KeyEvent.VK_K);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_L, KeyEvent.VK_L);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_M, KeyEvent.VK_M);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_N, KeyEvent.VK_N);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_O, KeyEvent.VK_O);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_P, KeyEvent.VK_P);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_Q, KeyEvent.VK_Q);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_R, KeyEvent.VK_R);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_S, KeyEvent.VK_S);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_T, KeyEvent.VK_T);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_U, KeyEvent.VK_U);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_V, KeyEvent.VK_V);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_W, KeyEvent.VK_W);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_X, KeyEvent.VK_X);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_Y, KeyEvent.VK_Y);
        KEY_LWJGL_TO_AWT.put(Keyboard.KEY_Z, KeyEvent.VK_Z);
    }

    private int mModifiers = 0;

    private void doKeys() {
        while (Keyboard.next()) {
            char eventChar = Keyboard.getEventCharacter();
            int eventKey = Keyboard.getEventKey();
            long eventTick = Keyboard.getEventNanoseconds();
            boolean eventState = Keyboard.getEventKeyState();
            //System.out.println("doKeys("+eventKey+")");
            if (eventKey == Keyboard.KEY_LSHIFT) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_SHIFT;
                } else {
                    mModifiers &= ~KeyEvent.VK_SHIFT;
                }
            } else if (eventKey == Keyboard.KEY_RSHIFT) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_SHIFT;
                } else {
                    mModifiers &= ~KeyEvent.VK_SHIFT;
                }
            } else if (eventKey == Keyboard.KEY_LCONTROL) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_CONTROL;
                } else {
                    mModifiers &= ~KeyEvent.VK_CONTROL;
                }
            } else if (eventKey == Keyboard.KEY_RCONTROL) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_CONTROL;
                } else {
                    mModifiers &= ~KeyEvent.VK_CONTROL;
                }
            } else if (eventKey == Keyboard.KEY_LMENU) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_ALT;
                } else {
                    mModifiers &= ~KeyEvent.VK_ALT;
                }
            } else if (eventKey == Keyboard.KEY_RMENU) {
                if (eventState) {
                    mModifiers |= KeyEvent.VK_ALT;
                } else {
                    mModifiers &= ~KeyEvent.VK_ALT;
                }
            }
            if (KEY_LWJGL_TO_AWT.containsKey(eventKey)) {
                eventKey = KEY_LWJGL_TO_AWT.get(eventKey);
            }
            KeyEvent e = new KeyEvent(this, eventState ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED,
                    eventTick, mModifiers, eventKey, eventChar);
            fireKeyEvent(e);
        }
    }

    private void doEye() {
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        float winX = mouseX;
        float winY = viewport.get(3) - mouseY;
        FloatBuffer winZBuffer = BufferUtils.createFloatBuffer(1);
        GL11.glReadPixels(mouseX, mouseY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZBuffer);
        float winZ = winZBuffer.get(0);
        FloatBuffer pos = BufferUtils.createFloatBuffer(3);
        GLU.gluUnProject(winX, winY, winZ, modelview, projection, viewport, pos);
        mEyeRay = new Point3f(pos.get(0), pos.get(1), pos.get(2));
    }

    private void doMouse() {
        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            boolean buttonState = Mouse.getEventButtonState();
            int dWheel = Mouse.getDWheel();
            int dX = Mouse.getEventDX();
            int dY = Mouse.getEventDY();
            long nanoseconds = Mouse.getEventNanoseconds();
            int x = Mouse.getEventX();
            int y = Mouse.getEventY();
            int modifiers = 0;
            if (button >= 0) {
                int jButton = (button == 0) ? MouseEvent.BUTTON1 : (button == 1) ? MouseEvent.BUTTON2 : MouseEvent.BUTTON3;
                if (mMouseState[button] != buttonState) {
                    //System.out.println("Button="+button+", state="+buttonState+", x="+x+", y="+y);
                    if (buttonState) {
                        MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_PRESSED, nanoseconds, modifiers, x, y, 1, false, jButton);
                        fireMouseEvent(event);
                    } else {
                        MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_RELEASED, nanoseconds, modifiers, x, y, 1, false, jButton);
                        fireMouseEvent(event);
                    }
                    mMouseState[button] = buttonState;
                }
            }
            if ((dX > 0) || (dY > 0)) {
                int jButton = 0;
                if (mMouseState[0]) {
                    jButton |= MouseEvent.BUTTON1;
                }
                if (mMouseState[1]) {
                    jButton |= MouseEvent.BUTTON2;
                }
                if (mMouseState[2]) {
                    jButton |= MouseEvent.BUTTON3;
                }
                if (jButton != 0) {
                    MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_DRAGGED, nanoseconds, modifiers, x, y, 1, false, jButton);
                    fireMouseMoveEvent(event);
                } else {
                    MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_MOVED, nanoseconds, modifiers, x, y, 1, false, jButton);
                    fireMouseMoveEvent(event);
                }
            }
            if (dWheel != 0) {
                MouseWheelEvent event = new MouseWheelEvent(this, MouseEvent.MOUSE_WHEEL, nanoseconds, modifiers,
                        x, y, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, dWheel, dWheel / 120);
                fireMouseWheelEvent(event);
            }
        }
    }

    private void doRender() {
        for (Runnable r : mScene.getBetweenRenderers()) {
            r.run();
        }
        DrawLogic.draw(mWidth, mHeight, System.currentTimeMillis(), mScene);
    }

    public JGLScene getScene() {
        return mScene;
    }

    public void setScene(JGLScene scene) {
        if ((mScene != null) && (mScene != scene)) {
            throw new IllegalArgumentException("Cannot set a new scene!");
        }
        mScene = scene;
        if (mScene != null) {
            init();
        }
    }

    public boolean isCloseRequested() {
        return mCloseRequested;
    }

    public void setCloseRequested(boolean closeRequested) {
        mCloseRequested = closeRequested;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (System.getProperty("os.name").contains("Mac")) {
            super.addMouseListener(l);
        } else {
            mMouseListeners.add(l);
        }
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        if (System.getProperty("os.name").contains("Mac")) {
            super.addMouseMotionListener(l);
        } else {
            mMouseMotionListeners.add(l);
        }
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        if (System.getProperty("os.name").contains("Mac")) {
            super.addMouseWheelListener(l);
        } else {
            mMouseWheelListeners.add(l);
        }
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        mMouseListeners.remove(l);
    }

    @Override
    public synchronized void removeMouseMotionListener(MouseMotionListener l) {
        mMouseMotionListeners.remove(l);
    }

    @Override
    public synchronized void removeMouseWheelListener(MouseWheelListener l) {
        mMouseWheelListeners.remove(l);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        mKeyListeners.add(l);
        super.addKeyListener(l);
    }

    @Override
    public synchronized void removeKeyListener(KeyListener l) {
        mKeyListeners.remove(l);
    }

    private void fireMouseEvent(MouseEvent e) {
        for (MouseListener l : mMouseListeners) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                l.mousePressed(e);
            } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                l.mouseReleased(e);
            }
        }
    }

    private void fireMouseMoveEvent(MouseEvent e) {
        for (MouseMotionListener l : mMouseMotionListeners) {
            if (e.getID() == MouseEvent.MOUSE_MOVED) {
                l.mouseMoved(e);
            } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                l.mouseDragged(e);
            }
        }
    }

    private void fireMouseWheelEvent(MouseWheelEvent e) {
        for (MouseWheelListener l : mMouseWheelListeners) {
            if (e.getID() == MouseEvent.MOUSE_WHEEL) {
                l.mouseWheelMoved(e);
            }
        }
    }

    private void fireKeyEvent(KeyEvent e) {
        for (KeyListener l : mKeyListeners) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                l.keyPressed(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                l.keyReleased(e);
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                l.keyTyped(e);
            }
        }
    }

    public Point3f getEyeRay() {
        return mEyeRay;
    }

    public void setEyeRay(Point3f eyeRay) {
        mEyeRay = eyeRay;
    }
}
