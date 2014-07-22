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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Chunk;
import jo.sm.ship.data.Data;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ShipLogic {

    public static void getBounds(Data datum, Point3i lower, Point3i upper) {
        boolean first = true;
        for (Chunk c : datum.getChunks()) {
            Point3i pos = c.getPosition();
            for (CubeIterator i = new CubeIterator(new Point3i(0, 0, 0), new Point3i(15, 15, 15)); i.hasNext();) {
                Point3i xyz = i.next();
                if ((c.getBlocks()[xyz.x][xyz.y][xyz.z] == null) || (c.getBlocks()[xyz.x][xyz.y][xyz.z].getBlockID() <= 0)) {
                    continue;
                }
                if (first) {
                    lower.add(pos, xyz);
                    upper.add(pos, xyz);
                    first = false;
                } else {
                    lower.x = Math.min(lower.x, pos.x + xyz.x);
                    lower.y = Math.min(lower.y, pos.y + xyz.y);
                    lower.z = Math.min(lower.z, pos.z + xyz.z);
                    upper.x = Math.max(upper.x, pos.x + xyz.x);
                    upper.y = Math.max(upper.y, pos.y + xyz.y);
                    upper.z = Math.max(upper.z, pos.z + xyz.z);
                }
            }
        }
    }

    public static void getBounds(Map<Point3i, Data> data, Point3i lower, Point3i upper) {
        boolean first = true;
        for (Point3i o : data.keySet()) {
            Point3i l = new Point3i();
            Point3i u = new Point3i();
            Data datum = data.get(o);
            getBounds(datum, l, u);
            if (first) {
                lower.set(l);
                upper.set(u);
                first = false;
            } else {
                lower.x = Math.min(lower.x, l.x);
                lower.y = Math.min(lower.y, l.y);
                lower.z = Math.min(lower.z, l.z);
                upper.x = Math.max(upper.x, u.x);
                upper.y = Math.max(upper.y, u.y);
                upper.z = Math.max(upper.z, u.z);
            }
        }
    }

    public static void dumpChunks(Map<Point3i, Data> data) {
        for (Point3i p : data.keySet()) {
            Data datum = data.get(p);
            Point3i lower = new Point3i();
            Point3i upper = new Point3i();
            getBounds(datum, lower, upper);
            System.out.println("Datum: " + p + ", " + lower + " -- " + upper);
            for (Chunk c : datum.getChunks()) {
                System.out.println("  Chunk: " + c.getPosition() + ", type=" + c.getType());
            }
        }
    }

    public static SparseMatrix<Block> getBlocks(Map<Point3i, Data> data) {
        SparseMatrix<Block> blocks = new SparseMatrix<>();
        for (Point3i dataOrigin : data.keySet()) {
            Data datum = data.get(dataOrigin);
            for (Chunk c : datum.getChunks()) {
                Point3i p = c.getPosition();
                //p.x += dataOrigin.x*256;
                //p.y += dataOrigin.y*256;
                //p.z += dataOrigin.z*256;
                for (CubeIterator i = new CubeIterator(new Point3i(0, 0, 0), new Point3i(15, 15, 15)); i.hasNext();) {
                    Point3i xyz = i.next();
                    Block b = c.getBlocks()[xyz.x][xyz.y][xyz.z];
                    if ((b != null) && (b.getBlockID() > 0)) {
                        blocks.set(p.x + xyz.x, p.y + xyz.y, p.z + xyz.z, b);
                    }
                }
            }
        }
        return blocks;
    }

    public static Map<Point3i, Data> getData(SparseMatrix<Block> blocks) {
        long now = System.currentTimeMillis();
        Map<Point3i, Map<Point3i, Chunk>> assemblies = new HashMap<>();
        for (Iterator<Point3i> i = blocks.iteratorNonNull(); i.hasNext();) {
            Point3i universePoint = i.next();
            Point3i superChunkIndex = getSuperChunkIndexFromPoint(universePoint);
            Point3i superChunkOrigin = getSuperChunkOriginFromIndex(superChunkIndex);
            Point3i superChunkLower = getSuperChunkLowerFromOrigin(superChunkOrigin);
            Map<Point3i, Chunk> assembly = assemblies.get(superChunkIndex);
            if (assembly == null) {
                assembly = new HashMap<>();
                assemblies.put(superChunkIndex, assembly);
            }
            Point3i chunkIndex = new Point3i(universePoint);
            chunkIndex.sub(superChunkLower);
            chunkIndex.scale(1, 16);
            Point3i chunkPosition = getChunkPositionFromSuperchunkLowerAndChunkIndex(superChunkLower, chunkIndex);
            Chunk chunk = assembly.get(chunkIndex);
            if (chunk == null) {
                chunk = new Chunk();
                chunk.setPosition(chunkPosition);
                chunk.setBlocks(new Block[16][16][16]);
                chunk.setTimestamp(now);
                chunk.setType(1);
                assembly.put(chunkIndex, chunk);
            }
            Point3i chunkOffset = new Point3i(universePoint.x - chunkPosition.x, universePoint.y - chunkPosition.y, universePoint.z - chunkPosition.z);
            try {
                chunk.getBlocks()[chunkOffset.x][chunkOffset.y][chunkOffset.z] = blocks.get(universePoint);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Universe Point: " + universePoint);
                System.out.println("Super Chunk Index: " + superChunkIndex);
                System.out.println("Super Chunk Origin: " + superChunkOrigin);
                System.out.println("Super Chunk Lower: " + superChunkLower);
                System.out.println("Chunk Index: " + chunkIndex);
                System.out.println("Chunk Position: " + chunkPosition);
                System.out.println("Chunk Offset: " + chunkOffset);
                e.printStackTrace();
            }
        }
        Map<Point3i, Data> data = new HashMap<Point3i, Data>();
        for (Point3i superChunkIndex : assemblies.keySet()) {
            Map<Point3i, Chunk> assembly = assemblies.get(superChunkIndex);
            Data datum = new Data();
            datum.setChunks(assembly.values().toArray(new Chunk[0]));
            data.put(superChunkIndex, datum);
        }
        return data;
        /*
         long now = System.currentTimeMillis();
         Map<Point3i, Data> data = new HashMap<Point3i, Data>();
         Point3i lowerUniverse = new Point3i();
         Point3i upperUniverse = new Point3i();
         blocks.getBounds(lowerUniverse, upperUniverse);
         Point3i lowerData = getSuperChunkIndexFromPoint(lowerUniverse);
         Point3i upperData = getSuperChunkIndexFromPoint(upperUniverse);
         System.out.println("Universe: "+lowerUniverse+" -- "+upperUniverse);
         System.out.println("Superchunks: "+lowerData+" -- "+upperData);
         for (Iterator<Point3i> i = new CubeIterator(lowerData, upperData); i.hasNext(); )
         {
         Point3i superChunkIndex = i.next();
         Data datum = new Data();
         Point3i superChunkOrigin = getSuperChunkOriginFromIndex(superChunkIndex);
         Point3i lowerSuperChunk = getSuperChunkLowerFromOrigin(superChunkOrigin);
         Point3i upperSuperChunk = getSuperChunkUpperFromOrigin(superChunkOrigin);
         System.out.println("  Index="+superChunkIndex+", origin="+superChunkOrigin+", lower="+lowerSuperChunk+", upper="+upperSuperChunk);
         List<Chunk> chunks = new ArrayList<Chunk>();
         System.out.println("Splitting "+superChunkIndex+" -> "+lowerSuperChunk+" / "+upperSuperChunk);
         for (Iterator<Point3i> j = new CubeIterator(lowerSuperChunk, upperSuperChunk, new Point3i(16, 16, 16)); j.hasNext(); )
         {
         Point3i chunkOrigin = j.next();
         Point3i chunkPos = new Point3i(chunkOrigin);
         //chunkPos.sub(origin);
         Chunk chunk = new Chunk();
         chunk.setPosition(chunkPos);
         chunk.setBlocks(new Block[16][16][16]);
         chunk.setTimestamp(now);
         chunk.setType(1);
         boolean doneAny = false;
         for (Iterator<Point3i> k = new CubeIterator(new Point3i(), new Point3i(15, 15, 15)); k.hasNext(); )
         {
         Point3i chunkOffset = k.next();
         Block b = blocks.get(chunkOrigin.x + chunkOffset.x, chunkOrigin.y + chunkOffset.y, chunkOrigin.z + chunkOffset.z);
         if (b == null)
         continue;
         chunk.getBlocks()[chunkOffset.x][chunkOffset.y][chunkOffset.z] = b;
         doneAny = true;
         }
         if (doneAny)
         chunks.add(chunk);
         }
         if (chunks.size() == 0)
         continue;
         datum.setChunks(chunks.toArray(new Chunk[0]));
         data.put(superChunkIndex,  datum);
         }
         return data;
         */
    }

    public static Point3i findCore(SparseMatrix<Block> grid) {
        Point3i p = new Point3i(8, 8, 8);
        Block b = grid.get(p);
        if ((b != null) && (b.getBlockID() == BlockTypes.CORE_ID)) {
            return p;
        }
        return findFirstBlock(grid, BlockTypes.CORE_ID);
    }

    public static Point3i findFirstBlock(SparseMatrix<Block> grid, short id) {
        List<Point3i> finds = findBlocks(grid, id, true);
        if (finds.isEmpty()) {
            return null;
        } else {
            return finds.get(0);
        }
    }

    public static List<Point3i> findBlocks(SparseMatrix<Block> grid, short id) {
        return findBlocks(grid, id, false);
    }

    public static List<Point3i> findBlocks(SparseMatrix<Block> grid, short id, boolean stopAfterFirst) {
        List<Point3i> finds = new ArrayList<>();
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i pp = i.next();
            Block b = grid.get(pp);
            if (b.getBlockID() == id) {
                finds.add(pp);
                if (stopAfterFirst) {
                    break;
                }
            }
        }
        return finds;
    }

    public static Point3i getSuperChunkOriginFromIndex(Point3i superChunkIndex) {
        Point3i superChunkOrigin = new Point3i();
        if (superChunkIndex.x >= 0) {
            superChunkOrigin.x = superChunkIndex.x * 256;
        } else {
            superChunkOrigin.x = superChunkIndex.x * 256 + 16;
        }
        if (superChunkIndex.y >= 0) {
            superChunkOrigin.y = superChunkIndex.y * 256;
        } else {
            superChunkOrigin.y = superChunkIndex.y * 256 + 16;
        }
        if (superChunkIndex.z >= 0) {
            superChunkOrigin.z = superChunkIndex.z * 256;
        } else {
            superChunkOrigin.z = superChunkIndex.z * 256 + 16;
        }
        return superChunkOrigin;
    }

    public static Point3i getSuperChunkLowerFromOrigin(Point3i superChunkOrigin) {
        Point3i lowerSuperChunk = new Point3i(superChunkOrigin.x - 128, superChunkOrigin.y - 128, superChunkOrigin.z - 128);
        return lowerSuperChunk;
    }

    public static Point3i getSuperChunkUpperFromOrigin(Point3i superChunkOrigin) {
        Point3i upperSuperChunk = new Point3i(superChunkOrigin.x + 127, superChunkOrigin.y + 127, superChunkOrigin.z + 127);
        if (superChunkOrigin.x == -240) {
            upperSuperChunk.x = -129;
        }
        if (superChunkOrigin.y == -240) {
            upperSuperChunk.y = -129;
        }
        if (superChunkOrigin.z == -240) {
            upperSuperChunk.z = -129;
        }
        return upperSuperChunk;
    }

    public static Point3i getSuperChunkIndexFromPoint(Point3i universePoint) {
        Point3i superChunkIndex = new Point3i(
                getIndexFromAxis(universePoint.x),
                getIndexFromAxis(universePoint.y),
                getIndexFromAxis(universePoint.z)
        );
        return superChunkIndex;
    }

    private static int getIndexFromAxis(int axis) {
        if (axis >= -128) {
            return (axis + 128) / 256;
        }
        if (axis < -368) {
            return -(143 - axis) / 256;
        }
        return -1;
    }

    public static Point3i getChunkPositionFromSuperchunkOriginAndChunkIndex(
            Point3i superChunkOrigin, Point3i chunkIndex) {
        Point3i chunkPosition = new Point3i();
        if (superChunkOrigin.x >= 0) {
            chunkPosition.x = superChunkOrigin.x + (chunkIndex.x - 8) * 16;
        } else {
            chunkPosition.x = superChunkOrigin.x + (7 - chunkIndex.x) * 16;
        }
        if (superChunkOrigin.y >= 0) {
            chunkPosition.y = superChunkOrigin.y + (chunkIndex.y - 8) * 16;
        } else {
            chunkPosition.y = superChunkOrigin.y + (7 - chunkIndex.y) * 16;
        }
        if (superChunkOrigin.z >= 0) {
            chunkPosition.z = superChunkOrigin.z + (chunkIndex.z - 8) * 16;
        } else {
            chunkPosition.z = superChunkOrigin.z + (7 - chunkIndex.z) * 16;
        }
        return chunkPosition;
    }

    private static Point3i getChunkPositionFromSuperchunkLowerAndChunkIndex(
            Point3i superChunkLower, Point3i chunkIndex) {
        Point3i chunkPosition = new Point3i(chunkIndex);
        chunkPosition.scale(16);
        chunkPosition.add(superChunkLower);
        return chunkPosition;
    }

    public static Point3i getChunkIndexFromSuperchunkOriginAndChunkPosition(
            Point3i superChunkOrigin, Point3i chunkPosition) {
        Point3i chunkIndex = new Point3i();
        if (superChunkOrigin.x >= 0) {
            chunkIndex.x = (chunkPosition.x - superChunkOrigin.x) / 16 + 8;
        } else {
            chunkIndex.x = (superChunkOrigin.x - chunkPosition.x) / 16 + 7;
        }
        if (superChunkOrigin.y >= 0) {
            chunkIndex.y = (chunkPosition.y - superChunkOrigin.y) / 16 + 8;
        } else {
            chunkIndex.y = (superChunkOrigin.y - chunkPosition.y) / 16 + 7;
        }
        if (superChunkOrigin.z >= 0) {
            chunkIndex.z = (chunkPosition.z - superChunkOrigin.z) / 16 + 8;
        } else {
            chunkIndex.z = (superChunkOrigin.z - chunkPosition.z) / 16 + 7;
        }
        return chunkIndex;
    }

    public static void ensureCore(SparseMatrix<Block> grid) {
        Point3i core = findCore(grid);
        if ((core != null) && (core.x == 8) && (core.y == 8) && (core.z == 8)) {
            return;
        }
        if (core != null) {
            grid.set(core, null);
        }
        List<Point3i> cores = findBlocks(grid, BlockTypes.CORE_ID);
        for (Point3i p : cores) {
            grid.set(p, null);
        }
        grid.set(8, 8, 8, new Block(BlockTypes.CORE_ID));
    }
}
