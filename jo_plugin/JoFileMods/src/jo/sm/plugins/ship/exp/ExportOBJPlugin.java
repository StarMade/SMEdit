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
package jo.sm.plugins.ship.exp;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.imageio.ImageIO;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.sm.ui.lwjgl.LWJGLRenderLogic;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;
import jo.vecmath.Point3f;
import jo.vecmath.logic.Point3fLogic;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ExportOBJPlugin implements IBlocksPlugin {

    public static final String NAME = "Export/OBJ";
    public static final String DESC = "Export OBJ file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_FILE, 26},
                {TYPE_STATION, SUBTYPE_FILE, 26},
                {TYPE_SHOP, SUBTYPE_FILE, 26},
                {TYPE_FLOATINGROCK, SUBTYPE_FILE, 26},
                {TYPE_PLANET, SUBTYPE_FILE, 26},};

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getAuthor() {
        return AUTH;
    }

    @Override
    public Object newParameterBean() {
        return new ExportOBJParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        ExportOBJParameters params = (ExportOBJParameters) p;
        try {
            JGLGroup quads = new JGLGroup();
            LWJGLRenderLogic.addBlocks(quads, original, false);
            writeFile(params.getFile(), quads);
            writeTexture(params.getFile());
        } catch (IOException e) {
            cb.setError(e);
        }
        return null;
    }

    private void writeTexture(String objFile) throws IOException {
        String pngFile = objFile.substring(0, objFile.length() - 4) + ".png";
        ImageIO.write(BlockTypeColors.mAllTextures, "PNG", new File(pngFile));
        BufferedImage noTrans = new BufferedImage(BlockTypeColors.mAllTextures.getWidth(),
                BlockTypeColors.mAllTextures.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = noTrans.getGraphics();
        g.drawImage(BlockTypeColors.mAllTextures, 0, 0, null);
        g.dispose();
        String jpgFile = objFile.substring(0, objFile.length() - 4) + ".jpg";
        ImageIO.write(noTrans, "JPG", new File(jpgFile));

        /*
         JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
         jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
         jpegParams.setCompressionQuality(0.15f);
         jpegParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
    	
         // use IIORegistry to get the available services
         IIORegistry registry = IIORegistry.getDefaultInstance();
         // return an iterator for the available ImageWriterSpi for jpeg images
         ServiceRegistry.Filter filter = new ServiceRegistry.Filter() {
         @Override
         public boolean filter(Object provider)
         {
         if (!(provider instanceof ImageWriterSpi)) return false;

         ImageWriterSpi writerSPI = (ImageWriterSpi) provider;
         String[] formatNames = writerSPI.getFormatNames();
         for (int i = 0; i < formatNames.length; i++) {
         if (formatNames[i].equalsIgnoreCase("JPEG")) {
         return true;
         }
         }

         return false;
         }
         };
    	
         Iterator<ImageWriterSpi> services = registry.getServiceProviders(ImageWriterSpi.class,
         filter,
         true);
         //...assuming that servies.hasNext() == true, I get the first available service.
         ImageWriterSpi writerSpi = services.next();
         ImageWriter writer = writerSpi.createWriterInstance();

         // specifies where the jpg image has to be written
         FileImageOutputStream os = new FileImageOutputStream(
         new File(jpgFile));
         writer.setOutput(os);

         // writes the file with given compression level 
         // from your JPEGImageWriteParam instance
         writer.write(null, new IIOImage(BlockTypeColors.mAllTextures, null, null), jpegParams);
         os.close();
         */
    }

    private void writeFile(String objFile, JGLGroup quads) throws IOException {
        File mtlFile = new File(objFile.substring(0, objFile.length() - 4) + ".mtl");
        File jpgFile = new File(objFile.substring(0, objFile.length() - 4) + ".jpg");
        BufferedWriter wtr = new BufferedWriter(new FileWriter(new File(objFile)));
        wtr.write("mtllib " + mtlFile.getName());
        wtr.newLine();
        wtr.newLine();
        wtr.write("g Mesh1 Model");
        wtr.newLine();
        wtr.newLine();
        wtr.write("usemtl material0");
        wtr.newLine();
        wtr.newLine();
        int vertPosition = 1;
        for (JGLNode node : quads.getChildren()) {
            if (node instanceof JGLObj) {
                JGLObj obj = (JGLObj) node;
                int vertCount = obj.getVertices();
                FloatBuffer verts = obj.getVertexBuffer();
                verts.rewind();
                Point3f v1 = null;
                Point3f v2 = null;
                Point3f v3 = null;
                for (int i = 0; i < vertCount; i++) {
                    float x = verts.get();
                    float y = verts.get();
                    float z = verts.get();
                    wtr.write("v " + x + " " + y + " " + z);
                    wtr.newLine();
                    if (i == 0) {
                        v1 = new Point3f(x, y, z);
                    } else if (i == 1) {
                        v2 = new Point3f(x, y, z);
                    } else if (i == 2) {
                        v3 = new Point3f(x, y, z);
                    }
                }
                FloatBuffer texts = obj.getTexturesBuffer();
                texts.rewind();
                if (texts == null) {
                } else {
                    for (int i = 0; i < vertCount; i++) {
                        float u = texts.get();
                        float v = texts.get();
                        wtr.write("vt " + u + " " + v);
                        wtr.newLine();
                    }
                    Point3f edge1 = Point3fLogic.sub(v2, v1);
                    Point3f edge2 = Point3fLogic.sub(v3, v1);
                    Point3f norm = Point3fLogic.cross(edge1, edge2);
                    for (int i = 0; i < vertCount; i++) {
                        wtr.write("vn " + norm.x + " " + norm.y + " " + norm.z);
                        wtr.newLine();
                    }
                }
                int faceCount = obj.getIndices();
                int facePoints = (obj.getMode() == JGLObj.TRIANGLES) ? 3 : 4;
                ShortBuffer indexes = obj.getIndexShortBuffer();
                if (indexes != null) {
                    indexes.rewind();
                    for (int i = 0; i < faceCount; i++) {
                        wtr.write("f");
                        for (int j = 0; j < facePoints; j++) {
                            short face = indexes.get();
                            wtr.write(" " + (face + vertPosition));
                            wtr.write("/" + (face + vertPosition));
                            wtr.write("/" + (face + vertPosition));
                        }
                        wtr.newLine();
                    }
                } else {
                    IntBuffer intIndexes = obj.getIndexIntBuffer();
                    if (intIndexes != null) {
                        intIndexes.rewind();
                        for (int i = 0; i < faceCount; i++) {
                            wtr.write("f");
                            for (int j = 0; j < facePoints; j++) {
                                int face = intIndexes.get();
                                wtr.write(" " + (face + vertPosition));
                                wtr.write("/" + (face + vertPosition));
                                wtr.write("/" + (face + vertPosition));
                            }
                            wtr.newLine();
                        }
                    }
                }
                vertPosition += vertCount;
            }
        }
        wtr.close();
        wtr = new BufferedWriter(new FileWriter(mtlFile));
        wtr.write("newmtl material0");
        wtr.newLine();
        wtr.write("Ka 1.000000 1.000000 1.000000");
        wtr.newLine();
        wtr.write("Kd 1.000000 1.000000 1.000000");
        wtr.newLine();
        wtr.write("Ks 0.000000 0.000000 0.000000");
        wtr.newLine();
        wtr.write("map_Kd " + jpgFile.getName());
        wtr.newLine();
        wtr.newLine();
        wtr.newLine();
        wtr.close();
    }
}
