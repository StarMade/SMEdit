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
package jo.sm.plugins.ship.imp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class BinvoxLogic {
    private static final Logger log = Logger.getLogger(BinvoxLogic.class.getName());

    public static BinvoxData read(InputStream is) throws IOException {
        BinvoxData data = new BinvoxData();
        DataInputStream dis = new DataInputStream(is);

        //
        // read header
        //
        String line = readLine(dis); // deprecated function though
        if (!line.startsWith("#binvox")) {
            log.log(Level.WARNING, "Error: first line reads [" + line + "] instead of [#binvox]");
            return null;
        }

        String version_string = line.substring(8);
        int version = Integer.parseInt(version_string);
        log.log(Level.INFO, "reading binvox version " + version);

        data.setZSpan(0);
        data.setXSPan(0);
        data.setYSpan(0);
        boolean done = false;

        while (!done) {

            line = readLine(dis);

            if (line.startsWith("data")) {
                done = true;
            } else {
                if (line.startsWith("dim")) {
                    String[] dimensions = line.split(" ");
                    data.setZSpan(Integer.parseInt(dimensions[1]));
                    data.setXSPan(Integer.parseInt(dimensions[2]));
                    data.setYSpan(Integer.parseInt(dimensions[3]));
                } else {
                    if (line.startsWith("translate")) {
                        String[] dimensions = line.split(" ");
                        data.setTX(Double.parseDouble(dimensions[1]));
                        data.setTY(Double.parseDouble(dimensions[2]));
                        data.setTZ(Double.parseDouble(dimensions[3]));
                    } else {
                        if (line.startsWith("scale")) {
                            String[] dimensions = line.split(" ");
                            data.setScale(Double.parseDouble(dimensions[1]));
                        } else {
                            log.log(Level.WARNING, "  unrecognized keyword ["
                                    + line + "], skipping");
                        }
                    }
                }
            }
        }

        if (!done) {
            log.log(Level.WARNING, "  error reading header");
            return null;
        }
        if (data.getZSpan() == 0) {
            log.log(Level.WARNING, "  missing dimensions in header");
            return null;
        }

        data.setSize(data.getYSpan() * data.getXSpan() * data.getZSpan());
        data.setVoxels(new boolean[data.getZSpan()][data.getYSpan()][data.getXSpan()]);

        //
        // read voxel data
        //
        byte value;
        int count;
        int index = 0;
        int end_index = 0;
        int nr_voxels = 0;

        // *input >> value; // read the linefeed char
        while (end_index < data.getSize()) {

            value = dis.readByte();
            // idiotic Java language doesn't have unsigned types, so we have to
            // use an int for 'count'
            // and make sure that we don't interpret it as a negative number if
            // bit 7 (the sign bit) is on
            count = dis.readByte() & 0xff;

            end_index = index + count;
            if (end_index > data.getSize()) {
                return null;
            }
            for (int i = index; i < end_index; i++) {
                if (value != 0) {
                    Point3i p = getCoords(i, data);
                    data.getVoxels()[p.z][p.y][p.x] = true;
                }
            }

            if (value > 0) {
                nr_voxels += count;
            }
            index = end_index;
            log.log(Level.INFO, index + "/" + data.getSize() + "   " + Runtime.getRuntime().freeMemory());
        } // while
        log.log(Level.INFO, "  read " + nr_voxels + " voxels");
        return data;

    } // read_binvox

    private static String readLine(InputStream dis) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (;;) {
            int ch = dis.read();
            if (ch == -1) {
                break;
            }
            if (ch == '\r') {
                continue;
            }
            if (ch == '\n') {
                break;
            }
            sb.append((char) ch);
        }
        return sb.toString();
    }

    public static BinvoxData readHeader(InputStream is) throws IOException {
        BinvoxData data = new BinvoxData();
        data.setInput(new DataInputStream(is));

        //
        // read header
        //
        String line = readLine(data.getInput()); // deprecated function though
        if (!line.startsWith("#binvox")) {
            log.log(Level.WARNING, "Error: first line reads [" + line
                    + "] instead of [#binvox]");
            return null;
        }

        String version_string = line.substring(8);
        int version = Integer.parseInt(version_string);
        log.log(Level.INFO, "reading binvox version " + version);

        data.setZSpan(0);
        data.setXSPan(0);
        data.setYSpan(0);
        boolean done = false;

        while (!done) {

            line = readLine(data.getInput());

            if (line.startsWith("data")) {
                done = true;
            } else {
                if (line.startsWith("dim")) {
                    String[] dimensions = line.split(" ");
                    data.setZSpan(Integer.parseInt(dimensions[1]));
                    data.setXSPan(Integer.parseInt(dimensions[2]));
                    data.setYSpan(Integer.parseInt(dimensions[3]));
                } else {
                    if (line.startsWith("translate")) {
                        String[] dimensions = line.split(" ");
                        data.setTX(Double.parseDouble(dimensions[1]));
                        data.setTY(Double.parseDouble(dimensions[2]));
                        data.setTZ(Double.parseDouble(dimensions[3]));
                    } else {
                        if (line.startsWith("scale")) {
                            String[] dimensions = line.split(" ");
                            data.setScale(Double.parseDouble(dimensions[1]));
                        } else {
                            log.log(Level.WARNING, "  unrecognized keyword ["
                                    + line + "], skipping");
                        }
                    }
                }
            }
        }

        if (!done) {
            log.log(Level.WARNING, "  error reading header");
            return null;
        }
        if (data.getZSpan() == 0) {
            log.log(Level.WARNING, "  missing dimensions in header");
            return null;
        }

        data.setSize(data.getYSpan() * data.getXSpan() * data.getZSpan());
        data.setVoxels(new boolean[data.getZSpan()][][]);
        data.setDone(false);
        data.setEndIndex(0);
        return data;
    }

    public static boolean readNextSlice(BinvoxData data) throws IOException {
        if (data.isDone()) {
            return true;
        }
        int index = data.getEndIndex();
        Point3i startPos = getCoords(index, data);
        while (data.getEndIndex() < data.getSize()) {
            byte value = data.getInput().readByte();
            // idiotic Java language doesn't have unsigned types, so we have to
            // use an int for 'count'
            // and make sure that we don't interpret it as a negative number if
            // bit 7 (the sign bit) is on
            int count = data.getInput().readByte() & 0xff;
            data.setEndIndex(index + count);
            if (data.getEndIndex() > data.getSize()) {
                data.getInput().close();
                return true;
            }
            for (int i = index; i < data.getEndIndex(); i++) {
                Point3i p = getCoords(i, data);
                setVoxel(data, p.x, p.y, p.z, value != 0);
            }

            index = data.getEndIndex();
            Point3i endPos = getCoords(index, data);
            log.log(Level.INFO, index+"/"+data.getSize()+"   "+Runtime.getRuntime().freeMemory());
            if (endPos.x > startPos.x) {
                break;
            }
        } // while
        return false;
    }

    public static void getBounds(BinvoxData hull, Point3i lower, Point3i upper) {
        boolean first = true;
        for (int z = 0; z < hull.getZSpan(); z++) {
            for (int x = 0; x < hull.getXSpan(); x++) {
                for (int y = 0; y < hull.getYSpan(); y++) {
                    if (hull.getVoxels()[z][y][x] == false) {
                        continue;
                    }
                    if (first) {
                        lower.x = x;
                        lower.y = y;
                        lower.z = z;
                        upper.x = x;
                        upper.y = y;
                        upper.z = z;
                        first = false;
                    } else {
                        lower.x = Math.min(lower.x, x);
                        lower.y = Math.min(lower.y, y);
                        lower.z = Math.min(lower.z, z);
                        upper.x = Math.max(upper.x, x);
                        upper.y = Math.max(upper.y, y);
                        upper.z = Math.max(upper.z, z);
                    }
                }
            }
        }
    }

    public static void setVoxel(BinvoxData hull, int x, int y, int z, boolean value) {
        if (hull.getVoxels() == null) {
            hull.setVoxels(new boolean[hull.getXSpan()][][]);
        }
        if (hull.getVoxels()[x] == null) {
            hull.getVoxels()[x] = new boolean[hull.getZSpan()][];
        }
        if (hull.getVoxels()[x][z] == null) {
            hull.getVoxels()[x][z] = new boolean[hull.getYSpan()];
        }
        hull.getVoxels()[x][z][y] = value;
    }

    public static boolean getVoxel(BinvoxData hull, int x, int y, int z) throws IOException {
        if ((x < 0) || (y < 0) || (z < 0)) {
            return false;
        }
        if ((x >= hull.getXSpan()) || (y >= hull.getYSpan()) || (z >= hull.getZSpan())) {
            return false;
        }
        while (hull.getVoxels() == null) {
            if (hull.isDone()) {
                return false;
            }
            readNextSlice(hull);
        }
        while (hull.getVoxels()[x] == null) {
            if (hull.isDone()) {
                return false;
            }
            readNextSlice(hull);
        }
        while (hull.getVoxels()[x][z] == null) {
            if (hull.isDone()) {
                return false;
            }
            readNextSlice(hull);
        }
        return hull.getVoxels()[x][z][y];
    }

    public static int getIndex(int x, int y, int z, BinvoxData hull) {
        int index = x * hull.getYSpan() * hull.getXSpan() + z * hull.getYSpan() + y;
        return index;
    }

    public static Point3i getCoords(int o, BinvoxData hull) {
        Point3i p = new Point3i();
        p.x = o / (hull.getYSpan() * hull.getXSpan());
        p.y = o % hull.getYSpan();
        p.z = (o / hull.getYSpan()) % hull.getXSpan();
        return p;
    }
}
