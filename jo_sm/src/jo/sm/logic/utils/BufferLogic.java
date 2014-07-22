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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class BufferLogic {

    /**
     * Construct a direct native-ordered bytebuffer with the specified size.
     *
     * @param size The size, in bytes
     * @return a ByteBuffer
     */
    public static ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }

    /**
     * Construct a direct native-order shortbuffer with the specified number of
     * elements.
     *
     * @param size The size, in shorts
     * @return a ShortBuffer
     */
    public static ShortBuffer createShortBuffer(int size) {
        return createByteBuffer(size << 1).asShortBuffer();
    }

    /**
     * Construct a direct native-order charbuffer with the specified number of
     * elements.
     *
     * @param size The size, in chars
     * @return an CharBuffer
     */
    public static CharBuffer createCharBuffer(int size) {
        return createByteBuffer(size << 1).asCharBuffer();
    }

    /**
     * Construct a direct native-order intbuffer with the specified number of
     * elements.
     *
     * @param size The size, in ints
     * @return an IntBuffer
     */
    public static IntBuffer createIntBuffer(int size) {
        return createByteBuffer(size << 2).asIntBuffer();
    }

    /**
     * Construct a direct native-order longbuffer with the specified number of
     * elements.
     *
     * @param size The size, in longs
     * @return an LongBuffer
     */
    public static LongBuffer createLongBuffer(int size) {
        return createByteBuffer(size << 3).asLongBuffer();
    }

    /**
     * Construct a direct native-order floatbuffer with the specified number of
     * elements.
     *
     * @param size The size, in floats
     * @return a FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(int size) {
        return createByteBuffer(size << 2).asFloatBuffer();
    }

    /**
     * Construct a direct native-order doublebuffer with the specified number of
     * elements.
     *
     * @param size The size, in floats
     * @return a FloatBuffer
     */
    public static DoubleBuffer createDoubleBuffer(int size) {
        return createByteBuffer(size << 3).asDoubleBuffer();
    }

    public static FloatBuffer create(float... src) {
        FloatBuffer buff = createFloatBuffer(src.length);
        buff.put(src);
        buff.rewind();
        return buff;
    }

    public static IntBuffer create(int... src) {
        IntBuffer buff = createIntBuffer(src.length);
        buff.put(src);
        buff.rewind();
        return buff;
    }
}
