package jo.vecmath.logic.ext;

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.Triangle3f;

public class Hull3fLogic {

    // it's inside if it has to intersect with an odd number of surfaces
    public static boolean isInside(Hull3f hull, Point3f p) {
        Line3f line = new Line3f(p, new Point3f(0, 0, 1));
        List<Point3f> hits = intersections(hull, line);
        int above = 0;
        for (Point3f hit : hits) {
            if (hit.z > p.z) {
                above++;
            }
        }
        return above % 2 == 1;
    }

    public static List<Point3f> intersections(Hull3f hull, Line3f line) {
        List<Point3f> points = new ArrayList<Point3f>();
        for (Triangle3f tri : hull.getTriangles()) {
            Point3f p = Triangle3fLogic.intersection(tri, line);
            if (p != null) {
                points.add(p);
            }
        }
        return points;
    }

    public static void getBounds(Hull3f hull, Point3f lower, Point3f upper) {
        lower.set(hull.getTriangles().get(0).getA());
        upper.set(lower);
        for (Triangle3f tri : hull.getTriangles()) {
            extendBounds(tri.getA(), lower, upper);
            extendBounds(tri.getB(), lower, upper);
            extendBounds(tri.getC(), lower, upper);
        }
    }

    private static void extendBounds(Point3f p, Point3f lower, Point3f upper) {
        lower.x = Math.min(p.x, lower.x);
        lower.y = Math.min(p.y, lower.y);
        lower.z = Math.min(p.z, lower.z);
        upper.x = Math.max(p.x, upper.x);
        upper.y = Math.max(p.y, upper.y);
        upper.z = Math.max(p.z, upper.z);
    }
}
