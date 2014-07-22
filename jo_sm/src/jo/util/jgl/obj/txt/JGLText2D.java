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
package jo.util.jgl.obj.txt;

import jo.util.jgl.obj.JGLNode;
import jo.vecmath.Color3f;
import jo.vecmath.Color4f;

public class JGLText2D {

    public static final int V_TOP = 0x01;
    public static final int V_CENTER = 0x02;
    public static final int V_BOTTOM = 0x04;
    public static final int H_LEFT = 0x10;
    public static final int H_CENTER = 0x20;
    public static final int H_RIGHT = 0x40;

    private boolean mCull;
    private String mText;
    private int mX;
    private int mY;
    private JGLNode mReference;
    private double mReferenceXInterpolate;
    private double mReferenceYInterpolate;
    private int mTextAlign;
    private Color4f mColor;
    private int mScreenX;
    private int mScreenY;
    private int mScreenWidth;
    private int mScreenHeight;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

    public JGLNode getReference() {
        return mReference;
    }

    public void setReference(JGLNode reference) {
        mReference = reference;
    }

    public double getReferenceXInterpolate() {
        return mReferenceXInterpolate;
    }

    public void setReferenceXInterpolate(double referenceXInterpolate) {
        mReferenceXInterpolate = referenceXInterpolate;
    }

    public double getReferenceYInterpolate() {
        return mReferenceYInterpolate;
    }

    public void setReferenceYInterpolate(double referenceYInterpolate) {
        mReferenceYInterpolate = referenceYInterpolate;
    }

    public int getTextAlign() {
        return mTextAlign;
    }

    public void setTextAlign(int textAlign) {
        mTextAlign = textAlign;
    }

    public Color4f getColor() {
        return mColor;
    }

    public void setColor(Color4f color) {
        mColor = color;
    }

    public void setColor(Color3f color) {
        mColor = new Color4f(color.x, color.y, color.z, 1);
    }

    public boolean isCull() {
        return mCull;
    }

    public void setCull(boolean cull) {
        mCull = cull;
    }

    public int getScreenX() {
        return mScreenX;
    }

    public void setScreenX(int screenX) {
        mScreenX = screenX;
    }

    public int getScreenY() {
        return mScreenY;
    }

    public void setScreenY(int screenY) {
        mScreenY = screenY;
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
