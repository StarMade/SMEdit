/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jo.util.lwjgl.win;

import java.awt.image.BufferedImage;

/**
 *
 * @author Wayne
 */
public class JGLTextureSpec {
    private String mFileName;
    private int mLeft;
    private int mTop;
    private int mWidth;
    private int mHeight;
    private BufferedImage mImage;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public int getLeft() {
        return mLeft;
    }

    public void setLeft(int left) {
        mLeft = left;
    }

    public int getTop() {
        return mTop;
    }

    public void setTop(int top) {
        mTop = top;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public BufferedImage getImage() {
        return mImage;
    }

    public void setImage(BufferedImage image) {
        mImage = image;
    }
    
}
