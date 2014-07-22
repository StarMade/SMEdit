package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.Plane3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point3fLogic;

public class Triangle3fLogic {

    //http://geomalgorithms.com/a06-_intersect-2.html
    public static boolean isPointInTriangle(Triangle3f tri, Point3f p) {
        Point3f u = Point3fLogic.sub(tri.getB(), tri.getA());
        Point3f v = Point3fLogic.sub(tri.getC(), tri.getA());
        Point3f w = Point3fLogic.sub(p, tri.getA());
        float uDotv = Point3fLogic.dot(u, v);
        float uDotu = Point3fLogic.dot(u, u);
        float vDotv = Point3fLogic.dot(v, v);
        float wDotu = Point3fLogic.dot(w, u);
        float wDotv = Point3fLogic.dot(w, v);
        float d = uDotv * uDotv - uDotu * vDotv;
        float sn = uDotv * wDotv - vDotv * wDotu;
        float tn = uDotv * wDotu - uDotu * wDotv;
        float s = sn / d;
        float t = tn / d;
        if (s < 0) {
            return false;
        }
        if (t < 0) {
            return false;
        }
        if (s + t > 1) {
            return false;
        }
        return true;
    }

    public static Point3f intersection(Triangle3f tri, Line3f line) {
        Plane3f plane = new Plane3f(tri.getA(), tri.getB(), tri.getC());
        if (Float.isNaN(plane.getN().x)) {
            plane = new Plane3f(tri.getA(), tri.getB(), tri.getC());
        }
        Point3f p = Plane3fLogic.intersection(plane, line);
        if (p == null) {
            return null;
        }
        if (!isPointInTriangle(tri, p)) {
            return null;
        }
        return p;
    }

    public static boolean isDegenerate(Triangle3f tri) {
        if (tri.getA().equals(tri.getB()) || tri.getA().equals(tri.getC()) || tri.getB().equals(tri.getC())) {
            return true;
        }
        Point3f u = Point3fLogic.sub(tri.getB(), tri.getA());
        Point3f v = Point3fLogic.sub(tri.getC(), tri.getA());
        Point3f cross = Point3fLogic.cross(u, v);
        if (MathUtils.isZero(cross.x) && MathUtils.isZero(cross.y) && MathUtils.isZero(cross.z)) {
            return true;
        }
        return false;

    }

}
