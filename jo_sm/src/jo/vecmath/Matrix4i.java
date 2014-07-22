/*
 * $RCSfile: Matrix4f.java,v $
 *
 * Copyright 1996-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 * $Revision: 1.8 $
 * $Date: 2008/02/28 20:18:50 $
 * $State: Exp $
 */
package jo.vecmath;

/**
 * A single precision floating point 4 by 4 matrix. Primarily to support 3D
 * rotations.
 *
 */
public class Matrix4i implements java.io.Serializable, Cloneable {

    // Compatible with 1.1
    static final long serialVersionUID = -8405036035410109353L;

    /**
     * The first element of the first row.
     */
    public int m00;

    /**
     * The second element of the first row.
     */
    public int m01;

    /**
     * The third element of the first row.
     */
    public int m02;

    /**
     * The fourth element of the first row.
     */
    public int m03;

    /**
     * The first element of the second row.
     */
    public int m10;

    /**
     * The second element of the second row.
     */
    public int m11;

    /**
     * The third element of the second row.
     */
    public int m12;

    /**
     * The fourth element of the second row.
     */
    public int m13;

    /**
     * The first element of the third row.
     */
    public int m20;

    /**
     * The second element of the third row.
     */
    public int m21;

    /**
     * The third element of the third row.
     */
    public int m22;

    /**
     * The fourth element of the third row.
     */
    public int m23;

    /**
     * The first element of the fourth row.
     */
    public int m30;

    /**
     * The second element of the fourth row.
     */
    public int m31;

    /**
     * The third element of the fourth row.
     */
    public int m32;

    /**
     * The fourth element of the fourth row.
     */
    public int m33;
    /*
     double[] tmp = new double[9];
     double[] tmp_scale = new double[3];
     double[] tmp_rot = new double[9];
     */
    private static final double EPS = 1.0E-8;

    /**
     * Constructs and initializes a Matrix4f from the specified 16 values.
     *
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m03 the [0][3] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m13 the [1][3] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     * @param m23 the [2][3] element
     * @param m30 the [3][0] element
     * @param m31 the [3][1] element
     * @param m32 the [3][2] element
     * @param m33 the [3][3] element
     */
    public Matrix4i(int m00, int m01, int m02, int m03,
            int m10, int m11, int m12, int m13,
            int m20, int m21, int m22, int m23,
            int m30, int m31, int m32, int m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;

        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;

        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;

        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;

    }

    /**
     * Constructs and initializes a Matrix4f from the specified 16 element
     * array. this.m00 =v[0], this.m01=v[1], etc.
     *
     * @param v the array of length 16 containing in order
     */
    public Matrix4i(int[] v) {
        this.m00 = v[ 0];
        this.m01 = v[ 1];
        this.m02 = v[ 2];
        this.m03 = v[ 3];

        this.m10 = v[ 4];
        this.m11 = v[ 5];
        this.m12 = v[ 6];
        this.m13 = v[ 7];

        this.m20 = v[ 8];
        this.m21 = v[ 9];
        this.m22 = v[10];
        this.m23 = v[11];

        this.m30 = v[12];
        this.m31 = v[13];
        this.m32 = v[14];
        this.m33 = v[15];

    }

    /**
     * Constructs and initializes a Matrix4f from the quaternion, translation,
     * and scale values; the scale is applied only to the rotational components
     * of the matrix (upper 3x3) and not to the translational components.
     *
     * @param q1 the quaternion value representing the rotational component
     * @param t1 the translational component of the matrix
     * @param s the scale value applied to the rotational components
     */
    public Matrix4i(Point4i q1, Point3i t1, int s) {
        m00 = (int) (s * (1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z));
        m10 = (int) (s * (2.0 * (q1.x * q1.y + q1.w * q1.z)));
        m20 = (int) (s * (2.0 * (q1.x * q1.z - q1.w * q1.y)));

        m01 = (int) (s * (2.0 * (q1.x * q1.y - q1.w * q1.z)));
        m11 = (int) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z));
        m21 = (int) (s * (2.0 * (q1.y * q1.z + q1.w * q1.x)));

        m02 = (int) (s * (2.0 * (q1.x * q1.z + q1.w * q1.y)));
        m12 = (int) (s * (2.0 * (q1.y * q1.z - q1.w * q1.x)));
        m22 = (int) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y));

        m03 = t1.x;
        m13 = t1.y;
        m23 = t1.z;

        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;

    }

    /**
     * Constructs a new matrix with the same values as the Matrix4d parameter.
     *
     * @param m1 the source matrix
     */
    public Matrix4i(Matrix4d m1) {
        this.m00 = (int) m1.m00;
        this.m01 = (int) m1.m01;
        this.m02 = (int) m1.m02;
        this.m03 = (int) m1.m03;

        this.m10 = (int) m1.m10;
        this.m11 = (int) m1.m11;
        this.m12 = (int) m1.m12;
        this.m13 = (int) m1.m13;

        this.m20 = (int) m1.m20;
        this.m21 = (int) m1.m21;
        this.m22 = (int) m1.m22;
        this.m23 = (int) m1.m23;

        this.m30 = (int) m1.m30;
        this.m31 = (int) m1.m31;
        this.m32 = (int) m1.m32;
        this.m33 = (int) m1.m33;

    }

    /**
     * Constructs a new matrix with the same values as the Matrix4f parameter.
     *
     * @param m1 the source matrix
     */
    public Matrix4i(Matrix4i m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;

        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;

        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;

        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;

    }

    /**
     * Constructs and initializes a Matrix4f from the rotation matrix,
     * translation, and scale values; the scale is applied only to the
     * rotational components of the matrix (upper 3x3) and not to the
     * translational components of the matrix.
     *
     * @param m1 the rotation matrix representing the rotational components
     * @param t1 the translational components of the matrix
     * @param s the scale value applied to the rotational components
     */
    public Matrix4i(Matrix3i m1, Point3i t1, int s) {
        this.m00 = m1.m00 * s;
        this.m01 = m1.m01 * s;
        this.m02 = m1.m02 * s;
        this.m03 = t1.x;

        this.m10 = m1.m10 * s;
        this.m11 = m1.m11 * s;
        this.m12 = m1.m12 * s;
        this.m13 = t1.y;

        this.m20 = m1.m20 * s;
        this.m21 = m1.m21 * s;
        this.m22 = m1.m22 * s;
        this.m23 = t1.z;

        this.m30 = 0;
        this.m31 = 0;
        this.m32 = 0;
        this.m33 = 1;

    }

    /**
     * Constructs and initializes a Matrix4f to all zeros.
     */
    public Matrix4i() {
        this.m00 = (int) 0.0;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = (int) 0.0;

        this.m10 = (int) 0.0;
        this.m11 = (int) 0.0;
        this.m12 = (int) 0.0;
        this.m13 = (int) 0.0;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = (int) 0.0;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 0.0;

    }

    /**
     * Returns a string that contains the values of this Matrix4f.
     *
     * @return the String representation
     */
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n"
                + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n"
                + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n"
                + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }

    /**
     * Sets this Matrix4f to identity.
     */
    public final void setIdentity() {
        this.m00 = (int) 1.0;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = (int) 0.0;

        this.m10 = (int) 0.0;
        this.m11 = (int) 1.0;
        this.m12 = (int) 0.0;
        this.m13 = (int) 0.0;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = (int) 1.0;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the specified element of this matrix4f to the value provided.
     *
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param value the new value
     */
    public final void setElement(int row, int column, int value) {
        switch (row) {
            case 0:
                switch (column) {
                    case 0:
                        this.m00 = value;
                        break;
                    case 1:
                        this.m01 = value;
                        break;
                    case 2:
                        this.m02 = value;
                        break;
                    case 3:
                        this.m03 = value;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;

            case 1:
                switch (column) {
                    case 0:
                        this.m10 = value;
                        break;
                    case 1:
                        this.m11 = value;
                        break;
                    case 2:
                        this.m12 = value;
                        break;
                    case 3:
                        this.m13 = value;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;

            case 2:
                switch (column) {
                    case 0:
                        this.m20 = value;
                        break;
                    case 1:
                        this.m21 = value;
                        break;
                    case 2:
                        this.m22 = value;
                        break;
                    case 3:
                        this.m23 = value;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;

            case 3:
                switch (column) {
                    case 0:
                        this.m30 = value;
                        break;
                    case 1:
                        this.m31 = value;
                        break;
                    case 2:
                        this.m32 = value;
                        break;
                    case 3:
                        this.m33 = value;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
        }
    }

    /**
     * Retrieves the value at the specified row and column of this matrix.
     *
     * @param row the row number to be retrieved (zero indexed)
     * @param column the column number to be retrieved (zero indexed)
     * @return the value at the indexed element
     */
    public final int getElement(int row, int column) {
        switch (row) {
            case 0:
                switch (column) {
                    case 0:
                        return (this.m00);
                    case 1:
                        return (this.m01);
                    case 2:
                        return (this.m02);
                    case 3:
                        return (this.m03);
                    default:
                        break;
                }
                break;
            case 1:
                switch (column) {
                    case 0:
                        return (this.m10);
                    case 1:
                        return (this.m11);
                    case 2:
                        return (this.m12);
                    case 3:
                        return (this.m13);
                    default:
                        break;
                }
                break;

            case 2:
                switch (column) {
                    case 0:
                        return (this.m20);
                    case 1:
                        return (this.m21);
                    case 2:
                        return (this.m22);
                    case 3:
                        return (this.m23);
                    default:
                        break;
                }
                break;

            case 3:
                switch (column) {
                    case 0:
                        return (this.m30);
                    case 1:
                        return (this.m31);
                    case 2:
                        return (this.m32);
                    case 3:
                        return (this.m33);
                    default:
                        break;
                }
                break;

            default:
                break;
        }
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
    }

    /**
     * Copies the matrix values in the specified row into the vector parameter.
     *
     * @param row the matrix row
     * @param v the vector into which the matrix row values will be copied
     */
    public final void getRow(int row, Point4i v) {
        if (row == 0) {
            v.x = m00;
            v.y = m01;
            v.z = m02;
            v.w = m03;
        } else if (row == 1) {
            v.x = m10;
            v.y = m11;
            v.z = m12;
            v.w = m13;
        } else if (row == 2) {
            v.x = m20;
            v.y = m21;
            v.z = m22;
            v.w = m23;
        } else if (row == 3) {
            v.x = m30;
            v.y = m31;
            v.z = m32;
            v.w = m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }

    }

    /**
     * Copies the matrix values in the specified row into the array parameter.
     *
     * @param row the matrix row
     * @param v the array into which the matrix row values will be copied
     */
    public final void getRow(int row, int v[]) {
        if (row == 0) {
            v[0] = m00;
            v[1] = m01;
            v[2] = m02;
            v[3] = m03;
        } else if (row == 1) {
            v[0] = m10;
            v[1] = m11;
            v[2] = m12;
            v[3] = m13;
        } else if (row == 2) {
            v[0] = m20;
            v[1] = m21;
            v[2] = m22;
            v[3] = m23;
        } else if (row == 3) {
            v[0] = m30;
            v[1] = m31;
            v[2] = m32;
            v[3] = m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }

    }

    /**
     * Copies the matrix values in the specified column into the vector
     * parameter.
     *
     * @param column the matrix column
     * @param v the vector into which the matrix row values will be copied
     */
    public final void getColumn(int column, Point4i v) {
        if (column == 0) {
            v.x = m00;
            v.y = m10;
            v.z = m20;
            v.w = m30;
        } else if (column == 1) {
            v.x = m01;
            v.y = m11;
            v.z = m21;
            v.w = m31;
        } else if (column == 2) {
            v.x = m02;
            v.y = m12;
            v.z = m22;
            v.w = m32;
        } else if (column == 3) {
            v.x = m03;
            v.y = m13;
            v.z = m23;
            v.w = m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }

    }

    /**
     * Copies the matrix values in the specified column into the array
     * parameter.
     *
     * @param column the matrix column
     * @param v the array into which the matrix row values will be copied
     */
    public final void getColumn(int column, int v[]) {
        if (column == 0) {
            v[0] = m00;
            v[1] = m10;
            v[2] = m20;
            v[3] = m30;
        } else if (column == 1) {
            v[0] = m01;
            v[1] = m11;
            v[2] = m21;
            v[3] = m31;
        } else if (column == 2) {
            v[0] = m02;
            v[1] = m12;
            v[2] = m22;
            v[3] = m32;
        } else if (column == 3) {
            v[0] = m03;
            v[1] = m13;
            v[2] = m23;
            v[3] = m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }

    }

    /**
     * Sets the scale component of the current matrix by factoring out the
     * current scale (by doing an SVD) from the rotational component and
     * multiplying by the new scale.
     *
     * @param scale the new scale amount
     */
    public final void setScale(int scale) {

        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix
        getScaleRotate(tmp_scale, tmp_rot);

        m00 = (int) (tmp_rot[0] * scale);
        m01 = (int) (tmp_rot[1] * scale);
        m02 = (int) (tmp_rot[2] * scale);

        m10 = (int) (tmp_rot[3] * scale);
        m11 = (int) (tmp_rot[4] * scale);
        m12 = (int) (tmp_rot[5] * scale);

        m20 = (int) (tmp_rot[6] * scale);
        m21 = (int) (tmp_rot[7] * scale);
        m22 = (int) (tmp_rot[8] * scale);

    }

    /**
     * Performs an SVD normalization of this matrix in order to acquire the
     * normalized rotational component; the values are placed into the Matrix3d
     * parameter.
     *
     * @param m1 matrix into which the rotational component is placed
     */
    public final void get(Matrix3d m1) {

        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m1.m00 = tmp_rot[0];
        m1.m01 = tmp_rot[1];
        m1.m02 = tmp_rot[2];

        m1.m10 = tmp_rot[3];
        m1.m11 = tmp_rot[4];
        m1.m12 = tmp_rot[5];

        m1.m20 = tmp_rot[6];
        m1.m21 = tmp_rot[7];
        m1.m22 = tmp_rot[8];

    }

    /**
     * Performs an SVD normalization of this matrix in order to acquire the
     * normalized rotational component; the values are placed into the Matrix3i
     * parameter.
     *
     * @param m1 matrix into which the rotational component is placed
     */
    public final void get(Matrix3i m1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m1.m00 = (int) tmp_rot[0];
        m1.m01 = (int) tmp_rot[1];
        m1.m02 = (int) tmp_rot[2];

        m1.m10 = (int) tmp_rot[3];
        m1.m11 = (int) tmp_rot[4];
        m1.m12 = (int) tmp_rot[5];

        m1.m20 = (int) tmp_rot[6];
        m1.m21 = (int) tmp_rot[7];
        m1.m22 = (int) tmp_rot[8];

    }

    /**
     * Performs an SVD normalization of this matrix to calculate the rotation as
     * a 3x3 matrix, the translation, and the scale. None of the matrix values
     * are modified.
     *
     * @param m1 the normalized matrix representing the rotation
     * @param t1 the translation component
     * @return the scale component of this transform
     */
    public final int get(Matrix3i m1, Point3i t1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m1.m00 = (int) tmp_rot[0];
        m1.m01 = (int) tmp_rot[1];
        m1.m02 = (int) tmp_rot[2];

        m1.m10 = (int) tmp_rot[3];
        m1.m11 = (int) tmp_rot[4];
        m1.m12 = (int) tmp_rot[5];

        m1.m20 = (int) tmp_rot[6];
        m1.m21 = (int) tmp_rot[7];
        m1.m22 = (int) tmp_rot[8];

        t1.x = m03;
        t1.y = m13;
        t1.z = m23;

        return ((int) Matrix3d.max3(tmp_scale));

    }

    /**
     * Performs an SVD normalization of this matrix in order to acquire the
     * normalized rotational component; the values are placed into the Point4i
     * parameter.
     *
     * @param q1 quaternion into which the rotation component is placed
     */
    public final void get(Point4i q1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix
        getScaleRotate(tmp_scale, tmp_rot);

        double ww;

        ww = 0.25 * (1.0 + tmp_rot[0] + tmp_rot[4] + tmp_rot[8]);
        if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
            q1.w = (int) Math.sqrt(ww);
            ww = 0.25 / q1.w;
            q1.x = (int) ((tmp_rot[7] - tmp_rot[5]) * ww);
            q1.y = (int) ((tmp_rot[2] - tmp_rot[6]) * ww);
            q1.z = (int) ((tmp_rot[3] - tmp_rot[1]) * ww);
            return;
        }

        q1.w = 0;
        ww = -0.5 * (tmp_rot[4] + tmp_rot[8]);
        if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
            q1.x = (int) Math.sqrt(ww);
            ww = 0.5 / q1.x;
            q1.y = (int) (tmp_rot[3] * ww);
            q1.z = (int) (tmp_rot[6] * ww);
            return;
        }

        q1.x = 0;
        ww = 0.5 * (1.0 - tmp_rot[8]);
        if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
            q1.y = (int) (Math.sqrt(ww));
            q1.z = (int) (tmp_rot[7] / (2.0 * q1.y));
            return;
        }

        q1.y = 0;
        q1.z = 1;

    }

    /**
     * Retrieves the translational components of this matrix.
     *
     * @param trans the vector that will receive the translational component
     */
    public final void get(Point3i trans) {
        trans.x = m03;
        trans.y = m13;
        trans.z = m23;
    }

    /**
     * Gets the upper 3x3 values of this matrix and places them into the matrix
     * m1.
     *
     * @param m1 the matrix that will hold the values
     */
    public final void getRotationScale(Matrix3i m1) {
        m1.m00 = m00;
        m1.m01 = m01;
        m1.m02 = m02;
        m1.m10 = m10;
        m1.m11 = m11;
        m1.m12 = m12;
        m1.m20 = m20;
        m1.m21 = m21;
        m1.m22 = m22;
    }

    /**
     * Performs an SVD normalization of this matrix to calculate and return the
     * uniform scale factor. If the matrix has non-uniform scale factors, the
     * largest of the x, y, and z scale factors will be returned. This matrix is
     * not modified.
     *
     * @return the scale factor of this matrix
     */
    public final int getScale() {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        return ((int) Matrix3d.max3(tmp_scale));

    }

    /**
     * Replaces the upper 3x3 matrix values of this matrix with the values in
     * the matrix m1.
     *
     * @param m1 the matrix that will be the new upper 3x3
     */
    public final void setRotationScale(Matrix3i m1) {
        m00 = m1.m00;
        m01 = m1.m01;
        m02 = m1.m02;
        m10 = m1.m10;
        m11 = m1.m11;
        m12 = m1.m12;
        m20 = m1.m20;
        m21 = m1.m21;
        m22 = m1.m22;
    }

    /**
     * Sets the specified row of this matrix4f to the four values provided.
     *
     * @param row the row number to be modified (zero indexed)
     * @param x the first column element
     * @param y the second column element
     * @param z the third column element
     * @param w the fourth column element
     */
    public final void setRow(int row, int x, int y, int z, int w) {
        switch (row) {
            case 0:
                this.m00 = x;
                this.m01 = y;
                this.m02 = z;
                this.m03 = w;
                break;

            case 1:
                this.m10 = x;
                this.m11 = y;
                this.m12 = z;
                this.m13 = w;
                break;

            case 2:
                this.m20 = x;
                this.m21 = y;
                this.m22 = z;
                this.m23 = w;
                break;

            case 3:
                this.m30 = x;
                this.m31 = y;
                this.m32 = z;
                this.m33 = w;
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    /**
     * Sets the specified row of this matrix4f to the Vector provided.
     *
     * @param row the row number to be modified (zero indexed)
     * @param v the replacement row
     */
    public final void setRow(int row, Point4i v) {
        switch (row) {
            case 0:
                this.m00 = v.x;
                this.m01 = v.y;
                this.m02 = v.z;
                this.m03 = v.w;
                break;

            case 1:
                this.m10 = v.x;
                this.m11 = v.y;
                this.m12 = v.z;
                this.m13 = v.w;
                break;

            case 2:
                this.m20 = v.x;
                this.m21 = v.y;
                this.m22 = v.z;
                this.m23 = v.w;
                break;

            case 3:
                this.m30 = v.x;
                this.m31 = v.y;
                this.m32 = v.z;
                this.m33 = v.w;
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    /**
     * Sets the specified row of this matrix4f to the four values provided in
     * the passed array.
     *
     * @param row the row number to be modified (zero indexed)
     * @param v the replacement row
     */
    public final void setRow(int row, int v[]) {
        switch (row) {
            case 0:
                this.m00 = v[0];
                this.m01 = v[1];
                this.m02 = v[2];
                this.m03 = v[3];
                break;

            case 1:
                this.m10 = v[0];
                this.m11 = v[1];
                this.m12 = v[2];
                this.m13 = v[3];
                break;

            case 2:
                this.m20 = v[0];
                this.m21 = v[1];
                this.m22 = v[2];
                this.m23 = v[3];
                break;

            case 3:
                this.m30 = v[0];
                this.m31 = v[1];
                this.m32 = v[2];
                this.m33 = v[3];
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    /**
     * Sets the specified column of this matrix4f to the four values provided.
     *
     * @param column the column number to be modified (zero indexed)
     * @param x the first row element
     * @param y the second row element
     * @param z the third row element
     * @param w the fourth row element
     */
    public final void setColumn(int column, int x, int y, int z, int w) {
        switch (column) {
            case 0:
                this.m00 = x;
                this.m10 = y;
                this.m20 = z;
                this.m30 = w;
                break;

            case 1:
                this.m01 = x;
                this.m11 = y;
                this.m21 = z;
                this.m31 = w;
                break;

            case 2:
                this.m02 = x;
                this.m12 = y;
                this.m22 = z;
                this.m32 = w;
                break;

            case 3:
                this.m03 = x;
                this.m13 = y;
                this.m23 = z;
                this.m33 = w;
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    /**
     * Sets the specified column of this matrix4f to the vector provided.
     *
     * @param column the column number to be modified (zero indexed)
     * @param v the replacement column
     */
    public final void setColumn(int column, Point4i v) {
        switch (column) {
            case 0:
                this.m00 = v.x;
                this.m10 = v.y;
                this.m20 = v.z;
                this.m30 = v.w;
                break;

            case 1:
                this.m01 = v.x;
                this.m11 = v.y;
                this.m21 = v.z;
                this.m31 = v.w;
                break;

            case 2:
                this.m02 = v.x;
                this.m12 = v.y;
                this.m22 = v.z;
                this.m32 = v.w;
                break;

            case 3:
                this.m03 = v.x;
                this.m13 = v.y;
                this.m23 = v.z;
                this.m33 = v.w;
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    /**
     * Sets the specified column of this matrix4f to the four values provided.
     *
     * @param column the column number to be modified (zero indexed)
     * @param v the replacement column
     */
    public final void setColumn(int column, int v[]) {
        switch (column) {
            case 0:
                this.m00 = v[0];
                this.m10 = v[1];
                this.m20 = v[2];
                this.m30 = v[3];
                break;

            case 1:
                this.m01 = v[0];
                this.m11 = v[1];
                this.m21 = v[2];
                this.m31 = v[3];
                break;

            case 2:
                this.m02 = v[0];
                this.m12 = v[1];
                this.m22 = v[2];
                this.m32 = v[3];
                break;

            case 3:
                this.m03 = v[0];
                this.m13 = v[1];
                this.m23 = v[2];
                this.m33 = v[3];
                break;

            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    /**
     * Adds a scalar to each component of this matrix.
     *
     * @param scalar the scalar adder
     */
    public final void add(int scalar) {
        m00 += scalar;
        m01 += scalar;
        m02 += scalar;
        m03 += scalar;
        m10 += scalar;
        m11 += scalar;
        m12 += scalar;
        m13 += scalar;
        m20 += scalar;
        m21 += scalar;
        m22 += scalar;
        m23 += scalar;
        m30 += scalar;
        m31 += scalar;
        m32 += scalar;
        m33 += scalar;
    }

    /**
     * Adds a scalar to each component of the matrix m1 and places the result
     * into this. Matrix m1 is not modified.
     *
     * @param scalar the scalar adder
     * @param m1 the original matrix values
     */
    public final void add(int scalar, Matrix4i m1) {
        this.m00 = m1.m00 + scalar;
        this.m01 = m1.m01 + scalar;
        this.m02 = m1.m02 + scalar;
        this.m03 = m1.m03 + scalar;
        this.m10 = m1.m10 + scalar;
        this.m11 = m1.m11 + scalar;
        this.m12 = m1.m12 + scalar;
        this.m13 = m1.m13 + scalar;
        this.m20 = m1.m20 + scalar;
        this.m21 = m1.m21 + scalar;
        this.m22 = m1.m22 + scalar;
        this.m23 = m1.m23 + scalar;
        this.m30 = m1.m30 + scalar;
        this.m31 = m1.m31 + scalar;
        this.m32 = m1.m32 + scalar;
        this.m33 = m1.m33 + scalar;
    }

    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
     *
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void add(Matrix4i m1, Matrix4i m2) {
        this.m00 = m1.m00 + m2.m00;
        this.m01 = m1.m01 + m2.m01;
        this.m02 = m1.m02 + m2.m02;
        this.m03 = m1.m03 + m2.m03;

        this.m10 = m1.m10 + m2.m10;
        this.m11 = m1.m11 + m2.m11;
        this.m12 = m1.m12 + m2.m12;
        this.m13 = m1.m13 + m2.m13;

        this.m20 = m1.m20 + m2.m20;
        this.m21 = m1.m21 + m2.m21;
        this.m22 = m1.m22 + m2.m22;
        this.m23 = m1.m23 + m2.m23;

        this.m30 = m1.m30 + m2.m30;
        this.m31 = m1.m31 + m2.m31;
        this.m32 = m1.m32 + m2.m32;
        this.m33 = m1.m33 + m2.m33;
    }

    /**
     * Sets the value of this matrix to the sum of itself and matrix m1.
     *
     * @param m1 the other matrix
     */
    public final void add(Matrix4i m1) {
        this.m00 += m1.m00;
        this.m01 += m1.m01;
        this.m02 += m1.m02;
        this.m03 += m1.m03;

        this.m10 += m1.m10;
        this.m11 += m1.m11;
        this.m12 += m1.m12;
        this.m13 += m1.m13;

        this.m20 += m1.m20;
        this.m21 += m1.m21;
        this.m22 += m1.m22;
        this.m23 += m1.m23;

        this.m30 += m1.m30;
        this.m31 += m1.m31;
        this.m32 += m1.m32;
        this.m33 += m1.m33;
    }

    /**
     * Performs an element-by-element subtraction of matrix m2 from matrix m1
     * and places the result into matrix this (this = m2 - m1).
     *
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void sub(Matrix4i m1, Matrix4i m2) {
        this.m00 = m1.m00 - m2.m00;
        this.m01 = m1.m01 - m2.m01;
        this.m02 = m1.m02 - m2.m02;
        this.m03 = m1.m03 - m2.m03;

        this.m10 = m1.m10 - m2.m10;
        this.m11 = m1.m11 - m2.m11;
        this.m12 = m1.m12 - m2.m12;
        this.m13 = m1.m13 - m2.m13;

        this.m20 = m1.m20 - m2.m20;
        this.m21 = m1.m21 - m2.m21;
        this.m22 = m1.m22 - m2.m22;
        this.m23 = m1.m23 - m2.m23;

        this.m30 = m1.m30 - m2.m30;
        this.m31 = m1.m31 - m2.m31;
        this.m32 = m1.m32 - m2.m32;
        this.m33 = m1.m33 - m2.m33;
    }

    /**
     * Sets this matrix to the matrix difference of itself and matrix m1 (this =
     * this - m1).
     *
     * @param m1 the other matrix
     */
    public final void sub(Matrix4i m1) {
        this.m00 -= m1.m00;
        this.m01 -= m1.m01;
        this.m02 -= m1.m02;
        this.m03 -= m1.m03;

        this.m10 -= m1.m10;
        this.m11 -= m1.m11;
        this.m12 -= m1.m12;
        this.m13 -= m1.m13;

        this.m20 -= m1.m20;
        this.m21 -= m1.m21;
        this.m22 -= m1.m22;
        this.m23 -= m1.m23;

        this.m30 -= m1.m30;
        this.m31 -= m1.m31;
        this.m32 -= m1.m32;
        this.m33 -= m1.m33;
    }

    /**
     * Sets the value of this matrix to its transpose in place.
     */
    public final void transpose() {
        int temp;

        temp = this.m10;
        this.m10 = this.m01;
        this.m01 = temp;

        temp = this.m20;
        this.m20 = this.m02;
        this.m02 = temp;

        temp = this.m30;
        this.m30 = this.m03;
        this.m03 = temp;

        temp = this.m21;
        this.m21 = this.m12;
        this.m12 = temp;

        temp = this.m31;
        this.m31 = this.m13;
        this.m13 = temp;

        temp = this.m32;
        this.m32 = this.m23;
        this.m23 = temp;
    }

    /**
     * Sets the value of this matrix to the transpose of the argument matrix.
     *
     * @param m1 the matrix to be transposed
     */
    public final void transpose(Matrix4i m1) {
        if (this != m1) {
            this.m00 = m1.m00;
            this.m01 = m1.m10;
            this.m02 = m1.m20;
            this.m03 = m1.m30;

            this.m10 = m1.m01;
            this.m11 = m1.m11;
            this.m12 = m1.m21;
            this.m13 = m1.m31;

            this.m20 = m1.m02;
            this.m21 = m1.m12;
            this.m22 = m1.m22;
            this.m23 = m1.m32;

            this.m30 = m1.m03;
            this.m31 = m1.m13;
            this.m32 = m1.m23;
            this.m33 = m1.m33;
        } else {
            this.transpose();
        }
    }

    /**
     * Sets the value of this matrix to the matrix conversion of the single
     * precision quaternion argument.
     *
     * @param q1 the quaternion to be converted
     */
    public final void set(Point4i q1) {
        this.m00 = (1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z);
        this.m10 = (2 * (q1.x * q1.y + q1.w * q1.z));
        this.m20 = (2 * (q1.x * q1.z - q1.w * q1.y));

        this.m01 = (2 * (q1.x * q1.y - q1.w * q1.z));
        this.m11 = (1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z);
        this.m21 = (2 * (q1.y * q1.z + q1.w * q1.x));

        this.m02 = (2 * (q1.x * q1.z + q1.w * q1.y));
        this.m12 = (2 * (q1.y * q1.z - q1.w * q1.x));
        this.m22 = (1 - 2 * q1.x * q1.x - 2 * q1.y * q1.y);

        this.m03 = (int) 0.0;
        this.m13 = (int) 0.0;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix to the matrix conversion of the double
     * precision quaternion argument.
     *
     * @param q1 the quaternion to be converted
     */
    public final void set(Quat4d q1) {
        this.m00 = (int) (1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z);
        this.m10 = (int) (2.0 * (q1.x * q1.y + q1.w * q1.z));
        this.m20 = (int) (2.0 * (q1.x * q1.z - q1.w * q1.y));

        this.m01 = (int) (2.0 * (q1.x * q1.y - q1.w * q1.z));
        this.m11 = (int) (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z);
        this.m21 = (int) (2.0 * (q1.y * q1.z + q1.w * q1.x));

        this.m02 = (int) (2.0 * (q1.x * q1.z + q1.w * q1.y));
        this.m12 = (int) (2.0 * (q1.y * q1.z - q1.w * q1.x));
        this.m22 = (int) (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y);

        this.m03 = (int) 0.0;
        this.m13 = (int) 0.0;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix to the matrix conversion of the double
     * precision axis and angle argument.
     *
     * @param a1 the axis and angle to be converted
     */
    public final void set(AxisAngle4d a1) {
        double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);

        if (mag < EPS) {
            m00 = 1;
            m01 = 0;
            m02 = 0;

            m10 = 0;
            m11 = 1;
            m12 = 0;

            m20 = 0;
            m21 = 0;
            m22 = 1;
        } else {
            mag = 1.0 / mag;
            double ax = a1.x * mag;
            double ay = a1.y * mag;
            double az = a1.z * mag;

            int sinTheta = (int) Math.sin(a1.angle);
            int cosTheta = (int) Math.cos(a1.angle);
            int t = 1 - cosTheta;

            int xz = (int) (ax * az);
            int xy = (int) (ax * ay);
            int yz = (int) (ay * az);

            this.m00 = t * (int) (ax * ax) + cosTheta;
            this.m01 = t * xy - sinTheta * (int) az;
            this.m02 = t * xz + sinTheta * (int) ay;

            this.m10 = t * xy + sinTheta * (int) az;
            this.m11 = t * (int) (ay * ay) + cosTheta;
            this.m12 = t * yz - sinTheta * (int) ax;

            this.m20 = t * xz - sinTheta * (int) ay;
            this.m21 = t * yz + sinTheta * (int) ax;
            this.m22 = t * (int) (az * az) + cosTheta;
        }
        this.m03 = 0;
        this.m13 = 0;
        this.m23 = 0;

        this.m30 = 0;
        this.m31 = 0;
        this.m32 = 0;
        this.m33 = 1;
    }

    /**
     * Sets the value of this matrix from the rotation expressed by the
     * quaternion q1, the translation t1, and the scale s.
     *
     * @param q1 the rotation expressed as a quaternion
     * @param t1 the translation
     * @param s the scale value
     */
    public final void set(Quat4d q1, Vector3d t1, double s) {
        this.m00 = (int) (s * (1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z));
        this.m10 = (int) (s * (2.0 * (q1.x * q1.y + q1.w * q1.z)));
        this.m20 = (int) (s * (2.0 * (q1.x * q1.z - q1.w * q1.y)));

        this.m01 = (int) (s * (2.0 * (q1.x * q1.y - q1.w * q1.z)));
        this.m11 = (int) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z));
        this.m21 = (int) (s * (2.0 * (q1.y * q1.z + q1.w * q1.x)));

        this.m02 = (int) (s * (2.0 * (q1.x * q1.z + q1.w * q1.y)));
        this.m12 = (int) (s * (2.0 * (q1.y * q1.z - q1.w * q1.x)));
        this.m22 = (int) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y));

        this.m03 = (int) t1.x;
        this.m13 = (int) t1.y;
        this.m23 = (int) t1.z;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix from the rotation expressed by the
     * quaternion q1, the translation t1, and the scale s.
     *
     * @param q1 the rotation expressed as a quaternion
     * @param t1 the translation
     * @param s the scale value
     */
    public final void set(Point4i q1, Point3i t1, int s) {
        this.m00 = (s * (1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z));
        this.m10 = (s * (2 * (q1.x * q1.y + q1.w * q1.z)));
        this.m20 = (s * (2 * (q1.x * q1.z - q1.w * q1.y)));

        this.m01 = (s * (2 * (q1.x * q1.y - q1.w * q1.z)));
        this.m11 = (s * (1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z));
        this.m21 = (s * (2 * (q1.y * q1.z + q1.w * q1.x)));

        this.m02 = (s * (2 * (q1.x * q1.z + q1.w * q1.y)));
        this.m12 = (s * (2 * (q1.y * q1.z - q1.w * q1.x)));
        this.m22 = (s * (1 - 2 * q1.x * q1.x - 2 * q1.y * q1.y));

        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix to the int value of the passed matrix4d m1.
     *
     * @param m1 the matrix4d to be converted to int
     */
    public final void set(Matrix4d m1) {
        this.m00 = (int) m1.m00;
        this.m01 = (int) m1.m01;
        this.m02 = (int) m1.m02;
        this.m03 = (int) m1.m03;

        this.m10 = (int) m1.m10;
        this.m11 = (int) m1.m11;
        this.m12 = (int) m1.m12;
        this.m13 = (int) m1.m13;

        this.m20 = (int) m1.m20;
        this.m21 = (int) m1.m21;
        this.m22 = (int) m1.m22;
        this.m23 = (int) m1.m23;

        this.m30 = (int) m1.m30;
        this.m31 = (int) m1.m31;
        this.m32 = (int) m1.m32;
        this.m33 = (int) m1.m33;
    }

    /**
     * Sets the value of this matrix to a copy of the passed matrix m1.
     *
     * @param m1 the matrix to be copied
     */
    public final void set(Matrix4i m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;

        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;

        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;

        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    /**
     * Sets the value of this matrix to the matrix inverse of the passed (user
     * declared) matrix m1.
     *
     * @param m1 the matrix to be inverted
     */
    public final void invert(Matrix4i m1) {

        invertGeneral(m1);
    }

    /**
     * Inverts this matrix in place.
     */
    public final void invert() {
        invertGeneral(this);
    }

    /**
     * General invert routine. Inverts m1 and places the result in "this". Note
     * that this routine handles both the "this" version and the non-"this"
     * version.
     *
     * Also note that since this routine is slow anyway, we won't worry about
     * allocating a little bit of garbage.
     */
    final void invertGeneral(Matrix4i m1) {
        double temp[] = new double[16];
        double result[] = new double[16];
        int row_perm[] = new int[4];
        int i;
      //int r, c;

        // Use LU decomposition and backsubstitution code specifically
        // for floating-point 4x4 matrices.
        // Copy source matrix to t1tmp 
        temp[0] = m1.m00;
        temp[1] = m1.m01;
        temp[2] = m1.m02;
        temp[3] = m1.m03;

        temp[4] = m1.m10;
        temp[5] = m1.m11;
        temp[6] = m1.m12;
        temp[7] = m1.m13;

        temp[8] = m1.m20;
        temp[9] = m1.m21;
        temp[10] = m1.m22;
        temp[11] = m1.m23;

        temp[12] = m1.m30;
        temp[13] = m1.m31;
        temp[14] = m1.m32;
        temp[15] = m1.m33;

        // Calculate LU decomposition: Is the matrix singular? 
        if (!luDecomposition(temp, row_perm)) {
            // Matrix has no inverse 
            throw new SingularMatrixException(VecMathI18N.getString("Matrix4f12"));
        }

        // Perform back substitution on the identity matrix 
        for (i = 0; i < 16; i++) {
            result[i] = 0.0;
        }
        result[0] = 1.0;
        result[5] = 1.0;
        result[10] = 1.0;
        result[15] = 1.0;
        luBacksubstitution(temp, row_perm, result);

        this.m00 = (int) result[0];
        this.m01 = (int) result[1];
        this.m02 = (int) result[2];
        this.m03 = (int) result[3];

        this.m10 = (int) result[4];
        this.m11 = (int) result[5];
        this.m12 = (int) result[6];
        this.m13 = (int) result[7];

        this.m20 = (int) result[8];
        this.m21 = (int) result[9];
        this.m22 = (int) result[10];
        this.m23 = (int) result[11];

        this.m30 = (int) result[12];
        this.m31 = (int) result[13];
        this.m32 = (int) result[14];
        this.m33 = (int) result[15];

    }

    /**
     * Given a 4x4 array "matrix0", this function replaces it with the LU
     * decomposition of a row-wise permutation of itself. The input parameters
     * are "matrix0" and "dimen". The array "matrix0" is also an output
     * parameter. The vector "row_perm[4]" is an output parameter that contains
     * the row permutations resulting from partial pivoting. The output
     * parameter "even_row_xchg" is 1 when the number of row exchanges is even,
     * or -1 otherwise. Assumes data type is always double.
     *
     * This function is similar to luDecomposition, except that it is tuned
     * specifically for 4x4 matrices.
     *
     * @return true if the matrix is nonsingular, or false otherwise.
     */
    //
    // Reference: Press, Flannery, Teukolsky, Vetterling, 
    //            _Numerical_Recipes_in_C_, Cambridge University Press, 
    //            1988, pp 40-45.
    //
    static boolean luDecomposition(double[] matrix0,
            int[] row_perm) {

        double row_scale[] = new double[4];

        // Determine implicit scaling information by looping over rows 
        {
            int i, j;
            int ptr, rs;
            double big, temp;

            ptr = 0;
            rs = 0;

            // For each row ... 
            i = 4;
            while (i-- != 0) {
                big = 0.0;

                // For each column, find the largest element in the row 
                j = 4;
                while (j-- != 0) {
                    temp = matrix0[ptr++];
                    temp = Math.abs(temp);
                    if (temp > big) {
                        big = temp;
                    }
                }

                // Is the matrix singular? 
                if (big == 0.0) {
                    return false;
                }
                row_scale[rs++] = 1.0 / big;
            }
        }

        {
            int j;
            int mtx;

            mtx = 0;

            // For all columns, execute Crout's method 
            for (j = 0; j < 4; j++) {
                int i, imax, k;
                int target, p1, p2;
                double sum, big, temp;

                // Determine elements of upper diagonal matrix U 
                for (i = 0; i < j; i++) {
                    target = mtx + (4 * i) + j;
                    sum = matrix0[target];
                    k = i;
                    p1 = mtx + (4 * i);
                    p2 = mtx + j;
                    while (k-- != 0) {
                        sum -= matrix0[p1] * matrix0[p2];
                        p1++;
                        p2 += 4;
                    }
                    matrix0[target] = sum;
                }

                // Search for largest pivot element and calculate
                // intermediate elements of lower diagonal matrix L.
                big = 0.0;
                imax = -1;
                for (i = j; i < 4; i++) {
                    target = mtx + (4 * i) + j;
                    sum = matrix0[target];
                    k = j;
                    p1 = mtx + (4 * i);
                    p2 = mtx + j;
                    while (k-- != 0) {
                        sum -= matrix0[p1] * matrix0[p2];
                        p1++;
                        p2 += 4;
                    }
                    matrix0[target] = sum;

                    // Is this the best pivot so far? 
                    if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
                        big = temp;
                        imax = i;
                    }
                }

                if (imax < 0) {
                    throw new RuntimeException(VecMathI18N.getString("Matrix4f13"));
                }

                // Is a row exchange necessary? 
                if (j != imax) {
                    // Yes: exchange rows 
                    k = 4;
                    p1 = mtx + (4 * imax);
                    p2 = mtx + (4 * j);
                    while (k-- != 0) {
                        temp = matrix0[p1];
                        matrix0[p1++] = matrix0[p2];
                        matrix0[p2++] = temp;
                    }

                    // Record change in scale factor 
                    row_scale[imax] = row_scale[j];
                }

                // Record row permutation 
                row_perm[j] = imax;

                // Is the matrix singular 
                if (matrix0[(mtx + (4 * j) + j)] == 0.0) {
                    return false;
                }

                // Divide elements of lower diagonal matrix L by pivot 
                if (j != (4 - 1)) {
                    temp = 1.0 / (matrix0[(mtx + (4 * j) + j)]);
                    target = mtx + (4 * (j + 1)) + j;
                    i = 3 - j;
                    while (i-- != 0) {
                        matrix0[target] *= temp;
                        target += 4;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Solves a set of linear equations. The input parameters "matrix1", and
     * "row_perm" come from luDecompostionD4x4 and do not change here. The
     * parameter "matrix2" is a set of column vectors assembled into a 4x4
     * matrix of floating-point values. The procedure takes each column of
     * "matrix2" in turn and treats it as the right-hand side of the matrix
     * equation Ax = LUx = b. The solution vector replaces the original column
     * of the matrix.
     *
     * If "matrix2" is the identity matrix, the procedure replaces its contents
     * with the inverse of the matrix from which "matrix1" was originally
     * derived.
     */
    //
    // Reference: Press, Flannery, Teukolsky, Vetterling, 
    //            _Numerical_Recipes_in_C_, Cambridge University Press, 
    //            1988, pp 44-45.
    //
    static void luBacksubstitution(double[] matrix1,
            int[] row_perm,
            double[] matrix2) {

        int i, ii, ip, j, k;
        int rp;
        int cv, rv;

        //    rp = row_perm;
        rp = 0;

        // For each column vector of matrix2 ... 
        for (k = 0; k < 4; k++) {
            //          cv = &(matrix2[0][k]);
            cv = k;
            ii = -1;

            // Forward substitution 
            for (i = 0; i < 4; i++) {
                double sum;

                ip = row_perm[rp + i];
                sum = matrix2[cv + 4 * ip];
                matrix2[cv + 4 * ip] = matrix2[cv + 4 * i];
                if (ii >= 0) {
                    //                rv = &(matrix1[i][0]);
                    rv = i * 4;
                    for (j = ii; j <= i - 1; j++) {
                        sum -= matrix1[rv + j] * matrix2[cv + 4 * j];
                    }
                } else if (sum != 0.0) {
                    ii = i;
                }
                matrix2[cv + 4 * i] = sum;
            }

            // Backsubstitution 
            //          rv = &(matrix1[3][0]);
            rv = 3 * 4;
            matrix2[cv + 4 * 3] /= matrix1[rv + 3];

            rv -= 4;
            matrix2[cv + 4 * 2] = (matrix2[cv + 4 * 2]
                    - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 2];

            rv -= 4;
            matrix2[cv + 4 * 1] = (matrix2[cv + 4 * 1]
                    - matrix1[rv + 2] * matrix2[cv + 4 * 2]
                    - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 1];

            rv -= 4;
            matrix2[cv + 4 * 0] = (matrix2[cv + 4 * 0]
                    - matrix1[rv + 1] * matrix2[cv + 4 * 1]
                    - matrix1[rv + 2] * matrix2[cv + 4 * 2]
                    - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 0];
        }
    }

    /**
     * Computes the determinate of this matrix.
     *
     * @return the determinate of the matrix
     */
    public final int determinant() {
        int det;

        // cofactor exapainsion along first row 
        det = m00 * (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32
                - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33);
        det -= m01 * (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32
                - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33);
        det += m02 * (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31
                - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33);
        det -= m03 * (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31
                - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);

        return (det);
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the single precision Matrix3i argument; the other elements of
     * this matrix are initialized as if this were an identity matrix (i.e.,
     * affine matrix with no translational component).
     *
     * @param m1 the single-precision 3x3 matrix
     */
    public final void set(Matrix3i m1) {
        m00 = m1.m00;
        m01 = m1.m01;
        m02 = m1.m02;
        m03 = 0;
        m10 = m1.m10;
        m11 = m1.m11;
        m12 = m1.m12;
        m13 = 0;
        m20 = m1.m20;
        m21 = m1.m21;
        m22 = m1.m22;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the double precision Matrix3d argument; the other elements of
     * this matrix are initialized as if this were an identity matrix (i.e.,
     * affine matrix with no translational component).
     *
     * @param m1 the double-precision 3x3 matrix
     */
    public final void set(Matrix3d m1) {
        m00 = (int) m1.m00;
        m01 = (int) m1.m01;
        m02 = (int) m1.m02;
        m03 = 0;
        m10 = (int) m1.m10;
        m11 = (int) m1.m11;
        m12 = (int) m1.m12;
        m13 = 0;
        m20 = (int) m1.m20;
        m21 = (int) m1.m21;
        m22 = (int) m1.m22;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;
    }

    /**
     * Sets the value of this matrix to a scale matrix with the the passed scale
     * amount.
     *
     * @param scale the scale factor for the matrix
     */
    public final void set(int scale) {
        this.m00 = scale;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = (int) 0.0;

        this.m10 = (int) 0.0;
        this.m11 = scale;
        this.m12 = (int) 0.0;
        this.m13 = (int) 0.0;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = scale;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the values in this Matrix4f equal to the row-major array parameter
     * (ie, the first four elements of the array will be copied into the first
     * row of this matrix, etc.).
     *
     * @param m the single precision array of length 16
     */
    public final void set(int[] m) {
        m00 = m[0];
        m01 = m[1];
        m02 = m[2];
        m03 = m[3];
        m10 = m[4];
        m11 = m[5];
        m12 = m[6];
        m13 = m[7];
        m20 = m[8];
        m21 = m[9];
        m22 = m[10];
        m23 = m[11];
        m30 = m[12];
        m31 = m[13];
        m32 = m[14];
        m33 = m[15];
    }

    /**
     * Sets the value of this matrix to a translate matrix with the passed
     * translation value.
     *
     * @param v1 the translation amount
     */
    public final void set(Point3i v1) {
        this.m00 = (int) 1.0;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = v1.x;

        this.m10 = (int) 0.0;
        this.m11 = (int) 1.0;
        this.m12 = (int) 0.0;
        this.m13 = v1.y;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = (int) 1.0;
        this.m23 = v1.z;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this transform to a scale and translation matrix; the
     * scale is not applied to the translation and all of the matrix values are
     * modified.
     *
     * @param scale the scale factor for the matrix
     * @param t1 the translation amount
     */
    public final void set(int scale, Point3i t1) {
        this.m00 = scale;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = t1.x;

        this.m10 = (int) 0.0;
        this.m11 = scale;
        this.m12 = (int) 0.0;
        this.m13 = t1.y;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = scale;
        this.m23 = t1.z;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this transform to a scale and translation matrix; the
     * translation is scaled by the scale factor and all of the matrix values
     * are modified.
     *
     * @param t1 the translation amount
     * @param scale the scale factor for the matrix
     */
    public final void set(Point3i t1, int scale) {
        this.m00 = scale;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = scale * t1.x;

        this.m10 = (int) 0.0;
        this.m11 = scale;
        this.m12 = (int) 0.0;
        this.m13 = scale * t1.y;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = scale;
        this.m23 = scale * t1.z;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix from the rotation expressed by the rotation
     * matrix m1, the translation t1, and the scale factor. The translation is
     * not modified by the scale.
     *
     * @param m1 the rotation component
     * @param t1 the translation component
     * @param scale the scale component
     */
    public final void set(Matrix3i m1, Point3i t1, int scale) {
        this.m00 = m1.m00 * scale;
        this.m01 = m1.m01 * scale;
        this.m02 = m1.m02 * scale;
        this.m03 = t1.x;

        this.m10 = m1.m10 * scale;
        this.m11 = m1.m11 * scale;
        this.m12 = m1.m12 * scale;
        this.m13 = t1.y;

        this.m20 = m1.m20 * scale;
        this.m21 = m1.m21 * scale;
        this.m22 = m1.m22 * scale;
        this.m23 = t1.z;

        this.m30 = 0;
        this.m31 = 0;
        this.m32 = 0;
        this.m33 = 1;
    }

    /**
     * Sets the value of this matrix from the rotation expressed by the rotation
     * matrix m1, the translation t1, and the scale factor. The translation is
     * not modified by the scale.
     *
     * @param m1 the rotation component
     * @param t1 the translation component
     * @param scale the scale factor
     */
    public final void set(Matrix3d m1, Vector3d t1, double scale) {
        this.m00 = (int) (m1.m00 * scale);
        this.m01 = (int) (m1.m01 * scale);
        this.m02 = (int) (m1.m02 * scale);
        this.m03 = (int) t1.x;

        this.m10 = (int) (m1.m10 * scale);
        this.m11 = (int) (m1.m11 * scale);
        this.m12 = (int) (m1.m12 * scale);
        this.m13 = (int) t1.y;

        this.m20 = (int) (m1.m20 * scale);
        this.m21 = (int) (m1.m21 * scale);
        this.m22 = (int) (m1.m22 * scale);
        this.m23 = (int) t1.z;

        this.m30 = 0;
        this.m31 = 0;
        this.m32 = 0;
        this.m33 = 1;
    }

    /**
     * Modifies the translational components of this matrix to the values of the
     * Point3i argument; the other values of this matrix are not modified.
     *
     * @param trans the translational component
     */
    public final void setTranslation(Point3i trans) {
        m03 = trans.x;
        m13 = trans.y;
        m23 = trans.z;
    }

    private int round(double d) {
        if (d > 0) {
            return (int) (d + .5);
        } else {
            return (int) (d - .5);
        }
    }

    /**
     * Sets the value of this matrix to a counter clockwise rotation about the x
     * axis.
     *
     * @param angle the angle to rotate about the X axis in radians
     */
    public final void rotX(float angle) {
        int sinAngle, cosAngle;

        sinAngle = round(Math.sin((double) angle));
        cosAngle = round(Math.cos((double) angle));

        this.m00 = (int) 1.0;
        this.m01 = (int) 0.0;
        this.m02 = (int) 0.0;
        this.m03 = (int) 0.0;

        this.m10 = (int) 0.0;
        this.m11 = cosAngle;
        this.m12 = -sinAngle;
        this.m13 = (int) 0.0;

        this.m20 = (int) 0.0;
        this.m21 = sinAngle;
        this.m22 = cosAngle;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix to a counter clockwise rotation about the y
     * axis.
     *
     * @param angle the angle to rotate about the Y axis in radians
     */
    public final void rotY(float angle) {
        int sinAngle, cosAngle;

        sinAngle = round(Math.sin((double) angle));
        cosAngle = round(Math.cos((double) angle));

        this.m00 = cosAngle;
        this.m01 = (int) 0.0;
        this.m02 = sinAngle;
        this.m03 = (int) 0.0;

        this.m10 = (int) 0.0;
        this.m11 = (int) 1.0;
        this.m12 = (int) 0.0;
        this.m13 = (int) 0.0;

        this.m20 = -sinAngle;
        this.m21 = (int) 0.0;
        this.m22 = cosAngle;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Sets the value of this matrix to a counter clockwise rotation about the z
     * axis.
     *
     * @param angle the angle to rotate about the Z axis in radians
     */
    public final void rotZ(float angle) {
        int sinAngle, cosAngle;

        sinAngle = round(Math.sin((double) angle));
        cosAngle = round(Math.cos((double) angle));

        this.m00 = cosAngle;
        this.m01 = -sinAngle;
        this.m02 = (int) 0.0;
        this.m03 = (int) 0.0;

        this.m10 = sinAngle;
        this.m11 = cosAngle;
        this.m12 = (int) 0.0;
        this.m13 = (int) 0.0;

        this.m20 = (int) 0.0;
        this.m21 = (int) 0.0;
        this.m22 = (int) 1.0;
        this.m23 = (int) 0.0;

        this.m30 = (int) 0.0;
        this.m31 = (int) 0.0;
        this.m32 = (int) 0.0;
        this.m33 = (int) 1.0;
    }

    /**
     * Multiplies each element of this matrix by a scalar.
     *
     * @param scalar the scalar multiplier.
     */
    public final void mul(int scalar) {
        m00 *= scalar;
        m01 *= scalar;
        m02 *= scalar;
        m03 *= scalar;
        m10 *= scalar;
        m11 *= scalar;
        m12 *= scalar;
        m13 *= scalar;
        m20 *= scalar;
        m21 *= scalar;
        m22 *= scalar;
        m23 *= scalar;
        m30 *= scalar;
        m31 *= scalar;
        m32 *= scalar;
        m33 *= scalar;
    }

    /**
     * Multiplies each element of matrix m1 by a scalar and places the result
     * into this. Matrix m1 is not modified.
     *
     * @param scalar the scalar multiplier.
     * @param m1 the original matrix.
     */
    public final void mul(int scalar, Matrix4i m1) {
        this.m00 = m1.m00 * scalar;
        this.m01 = m1.m01 * scalar;
        this.m02 = m1.m02 * scalar;
        this.m03 = m1.m03 * scalar;
        this.m10 = m1.m10 * scalar;
        this.m11 = m1.m11 * scalar;
        this.m12 = m1.m12 * scalar;
        this.m13 = m1.m13 * scalar;
        this.m20 = m1.m20 * scalar;
        this.m21 = m1.m21 * scalar;
        this.m22 = m1.m22 * scalar;
        this.m23 = m1.m23 * scalar;
        this.m30 = m1.m30 * scalar;
        this.m31 = m1.m31 * scalar;
        this.m32 = m1.m32 * scalar;
        this.m33 = m1.m33 * scalar;
    }

    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix m1.
     *
     * @param m1 the other matrix
     */
    public final void mul(Matrix4i m1) {
        int m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33;  // vars for temp result matrix

        m00 = this.m00 * m1.m00 + this.m01 * m1.m10
                + this.m02 * m1.m20 + this.m03 * m1.m30;
        m01 = this.m00 * m1.m01 + this.m01 * m1.m11
                + this.m02 * m1.m21 + this.m03 * m1.m31;
        m02 = this.m00 * m1.m02 + this.m01 * m1.m12
                + this.m02 * m1.m22 + this.m03 * m1.m32;
        m03 = this.m00 * m1.m03 + this.m01 * m1.m13
                + this.m02 * m1.m23 + this.m03 * m1.m33;

        m10 = this.m10 * m1.m00 + this.m11 * m1.m10
                + this.m12 * m1.m20 + this.m13 * m1.m30;
        m11 = this.m10 * m1.m01 + this.m11 * m1.m11
                + this.m12 * m1.m21 + this.m13 * m1.m31;
        m12 = this.m10 * m1.m02 + this.m11 * m1.m12
                + this.m12 * m1.m22 + this.m13 * m1.m32;
        m13 = this.m10 * m1.m03 + this.m11 * m1.m13
                + this.m12 * m1.m23 + this.m13 * m1.m33;

        m20 = this.m20 * m1.m00 + this.m21 * m1.m10
                + this.m22 * m1.m20 + this.m23 * m1.m30;
        m21 = this.m20 * m1.m01 + this.m21 * m1.m11
                + this.m22 * m1.m21 + this.m23 * m1.m31;
        m22 = this.m20 * m1.m02 + this.m21 * m1.m12
                + this.m22 * m1.m22 + this.m23 * m1.m32;
        m23 = this.m20 * m1.m03 + this.m21 * m1.m13
                + this.m22 * m1.m23 + this.m23 * m1.m33;

        m30 = this.m30 * m1.m00 + this.m31 * m1.m10
                + this.m32 * m1.m20 + this.m33 * m1.m30;
        m31 = this.m30 * m1.m01 + this.m31 * m1.m11
                + this.m32 * m1.m21 + this.m33 * m1.m31;
        m32 = this.m30 * m1.m02 + this.m31 * m1.m12
                + this.m32 * m1.m22 + this.m33 * m1.m32;
        m33 = this.m30 * m1.m03 + this.m31 * m1.m13
                + this.m32 * m1.m23 + this.m33 * m1.m33;

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices together.
     *
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void mul(Matrix4i m1, Matrix4i m2) {
        if (this != m1 && this != m2) {

            this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10
                    + m1.m02 * m2.m20 + m1.m03 * m2.m30;
            this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11
                    + m1.m02 * m2.m21 + m1.m03 * m2.m31;
            this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12
                    + m1.m02 * m2.m22 + m1.m03 * m2.m32;
            this.m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13
                    + m1.m02 * m2.m23 + m1.m03 * m2.m33;

            this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10
                    + m1.m12 * m2.m20 + m1.m13 * m2.m30;
            this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11
                    + m1.m12 * m2.m21 + m1.m13 * m2.m31;
            this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12
                    + m1.m12 * m2.m22 + m1.m13 * m2.m32;
            this.m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13
                    + m1.m12 * m2.m23 + m1.m13 * m2.m33;

            this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10
                    + m1.m22 * m2.m20 + m1.m23 * m2.m30;
            this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11
                    + m1.m22 * m2.m21 + m1.m23 * m2.m31;
            this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12
                    + m1.m22 * m2.m22 + m1.m23 * m2.m32;
            this.m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13
                    + m1.m22 * m2.m23 + m1.m23 * m2.m33;

            this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10
                    + m1.m32 * m2.m20 + m1.m33 * m2.m30;
            this.m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11
                    + m1.m32 * m2.m21 + m1.m33 * m2.m31;
            this.m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12
                    + m1.m32 * m2.m22 + m1.m33 * m2.m32;
            this.m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13
                    + m1.m32 * m2.m23 + m1.m33 * m2.m33;
        } else {
            int m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23,
                    m30, m31, m32, m33;  // vars for temp result matrix
            m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
            m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
            m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
            m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;

            m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
            m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
            m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
            m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;

            m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
            m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
            m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
            m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;

            m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
            m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
            m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
            m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;

            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }
    }

    /**
     * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
     * and places the result into this.
     *
     * @param m1 the matrix on the left hand side of the multiplication
     * @param m2 the matrix on the right hand side of the multiplication
     */
    public final void mulTransposeBoth(Matrix4i m1, Matrix4i m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
            this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
            this.m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
            this.m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;

            this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
            this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
            this.m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
            this.m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;

            this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
            this.m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
            this.m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
            this.m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;

            this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
            this.m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
            this.m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
            this.m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;
        } else {
            int m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23, // vars for temp result matrix
                    m30, m31, m32, m33;

            m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
            m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
            m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
            m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;

            m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
            m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
            m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
            m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;

            m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
            m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
            m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
            m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;

            m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
            m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
            m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
            m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;

            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }

    }

    /**
     * Multiplies matrix m1 times the transpose of matrix m2, and places the
     * result into this.
     *
     * @param m1 the matrix on the left hand side of the multiplication
     * @param m2 the matrix on the right hand side of the multiplication
     */
    public final void mulTransposeRight(Matrix4i m1, Matrix4i m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
            this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
            this.m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
            this.m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;

            this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
            this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
            this.m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
            this.m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;

            this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
            this.m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
            this.m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
            this.m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;

            this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
            this.m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
            this.m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
            this.m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;
        } else {
            int m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23, // vars for temp result matrix
                    m30, m31, m32, m33;

            m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
            m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
            m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
            m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;

            m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
            m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
            m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
            m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;

            m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
            m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
            m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
            m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;

            m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
            m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
            m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
            m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;

            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }

    }

    /**
     * Multiplies the transpose of matrix m1 times matrix m2, and places the
     * result into this.
     *
     * @param m1 the matrix on the left hand side of the multiplication
     * @param m2 the matrix on the right hand side of the multiplication
     */
    public final void mulTransposeLeft(Matrix4i m1, Matrix4i m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
            this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
            this.m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
            this.m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;

            this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
            this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
            this.m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
            this.m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;

            this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
            this.m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
            this.m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
            this.m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;

            this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
            this.m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
            this.m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
            this.m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;
        } else {
            int m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23, // vars for temp result matrix
                    m30, m31, m32, m33;

            m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
            m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
            m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
            m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;

            m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
            m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
            m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
            m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;

            m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
            m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
            m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
            m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;

            m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
            m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
            m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
            m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;

            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }

    }

    /**
     * Returns true if all of the data members of Matrix4f m1 are equal to the
     * corresponding data members in this Matrix4f.
     *
     * @param m1 the matrix with which the comparison is made.
     * @return true or false
     */
    public boolean equals(Matrix4i m1) {
        try {
            return (this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02
                    && this.m03 == m1.m03 && this.m10 == m1.m10 && this.m11 == m1.m11
                    && this.m12 == m1.m12 && this.m13 == m1.m13 && this.m20 == m1.m20
                    && this.m21 == m1.m21 && this.m22 == m1.m22 && this.m23 == m1.m23
                    && this.m30 == m1.m30 && this.m31 == m1.m31 && this.m32 == m1.m32
                    && this.m33 == m1.m33);
        } catch (NullPointerException e2) {
            return false;
        }

    }

    /**
     * Returns true if the Object t1 is of type Matrix4f and all of the data
     * members of t1 are equal to the corresponding data members in this
     * Matrix4f.
     *
     * @param t1 the matrix with which the comparison is made.
     * @return true or false
     */
    public boolean equals(Object t1) {
        try {
            Matrix4i m2 = (Matrix4i) t1;
            return (this.m00 == m2.m00 && this.m01 == m2.m01 && this.m02 == m2.m02
                    && this.m03 == m2.m03 && this.m10 == m2.m10 && this.m11 == m2.m11
                    && this.m12 == m2.m12 && this.m13 == m2.m13 && this.m20 == m2.m20
                    && this.m21 == m2.m21 && this.m22 == m2.m22 && this.m23 == m2.m23
                    && this.m30 == m2.m30 && this.m31 == m2.m31 && this.m32 == m2.m32
                    && this.m33 == m2.m33);
        } catch (ClassCastException e1) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    /**
     * Returns true if the L-infinite distance between this matrix and matrix m1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[i=0,1,2,3 ; j=0,1,2,3 ;
     * abs(this.m(i,j) - m1.m(i,j)]
     *
     * @param m1 the matrix to be compared to this matrix
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals(Matrix4i m1, int epsilon) {

        boolean status = true;

        if (Math.abs(this.m00 - m1.m00) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m01 - m1.m01) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m02 - m1.m02) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m03 - m1.m03) > epsilon) {
            status = false;
        }

        if (Math.abs(this.m10 - m1.m10) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m11 - m1.m11) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m12 - m1.m12) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m13 - m1.m13) > epsilon) {
            status = false;
        }

        if (Math.abs(this.m20 - m1.m20) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m21 - m1.m21) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m22 - m1.m22) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m23 - m1.m23) > epsilon) {
            status = false;
        }

        if (Math.abs(this.m30 - m1.m30) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m31 - m1.m31) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m32 - m1.m32) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m33 - m1.m33) > epsilon) {
            status = false;
        }

        return (status);

    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different Matrix4f objects with identical data values (i.e.,
     * Matrix4f.equals returns true) will return the same hash code value. Two
     * objects with different data members may return the same hash value,
     * although this is not likely.
     *
     * @return the integer hash code value
     */
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m00);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m01);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m02);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m03);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m10);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m11);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m12);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m13);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m20);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m21);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m22);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m23);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m30);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m31);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m32);
        bits = 31L * bits + (long) VecMathUtil.floatToIntBits(m33);
        return (int) (bits ^ (bits >> 32));
    }

    /**
     * Transform the vector vec using this Matrix4f and place the result into
     * vecOut.
     *
     * @param vec the single precision vector to be transformed
     * @param vecOut the vector into which the transformed values are placed
     */
    public final void transform(Tuple4i vec, Tuple4i vecOut) {
        int x, y, z;
        x = m00 * vec.x + m01 * vec.y
                + m02 * vec.z + m03 * vec.w;
        y = m10 * vec.x + m11 * vec.y
                + m12 * vec.z + m13 * vec.w;
        z = m20 * vec.x + m21 * vec.y
                + m22 * vec.z + m23 * vec.w;
        vecOut.w = m30 * vec.x + m31 * vec.y
                + m32 * vec.z + m33 * vec.w;
        vecOut.x = x;
        vecOut.y = y;
        vecOut.z = z;
    }

    /**
     * Transform the vector vec using this Transform and place the result back
     * into vec.
     *
     * @param vec the single precision vector to be transformed
     */
    public final void transform(Tuple4i vec) {
        int x, y, z;

        x = m00 * vec.x + m01 * vec.y
                + m02 * vec.z + m03 * vec.w;
        y = m10 * vec.x + m11 * vec.y
                + m12 * vec.z + m13 * vec.w;
        z = m20 * vec.x + m21 * vec.y
                + m22 * vec.z + m23 * vec.w;
        vec.w = m30 * vec.x + m31 * vec.y
                + m32 * vec.z + m33 * vec.w;
        vec.x = x;
        vec.y = y;
        vec.z = z;
    }

    /**
     * Transforms the point parameter with this Matrix4f and places the result
     * into pointOut. The fourth element of the point input paramter is assumed
     * to be one.
     *
     * @param point the input point to be transformed.
     * @param pointOut the transformed point public final void transform(Point3i
     * point, Point3i pointOut) { int x,y; x = m00*point.x + m01*point.y +
     * m02*point.z + m03; y = m10*point.x + m11*point.y + m12*point.z + m13;
     * pointOut.z = m20*point.x + m21*point.y + m22*point.z + m23; pointOut.x =
     * x; pointOut.y = y; }
     */
    /**
     * Transforms the point parameter with this Matrix4f and places the result
     * back into point. The fourth element of the point input paramter is
     * assumed to be one.
     *
     * @param point the input point to be transformed. public final void
     * transform(Point3i point) { int x, y; x = m00*point.x + m01*point.y +
     * m02*point.z + m03; y = m10*point.x + m11*point.y + m12*point.z + m13;
     * point.z = m20*point.x + m21*point.y + m22*point.z + m23; point.x = x;
     * point.y = y; }
     */
    /**
     * Transforms the normal parameter by this Matrix4f and places the value
     * into normalOut. The fourth element of the normal is assumed to be zero.
     *
     * @param normal the input normal to be transformed.
     * @param normalOut the transformed normal
     */
    public final void transform(Point3i normal, Point3i normalOut) {
        int x, y;
        x = m00 * normal.x + m01 * normal.y + m02 * normal.z;
        y = m10 * normal.x + m11 * normal.y + m12 * normal.z;
        normalOut.z = m20 * normal.x + m21 * normal.y + m22 * normal.z;
        normalOut.x = x;
        normalOut.y = y;
    }

    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal. The fourth element of the normal is assumed to be zero.
     *
     * @param normal the input normal to be transformed.
     */
    public final void transform(Point3i normal) {
        int x, y;

        x = m00 * normal.x + m01 * normal.y + m02 * normal.z;
        y = m10 * normal.x + m11 * normal.y + m12 * normal.z;
        normal.z = m20 * normal.x + m21 * normal.y + m22 * normal.z;
        normal.x = x;
        normal.y = y;
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the double precision Matrix3d argument; the other elements of
     * this matrix are unchanged; a singular value decomposition is performed on
     * this object's upper 3x3 matrix to factor out the scale, then this
     * object's upper 3x3 matrix components are replaced by the passed rotation
     * components, and then the scale is reapplied to the rotational components.
     *
     * @param m1 double precision 3x3 matrix
     */
    public final void setRotation(Matrix3d m1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m00 = (int) (m1.m00 * tmp_scale[0]);
        m01 = (int) (m1.m01 * tmp_scale[1]);
        m02 = (int) (m1.m02 * tmp_scale[2]);

        m10 = (int) (m1.m10 * tmp_scale[0]);
        m11 = (int) (m1.m11 * tmp_scale[1]);
        m12 = (int) (m1.m12 * tmp_scale[2]);

        m20 = (int) (m1.m20 * tmp_scale[0]);
        m21 = (int) (m1.m21 * tmp_scale[1]);
        m22 = (int) (m1.m22 * tmp_scale[2]);

    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the single precision Matrix3i argument; the other elements of
     * this matrix are unchanged; a singular value decomposition is performed on
     * this object's upper 3x3 matrix to factor out the scale, then this
     * object's upper 3x3 matrix components are replaced by the passed rotation
     * components, and then the scale is reapplied to the rotational components.
     *
     * @param m1 single precision 3x3 matrix
     */
    public final void setRotation(Matrix3i m1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m00 = (int) (m1.m00 * tmp_scale[0]);
        m01 = (int) (m1.m01 * tmp_scale[1]);
        m02 = (int) (m1.m02 * tmp_scale[2]);

        m10 = (int) (m1.m10 * tmp_scale[0]);
        m11 = (int) (m1.m11 * tmp_scale[1]);
        m12 = (int) (m1.m12 * tmp_scale[2]);

        m20 = (int) (m1.m20 * tmp_scale[0]);
        m21 = (int) (m1.m21 * tmp_scale[1]);
        m22 = (int) (m1.m22 * tmp_scale[2]);
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * equivalent values of the quaternion argument; the other elements of this
     * matrix are unchanged; a singular value decomposition is performed on this
     * object's upper 3x3 matrix to factor out the scale, then this object's
     * upper 3x3 matrix components are replaced by the matrix equivalent of the
     * quaternion, and then the scale is reapplied to the rotational components.
     *
     * @param q1 the quaternion that specifies the rotation
     */
    public final void setRotation(Point4i q1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix
        getScaleRotate(tmp_scale, tmp_rot);

        m00 = (int) ((1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z) * tmp_scale[0]);
        m10 = (int) ((2 * (q1.x * q1.y + q1.w * q1.z)) * tmp_scale[0]);
        m20 = (int) ((2 * (q1.x * q1.z - q1.w * q1.y)) * tmp_scale[0]);

        m01 = (int) ((2 * (q1.x * q1.y - q1.w * q1.z)) * tmp_scale[1]);
        m11 = (int) ((1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z) * tmp_scale[1]);
        m21 = (int) ((2 * (q1.y * q1.z + q1.w * q1.x)) * tmp_scale[1]);

        m02 = (int) ((2 * (q1.x * q1.z + q1.w * q1.y)) * tmp_scale[2]);
        m12 = (int) ((2 * (q1.y * q1.z - q1.w * q1.x)) * tmp_scale[2]);
        m22 = (int) ((1 - 2 * q1.x * q1.x - 2 * q1.y * q1.y) * tmp_scale[2]);

    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * equivalent values of the quaternion argument; the other elements of this
     * matrix are unchanged; a singular value decomposition is performed on this
     * object's upper 3x3 matrix to factor out the scale, then this object's
     * upper 3x3 matrix components are replaced by the matrix equivalent of the
     * quaternion, and then the scale is reapplied to the rotational components.
     *
     * @param q1 the quaternion that specifies the rotation
     */
    public final void setRotation(Quat4d q1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        m00 = (int) ((1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z) * tmp_scale[0]);
        m10 = (int) ((2 * (q1.x * q1.y + q1.w * q1.z)) * tmp_scale[0]);
        m20 = (int) ((2 * (q1.x * q1.z - q1.w * q1.y)) * tmp_scale[0]);

        m01 = (int) ((2 * (q1.x * q1.y - q1.w * q1.z)) * tmp_scale[1]);
        m11 = (int) ((1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z) * tmp_scale[1]);
        m21 = (int) ((2 * (q1.y * q1.z + q1.w * q1.x)) * tmp_scale[1]);

        m02 = (int) ((2 * (q1.x * q1.z + q1.w * q1.y)) * tmp_scale[2]);
        m12 = (int) ((2 * (q1.y * q1.z - q1.w * q1.x)) * tmp_scale[2]);
        m22 = (int) ((1 - 2 * q1.x * q1.x - 2 * q1.y * q1.y) * tmp_scale[2]);
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * equivalent values of the axis-angle argument; the other elements of this
     * matrix are unchanged; a singular value decomposition is performed on this
     * object's upper 3x3 matrix to factor out the scale, then this object's
     * upper 3x3 matrix components are replaced by the matrix equivalent of the
     * axis-angle, and then the scale is reapplied to the rotational components.
     *
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */
    public final void setRotation(AxisAngle4f a1) {
        double[] tmp_rot = new double[9];  // scratch matrix
        double[] tmp_scale = new double[3];  // scratch matrix

        getScaleRotate(tmp_scale, tmp_rot);

        double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
        if (mag < EPS) {
            m00 = 1;
            m01 = 0;
            m02 = 0;

            m10 = 0;
            m11 = 1;
            m12 = 0;

            m20 = 0;
            m21 = 0;
            m22 = 1;
        } else {
            mag = 1.0 / mag;
            double ax = a1.x * mag;
            double ay = a1.y * mag;
            double az = a1.z * mag;

            double sinTheta = Math.sin(a1.angle);
            double cosTheta = Math.cos(a1.angle);
            double t = 1.0 - cosTheta;

            double xz = a1.x * a1.z;
            double xy = a1.x * a1.y;
            double yz = a1.y * a1.z;

            m00 = (int) ((t * ax * ax + cosTheta) * tmp_scale[0]);
            m01 = (int) ((t * xy - sinTheta * az) * tmp_scale[1]);
            m02 = (int) ((t * xz + sinTheta * ay) * tmp_scale[2]);

            m10 = (int) ((t * xy + sinTheta * az) * tmp_scale[0]);
            m11 = (int) ((t * ay * ay + cosTheta) * tmp_scale[1]);
            m12 = (int) ((t * yz - sinTheta * ax) * tmp_scale[2]);

            m20 = (int) ((t * xz - sinTheta * ay) * tmp_scale[0]);
            m21 = (int) ((t * yz + sinTheta * ax) * tmp_scale[1]);
            m22 = (int) ((t * az * az + cosTheta) * tmp_scale[2]);
        }

    }

    /**
     * Sets this matrix to all zeros.
     */
    public final void setZero() {
        m00 = 0;
        m01 = 0;
        m02 = 0;
        m03 = 0;
        m10 = 0;
        m11 = 0;
        m12 = 0;
        m13 = 0;
        m20 = 0;
        m21 = 0;
        m22 = 0;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 0;
    }

    /**
     * Negates the value of this matrix: this = -this.
     */
    public final void negate() {
        m00 = -m00;
        m01 = -m01;
        m02 = -m02;
        m03 = -m03;
        m10 = -m10;
        m11 = -m11;
        m12 = -m12;
        m13 = -m13;
        m20 = -m20;
        m21 = -m21;
        m22 = -m22;
        m23 = -m23;
        m30 = -m30;
        m31 = -m31;
        m32 = -m32;
        m33 = -m33;
    }

    /**
     * Sets the value of this matrix equal to the negation of of the Matrix4f
     * parameter.
     *
     * @param m1 the source matrix
     */
    public final void negate(Matrix4i m1) {
        this.m00 = -m1.m00;
        this.m01 = -m1.m01;
        this.m02 = -m1.m02;
        this.m03 = -m1.m03;
        this.m10 = -m1.m10;
        this.m11 = -m1.m11;
        this.m12 = -m1.m12;
        this.m13 = -m1.m13;
        this.m20 = -m1.m20;
        this.m21 = -m1.m21;
        this.m22 = -m1.m22;
        this.m23 = -m1.m23;
        this.m30 = -m1.m30;
        this.m31 = -m1.m31;
        this.m32 = -m1.m32;
        this.m33 = -m1.m33;
    }

    private final void getScaleRotate(double scales[], double rots[]) {

        double[] tmp = new double[9];  // scratch matrix
        tmp[0] = m00;
        tmp[1] = m01;
        tmp[2] = m02;

        tmp[3] = m10;
        tmp[4] = m11;
        tmp[5] = m12;

        tmp[6] = m20;
        tmp[7] = m21;
        tmp[8] = m22;

        Matrix3d.compute_svd(tmp, scales, rots);

        return;
    }

    /**
     * Creates a new object of the same class as this object.
     *
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     * @since vecmath 1.3
     */
    public Object clone() {
        Matrix4i m1 = null;
        try {
            m1 = (Matrix4i) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }

        return m1;
    }

    /**
     * Get the first matrix element in the first row.
     *
     * @return Returns the m00.
     *
     * @since vecmath 1.5
     */
    public final int getM00() {
        return m00;
    }

    /**
     * Set the first matrix element in the first row.
     *
     * @param m00 The m00 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM00(int m00) {
        this.m00 = m00;
    }

    /**
     * Get the second matrix element in the first row.
     *
     * @return Returns the m01.
     *
     * @since vecmath 1.5
     */
    public final int getM01() {
        return m01;
    }

    /**
     * Set the second matrix element in the first row.
     *
     * @param m01 The m01 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM01(int m01) {
        this.m01 = m01;
    }

    /**
     * Get the third matrix element in the first row.
     *
     * @return Returns the m02.
     *
     * @since vecmath 1.5
     */
    public final int getM02() {
        return m02;
    }

    /**
     * Set the third matrix element in the first row.
     *
     * @param m02 The m02 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM02(int m02) {
        this.m02 = m02;
    }

    /**
     * Get first matrix element in the second row.
     *
     * @return Returns the m10.
     *
     * @since vecmath 1.5
     */
    public final int getM10() {
        return m10;
    }

    /**
     * Set first matrix element in the second row.
     *
     * @param m10 The m10 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM10(int m10) {
        this.m10 = m10;
    }

    /**
     * Get second matrix element in the second row.
     *
     * @return Returns the m11.
     *
     * @since vecmath 1.5
     */
    public final int getM11() {
        return m11;
    }

    /**
     * Set the second matrix element in the second row.
     *
     * @param m11 The m11 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM11(int m11) {
        this.m11 = m11;
    }

    /**
     * Get the third matrix element in the second row.
     *
     * @return Returns the m12.
     *
     * @since vecmath 1.5
     */
    public final int getM12() {
        return m12;
    }

    /**
     * Set the third matrix element in the second row.
     *
     * @param m12 The m12 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM12(int m12) {
        this.m12 = m12;
    }

    /**
     * Get the first matrix element in the third row.
     *
     * @return Returns the m20.
     *
     * @since vecmath 1.5
     */
    public final int getM20() {
        return m20;
    }

    /**
     * Set the first matrix element in the third row.
     *
     * @param m20 The m20 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM20(int m20) {
        this.m20 = m20;
    }

    /**
     * Get the second matrix element in the third row.
     *
     * @return Returns the m21.
     *
     * @since vecmath 1.5
     */
    public final int getM21() {
        return m21;
    }

    /**
     * Set the second matrix element in the third row.
     *
     * @param m21 The m21 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM21(int m21) {
        this.m21 = m21;
    }

    /**
     * Get the third matrix element in the third row.
     *
     * @return Returns the m22.
     *
     * @since vecmath 1.5
     */
    public final int getM22() {
        return m22;
    }

    /**
     * Set the third matrix element in the third row.
     *
     * @param m22 The m22 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM22(int m22) {
        this.m22 = m22;
    }

    /**
     * Get the fourth element of the first row.
     *
     * @return Returns the m03.
     *
     * @since vecmath 1.5
     */
    public final int getM03() {
        return m03;
    }

    /**
     * Set the fourth element of the first row.
     *
     * @param m03 The m03 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM03(int m03) {
        this.m03 = m03;
    }

    /**
     * Get the fourth element of the second row.
     *
     * @return Returns the m13.
     *
     * @since vecmath 1.5
     */
    public final int getM13() {
        return m13;
    }

    /**
     * Set the fourth element of the second row.
     *
     * @param m13 The m13 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM13(int m13) {
        this.m13 = m13;
    }

    /**
     * Get the fourth element of the third row.
     *
     * @return Returns the m23.
     *
     * @since vecmath 1.5
     */
    public final int getM23() {
        return m23;
    }

    /**
     * Set the fourth element of the third row.
     *
     * @param m23 The m23 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM23(int m23) {
        this.m23 = m23;
    }

    /**
     * Get the first element of the fourth row.
     *
     * @return Returns the m30.
     *
     * @since vecmath 1.5
     */
    public final int getM30() {
        return m30;
    }

    /**
     * Set the first element of the fourth row.
     *
     * @param m30 The m30 to set.
     *
     *
     * @since vecmath 1.5
     */
    public final void setM30(int m30) {
        this.m30 = m30;
    }

    /**
     * Get the second element of the fourth row.
     *
     * @return Returns the m31.
     *
     * @since vecmath 1.5
     */
    public final int getM31() {
        return m31;
    }

    /**
     * Set the second element of the fourth row.
     *
     * @param m31 The m31 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM31(int m31) {
        this.m31 = m31;
    }

    /**
     * Get the third element of the fourth row.
     *
     * @return Returns the m32.
     *
     * @since vecmath 1.5
     */
    public final int getM32() {
        return m32;
    }

    /**
     * Set the third element of the fourth row.
     *
     * @param m32 The m32 to set.
     *
     *
     * @since vecmath 1.5
     */
    public final void setM32(int m32) {
        this.m32 = m32;
    }

    /**
     * Get the fourth element of the fourth row.
     *
     * @return Returns the m33.
     *
     * @since vecmath 1.5
     */
    public final int getM33() {
        return m33;
    }

    /**
     * Set the fourth element of the fourth row.
     *
     * @param m33 The m33 to set.
     *
     * @since vecmath 1.5
     */
    public final void setM33(int m33) {
        this.m33 = m33;
    }
}
