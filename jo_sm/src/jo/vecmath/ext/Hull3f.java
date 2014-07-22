package jo.vecmath.ext;

import java.util.ArrayList;
import java.util.List;

public class Hull3f {

    private List<Triangle3f> mTriangles;

    public Hull3f() {
        mTriangles = new ArrayList<Triangle3f>();
    }

    public List<Triangle3f> getTriangles() {
        return mTriangles;
    }

    public void setTriangles(List<Triangle3f> triangles) {
        mTriangles = triangles;
    }
}
