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
package jo.util.jgl.obj.flat;

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Color4f;
import jo.vecmath.Point2f;

import jo.util.jgl.obj.JGLNode;

public class JGLFlatPoints extends JGLNode {

    private List<Point2f> mLocations;
    private float mRadius;
    private Color4f mColor;
    private boolean mAntiAlias;

    public JGLFlatPoints() {
        mLocations = new ArrayList<>();
        mRadius = 1;
        mAntiAlias = true;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public Color4f getColor() {
        return mColor;
    }

    public void setColor(Color4f color) {
        mColor = color;
    }

    public boolean isAntiAlias() {
        return mAntiAlias;
    }

    public void setAntiAlias(boolean antiAlias) {
        mAntiAlias = antiAlias;
    }

    public List<Point2f> getLocations() {
        return mLocations;
    }

    public void setLocations(List<Point2f> locations) {
        mLocations = locations;
    }
}
