package jo.util.jgl.obj.tri;

final class JGLObjMeshTexture extends JGLObj {

    private int mMeshID;

    public JGLObjMeshTexture(int meshID, int textureID) {
        setMeshID(meshID);
        setTextureID(textureID);
    }

    public int getMeshID() {
        return mMeshID;
    }

    public void setMeshID(int meshID) {
        mMeshID = meshID;
    }
}
