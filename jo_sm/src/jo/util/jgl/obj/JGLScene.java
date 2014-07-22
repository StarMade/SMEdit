/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl.obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import jo.util.jgl.enm.JGLColorMaterialFace;
import jo.util.jgl.enm.JGLColorMaterialMode;
import jo.util.jgl.enm.JGLFogMode;
import jo.util.jgl.obj.txt.JGLTextGroup;
import jo.vecmath.Color4f;

public class JGLScene {
    private static final Logger LOG = Logger.getLogger(JGLScene.class.getName());

    private JGLNode mNode;
    private Color4f mBackground;
    private Color4f mAmbientLight;
    private float mFieldOfView;
    private float mMinZ;
    private float mMaxZ;
    private JGLColorMaterialFace mColorMaterialFace;
    private JGLColorMaterialMode mColorMaterialMode;
    private Color4f mMaterialAmbient;
    private Color4f mMaterialDiffuse;
    private Color4f mMaterialSpecular;
    private Color4f mMaterialEmission;
    private float mMaterialShininess;
    private JGLFogMode mFogMode;
    private float mFogDensity;
    private float mFogStart;
    private float mFogEnd;
    private float mFogIndex;
    private Color4f mFogColor;
    private List<Runnable> mBetweenRenderers;
    private List<JGLTextGroup> mTexts;
    private int mScreenWidth;
    private int mScreenHeight;

    public JGLScene() {
        mFieldOfView = 45;
        mMinZ = .1f;
        mMaxZ = 450;
        mColorMaterialFace = JGLColorMaterialFace.UNSET;
        mColorMaterialMode = JGLColorMaterialMode.UNSET;
        mMaterialShininess = -1;
        mFogMode = JGLFogMode.UNSET;
        mFogDensity = 1;
        mFogStart = 0;
        mFogEnd = 1;
        mFogIndex = 0;
        mBetweenRenderers = new ArrayList<>();
        mTexts = new ArrayList<>();
    }

    public void addBetweenRenderer(Runnable r) {
        mBetweenRenderers.add(r);
    }

    public void removeBetweenRenderer(Runnable r) {
        mBetweenRenderers.remove(r);
    }

    public JGLNode getNode() {
        return mNode;
    }

    public void setNode(JGLNode node) {
        mNode = node;
    }

    public Color4f getBackground() {
        return mBackground;
    }

    public void setBackground(Color4f background) {
        mBackground = background;
    }

    public float getFieldOfView() {
        return mFieldOfView;
    }

    public void setFieldOfView(float fieldOfView) {
        mFieldOfView = fieldOfView;
    }

    public float getMinZ() {
        return mMinZ;
    }

    public void setMinZ(float minZ) {
        mMinZ = minZ;
    }

    public float getMaxZ() {
        return mMaxZ;
    }

    public void setMaxZ(float maxZ) {
        mMaxZ = maxZ;
    }

    public List<Runnable> getBetweenRenderers() {
        return Collections.unmodifiableList(mBetweenRenderers);
    }

    public void setBetweenRenderers(List<Runnable> betweenRenderers) {
        mBetweenRenderers = betweenRenderers;
    }

    public Color4f getAmbientLight() {
        return mAmbientLight;
    }

    public void setAmbientLight(Color4f ambientLight) {
        mAmbientLight = ambientLight;
    }

    public JGLColorMaterialFace getColorMaterialFace() {
        return mColorMaterialFace;
    }

    public void setColorMaterialFace(JGLColorMaterialFace colorMaterialFace) {
        mColorMaterialFace = colorMaterialFace;
    }

    public JGLColorMaterialMode getColorMaterialMode() {
        return mColorMaterialMode;
    }

    public void setColorMaterialMode(JGLColorMaterialMode colorMaterialMode) {
        mColorMaterialMode = colorMaterialMode;
    }

    public Color4f getMaterialAmbient() {
        return mMaterialAmbient;
    }

    public void setMaterialAmbient(Color4f materialAmbient) {
        mMaterialAmbient = materialAmbient;
    }

    public Color4f getMaterialDiffuse() {
        return mMaterialDiffuse;
    }

    public void setMaterialDiffuse(Color4f materialDiffuse) {
        mMaterialDiffuse = materialDiffuse;
    }

    public Color4f getMaterialSpecular() {
        return mMaterialSpecular;
    }

    public void setMaterialSpecular(Color4f materialSpecular) {
        mMaterialSpecular = materialSpecular;
    }

    public Color4f getMaterialEmission() {
        return mMaterialEmission;
    }

    public void setMaterialEmission(Color4f materialEmission) {
        mMaterialEmission = materialEmission;
    }

    public float getMaterialShininess() {
        return mMaterialShininess;
    }

    public void setMaterialShininess(float materialShininess) {
        mMaterialShininess = materialShininess;
    }

    public JGLFogMode getFogMode() {
        return mFogMode;
    }

    public void setFogMode(JGLFogMode fogMode) {
        mFogMode = fogMode;
    }

    public float getFogDensity() {
        return mFogDensity;
    }

    public void setFogDensity(float fogDensity) {
        mFogDensity = fogDensity;
    }

    public float getFogStart() {
        return mFogStart;
    }

    public void setFogStart(float fogStart) {
        mFogStart = fogStart;
    }

    public float getFogEnd() {
        return mFogEnd;
    }

    public void setFogEnd(float fogEnd) {
        mFogEnd = fogEnd;
    }

    public float getFogIndex() {
        return mFogIndex;
    }

    public void setFogIndex(float fogIndex) {
        mFogIndex = fogIndex;
    }

    public Color4f getFogColor() {
        return mFogColor;
    }

    public void setFogColor(Color4f fogColor) {
        mFogColor = fogColor;
    }

    public List<JGLTextGroup> getTexts() {
        return Collections.unmodifiableList(mTexts);
    }

    public void setTexts(List<JGLTextGroup> texts) {
        mTexts = texts;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        mScreenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        mScreenHeight = screenHeight;
    }
}
