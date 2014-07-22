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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.ShapeLibraryEntry;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.utils.FileUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLEditUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.util.Paths;
import jo.vecmath.Point3i;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class ShapeLibraryLogic {

    private static long mLastRead = 0;
    private static final List<ShapeLibraryEntry> mEntries = new ArrayList<>();
    private static final Logger log = Logger.getLogger(ShapeLibraryLogic.class.getName());

    public static void update() {
        File plugins = new File(Paths.getPluginsDirectory());
        File shapeLibDir = new File(plugins, "shapeLibrary");
        if (!shapeLibDir.exists()) {
            mLastRead = 0;
            mEntries.clear();
            return;
        }
        if (shapeLibDir.lastModified() <= mLastRead) {
            return; // up to date
        }        // check files
        for (File shapeFile : shapeLibDir.listFiles()) {
            ShapeLibraryEntry entry = findByFile(shapeFile);
            if (entry != null) {
                if (entry.getLastRead() >= shapeFile.lastModified()) {
                    continue;
                } else {
                    mEntries.remove(entry);
                }
            }
            entry = new ShapeLibraryEntry();
            entry.setShape(shapeFile);
            try {
                updateEntry(entry);
            } catch (IOException e) {
                log.log(Level.WARNING, "ShapeLibrary failed!", e);
                e.printStackTrace();
            }
            mEntries.add(entry);
        }
    }

    private static void updateEntry(ShapeLibraryEntry entry) throws IOException {
        String xml = FileUtils.readFileAsString(entry.getShape().toString(), 1024);
        String name = getAttribute(xml, "name");
        if (StringUtils.isTrivial(name)) {
            name = entry.getShape().getName();
            if (name.endsWith(".xml")) {
                name = name.substring(0, name.length() - 4);
            }
        }
        entry.setName(name);
        String author = getAttribute(xml, "author");
        if (StringUtils.isTrivial(author)) {
            author = "A. N. Onomous";
        }
        entry.setAuthor(author);
        String lower = getAttribute(xml, "lower");
        if (!StringUtils.isTrivial(lower)) {
            entry.setLower(new Point3i(lower));
        } else {
            entry.setLower(new Point3i());
        }
        String upper = getAttribute(xml, "upper");
        if (!StringUtils.isTrivial(upper)) {
            entry.setUpper(new Point3i(upper));
        } else {
            entry.setUpper(new Point3i());
        }
        String classes = getAttribute(xml, "classifications");
        if (!StringUtils.isTrivial(classes)) {
            classes = classes.toLowerCase();
            if (classes.contains("ship")) {
                entry.getClassifications().add(IBlocksPlugin.TYPE_SHIP);
            }
            if (classes.contains("shop")) {
                entry.getClassifications().add(IBlocksPlugin.TYPE_SHOP);
            }
            if (classes.contains("station")) {
                entry.getClassifications().add(IBlocksPlugin.TYPE_STATION);
            }
            if (classes.contains("planet")) {
                entry.getClassifications().add(IBlocksPlugin.TYPE_PLANET);
            }
            if (classes.contains("floatingrock")) {
                entry.getClassifications().add(IBlocksPlugin.TYPE_FLOATINGROCK);
            }
        }
        if (entry.getClassifications().isEmpty()) {
            entry.getClassifications().add(IBlocksPlugin.TYPE_ALL);
        }
        entry.setLastRead(entry.getShape().lastModified());
    }

    private static String getAttribute(String xml, String name) {
        int o = xml.indexOf(name);
        if (o < 0) {
            return null;
        }
        xml = xml.substring(o + name.length()).trim();
        if (!xml.startsWith("=")) {
            return null;
        }
        xml = xml.substring(1).trim();
        if (xml.startsWith("\"")) {
            xml = xml.substring(1);
            o = xml.indexOf("\"");
            if (o < 0) {
                return null;
            }
            return xml.substring(0, o);
        }
        if (xml.startsWith("\'")) {
            xml = xml.substring(1);
            o = xml.indexOf("\'");
            if (o < 0) {
                return null;
            }
            return xml.substring(0, o);
        }
        return null;
    }

    public static ShapeLibraryEntry findByFile(File shapeFile) {
        for (ShapeLibraryEntry entry : mEntries) {
            if (shapeFile.equals(entry)) {
                return entry;
            } else {
            }
        }
        return null;
    }

    public static List<ShapeLibraryEntry> getEntries(int type) {
        update();
        List<ShapeLibraryEntry> entries = new ArrayList<>();
        for (ShapeLibraryEntry entry : mEntries) {
            if (isType(entry, type)) {
                entries.add(entry);
            }
        }
        return entries;
    }

    public static ShapeLibraryEntry getEntry(int unid) {
        for (ShapeLibraryEntry entry : mEntries) {
            if (entry.getUNID() == unid) {
                return entry;
            }
        }
        return null;
    }

    public static void addEntry(SparseMatrix<Block> grid, String name, String author, int type) {
        Document doc = GridLogic.toDocument(grid);
        Node root = doc.getFirstChild();
        if (!StringUtils.isTrivial(name)) {
            XMLEditUtils.addAttribute(root, "name", name);
        }
        if (!StringUtils.isTrivial(author)) {
            XMLEditUtils.addAttribute(root, "author", author);
        }
        switch (type) {
            case IBlocksPlugin.TYPE_ALL:
                XMLEditUtils.addAttribute(root, "classifications", "all");
                break;
            case IBlocksPlugin.TYPE_FLOATINGROCK:
                XMLEditUtils.addAttribute(root, "classifications", "floatingrock");
                break;
            case IBlocksPlugin.TYPE_SHIP:
                XMLEditUtils.addAttribute(root, "classifications", "ship");
                break;
            case IBlocksPlugin.TYPE_SHOP:
                XMLEditUtils.addAttribute(root, "classifications", "shop");
                break;
            case IBlocksPlugin.TYPE_STATION:
                XMLEditUtils.addAttribute(root, "classifications", "station");
                break;
            case IBlocksPlugin.TYPE_PLANET:
                XMLEditUtils.addAttribute(root, "classifications", "planet");
                break;
            default:
                XMLEditUtils.addAttribute(root, "classifications", "all");
                break;
        }
        File plugins = new File(Paths.getPluginsDirectory());
        File shapeLibDir = new File(plugins, "shapeLibrary");
        if (!shapeLibDir.exists()) {
            shapeLibDir.mkdirs();
        }
        File shapeFile = new File(shapeLibDir, name + ".xml");
        XMLUtils.writeFile(root, shapeFile);
        mLastRead = 0;
    }

    private static boolean isType(ShapeLibraryEntry entry, int type) {
        if (type == IBlocksPlugin.TYPE_ALL) {
            return true;
        }
        if (entry.getClassifications().isEmpty()) {
            return true;
        }
        if (entry.getClassifications().contains(IBlocksPlugin.TYPE_ALL)) {
            return true;
        }
        return entry.getClassifications().contains(type);
    }

    public static Map<String, Object> getEntryMap() {
        Map<String, Object> shapeMap = new HashMap<>();
        int type = StarMadeLogic.getInstance().getCurrentModel().getType();
        for (ShapeLibraryEntry entry : ShapeLibraryLogic.getEntries(type)) {
            shapeMap.put(entry.getName(), entry.getUNID());
        }
        if (shapeMap.isEmpty()) {
            shapeMap.put("No shapes recorded", -1);
        }
        return shapeMap;
    }
}