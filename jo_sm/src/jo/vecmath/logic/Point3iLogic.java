package jo.vecmath.logic;

import jo.vecmath.Point3i;

public class Point3iLogic {

    public static int distance(Point3i p1, Point3i p2) {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int dz = p1.z - p2.z;
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Point3i min(Point3i p, Point3i lower, Point3i upper) {
        if (lower == null) {
            if (upper == null) {
                return p;
            } else {
                p.set(upper);
                return p;
            }
        }
        if (upper == null) {
            p.set(lower);
            return p;
        }
        p.x = Math.min(lower.x, upper.x);
        p.y = Math.min(lower.y, upper.y);
        p.z = Math.min(lower.z, upper.z);
        return p;
    }

    public static Point3i max(Point3i p, Point3i lower, Point3i upper) {
        if (lower == null) {
            if (upper == null) {
                return p;
            } else {
                p.set(upper);
                return p;
            }
        }
        if (upper == null) {
            p.set(lower);
            return p;
        }
        p.x = Math.max(lower.x, upper.x);
        p.y = Math.max(lower.y, upper.y);
        p.z = Math.max(lower.z, upper.z);
        return p;
    }

    public static Point3i min(Point3i lower, Point3i upper) {
        if (lower == null) {
            return upper;
        }
        if (upper == null) {
            return lower;
        }
        return min(new Point3i(), lower, upper);
    }

    public static Point3i max(Point3i lower, Point3i upper) {
        if (lower == null) {
            return upper;
        }
        if (upper == null) {
            return lower;
        }
        return max(new Point3i(), lower, upper);
    }

    public static Point3i interpolate(Point3i lower, Point3i upper, float f) {
        Point3i p = new Point3i(
                (int) (MathUtils.interpolate(f, 0, 1, lower.x, upper.x) + .5f),
                (int) (MathUtils.interpolate(f, 0, 1, lower.y, upper.y) + .5f),
                (int) (MathUtils.interpolate(f, 0, 1, lower.z, upper.z) + .5f)
        );
        return p;
    }
}
