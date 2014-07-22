/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jo.util.lwjgl.win;

import java.nio.ByteBuffer;

/**
 *
 * @author Wayne
 */
public class Texture {
    private final ByteBuffer pixels;
    private final int width;
    private final int height;

    public Texture(ByteBuffer pixels, int width, int height) {
        this.height = height;
        this.pixels = pixels;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }
    
}
