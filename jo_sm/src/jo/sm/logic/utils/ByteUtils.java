/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.logic.utils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ByteUtils {

    public static byte flipByte(byte b) {
        return (byte) (((b & 0x01) << 7)
                | ((b & 0x02) << 5)
                | ((b & 0x04) << 3)
                | ((b & 0x08) << 1)
                | ((b & 0x10) >> 1)
                | ((b & 0x20) >> 3)
                | ((b & 0x40) >> 5)
                | ((b & 0x80) >> 7));
    }

    public static boolean getBit(byte[] bytes, int off) {
        byte b = bytes[off / 8];
        return (b & (0x01 << (off % 8))) != 0;
    }

    public static void setBit(byte[] bytes, int off, boolean v) {
        if (v) {
            bytes[off / 8] |= (0x01 << (off % 8));
        } else {
            bytes[off / 8] &= ~(0x01 << (off % 8));
        }
    }

    public static void flipBits(byte[] bytes, int start, int bitLength) {
        for (int i = 0; i < bitLength / 2; i++) {
            boolean leftBit = getBit(bytes, start * 8 + i);
            boolean rightBit = getBit(bytes, start * 8 + bitLength - i - 1);
            setBit(bytes, start * 8 + i, rightBit);
            setBit(bytes, start * 8 + bitLength - i - 1, leftBit);
        }
    }

    public static void copyBits(byte[] from, int fromByteOffset, int fromBitOffset, byte[] to, int toByteOffset, int toBitOffset, int bits) {
        for (int i = 0; i < bits; i++) {
            boolean bit = getBit(from, fromByteOffset * 8 + fromBitOffset + i);
            setBit(to, toByteOffset * 8 + toBitOffset + i, bit);
        }
    }

    public static Object[] toArray(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        Byte[] objArray = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            objArray[i] = byteArray[i];
        }
        return objArray;
    }

    // LONG CONVERSION
    public static long toLong(byte[] readBuffer, int o) {
        return (((long) readBuffer[0] << 56)
                + ((long) (readBuffer[1] & 255) << 48)
                + ((long) (readBuffer[2] & 255) << 40)
                + ((long) (readBuffer[3] & 255) << 32)
                + ((long) (readBuffer[4] & 255) << 24)
                + ((readBuffer[5] & 255) << 16)
                + ((readBuffer[6] & 255) << 8)
                + ((readBuffer[7] & 255)));
    }

    public static long toLong(byte[] readBuffer) {
        return toLong(readBuffer, 0);
    }

    public static long[] toLongs(byte[] readBuffer, int o, int l) {
        long[] v = new long[l / 8];
        for (int i = 0; i < v.length; i++) {
            v[i] = toLong(readBuffer, o + i * 8);
        }
        return v;
    }

    public static byte[] toBytes(long v, byte[] writeBuffer, int o) {
        writeBuffer[o + 0] = (byte) (v >>> 56);
        writeBuffer[o + 1] = (byte) (v >>> 48);
        writeBuffer[o + 2] = (byte) (v >>> 40);
        writeBuffer[o + 3] = (byte) (v >>> 32);
        writeBuffer[o + 4] = (byte) (v >>> 24);
        writeBuffer[o + 5] = (byte) (v >>> 16);
        writeBuffer[o + 6] = (byte) (v >>> 8);
        writeBuffer[o + 7] = (byte) (v);
        return writeBuffer;
    }

    public static byte[] toBytes(long v, byte[] writeBuffer) {
        return toBytes(v, writeBuffer, 0);
    }

    public static byte[] toBytes(long v) {
        byte[] writeBuffer = new byte[8];
        return toBytes(v, writeBuffer, 0);
    }

    public static byte[] toBytes(long[] v, byte[] writeBuffer, int o) {
        for (int i = 0; i < v.length; i++) {
            toBytes(v[i], writeBuffer, o + i * 8);
        }
        return writeBuffer;
    }

    public static byte[] toBytes(long[] v) {
        byte[] writeBuffer = new byte[8 * v.length];
        return toBytes(v, writeBuffer, 0);
    }

    // INT CONVERSION
    public static int toInt(byte[] readBuffer, int o) {
        int ch1 = readBuffer[o++];
        int ch2 = readBuffer[o++];
        int ch3 = readBuffer[o++];
        int ch4 = readBuffer[o++];
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new IllegalStateException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    public static int toInt(byte[] readBuffer) {
        return toInt(readBuffer, 0);
    }

    public static int[] toInts(byte[] readBuffer, int o, int l) {
        int[] v = new int[l / 4];
        for (int i = 0; i < v.length; i++) {
            v[i] = toInt(readBuffer, o + i * 4);
        }
        return v;
    }

    public static byte[] toBytes(int v, byte[] writeBuffer, int o) {
        writeBuffer[o + 0] = (byte) (v >>> 24);
        writeBuffer[o + 1] = (byte) (v >>> 16);
        writeBuffer[o + 2] = (byte) (v >>> 8);
        writeBuffer[o + 3] = (byte) (v);
        return writeBuffer;
    }

    public static byte[] toBytes(int v) {
        byte[] writeBuffer = new byte[4];
        return toBytes(v, writeBuffer, 0);
    }

    public static byte[] toBytes(int[] v, byte[] writeBuffer, int o) {
        for (int i = 0; i < v.length; i++) {
            toBytes(v[i], writeBuffer, o + i * 4);
        }
        return writeBuffer;
    }

    public static byte[] toBytes(int[] v) {
        byte[] writeBuffer = new byte[4 * v.length];
        return toBytes(v, writeBuffer, 0);
    }

    // SHORT CONVERSION
    public static short toShort(byte[] readBuffer, int o) {
        return (short) (((readBuffer[o + 0] & 255) << 8)
                + ((readBuffer[o + 1] & 255)));
    }

    public static short toShort(byte[] readBuffer) {
        return toShort(readBuffer, 0);
    }

    public static short[] toShorts(byte[] readBuffer, int o, int l) {
        short[] v = new short[l / 2];
        for (int i = 0; i < v.length; i++) {
            v[i] = toShort(readBuffer, o + i * 2);
        }
        return v;
    }

    public static byte[] toBytes(short v, byte[] writeBuffer, int o) {
        writeBuffer[o + 0] = (byte) (v >>> 8);
        writeBuffer[o + 1] = (byte) (v);
        return writeBuffer;
    }

    public static byte[] toBytes(short v) {
        byte[] writeBuffer = new byte[2];
        return toBytes(v, writeBuffer, 0);
    }

    public static byte[] toBytes(short[] v, byte[] writeBuffer, int o) {
        for (int i = 0; i < v.length; i++) {
            toBytes(v[i], writeBuffer, o + i * 2);
        }
        return writeBuffer;
    }

    public static byte[] toBytes(short[] v) {
        byte[] writeBuffer = new byte[2 * v.length];
        return toBytes(v, writeBuffer, 0);
    }

    public static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            String s = Integer.toHexString(b);
            if (s.length() > 2) {
                s = s.substring(s.length() - 2);
            } else if (s.length() == 1) {
                s = "0" + s;
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toStringDump(byte[] b) {
        return toStringDump(b, 0, b.length);
    }

    public static String toStringDump(byte[] b, int o, int l) {
        StringBuilder sb = new StringBuilder();
        if (b != null) {
            for (int i = 0; i < l; i += 16) {
                sb.append(StringUtils.zeroPrefix(Integer.toHexString(i), 4));
                for (int j = 0; j < 16; j++) {
                    if (j % 4 == 0) {
                        sb.append("  ");
                    }
                    sb.append(" ");
                    if (o + i + j < b.length) {
                        sb.append(StringUtils.zeroPrefix(Integer.toHexString(b[o + i + j] & 0xff), 2));
                    } else {
                        sb.append("  ");
                    }
                }
                sb.append("    ");
                for (int j = 0; j < 16; j++) {
                    if (o + i + j < b.length) {
                        char ch = (char) (b[o + i + j] & 0xff);
                        if ((ch >= ' ') && (ch <= '~')) {
                            sb.append(ch);
                        } else {
                            sb.append('?');
                        }
                    } else {
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
