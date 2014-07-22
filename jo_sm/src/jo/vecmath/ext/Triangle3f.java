package jo.vecmath.ext;

import java.awt.image.BufferedImage;

import jo.vecmath.Color3f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;

public class Triangle3f {

    private Point3f mA;
    private Point3f mB;
    private Point3f mC;
    private Point2f mAUV;
    private Point2f mBUV;
    private Point2f mCUV;
    private BufferedImage mTexture;
    private Color3f mColor;

    public Triangle3f() {
        mA = new Point3f();
        mB = new Point3f(1, 0, 0);
        mC = new Point3f(0, 1, 0);
    }

    public Triangle3f(Point3f a, Point3f b, Point3f c) {
        this();
        mA.set(a);
        mB.set(b);
        mC.set(c);
    }

    public Triangle3f(Triangle3f l) {
        this(l.getA(), l.getB(), l.getC());
    }

    public String toString() {
        return mA.toString() + "--" + mB.toString() + "--" + mC.toString();
    }

    public Point3f getA() {
        return mA;
    }

    public void setA(Point3f a) {
        mA = a;
    }

    public Point3f getB() {
        return mB;
    }

    public void setB(Point3f b) {
        mB = b;
    }

    public Point3f getC() {
        return mC;
    }

    public void setC(Point3f c) {
        mC = c;
    }

    public Color3f getColor() {
        return mColor;
    }

    public void setColor(Color3f color) {
        mColor = color;
    }

    public Point2f getAUV() {
        return mAUV;
    }

    public void setAUV(Point2f aUV) {
        mAUV = aUV;
    }

    public Point2f getBUV() {
        return mBUV;
    }

    public void setBUV(Point2f bUV) {
        mBUV = bUV;
    }

    public Point2f getCUV() {
        return mCUV;
    }

    public void setCUV(Point2f cUV) {
        mCUV = cUV;
    }

    public BufferedImage getTexture() {
        return mTexture;
    }

    public void setTexture(BufferedImage texture) {
        mTexture = texture;
    }
}
