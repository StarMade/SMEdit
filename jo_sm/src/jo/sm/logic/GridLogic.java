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
package jo.sm.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.utils.StreamUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLEditUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class GridLogic {
    private static final Logger log = Logger.getLogger(GridLogic.class.getName());

    public static SparseMatrix<Block> extract(SparseMatrix<Block> grid, Point3i lower, Point3i upper) {
        SparseMatrix<Block> subset = new SparseMatrix<>();
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            Point3i p = i.next();
            subset.set(p, grid.get(p));
        }
        return subset;
    }

    public static void insert(SparseMatrix<Block> grid, SparseMatrix<Block> insertion, Point3i lowerInsertionPoint) {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        insertion.getBounds(lower, upper);
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            Point3i insertionPoint = i.next();
            Block b = insertion.get(insertionPoint);
            if (b == null) {
                continue;
            }
            Point3i gridPoint = new Point3i(insertionPoint);
            gridPoint.sub(lower);
            gridPoint.add(lowerInsertionPoint);
            grid.set(gridPoint, b);
        }
    }

    public static String toString(SparseMatrix<Block> grid) {
        Document doc = toDocument(grid);
        return XMLUtils.writeString(doc.getFirstChild());
    }

    public static Document toDocument(SparseMatrix<Block> grid) {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        grid.getBounds(lower, upper);
        Document doc = XMLUtils.newDocument();
        Node root = XMLEditUtils.addElement(doc, "blocks");
        XMLEditUtils.addAttribute(root, "lower", lower.x + "," + lower.y + "," + lower.z);
        XMLEditUtils.addAttribute(root, "upper", upper.x + "," + upper.y + "," + upper.z);
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext();) {
            Point3i p = i.next();
            Block b = grid.get(p);
            Node block = XMLEditUtils.addElement(root, "block");
            XMLEditUtils.addAttribute(block, "location", p.x + "," + p.y + "," + p.z);
            XMLEditUtils.addAttribute(block, "type", String.valueOf(b.getBlockID()));
            if (b.getOrientation() != 0) {
                XMLEditUtils.addAttribute(block, "orientation", String.valueOf(b.getOrientation()));
            }
        }
        return doc;
    }

    public static SparseMatrix<Block> fromString(String xml) {
        SparseMatrix<Block> grid = new SparseMatrix<>();
        Document doc = XMLUtils.readString(xml);
        for (Node block : XMLUtils.findNodes(doc, "blocks/block")) {
            String[] xyz = XMLUtils.getAttribute(block, "location").split(",");
            Point3i p = new Point3i(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
            short id = Short.parseShort(XMLUtils.getAttribute(block, "type"));
            Block b = new Block(id);
            String ori = XMLUtils.getAttribute(block, "orientation");
            if (!StringUtils.isTrivial(ori)) {
                b.setOrientation(Short.parseShort(ori));
            }
            grid.set(p, b);
        }
        return grid;
    }

    public static byte[] toBytes(SparseMatrix<Block> grid) {
        try {
            ByteArrayOutputStream baos;
            GZIPOutputStream os;
            try (ByteArrayInputStream is = new ByteArrayInputStream(toString(grid).getBytes())) {
                baos = new ByteArrayOutputStream();
                os = new GZIPOutputStream(baos);
                StreamUtils.copy(is, os);
            }
            os.close();
            return baos.toByteArray();
        } catch (IOException e) {
            log.log(Level.WARNING, "ByteArrayOutputStream failed!", e);
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static SparseMatrix<Block> fromBytes(byte[] bytes) {
        try {
            ByteArrayOutputStream os;
            try (GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
                os = new ByteArrayOutputStream();
                StreamUtils.copy(is, os);
            }
            os.close();
            return fromString(new String(os.toByteArray()));
        } catch (IOException e) {
            log.log(Level.WARNING, "ByteArrayOutputStream failed!", e);
            e.printStackTrace();
            return null;
        }
    }

    public static void delete(SparseMatrix<Block> grid, Point3i lower, Point3i upper) {
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            Point3i p = i.next();
            grid.set(p, null);
        }
    }

}
