package jo.util.jgl.obj.tri;

import jo.vecmath.Color3f;

import jo.util.jgl.JGLUtils;

final class JGLObjMeshColors extends JGLObj {

    private int mMeshID;
    private Color3f mBaseColor;
    private Color3f mDeltaColor;

    public JGLObjMeshColors(int meshID, Color3f baseColor, Color3f deltaColor) {
        setMeshID(meshID);
        setBaseColor(baseColor);
        setDeltaColor(deltaColor);
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
