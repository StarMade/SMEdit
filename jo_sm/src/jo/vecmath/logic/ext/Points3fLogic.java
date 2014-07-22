package jo.vecmath.logic.ext;

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Plane3f;
import jo.vecmath.logic.Point3fLogic;

public class Points3fLogic {

    public static Point3f getCenter(Point3f p, List<Point3f> points) {
        Point3f total = new Point3f();
        for (Point3f p2 : points) {
            total.add(p2);
        }
        total.scale(1.0f / points.size());
        return total;
    }

    public static Point3f getClosest(Point3f p, List<Point3f> points) {
        Point3f closest = null;
        float closestDist = -1;
        for (Point3f p2 : points) {
            float d = p2.distance(p);
            if ((closest == null) || (d < closestDist)) {
                closest = p2;
                closestDist = d;
            }
        }
        return closest;
    }

    public static List<Point3f> getPointsOnPlane(Plane3f plane, List<Point3f> points) {
        List<Point3f> onPlane = new ArrayList<Point3f>();
        for (Point3f p : points) {
            if (Point3fLogic.isZero(plane.dist(p))) {
                onPlane.add(p);
            }
        }
        return onPlane;
    }

    public static int equals(Point3f p, List<Point3f> points) {
        for (int i = 0; i < points.size(); i++) {
            if (Point3fLogic.equals(p, points.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /*
     public static void sortByAngleAround(final Point3f center, final Point3f closest,
     List<Point3f> points, final Point3f normal, List<Integer> angles) {
     Collections.sort(points, new Comparator<Point3f>() {
     @Override
     public int compare(Point3f o1, Point3f o2) {
     float a1 = angleBetween(o1);
     float a2 = angleBetween(o2);
     int ret = Point3fLogic.sgn(a1 - a2);
     if (ret == 0)
     {
     float d1 = o1.distance(center);
     float d2 = o2.distance(center);
     return Point3fLogic.sgn(d2 - d1);
     }
     return ret;
     }
     private float angleBetween(Point3f p2)
     {
     float a;
     if (normal != null)
     a = Point3fLogic.signedAngleBetween(closest.sub(center), p2.sub(center), normal);
     else
     a = Point3fLogic.angleBetween(center, closest, p2);
     if (a < 0)
     a += Math.PI*2;
     return a;
     }
     });
     System.out.println("Sorted points: (closest="+closest+", center="+center+", normal="+normal+")");
     for (Point3f p : points)
     {
     float a;
     if (normal != null)
     a = Point3fLogic.signedAngleBetween(closest.sub(center), p.sub(center), normal);
     else
     a = Point3fLogic.angleBetween(center, closest, p);
     if (a < 0)
     a += Math.PI*2;
     System.out.println("  "+a+" "+p);
     if (angles != null)
     angles.add((int)(a/Math.PI*180));
     }
     }
     */
}
