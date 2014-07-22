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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.ent.data.Tag;
import jo.sm.ent.logic.TagLogic;
import jo.sm.ent.logic.TagUtils;
import jo.sm.logic.IOLogic;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.DockEntry;
import jo.sm.ship.data.Meta;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MetaLogic {
    private static final Logger log = Logger.getLogger(MetaLogic.class.getName());

    public static Meta readFile(InputStream is, boolean close) throws IOException {
        DataInputStream dis;
        if (is instanceof DataInputStream) {
            dis = (DataInputStream) is;
        } else {
            dis = new DataInputStream(is);
        }
        Meta meta = new Meta();
        meta.setUnknown1(dis.readInt());
        meta.setUnknown2(dis.readByte());
        if (meta.getUnknown2() == 1) {
            return meta;
        }
        if (meta.getUnknown2() != 3) {
            throw new IllegalArgumentException("Unrecognized unknown2: " + meta.getUnknown2());
        }
        int docksNum = dis.readInt();
        for (int i = 0; i < docksNum; i++) {
            DockEntry entry = new DockEntry();
            entry.setSubFolder(dis.readUTF());
//            int clen = dis.readInt();
//            byte[] cdata = new byte[clen];
//            dis.readFully(cdata);
//            entry.setSubFolder(new String(cdata, "utf-8"));
            entry.setPosition(IOLogic.readPoint3i(dis));
            entry.setA(IOLogic.readPoint3f(dis));
            entry.setBlockID(dis.readShort());
            entry.setUnknown1(dis.readByte());
            meta.getDocks().add(entry);
        }
        meta.setUnknown3(dis.readByte());
        Tag tags = TagLogic.readFile(dis, close);
        meta.setData(tags);
        return meta;
    }

    public static void writeFile(Meta meta, OutputStream os, boolean close) throws IOException {
        DataOutputStream dos;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream) os;
        } else {
            dos = new DataOutputStream(os);
        }
        dos.writeInt(meta.getUnknown1());
        dos.writeByte(meta.getUnknown2());
        if (meta.getUnknown2() == 3) {
            dos.writeInt(meta.getDocks().size());
            for (DockEntry entry : meta.getDocks()) {
                dos.writeUTF(entry.getSubFolder());
                IOLogic.write(dos, entry.getPosition());
                IOLogic.write(dos, entry.getA());
                dos.writeShort(entry.getBlockID());
                dos.writeByte(entry.getUnknown1());
            }
            dos.writeByte(meta.getUnknown3());
            TagLogic.writeFile(meta.getData(), dos, false);
        } else if (meta.getUnknown2() == 1) {
            ;//unknown
        } else {
            throw new IllegalArgumentException("Unrecognized unknown2: " + meta.getUnknown2());
        }
        if (close) {
            dos.close();
        }
    }

    public static void dump(Meta meta) {
        log.log(Level.INFO, "Meta (u1=" + meta.getUnknown1() + ", u2=" + meta.getUnknown2() + ", u3=" + meta.getUnknown3() + ")");
        //System.out.println("Meta (u1=" + meta.getUnknown1() + ", u2=" + meta.getUnknown2() + ", u3=" + meta.getUnknown3() + ")");
        for (DockEntry entry : meta.getDocks()) {
            log.log(Level.INFO, "    " + entry.getSubFolder() + " @" + entry.getPosition() + "/" + entry.getA()
                    + " " + BlockTypes.BLOCK_NAMES.get(entry.getBlockID()) + " (" + entry.getUnknown1() + ")");
            //System.out.println("    " + entry.getSubFolder() + " @" + entry.getPosition() + "/" + entry.getA()
                    //+ " " + BlockTypes.BLOCK_NAMES.get(entry.getBlockID()) + " (" + entry.getUnknown1() + ")");
        }
        TagUtils.dump(meta.getData(), "  ");
    }

    public static Meta make(SparseMatrix<Block> grid) {
        Meta meta = new Meta();
        meta.setUnknown1(0);
        meta.setUnknown2((byte) 1);
        return meta;
    }
}
