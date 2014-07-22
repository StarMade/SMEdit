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

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Color4f;

public class JGLTextGroup {

    private boolean mCull;
    private String mFace;
    private int mStyle;
    private int mSize;
    private Color4f mColor;
    private List<JGLText2D> mTexts;

    public JGLTextGroup() {
        mTexts = new ArrayList<>();
    }

    public void add(JGLText2D text) {
        synchronized (this) {
            mTexts.add(text);
        }
    }

    public void remove(JGLText2D text) {
        synchronized (this) {
            mTexts.remove(text);
        }
    }

    public String getFace() {
        return mFace;
    }

    public void setFace(String face) {
        mFace = face;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public List<JGLText2D> getTexts() {
        return mTexts;
    }

    public void setTexts(List<JGLText2D> texts) {
        mTexts = texts;
    }

    public boolean isCull() {
        return mCull;
    }

    public void setCull(boolean cull) {
        mCull = cull;
    }

    public Color4f getColor() {
        return mColor;
    }

    public void setColor(Color4f color) {
        mColor = color;
    }

}
