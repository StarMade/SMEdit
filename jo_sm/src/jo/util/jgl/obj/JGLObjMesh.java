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

import java.util.logging.Logger;
import jo.util.jgl.JGLUtils;
import jo.util.jgl.obj.tri.JGLObj;
import jo.vecmath.Color3f;

public final class JGLObjMesh extends JGLObj {
    private static final Logger LOG = Logger.getLogger(JGLObjMesh.class.getName());

    private int mMeshID;
    private Color3f mBaseColor;
    private Color3f mDeltaColor;

    public JGLObjMesh(int meshID, Color3f baseColor, Color3f deltaColor) {
        setMeshID(meshID);
        setBaseColor(baseColor);
        setDeltaColor(deltaColor);
    }

    public JGLObjMesh(int meshID, int textureID) {
        setMeshID(meshID);
        setTextureID(textureID);
    }

    @Override
    public void init() {
        if (mBaseColor != null) {
            setColors(JGLUtils.rndColors(getIndices(), mBaseColor, mDeltaColor));
        }
        super.init();
    }

    public int getMeshID() {
        return mMeshID;
    }

    public void setMeshID(int meshID) {
        mMeshID = meshID;
    }

    public Color3f getBaseColor() {
        return mBaseColor;
    }

    public void setBaseColor(Color3f baseColor) {
        mBaseColor = baseColor;
    }

    public Color3f getDeltaColor() {
        return mDeltaColor;
    }

    public void setDeltaColor(Color3f deltaColor) {
        mDeltaColor = deltaColor;
    }
}
