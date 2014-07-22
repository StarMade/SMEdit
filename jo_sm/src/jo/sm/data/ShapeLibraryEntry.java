
package jo.sm.data;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ShapeLibraryEntry {

    private static int mNextUNID = 1;

    private int mUNID;
    private File mShape;
    private long mLastRead;
    private String mName;
    private String mAuthor;
    private Point3i mLower;
    private Point3i mUpper;
    private Set<Integer> mClassifications;

    public ShapeLibraryEntry() {
        mClassifications = new HashSet<>();
        mUNID = mNextUNID++;
    }

    public File getShape() {
        return mShape;
    }

    public void setShape(File shape) {
        mShape = shape;
    }

    public long getLastRead() {
        return mLastRead;
    }

    public void setLastRead(long lastRead) {
        mLastRead = lastRead;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public Point3i getLower() {
        return mLower;
    }

    public void setLower(Point3i lower) {
        mLower = lower;
    }

    public Point3i getUpper() {
        return mUpper;
    }

    public void setUpper(Point3i upper) {
        mUpper = upper;
    }

    public Set<Integer> getClassifications() {
        return mClassifications;
    }

    public void setClassifications(Set<Integer> classifications) {
        mClassifications = classifications;
    }

    public int getUNID() {
        return mUNID;
    }

    public void setUNID(int uNID) {
        mUNID = uNID;
    }
}
