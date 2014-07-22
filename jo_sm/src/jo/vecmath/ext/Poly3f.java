package jo.vecmath.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.vecmath.Point3f;

public class Poly3f {

    private List<Plane3f> mPlanes;
    private List<Line3f> mLines;
    private List<Point3f> mPoints;
    private List<List<Point3f>> mPolys;
    private List<Point3f> mCenters;
    private List<List<Integer>> mAngles;
    private Map<Plane3f, List<Plane3f>> mPlanesForPlanes;
    private Map<Line3f, List<Plane3f>> mPlanesForLines;
    private Map<Plane3f, List<Line3f>> mLinesForPlanes;
    private Map<Plane3f, List<Point3f>> mPointsForPlanes;

    public Poly3f() {
        mPlanes = new ArrayList<Plane3f>();
        mLines = new ArrayList<Line3f>();
        mPoints = new ArrayList<Point3f>();
        mPolys = new ArrayList<List<Point3f>>();
        mCenters = new ArrayList<Point3f>();
        mAngles = new ArrayList<List<Integer>>();
        mPlanesForPlanes = new HashMap<Plane3f, List<Plane3f>>();
        mPlanesForLines = new HashMap<Line3f, List<Plane3f>>();
        mLinesForPlanes = new HashMap<Plane3f, List<Line3f>>();
        mPointsForPlanes = new HashMap<Plane3f, List<Point3f>>();
    }

    public List<Plane3f> getPlanes() {
        return mPlanes;
    }

    public void setPlanes(List<Plane3f> planes) {
        mPlanes = planes;
    }

    public List<Line3f> getLines() {
        return mLines;
    }

    public void setLines(List<Line3f> lines) {
        mLines = lines;
    }

    public List<Point3f> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point3f> points) {
        mPoints = points;
    }

    public List<List<Point3f>> getPolys() {
        return mPolys;
    }

    public void setPolys(List<List<Point3f>> polys) {
        mPolys = polys;
    }

    public List<Point3f> getCenters() {
        return mCenters;
    }

    public void setCenters(List<Point3f> centers) {
        mCenters = centers;
    }

    public List<List<Integer>> getAngles() {
        return mAngles;
    }

    public void setAngles(List<List<Integer>> angles) {
        mAngles = angles;
    }

    public Map<Line3f, List<Plane3f>> getPlanesForLines() {
        return mPlanesForLines;
    }

    public void setPlanesForLines(Map<Line3f, List<Plane3f>> planesForLines) {
        mPlanesForLines = planesForLines;
    }

    public Map<Plane3f, List<Line3f>> getLinesForPlanes() {
        return mLinesForPlanes;
    }

    public void setLinesForPlanes(Map<Plane3f, List<Line3f>> linesForPlanes) {
        mLinesForPlanes = linesForPlanes;
    }

    public Map<Plane3f, List<Point3f>> getPointsForPlanes() {
        return mPointsForPlanes;
    }

    public void setPointsForPlanes(Map<Plane3f, List<Point3f>> pointsForPlanes) {
        mPointsForPlanes = pointsForPlanes;
    }

    public Map<Plane3f, List<Plane3f>> getPlanesForPlanes() {
        return mPlanesForPlanes;
    }

    public void setPlanesForPlanes(Map<Plane3f, List<Plane3f>> planesForPlanes) {
        mPlanesForPlanes = planesForPlanes;
    }

}
