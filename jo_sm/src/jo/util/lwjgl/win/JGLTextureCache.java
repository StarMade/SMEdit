package jo.util.lwjgl.win;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import jo.sm.logic.utils.BufferLogic;
import jo.sm.logic.utils.IntegerUtils;

import org.lwjgl.opengl.GL11;

public class JGLTextureCache {

    private static final Map<Integer, JGLTextureSpec> mSpecCache = new HashMap<>();
    private static final Map<Integer, Integer> mLoadedCache = new HashMap<>();
    private static final Set<Integer> mMRULoaded = new HashSet<>();

    public static void register(int id, JGLTextureSpec spec) {
        mSpecCache.put(id, spec);
    }

    public static void register(int id, String fileName) {
        JGLTextureSpec spec = new JGLTextureSpec();
        spec.setFileName(fileName);
        register(id, spec);
    }

    public static void register(int id, BufferedImage img) {
        JGLTextureSpec spec = new JGLTextureSpec();
        spec.setImage(img);
        register(id, spec);
    }

    public static void register(int id, String fileName, int left, int top, int width, int height) {
        JGLTextureSpec spec = new JGLTextureSpec();
        spec.setFileName(fileName);
        spec.setLeft(left);
        spec.setTop(top);
        spec.setWidth(width);
        spec.setHeight(height);
        register(id, spec);
    }

    public static boolean isRegistered(int id) {
        return mSpecCache.containsKey(id);
    }

    public static void markTextures() {
        synchronized (mMRULoaded) {
            mMRULoaded.clear();
        }
    }

    public static void freeUnusedTextures() {
        int[] unused = getUnUsedTextures();
        System.out.println("Freeing gl#" + unused);
        GL11.glDeleteTextures(IntBuffer.wrap(unused));
    }

    public static void freeAllTextures() {
        int[] used = getUsedTextures();
        GL11.glDeleteTextures(IntBuffer.wrap(used));
    }

    public static void useTexture(int textureID) {
        synchronized (mMRULoaded) {
            if (textureID == 0) {
                return;
            }
            mMRULoaded.add(textureID);
        }
    }

    public static int[] getUsedTextures() {
        synchronized (mMRULoaded) {
            return IntegerUtils.toArray(mMRULoaded.toArray());
        }
    }

    public static int[] getUnUsedTextures() {
        Set<Integer> unused = new HashSet<>();
        synchronized (mMRULoaded) {
            unused.addAll(mLoadedCache.keySet());
            unused.removeAll(mMRULoaded);
        }
        return IntegerUtils.toArray(unused.toArray());
    }

    public static int getTexture(int textureID) {
        useTexture(textureID);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (mLoadedCache.containsKey(textureID)) {
            return mLoadedCache.get(textureID);
        }
        if (!mSpecCache.containsKey(textureID)) {
            throw new IllegalArgumentException("Unknown texture ID=" + textureID);
        }
        int texture = genTexture();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        BufferedImage img = readImage(textureID);
        boolean storeAlphaChannel = true; // TODO: work it out from image
        ByteBuffer tex = readPixels(img, storeAlphaChannel);
        makeRGBTexture(tex, img.getWidth(), img.getHeight(), storeAlphaChannel);
        mLoadedCache.put(textureID, texture);
        System.out.println("Binding texture ID#" + textureID + " to gl#" + texture);
        return texture;
    }

    private static int genTexture() {
        IntBuffer tmp = BufferLogic.createIntBuffer(1);
        GL11.glGenTextures(tmp);
        return tmp.get(0);
    }

    private static void makeRGBTexture(ByteBuffer img, int width, int height,
            boolean storeAlphaChannel) {
        int components = storeAlphaChannel ? 4 : 3;
        int format = storeAlphaChannel ? GL11.GL_RGBA : GL11.GL_RGB;
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, components, width,
                height, 0, format, GL11.GL_UNSIGNED_BYTE, img);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    }

    private static BufferedImage readImage(int textureID) {
        try {
            JGLTextureSpec spec = mSpecCache.get(textureID);
            if (spec.getImage() != null) {
                return spec.getImage();
            }
            BufferedImage img = ImageIO.read(new File(spec.getFileName()));
            if (spec.getLeft() >= 0) {
                img = img.getSubimage(spec.getLeft(), spec.getTop(), spec.getWidth(), spec.getHeight());
            }
            spec.setImage(img);
            return img;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read image id=" + textureID, e);
        }
    }

    private static ByteBuffer readPixels(BufferedImage img,
            boolean storeAlphaChannel) {
        int w = img.getWidth();
        int h = img.getHeight();
        ByteBuffer texBuf;
        if (storeAlphaChannel) {
            texBuf = ByteBuffer.allocateDirect(w * h * 4);
        } else {
            texBuf = ByteBuffer.allocateDirect(w * h * 3);
        }
        for (int i = h - 1; i >= 0; i--) {
            for (int j = 0; j < w; j++) {
                int pix = img.getRGB(j, i);
                texBuf.put((byte) ((pix >> 16) & 0xff));// red
                texBuf.put((byte) ((pix >> 8) & 0xff)); // green
                texBuf.put((byte) ((pix) & 0xff)); // blue
                if (storeAlphaChannel) {
                    texBuf.put((byte) ((pix >> 24) & 0xff));// alpha
                }
            }
        }
        texBuf.rewind();
        return texBuf;
        /*
         int[] packedPixels = new int[img.getWidth() * img.getHeight()];

         PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(),
         img.getHeight(), packedPixels, 0, img.getWidth());
         try
         {
         pixelgrabber.grabPixels();
         }
         catch (InterruptedException e)
         {
         throw new RuntimeException();
         }

         int bytesPerPixel = storeAlphaChannel ? 4 : 3;
         // ByteBuffer unpackedPixels =
         // BufferUtil.newByteBuffer(packedPixels.length * bytesPerPixel);
         ByteBuffer unpackedPixels = ByteBuffer
         .allocateDirect(packedPixels.length * bytesPerPixel);

         for (int row = img.getHeight() - 1; row >= 0; row--)
         {
         for (int col = 0; col < img.getWidth(); col++)
         {
         int packedPixel = packedPixels[row * img.getWidth() + col];
         unpackedPixels.put((byte)((packedPixel >> 16) & 0xFF));
         unpackedPixels.put((byte)((packedPixel >> 8) & 0xFF));
         unpackedPixels.put((byte)((packedPixel >> 0) & 0xFF));
         if (storeAlphaChannel)
         {
         unpackedPixels.put((byte)((packedPixel >> 24) & 0xFF));
         }
         }
         }

         unpackedPixels.flip();

         return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
         */
    }


}
