package jo.util.lwjgl.win;

import jo.util.jgl.obj.JGLLight;
import jo.util.jgl.obj.JGLNode;
import jo.vecmath.logic.Color4fLogic;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point3fLogic;

import org.lwjgl.opengl.GL11;

public class LightDrawHandler extends NodeDrawHandler {

    private static final int[] LIGHT_NUM = {
        GL11.GL_LIGHT0,
        GL11.GL_LIGHT1,
        GL11.GL_LIGHT2,
        GL11.GL_LIGHT3,
        GL11.GL_LIGHT4,
        GL11.GL_LIGHT5,
        GL11.GL_LIGHT6,
        GL11.GL_LIGHT7,};

    private long mTick = -1;
    private int mLightCount = 0;

    @Override
    public void draw(long tick, JGLNode node) {
        if (mTick != tick) {
            mTick = tick;
            mLightCount = 0;
        } else {
            mLightCount++;
        }
        if (mLightCount >= LIGHT_NUM.length) {
            return;
        }
        preDraw(tick, node);
        JGLLight light = (JGLLight) node;
        int l = LIGHT_NUM[mLightCount];
        GL11.glEnable(l);
        if (light.getAmbient() != null) {
            GL11.glLight(l, GL11.GL_AMBIENT, Color4fLogic.toFloatBuffer(light.getAmbient()));
        }
        if (light.getDiffuse() != null) {
            GL11.glLight(l, GL11.GL_DIFFUSE, Color4fLogic.toFloatBuffer(light.getDiffuse()));
        }
        if (light.getSpecular() != null) {
            GL11.glLight(l, GL11.GL_SPECULAR, Color4fLogic.toFloatBuffer(light.getSpecular()));
        }
        if (light.getPosition() != null) {
            GL11.glLight(l, GL11.GL_POSITION, Color4fLogic.toFloatBuffer(light.getPosition()));
        }
        if (light.getSpotDirection() != null) {
            GL11.glLight(l, GL11.GL_SPOT_DIRECTION, Point3fLogic.toFloatBuffer(light.getSpotDirection()));
        }
        if (light.getSpotDirection() != null) {
            GL11.glLight(l, GL11.GL_SPOT_DIRECTION, Point3fLogic.toFloatBuffer(light.getSpotDirection()));
        }
        if (light.getSpotExponent() != 0) {
            GL11.glLightf(l, GL11.GL_SPOT_EXPONENT, light.getSpotExponent());
        }
        if (!MathUtils.epsilonEquals(light.getSpotCutoff(), 180.0f)) {
            GL11.glLightf(l, GL11.GL_SPOT_CUTOFF, light.getSpotExponent());
        }
        if (light.getConstantAttenuation() != 1) {
            GL11.glLightf(l, GL11.GL_CONSTANT_ATTENUATION, light.getConstantAttenuation());
        }
        if (light.getLinearAttenuation() != 0) {
            GL11.glLightf(l, GL11.GL_LINEAR_ATTENUATION, light.getLinearAttenuation());
        }
        if (light.getQuadraticAttenuation() != 0) {
            GL11.glLightf(l, GL11.GL_QUADRATIC_ATTENUATION, light.getQuadraticAttenuation());
        }
        postDraw(tick, node);
    }
}
