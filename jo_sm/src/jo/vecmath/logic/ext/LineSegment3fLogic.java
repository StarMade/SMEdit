package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.LineSegment3f;
import jo.vecmath.logic.Point3fLogic;

public class LineSegment3fLogic {

    // make a line from two points
    public static LineSegment3f fromPoints(Point3f p1, Point3f p2) {
        return new LineSegment3f(p1, p2);
    }

    public static boolean intersect(LineSegment3f l, Point3f p) {
        return Point3fLogic.equals(l.getP1().distance(p) + l.getP2().distance(p), l.length());
    }

    public static Point3f intersect(Line3f line, LineSegment3f segment) {
        Line3f line2 = Line3fLogic.fromSegment(segment);
        Point3f[] closest = Line3fLogic.closestPoints(line, line2);
        if (closest == null) {
            return null;
        }
        if (!Point3fLogic.equals(closest[0].distance(closest[1]), 0)) {
            return null;
        }
        if (!intersect(segment, closest[0])) {
            return null;
        }
        return closest[0];
    }
}
