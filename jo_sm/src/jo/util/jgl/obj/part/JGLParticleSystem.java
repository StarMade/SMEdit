/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl.obj.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.vecmath.Color4f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Vector3f;
import jo.vecmath.logic.ITransformer;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.QuadraticTransformer;

public class JGLParticleSystem extends JGLGroup {

    public static final int MAX_PARTICLES = 2000;

    // values for mPsysPartFlags
    // When set, particle color and alpha transition from their START settings to their END settings during the particle's lifetime. 
    // The transition is a smooth interpolation. 
    public static final int PART_INTERP_COLOR_MASK = 0x001;
    //  When set, particle size/scale transitions from its START setting to its END setting during the particle's lifetime. 
    public static final int PART_INTERP_SCALE_MASK = 0x002;

    // values for mPsysSrcPattern
    // Sprays particles outwards in a spherical area. The Initial velocity of each particle is determined by SRC_BURST_SPEED_MIN and SRC_BURST_SPEED_MAX. The EXPLODE pattern ignores the ANGLE parameters.
    public static final int SRC_PATTERN_EXPLODE = 0x02;
    // Sprays particles outwards in a spherical, sub-spherical, conical or ring shaped area, as defined by the ANGLE parameters SRC_ANGLE_BEGIN and SRC_ANGLE_END. The ANGLE_CONE pattern can be used to imitate the EXPLODE pattern by explicitly setting SRC_ANGLE_BEGIN to 0.00000 and SRC_ANGLE_END to 3.14159 (or PI) (or vice versa).
    public static final int SRC_PATTERN_ANGLE_CONE = 0x08;
    // Sprays particles outward in a flat circular, semi-circular, arc or ray shaped areas, as defined by SRC_ANGLE_BEGIN and SRC_ANGLE_END. The circular pattern radiates outwards around the prim's local X axis line.
    public static final int SRC_PATTERN_ANGLE = 0x04;
    // Creates particles with no initial velocity. The DROP pattern will override any values given for SRC_BURST_RADIUS, SRC_BURST_SPEED_MIN, and SRC_BURST_SPEED_MAX, setting each to 0.00000. (All patterns will behave like the DROP pattern, if RADIUS, SPEED_MIN and SPEED_MAX are explicitly set to 0.0000.)
    public static final int SRC_PATTERN_DROP = 0x01;
    //  (incomplete implementation) acts the same as the SRC_PATTERN_DROP pattern, it is believed that the original intention for this pattern was to invert the effect of the ANGLE parameters, making them delineate an area where particles were NOT to be sprayed. (effectively the inverse or opposite of the behavior of the ANGLE_CONE pattern).
    public static final int SRC_PATTERN_ANGLE_CONE_EMPTY = 0x10;

    private int mPartFlags;
    private int mSrcPattern;
    // Specifies the distance from the emitter where particles will be created. This rule is ignored when the PART_FOLLOW_SRC_MASK flag is set.
    // A test in http://forums-archive.secondlife.com/327/f5/226722/1.html indicates that the maximum value is 50.00 
    private float mSrcBurstRadius;
    //  Specifies a half angle, in radians, of a circular or spherical "dimple" or conic section (starting from the emitter facing) within which particles 
    // will NOT be emitted. Valid values are the same as for SRC_ANGLE_END, though the effects are reversed accordingly. If the pattern 
    // is SRC_PATTERN_ANGLE, the presentation is a 2D flat circular section. If SRC_PATTERN_ANGLE_CONE or SRC_PATTERN_ANGLE_CONE_EMPTY is used, 
    // the presentation is a 3D spherical section. Note that the value of this parameter and SRC_ANGLE_END are internally re-ordered such that this 
    // parameter gets the smaller of the two values. 
    private float mSrcAngleBegin;
    // Specifies a half angle, in radians, of a circular or spherical "dimple" or conic section (starting from the emitter facing) within which 
    // particles WILL be emitted. Valid values are 0.0, which will result in particles being emitted in a straight line in the direction of the 
    // emitter facing, to PI, which will result in particles being emitted in a full circular or spherical arc around the emitter, not including 
    // the "dimple" or conic section defined by PSYS_SRC_ANGLE_BEGIN. If the pattern is PSYS_SRC_PATTERN_ANGLE, the presentation is a 2D flat 
    // circular section. If PSYS_SRC_PATTERN_ANGLE_CONE or PSYS_SRC_PATTERN_ANGLE_CONE_EMPTY is used, the presentation is a 3D spherical section. 
    // Note that the value of this parameter and PSYS_SRC_ANGLE_BEGIN are internally re-ordered such that this parameter gets the larger of the two values. 
    private float mSrcAngleEnd;
    private Color4f mPartStartColor;
    private Color4f mPartEndColor;
    private float mPartStartScale;
    private float mPartEndScale;
    private int mTextureID;
    //  Specifies the length of time, in seconds, that the emitter will operate upon coming into view range (if the particle system is already set) 
    // or upon execution of this function (if already in view range). Upon expiration, no more particles will be emitted, except as specified above. 
    // Zero will give the particle system an infinite duration
    private float mSrcMaxAge;
    // Specifies the lifetime of each particle emitted, in seconds. Maximum is 30.0 seconds. During this time, the particle will appear, 
    // change appearance and move according to the parameters specified in the other sections, and then disappear. 
    private float mPartMaxAge;
    // Specifies the time interval, in seconds, between "bursts" of particles being emitted. 
    // Specifying a value of 0.0 will cause the emission of particles as fast as the viewer can do so. 
    private float mSrcBurstRate;
    //  Specifies the number of particles emitted in each "burst". 
    private int mSrcBurstPartCount;
    // Specifies a directional acceleration vector applied to each particle as it is emitted, in meters per second.
    // Valid values are 0.0 to 100.0 for each direction both positive and negative, as region coordinates. 
    private Vector3f mSrcAccel;
    //  Specifies the rotational spin of the emitter in radians per second along each axis. This "unsticks" the emitter facing from the prim's
    // positive Z axis and is noticeable in directional presentations. Prim spin (via llTargetOmega) has no effect on emitter spin.
    private Point3f mSrcOmega;
    // Specifies the minimum value of a random range of values which is selected for each particle in a burst as its initial speed upon emission, 
    // in meters per second. Note that the value of this parameter and PSYS_SRC_BURST_SPEED_MAX are internally re-ordered such that this parameter
    // gets the smaller of the two values. 
    private float mSrcBurstSpeedMin;
    // Specifies the maximum value of a random range of values which is selected for each particle in a burst as its initial speed upon emission, 
    // in meters per second. Note that the value of this parameter and PSYS_SRC_BURST_SPEED_MIN are internally re-ordered such that this parameter 
    // gets the larger of the two values. 
    private float mSrcBurstSpeedMax;

    private long mNextBurst;
    private List<JGLObjParticle> mChildByAge;
    private List<Long> mChildAge;

    private static int mTotalParticles = 0;
    private static final Random mRND = new Random();

    public JGLParticleSystem() {
        mPartMaxAge = 1.0f;
        mSrcBurstRadius = 0;
        mSrcBurstRate = 1;
        mSrcBurstPartCount = 10;
        mPartStartScale = 1;
        mPartEndScale = 1;
        mSrcBurstSpeedMin = 0;
        mSrcBurstSpeedMax = 0;
        mChildAge = new ArrayList<>();
        mChildByAge = new ArrayList<>();
        mSrcAccel = new Vector3f();
        setTransformer(new ITransformer() {
            @Override
            public Matrix4f calcTransform(Matrix4f transform) {
                updateParticles();
                return transform;
            }
        });
    }

    public void updateParticles() {
        long now = System.currentTimeMillis();
        expireOldParticles(now);
        if (mNextBurst < now) {
            doBurst(now);
        }
        orderParticles(now);
    }

    private void orderParticles(long now) {
        final Map<JGLNode, Float> zorder = new HashMap<>();
        for (JGLNode p : mChildren) {
            Matrix4f t = p.calcTransform(now);
            zorder.put(p, t.m23);
        }
        Collections.sort(mChildren, new Comparator<JGLNode>() {
            @Override
            public int compare(JGLNode o1, JGLNode o2) {
                float z1 = zorder.get(o1);
                float z2 = zorder.get(o2);
                return (int) Math.signum(z1 - z2);
            }
        });
    }

    private void doBurst(long now) {
        mNextBurst = now + (long) (mSrcBurstRate * 1000);
        for (int i = 0; i < mSrcBurstPartCount; i++) {
            addParticle(now);
        }
    }

    private void addParticle(long now) {
        if (mTotalParticles >= MAX_PARTICLES) {
            return;
        }
        long expires = now + (long) (mPartMaxAge * 1000);
        JGLObjParticle particle = new JGLObjParticle();
        particle.setTextureID(mTextureID);
        particle.setStart(now);
        particle.setEnd(expires);
        particle.setStartColor(mPartStartColor);
        particle.setEndColor(mPartEndColor);
        QuadraticTransformer tr = new QuadraticTransformer();
        tr.setStartTime(now);
        if (!MathUtils.epsilonEquals(mPartStartScale, mPartEndScale)) {
            tr.setScaleQuad(new Point3f(0, (mPartEndScale - mPartStartScale) / mPartMaxAge, mPartStartScale));
        }
        Vector3f s = getDirectionVector();
        Vector3f u = getVelocityVector(s);
        Vector3f a = getAccelerationVector(s);
        tr.setXQuad(new Point3f(.5f * a.x, u.x, s.x));
        tr.setYQuad(new Point3f(.5f * a.y, u.y, s.y));
        tr.setZQuad(new Point3f(.5f * a.z, u.z, s.z));
        particle.setTransformer(tr);
        add(particle);
        mChildAge.add(expires);
        mChildByAge.add(particle);
        mTotalParticles++;
        //System.out.println("Add particle until "+expires);
        //System.out.println("  s="+s+", u="+u+", a="+a);
    }

    private Vector3f getDirectionVector() {
        // take into account mPsysSrcPattern, only support SRC_PATTERN_EXPLODE for now
        Vector3f v = new Vector3f(mRND.nextFloat() - .5f, mRND.nextFloat() - .5f, mRND.nextFloat() - .5f);
        v.normalize();
        v.scale(mSrcBurstRadius);
        return v;
    }

    private Vector3f getVelocityVector(Vector3f s) {
        Vector3f v = new Vector3f(s);
        v.scale((mSrcBurstSpeedMax - mSrcBurstSpeedMin) * mRND.nextFloat() + mSrcBurstSpeedMin);
        return v;
    }

    private Vector3f getAccelerationVector(Vector3f s) {
        return mSrcAccel;
    }

    private void expireOldParticles(long now) {
        while (mChildren.size() > 0) {
            long expires = mChildAge.get(0);
            if (expires > now) {
                break;
            }
            mChildren.remove(mChildByAge.get(0));
            mChildAge.remove(0);
            mChildByAge.remove(0);
            mTotalParticles--;
            //System.out.println("Expire particle "+expires);
        }
    }

    public int getPartFlags() {
        return mPartFlags;
    }

    public void setPartFlags(int partFlags) {
        mPartFlags = partFlags;
    }

    public int getSrcPattern() {
        return mSrcPattern;
    }

    public void setSrcPattern(int srcPattern) {
        mSrcPattern = srcPattern;
    }

    public float getSrcBurstRadius() {
        return mSrcBurstRadius;
    }

    public void setSrcBurstRadius(float srcBurstRadius) {
        mSrcBurstRadius = srcBurstRadius;
    }

    public float getSrcAngleBegin() {
        return mSrcAngleBegin;
    }

    public void setSrcAngleBegin(float srcAngleBegin) {
        mSrcAngleBegin = srcAngleBegin;
    }

    public float getSrcAngleEnd() {
        return mSrcAngleEnd;
    }

    public void setSrcAngleEnd(float srcAngleEnd) {
        mSrcAngleEnd = srcAngleEnd;
    }

    public Color4f getPartStartColor() {
        return mPartStartColor;
    }

    public void setPartStartColor(Color4f partStartColor) {
        mPartStartColor = partStartColor;
    }

    public Color4f getPartEndColor() {
        return mPartEndColor;
    }

    public void setPartEndColor(Color4f partEndColor) {
        mPartEndColor = partEndColor;
    }

    public float getPartStartScale() {
        return mPartStartScale;
    }

    public void setPartStartScale(float partStartScale) {
        mPartStartScale = partStartScale;
    }

    public float getPartEndScale() {
        return mPartEndScale;
    }

    public void setPartEndScale(float partEndScale) {
        mPartEndScale = partEndScale;
    }

    public int getTextureID() {
        return mTextureID;
    }

    public void setTextureID(int textureID) {
        mTextureID = textureID;
    }

    public float getSrcMaxAge() {
        return mSrcMaxAge;
    }

    public void setSrcMaxAge(float srcMaxAge) {
        mSrcMaxAge = srcMaxAge;
    }

    public float getPartMaxAge() {
        return mPartMaxAge;
    }

    public void setPartMaxAge(float partMaxAge) {
        mPartMaxAge = partMaxAge;
    }

    public float getSrcBurstRate() {
        return mSrcBurstRate;
    }

    public void setSrcBurstRate(float srcBurstRate) {
        mSrcBurstRate = srcBurstRate;
    }

    public int getSrcBurstPartCount() {
        return mSrcBurstPartCount;
    }

    public void setSrcBurstPartCount(int srcBurstPartCount) {
        mSrcBurstPartCount = srcBurstPartCount;
    }

    public Vector3f getSrcAccel() {
        return mSrcAccel;
    }

    public void setSrcAccel(Vector3f srcAccel) {
        mSrcAccel = srcAccel;
    }

    public Point3f getSrcOmega() {
        return mSrcOmega;
    }

    public void setSrcOmega(Point3f srcOmega) {
        mSrcOmega = srcOmega;
    }

    public float getSrcBurstSpeedMin() {
        return mSrcBurstSpeedMin;
    }

    public void setSrcBurstSpeedMin(float srcBurstSpeedMin) {
        mSrcBurstSpeedMin = srcBurstSpeedMin;
    }

    public float getSrcBurstSpeedMax() {
        return mSrcBurstSpeedMax;
    }

    public void setSrcBurstSpeedMax(float srcBurstSpeedMax) {
        mSrcBurstSpeedMax = srcBurstSpeedMax;
    }
}
