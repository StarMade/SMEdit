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
package jo.sm.ship.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.logic.IOLogic;
import jo.sm.logic.utils.DebugLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Chunk;
import jo.sm.ship.data.Data;
import jo.vecmath.Point3i;
import jo.vecmath.logic.Point3iLogic;


public class DataLogic {
    private static final Logger log = Logger.getLogger(DataLogic.class.getName());

    public static Map<Point3i, Data> readFiles(File dataDir, String prefix, IPluginCallback cb) throws IOException {
        cb.setStatus("Reading " + prefix);
        Map<Point3i, Data> data = new HashMap<>();
        List<File> files = new ArrayList<>();
        for (File dataFile : dataDir.listFiles()) {
            if (dataFile.getName().endsWith(".smd2")
                    && dataFile.getName().startsWith(prefix)) {
                files.add(dataFile);
            }
        }
        if (files.isEmpty()) {
            for (File dataFile : dataDir.listFiles()) {
                if (dataFile.getName().endsWith(".smd2")) {
                    if ((dataFile.toString().contains("server-database"))
                            && !dataFile.getName().startsWith("ENTITY_SHIP_")) {
                        continue;
                    }
                    files.add(dataFile);
                }
            }
        }
        cb.startTask(files.size());
        for (File dataFile : files) {
            readDataFromEntityFile(dataFile, data);
            cb.workTask(1);
        }
        cb.endTask();
        return data;
    }

    private static void readDataFromEntityFile(File dataFile,
            Map<Point3i, Data> data) throws IOException, FileNotFoundException {
        String[] parts = dataFile.getName().split("\\.");
        int l = parts.length;
        Point3i position = new Point3i(Integer.parseInt(parts[l - 4]),
                Integer.parseInt(parts[l - 3]),
                Integer.parseInt(parts[l - 2]));
        log.log(Level.INFO, "Reading from " + dataFile.getName() + " - " + position);
        //System.out.println("Reading from " + dataFile.getName() + " - " + position);
        Data datum = DataLogic.readFile(new FileInputStream(dataFile), true, position);
        data.put(position, datum);
    }

    public static Data readFile(InputStream is, boolean close) throws IOException {
        return readFile(is, close, null);
    }

    public static Data readFile(InputStream is, boolean close, Point3i superChunkIndex) throws IOException {
        //System.out.println("Reading...");
        Point3i superChunkOrigin = ShipLogic.getSuperChunkOriginFromIndex(superChunkIndex);
        System.out.println("Super Chunk Index: " + superChunkIndex);
        System.out.println("Super Chunk Origin: " + superChunkOrigin);
        System.out.println("Super Chunk Lower: " + ShipLogic.getSuperChunkLowerFromOrigin(superChunkOrigin));
        System.out.println("Super Chunk Upper: " + ShipLogic.getSuperChunkUpperFromOrigin(superChunkOrigin));
        Point3i min = null;
        Point3i max = null;
        DataInputStream dis;
        if (is instanceof DataInputStream) {
            dis = (DataInputStream) is;
        } else {
            dis = new DataInputStream(is);
        }
        Data data = new Data();
        data.setUnknown1(dis.readInt());
        int[][][][] offsetSizeTable = new int[16][16][16][2];
        IOLogic.readFully(dis, offsetSizeTable);
        // index offsets
        Map<Integer, Point3i> chunkOffsets = new HashMap<>();
        int maxOffset = -1;
        for (CubeIterator i = new CubeIterator(new Point3i(), new Point3i(15, 15, 15)); i.hasNext();) {
            Point3i p = i.next();
            int offset = offsetSizeTable[p.z][p.y][p.x][0];
            if (offset >= 0) {
                chunkOffsets.put(offset, p);
                maxOffset = Math.max(maxOffset, offset);
            }
        }
        long[][][] timestampTable = new long[16][16][16];
        IOLogic.readFully(dis, timestampTable);
        //data.setTimestampTable(unknown3);
        List<Chunk> chunks = new ArrayList<>();
        for (int offset = 0; offset <= maxOffset; offset++) {
            byte[] chunkData = new byte[5120];
            try {
                dis.readFully(chunkData);
            } catch (EOFException e) {
                break;
            }
            //System.out.println(ByteUtils.toStringDump(chunkData));
            DataInputStream dis2 = new DataInputStream(new ByteArrayInputStream(chunkData));
            Chunk chunk = new Chunk();
            chunk.setTimestamp(dis2.readLong());
            chunk.setPosition(IOLogic.readPoint3i(dis2));
            chunk.setType(dis2.readByte());
            int compressedLen = dis2.readInt();
            //System.out.println("Chunk "+chunk.getPosition());
            //System.out.println("CompressedLen="+compressedLen);
            byte[] compressedData = new byte[compressedLen];
            dis2.readFully(compressedData);
            DataInputStream dis3 = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(compressedData)));
            Block[][][] blocks = new Block[16][16][16];
            //int blockCount = 0;
            byte[] inbuf = new byte[3];
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        blocks[x][y][z] = new Block();
                        dis3.readFully(inbuf);
                        int bitfield = toUnsignedInt(inbuf);
                        blocks[x][y][z].setBlockID((short) ((bitfield >> 0) & 0x7ff));
                        blocks[x][y][z].setHitPoints((short) ((bitfield >> 11) & 0x1ff));
                        blocks[x][y][z].setActive(((bitfield >> 20) & 0x1) == 1);
                        blocks[x][y][z].setOrientation((short) (((bitfield >> 21) & 0x7)
                                | ((bitfield >> (20 - 3)) & 0x8)));
                        //if (BlockTypes.isHull(blocks[x][y][z].getBlockID()))
                        //    System.out.println("  Block "+Integer.toHexString(bitfield)
                        //            +" (id="+ blocks[x][y][z].getBlockID()+", hp="+ blocks[x][y][z].getHitPoints()+")"
                        //            +" "+ByteUtils.toString(inbuf));
                        //if (bitfield != 0)
                        //    blockCount++;
                        if (blocks[x][y][z].getBlockID() <= 0) {
                            blocks[x][y][z] = null; // clear out unneeded blocks
                        } else if (DebugLogic.HULL_ONLY) {
                            if (!BlockTypes.isAnyHull(blocks[x][y][z].getBlockID())) {
                                blocks[x][y][z] = null; // clear out unneeded blocks
                            }
                        }
                    }
                }
            }
            //System.out.println("Block count="+blockCount);
            chunk.setBlocks(blocks);
            min = Point3iLogic.min(min, chunk.getPosition());
            max = Point3iLogic.max(max, chunk.getPosition());
            if (chunkOffsets.containsKey(offset)) {
                Point3i chunkIndex = chunkOffsets.get(offset);
                Point3i expected = ShipLogic.getChunkPositionFromSuperchunkOriginAndChunkIndex(superChunkOrigin, chunkIndex);
                if (!chunk.getPosition().equals(expected)) {
                    log.log(Level.INFO, "Chunk #" + offset + "/" + chunkOffsets.get(offset) + " expected in position " + expected + ", but actually in " + chunk.getPosition());
                    //System.out.println("Chunk #" + offset + "/" + chunkOffsets.get(offset) + " expected in position " + expected + ", but actually in " + chunk.getPosition());
                    chunk.setPosition(expected);
                }
                chunks.add(chunk);
            } else {
                log.log(Level.INFO, "Orphan chunk '" + chunk.getPosition() + "/" + offset);
                //System.out.println("Orphan chunk '" + chunk.getPosition() + "/" + offset);
            }
            // backtrack offset table
            /*
             if ((superChunkIndex != null) && (superChunkIndex.z < 0) && (superChunkIndex.y == 0) && (superChunkIndex.x == 0))
             {
             if ((chunk.getPosition().x == 0) && (chunk.getPosition().y == 0))
             {
             System.out.println("Chunk "+chunk.getPosition());
             for (int z = 0; z < 16; z++)
             {
             System.out.print("|");	                
             for (int x = 15; x >= 0; x--)
             {
             boolean any = false;
             for (int y = 0; y < 16; y++)
             if (blocks[x][y][z] != null)
             {
             if (blocks[x][y][z].getBlockID() == BlockTypes.HULL_COLOR_BLACK_ID)
             System.out.print("#");
             else if (blocks[x][y][z].getBlockID() == BlockTypes.HULL_COLOR_WHITE_ID)
             System.out.print(".");
             else if (blocks[x][y][z].getBlockID() == BlockTypes.HULL_COLOR_YELLOW_ID)
             System.out.print(",");
             else
             System.out.print("?");
             any = true;
             break;
             }
             if (!any)
             System.out.print(" ");
             }
             System.out.println("|");
             }
             }
             }
             */
        }
        data.setChunks(chunks.toArray(new Chunk[0]));
        if (close) {
            dis.close();
        }
        if (chunks.isEmpty()) {
            log.log(Level.INFO, "NO CHUNKS!");
            //System.out.println("NO CHUNKS!");
        } else if (chunkOffsets.isEmpty()) {
            log.log(Level.INFO, "NO OFFSET TABLE!");
            //System.out.println("NO OFFSET TABLE!");
        }
        log.log(Level.INFO, "Range: " + min + " -> " + max);
        //System.out.println("Range: " + min + " -> " + max);
        return data;
    }

    public static void writeFile(Point3i superChunkIndex, Data data, OutputStream os, boolean close, IPluginCallback cb) throws IOException {
        Point3i superChunkOrigin = ShipLogic.getSuperChunkOriginFromIndex(superChunkIndex);
        if (cb != null) {
            cb.setStatus("Writing " + superChunkIndex);
        }
        DataOutputStream dos;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream) os;
        } else {
            dos = new DataOutputStream(os);
        }
        dos.writeInt(0); // non-compressed header

        int[][][][] offsetSizeTable = new int[16][16][16][2];
        for (int z = 0; z < 16; z++) {
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < 16; x++) {
                    offsetSizeTable[x][y][z][0] = -1;
                }
            }
        }
        long[][][] timestampTable = new long[16][16][16];
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DataOutputStream dos2 = new DataOutputStream(baos2);
        for (int i = 0; i < data.getChunks().length; i++) {
            Chunk chunk = data.getChunks()[i];
            ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
            //int blockCount = 0;
            try (DataOutputStream dos3 = new DataOutputStream(new DeflaterOutputStream(baos3))) {
                //int blockCount = 0;
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        for (int x = 0; x < 16; x++) {
                            Block b = chunk.getBlocks()[x][y][z];
                            int bitfield = 0;
                            if (b != null) {
                                bitfield |= ((b.getBlockID() & 0x7ff) << 0);
                                bitfield |= ((b.getHitPoints() & 0x1ff) << 11);
                                bitfield |= ((b.getOrientation() & 0x8) << (20 - 3));
                                bitfield |= ((b.getOrientation() & 0x7) << 21);
//                            blockCount++;
//                            if (BlockTypes.isHull(b.getBlockID()))
//                                System.out.println("  Block "+Integer.toHexString(bitfield)
//                                        +" (id="+b.getBlockID()+", hp="+b.getHitPoints()+")"
//                                        +" "+ByteUtils.toString(fromUnsignedInt(bitfield)));
                            }
                            dos3.write(fromUnsignedInt(bitfield));
                        }
                    }
                }
            }
            byte[] compressedData = baos3.toByteArray();
            //System.out.println("Block count="+blockCount);

            dos2.writeLong(chunk.getTimestamp());
            IOLogic.write(dos2, chunk.getPosition());
            dos2.writeByte(chunk.getType());
            //System.out.println("CompressedLen="+compressedData.length);
            dos2.writeInt(compressedData.length);
            dos2.write(compressedData);
            for (int j = 25 + compressedData.length; j < 5120; j++) {
                dos2.writeByte(0);
            }
            Point3i chunkIndex = ShipLogic.getChunkIndexFromSuperchunkOriginAndChunkPosition(superChunkOrigin, chunk.getPosition());
            try {
                offsetSizeTable[chunkIndex.z][chunkIndex.y][chunkIndex.x][1] = 25 + compressedData.length;
                offsetSizeTable[chunkIndex.z][chunkIndex.y][chunkIndex.x][0] = i;
                timestampTable[chunkIndex.z][chunkIndex.y][chunkIndex.x] = chunk.getTimestamp();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                log.log(Level.INFO, "ChunkPosition: " + chunk.getPosition() + "SuperChunkIndex: " + superChunkIndex + ", index=" + chunkIndex);
                //System.out.println("ChunkPosition: " + chunk.getPosition() + "SuperChunkIndex: " + superChunkIndex + ", index=" + chunkIndex);
            }
        }
        dos2.flush();

        // chunk index
        IOLogic.write(dos, offsetSizeTable);
        IOLogic.write(dos, timestampTable);
        dos.write(baos2.toByteArray());

        if (close) {
            dos.close();
        }
    }

    public static Point3i getLocalIndex(Point3i superChunkIndex,
            Point3i superChunkLower, Point3i universePoint) {
        Point3i index = new Point3i(universePoint);
        index.sub(superChunkLower);
        index.scale(1, 16);
        // weird reversal
        if (superChunkIndex.x < 0) {
            index.x = 15 - index.x;
        }
        if (superChunkIndex.y < 0) {
            index.y = 15 - index.y;
        }
        if (superChunkIndex.z < 0) {
            index.z = 15 - index.z;
        }
        return index;
    }

    private static byte[] fromUnsignedInt(int i) {
        byte[] outbuf = new byte[3];
        outbuf[2] = (byte) (i & 0xff);
        i >>= 8;
        outbuf[1] = (byte) (i & 0xff);
        i >>= 8;
        outbuf[0] = (byte) (i & 0xff);
        return outbuf;
    }

    private static int toUnsignedInt(byte[] bytes) {
        return toUnsignedInt(bytes, 0, bytes.length);
    }

    private static int toUnsignedInt(byte[] bytes, int o, int l) {
        long v = 0;
        for (int i = 0; i < l; i++) {
            v = (v << 8) | (bytes[o + i] & 0xff);
        }
        return (int) v;
    }

    public static void writeFiles(Map<Point3i, Data> data, File baseDir,
            String baseName, IPluginCallback cb) throws IOException {
        // clean up first
        File[] oldFiles = baseDir.listFiles();
        if (oldFiles != null) {
            for (File f : oldFiles) {
                if (f.getName().startsWith(baseName)) {
                    f.delete();
                }
            }
        }
        if (cb != null) {
            cb.setStatus("Writing " + baseName);
            cb.startTask(data.size());
        }
        for (Point3i p : data.keySet()) {
            File dataFile = new File(baseDir, baseName + "." + p.x + "." + p.y + "." + p.z + ".smd2");
            // TEST
            /*
             byte[] original = FileUtils.readFile(dataFile.toString());
             //String oTxt = ByteUtils.toStringDump(original);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             writeFile(data.get(p), baos, true);
             byte[] save = baos.toByteArray();
             String sTxt = ByteUtils.toStringDump(save);
             if (oTxt.equals(sTxt))
             System.out.println("Identical");
             else
             {
             StringTokenizer oST = new StringTokenizer(oTxt, "\r\n");
             StringTokenizer sST = new StringTokenizer(sTxt, "\r\n");
             while (oST.hasMoreTokens())
             {
             String oLine = oST.nextToken();
             String sLine = sST.nextToken();
             if (!oLine.equals(sLine))
             {
             System.out.println("O: "+oLine);
             System.out.println("S: "+sLine);
             }
             }
             }
             try
             {
             readFile(new ByteArrayInputStream(save), true);
             }
             catch (Exception e)
             {
             e.printStackTrace();
             return;
             }
             */
            if (dataFile.exists()) {
                File dest = new File(baseDir, baseName + "." + p.x + "." + p.y + "." + p.z + ".smd2.bak");
                if (dest.exists()) {
                    dest.delete();
                }
                dataFile.renameTo(dest);
            }
            writeFile(p, data.get(p), new FileOutputStream(dataFile), true, cb);
            if (cb != null) {
                cb.workTask(1);
            }
        }
        if (cb != null) {
            cb.endTask();
        }
    }
}
