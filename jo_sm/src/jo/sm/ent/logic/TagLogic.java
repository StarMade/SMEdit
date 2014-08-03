
package jo.sm.ent.logic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import jo.sm.ent.data.ControlElement;
import jo.sm.ent.data.ControlElementMap;
import jo.sm.ent.data.ControlSubElement;
import jo.sm.ent.data.Tag;
import jo.sm.ent.data.TagType;
import jo.sm.logic.utils.ByteUtils;
import jo.sm.logic.utils.DebugLogic;
import jo.vecmath.Point3b;
import jo.vecmath.Point3i;
import jo.vecmath.Tuple3b;
import jo.vecmath.Tuple3f;
import jo.vecmath.Tuple3i;
import jo.vecmath.Vector3f;


public class TagLogic {
    private static final Logger log = Logger.getLogger(TagLogic.class.getName());

    public static Tag readFile(InputStream is, boolean closeStream)
            throws IOException {
        DebugLogic.setIndent("");
        DebugLogic.debug("Reading file");
        DebugLogic.indent();
        if (!(is instanceof PushbackInputStream)) {
            is = new PushbackInputStream(is, 2);
        }
        byte header[] = new byte[2];
        is.read(header);
        ((PushbackInputStream) is).unread(header);
        DataInputStream dis;
        if (header[0] == 31 && header[1] == -117) {
            DebugLogic.debug("Zipped input");
            dis = new DataInputStream(new GZIPInputStream(is, 4096));
        } else {
            dis = new DataInputStream(new BufferedInputStream(is, 4096));
            dis.readShort();
        }
        byte t = dis.readByte();
        TagType type = TagType.values()[t];
        Tag input = null;
        if (type == TagType.FINISH) {
            input = new Tag(TagType.FINISH, null, (null));
        } else {
            String name = dis.readUTF();
            DebugLogic.debug("Reading " + name);
            DebugLogic.indent();
            try {
                Object val = readValue(dis, type);
                input = new Tag(type, name, val);
            } catch (IOException e) {
                log.log(Level.WARNING, "EXCEPTION WHILE READING TAG " + name);
                throw e;
            }
            DebugLogic.outdent();
        }
        if (closeStream) {
            DebugLogic.debug("Closing");
            is.close();
        }
        DebugLogic.outdent();
        DebugLogic.debug("Done reading file");
        return input;
    }

    public static void writeFile(Tag tag, OutputStream os, boolean closeFile)
            throws IOException {
        DataOutputStream dos;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream) os;
        } else {
            dos = new DataOutputStream(os);
        }
        dos.writeShort(0);
        dos.writeByte(tag.getType().ordinal());
        if (tag.getType() != TagType.FINISH) {
            dos.writeUTF(tag.getName());
            writeValue(tag, dos);
        }
        if (closeFile) {
            dos.close();
        }
    }

    private static Object readValue(DataInputStream dis, TagType type)
            throws IOException {
        DebugLogic.indent();
        Object value = null;
        switch (type) {
            case FINISH:
                value = null;
                break;
            case BYTE:
                value = dis.readByte();
                break;
            case SHORT:
                value = dis.readShort();
                break;
            case INT:
                value = dis.readInt();
                break;
            case LONG:
                value = dis.readLong();
                break;
            case FLOAT:
                value = dis.readFloat();
                break;
            case DOUBLE:
                value = dis.readDouble();
                break;
            case BYTE_ARRAY: {
                byte[] buf = new byte[dis.readInt()];
                dis.readFully(buf);
                value = buf;
                break;
            }
            case STRING:
                value = dis.readUTF();
                break;
            case VECTOR3f:
                value = new Vector3f(dis.readFloat(),
                        dis.readFloat(),
                        dis.readFloat());
                break;
            case VECTOR3i:
                value = new Point3i(dis.readInt(),
                        dis.readInt(), dis.readInt());
                break;
            case VECTOR3b:
                value = new Point3b((byte) dis.read(),
                        (byte) dis.read(), (byte) dis.read());
                break;
            case LIST: {
                DebugLogic.debug("Reading list");
                byte st = dis.readByte();
                TagType subtype = TagType.values()[st];
                int len = dis.readInt();
                Tag tagbuf[] = new Tag[len];
                for (int j = 0; j < len; j++) {
                    tagbuf[j] = new Tag(subtype, null, readValue(dis, subtype));
                }
                if (tagbuf.length == 0) {
                    value = subtype;
                } else {
                    value = tagbuf;
                }
                break;
            }
            case STRUCT: {
                DebugLogic.debug("Reading struct");
                List<Tag> inbuf = new ArrayList<>();
                TagType nextType;
                do {
                    byte nt = dis.readByte();
                    if ((nt & 0xff) == 0xf3) // HACK
                    {
                        dis.skip(23);
                        nt = dis.readByte();
                    } else if ((nt & 0xff) == 0xff) // HACK
                    {
                        dis.skip(2);
                        nt = dis.readByte();
                    }
                    try {
                        nextType = TagType.values()[nt];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        byte[] buf = new byte[128];
                        buf[0] = nt;
                        dis.read(buf, 1, 127);
                        log.log(Level.WARNING, "Bad tag data:\n" + ByteUtils.toStringDump(buf));
                        throw new IllegalStateException("Unknown tag type '" + nt + "'");
                    }
                    String name = null;
                    String msg = "Reading member #" + (inbuf.size() + 1);
                    if (nextType != TagType.FINISH) {
                        name = dis.readUTF();
                        msg += " " + name;
                    }
                    msg += " (" + nextType + ")";
                    DebugLogic.debug(msg);
                    Object val = readValue(dis, nextType);
                    Tag t = new Tag(nextType, name, val);
                    inbuf.add(t);
                } while (nextType != TagType.FINISH);
                value = inbuf.toArray(new Tag[inbuf.size()]);
                break;
            }
            case SERIALIZABLE: {
                DebugLogic.debug("Reading Serializable");
                ControlElementMap map = new ControlElementMap();
                map.setFactory(dis.readByte());
                int controlElementMapperSize = dis.readInt();
                for (int i = 0; i < controlElementMapperSize; i++) {
                    ControlElement ele = new ControlElement();
                    short index1 = dis.readShort();
                    short index2 = dis.readShort();
                    short index3 = dis.readShort();
                    ele.setIndex(shortToIndex(index1, index2, index3));
                    int size2 = dis.readInt();
                    for (int j = 0; j < size2; j++) {
                        ControlSubElement sub = new ControlSubElement();
                        sub.setVal(dis.readShort());
                        int size3 = dis.readInt();
                        for (int k = 0; k < size3; k++) {
                            Point3i p = new Point3i();
                            p.x = dis.readShort();
                            p.y = dis.readShort();
                            p.z = dis.readShort();
                            sub.getVals().add(p);
                        }
                        ele.getElements().add(sub);
                    }
                    map.getElements().add(ele);
                }
                value = map;
                break;
            }
        }
        DebugLogic.outdent();
        return value;
    }

    private static short[] indexToShort(long index) {
        long l1 = index / 4294705156L;
        long l2 = (index -= l1 * 4294705156L) / 65534L;
        index -= l2 * 65534L;
        short[] ret = new short[3];
        ret[0] = (short) (int) (index - 32767L);
        ret[1] = (short) (int) (l2 - 32767L);
        ret[2] = (short) (int) (l1 - 32767L);
        return ret;
    }

    public static long shortToIndex(int paramInt1, int paramInt2, int paramInt3) {
        long l1 = paramInt1 + 32767;
        long l2 = paramInt2 + 32767;
        long l3;
        if ((l3 = (paramInt3 + 32767) * 4294705156L + l2 * 65534L + l1) < 0L) {
            throw new IllegalArgumentException("ElementCollection Index out of bounds: " + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + " -> " + l3);
        }
        return l3;
    }

    private static void writeValue(Tag tag, DataOutputStream dos)
            throws IOException {
        switch (tag.getType()) {
            case FINISH:
                return;
            case BYTE:
                dos.writeByte(((int) tag.getValue()));
                return;
            case SHORT:
                dos.writeShort(((int) tag.getValue()));
                return;
            case INT:
                dos.writeInt(((Integer) tag.getValue()));
                return;
            case LONG:
                dos.writeLong(((Long) tag.getValue()));
                return;
            case FLOAT:
                dos.writeFloat(((Float) tag.getValue()));
                return;
            case DOUBLE:
                dos.writeDouble(((Double) tag.getValue()));
                return;
            case BYTE_ARRAY:
                byte outbuf[] = (byte[]) tag.getValue();
                dos.writeInt(outbuf.length);
                dos.write(outbuf);
                return;
            case STRING:
                dos.writeUTF((String) tag.getValue());
                return;
            case VECTOR3f:
                dos.writeFloat(((Tuple3f) tag.getValue()).x);
                dos.writeFloat(((Tuple3f) tag.getValue()).y);
                dos.writeFloat(((Tuple3f) tag.getValue()).z);
                return;
            case VECTOR3i:
                dos.writeInt(((Tuple3i) tag.getValue()).x);
                dos.writeInt(((Tuple3i) tag.getValue()).y);
                dos.writeInt(((Tuple3i) tag.getValue()).z);
                return;
            case VECTOR3b:
                dos.write(((Tuple3b) tag.getValue()).x);
                dos.write(((Tuple3b) tag.getValue()).y);
                dos.write(((Tuple3b) tag.getValue()).z);
                return;
            case LIST: {
                Tag[] val = (Tag[]) tag.getValue();
                if (val.length > 0) {
                    dos.writeByte(val[0].getType().ordinal());
                } else {
                    dos.writeByte(tag.getSubType().ordinal());
                }
                dos.writeInt(val.length);
            for (Tag v : val) {
                writeValue(v, dos);
            }
                /*
                 * dataoutputstream.writeByte(((Ab)a_java_lang_Object_fld).
                 * getFactoryId());
                 * ((Ab)a_java_lang_Object_fld).writeToTag(dataoutputstream);
                 */
                return;
            }
            case STRUCT: {
                Tag[] tagbuf = (Tag[]) tag.getValue();
                for (Tag t : tagbuf) {
                    dos.writeByte(t.getType().ordinal());
                    if (t.getType() != TagType.FINISH) {
                        dos.writeUTF(t.getName());
                    }
                    writeValue(t, dos);
                }
                /*
                 * Ad aad[] = (Ad[])a_java_lang_Object_fld;
                 * dataoutputstream.writeByte(b.ordinal());
                 * dataoutputstream.writeInt(aad.length); int i = aad.length;
                 * for(int j = 0; j < i; j++)
                 * aad[j].writeValue(dataoutputstream);
                 */
                return;
            }
            case SERIALIZABLE: {
                ControlElementMap map = (ControlElementMap) tag.getValue();
                dos.writeByte(map.getFactory());
                dos.writeInt(map.getElements().size());
                for (ControlElement ele : map.getElements()) {
                    short[] index = indexToShort(ele.getIndex());
                    dos.writeShort(index[0]);
                    dos.writeShort(index[1]);
                    dos.writeShort(index[2]);
                    dos.writeInt(ele.getElements().size());
                    for (ControlSubElement sub : ele.getElements()) {
                        dos.writeShort(sub.getVal());
                        dos.writeInt(sub.getVals().size());
                        for (Point3i p : sub.getVals()) {
                            dos.writeShort(p.x);
                            dos.writeShort(p.y);
                            dos.writeShort(p.z);
                        }
                    }
                }
            }
        }
    }

    public static void setValue(Tag tag, Object value) {
        switch (tag.getType()) {
            case FINISH:
                if (value != null) {
                    throw new IllegalArgumentException();
                }
                break;
            case BYTE:
                if (!(value instanceof Byte)) {
                    throw new IllegalArgumentException();
                }
                break;
            case SHORT:
                if (!(value instanceof Short)) {
                    throw new IllegalArgumentException();
                }
                break;
            case INT:
                if (!(value instanceof Integer)) {
                    throw new IllegalArgumentException();
                }
                break;
            case LONG:
                if (!(value instanceof Long)) {
                    throw new IllegalArgumentException();
                }
                break;
            case FLOAT:
                if (!(value instanceof Float)) {
                    throw new IllegalArgumentException();
                }
                break;
            case DOUBLE:
                if (!(value instanceof Double)) {
                    throw new IllegalArgumentException();
                }
                break;
            case BYTE_ARRAY:
                if (!(value instanceof byte[])) {
                    throw new IllegalArgumentException();
                }
                break;
            case STRING:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException();
                }
                break;
            case VECTOR3f:
                if (!(value instanceof Vector3f)) {
                    throw new IllegalArgumentException();
                }
                break;
            case VECTOR3i:
                if (!(value instanceof Point3i)) {
                    throw new IllegalArgumentException();
                }
                break;
            case VECTOR3b:
                if (!(value instanceof Point3b)) {
                    throw new IllegalArgumentException();
                }
                break;
            case LIST:
                if (value instanceof TagType) {
                    tag.setSubType((TagType) value);
                    value = new Tag[0];
                    break;
                }
                if (!(value instanceof Tag[])) {
                    throw new IllegalArgumentException();
                }
                tag.setSubType(((Tag[]) value)[0].getType());
                break;
            case STRUCT:
                if (!(value instanceof Tag[])) {
                    throw new IllegalArgumentException();
                }
                break;
            case SERIALIZABLE:
                if (!(value instanceof ControlElementMap)) {
                    throw new IllegalArgumentException();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        tag.setValue(value);
    }
}
