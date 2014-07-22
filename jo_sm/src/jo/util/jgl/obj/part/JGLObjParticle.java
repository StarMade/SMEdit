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
package jo.util.jgl.obj.part;

import jo.util.jgl.obj.tri.JGLObj;
import jo.vecmath.Color4f;
import jo.vecmath.logic.Color4fLogic;

public class JGLObjParticle extends JGLObj {

    private long mStart;
    private long mEnd;
    private Color4f mStartColor;
    private Color4f mEndColor;

    public JGLObjParticle() {
        setMode(JGLObj.QUADS);
        setVertices(new float[]{
            -1, -1, 0,
            1, -1, 0,
            1, 1, 0,
            -1, 1, 0,});
        setIndices(new short[]{
            0, 1, 2, 3,});
        setTextures(new float[]{
            0, 0,
            1, 0,
            1, 1,
            0, 1,});
    }

    @Override
    public Color4f getTextureColor() {
        if (mStartColor == null) {
            return super.getTextureColor();
        } else if (mEndColor == null) {
            return mStartColor;
        } else {
            return Color4fLogic.interpolate(System.currentTimeMillis() - mStart, 0, mEnd - mStart, mStartColor, mEndColor);
        }
    }

    public long getStart() {
        return mStart;
    }

    public void setStart(long start) {
        mStart = start;
    }

    public long getEnd() {
        return mEnd;
    }

    public void setEnd(long end) {
        mEnd = end;
    }

    public Color4f getStartColor() {
        return mStartColor;
    }

    public void setStartColor(Color4f startColor) {
        mStartColor = startColor;
    }

    public Color4f getEndColor() {
        return mEndColor;
    }

    public void setEndColor(Color4f endColor) {
        mEndColor = endColor;
    }
}
