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
package jo.sm.factories.planet.veg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.FloatUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ResourceUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IStarMadePlugin;
import jo.sm.mods.IStarMadePluginFactory;
import jo.sm.ui.BlockTypeColors;
import jo.util.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class VegetationFactory implements IStarMadePluginFactory {
    private static final Logger log = Logger.getLogger(VegetationFactory.class.getName());

    private List<VegetationDefinition> mDefs = new ArrayList<>();
    private List<IStarMadePlugin> mPlugins = new ArrayList<>();

    public VegetationFactory() {
        loadDefinitions();
        loadPlugins();
    }

    @Override
    public IStarMadePlugin[] getPlugins() {
        return mPlugins.toArray(new IStarMadePlugin[0]);
    }

    private void loadPlugins() {
        for (VegetationDefinition fd : mDefs) {
            mPlugins.add(new VegetationPlugin(fd));
        }
    }

    private void loadDefinitions() {
        File plugins = new File(Paths.getPluginsDirectory());
        File viewFilters = new File(plugins, "SurfaceVegetation.xml");
        Document xml;
        if (viewFilters.exists()) {
            xml = XMLUtils.readFile(viewFilters);
        } else {
            xml = XMLUtils.readStream(ResourceUtils.loadSystemResourceStream("SurfaceVegetation.xml", VegetationFactory.class));
        }
        loadDefinitions(xml);
    }

    private void loadDefinitions(Document xml) {
        BlockTypeColors.loadBlockIcons();
        Node fs = XMLUtils.findFirstNode(xml, "vegetations");
        String baseAuthor = XMLUtils.getAttribute(fs, "author");
        for (Node f : XMLUtils.findNodes(fs, "vegetation")) {
            String title = XMLUtils.getAttribute(f, "title");
            if (StringUtils.isTrivial(title)) {
                continue;
            }
            String desc = XMLUtils.getAttribute(f, "descriptions");
            String author = XMLUtils.getAttribute(f, "author");
            int priority = IntegerUtils.parseInt(XMLUtils.getAttribute(f, "priority"));
            if (StringUtils.isTrivial(author)) {
                author = baseAuthor;
            }
            float density = FloatUtils.parseFloat(XMLUtils.getAttribute(f, "density"));
            if (density > 1) {
                density = 1 / density;
            }
            VegetationDefinition def = new VegetationDefinition();
            def.setTitle(title);
            def.setDescription(desc);
            def.setAuthor(author);
            def.setPriority(priority);
            def.setDensity(density);
            for (Node ob : XMLUtils.findNodes(f, "vegetable")) {
                VegetationEntry entry = parseEntry(ob);
                if (entry == null) {
                    continue;
                }
                if ((entry.getPercent() <= 0) || (entry.getPercent() > 100)) {
                    log.log(Level.WARNING, "Vegetables must have percentage between 1 and 100: " + XMLUtils.writeString(ob));
                    continue;
                }
                def.getVegetation().add(entry);
            }
            mDefs.add(def);
        }
    }

    private VegetationEntry parseEntry(Node b) {
        VegetationEntry entry = new VegetationEntry();
        String id = XMLUtils.getAttribute(b, "type");
        if ("*".equals(id)) {
            entry.setBlockID((short) -1);
        } else if (BlockTypeColors.mBlockTypes.containsKey(id)) {
            entry.setBlockID(ShortUtils.parseShort(BlockTypeColors.mBlockTypes.get(id)));
        } else {
            short val = ShortUtils.parseShort(id);
            if (val > 0) {
                entry.setBlockID(val);
            } else {
                System.err.println("Unknown block type '" + id + "' : " + XMLUtils.writeString(b));
                return null;
            }
        }
        entry.setPercent(IntegerUtils.parseInt(XMLUtils.getAttribute(b, "percent")));
        int[] low = parseRange(XMLUtils.getAttribute(b, "low"));
        if (low != null) {
            entry.setLowStrategy(low[0]);
            entry.setLowValue(low[1]);
        }
        int[] high = parseRange(XMLUtils.getAttribute(b, "high"));
        if (high != null) {
            entry.setHighStrategy(high[0]);
            entry.setHighValue(high[1]);
        }
        return entry;
    }

    private int[] parseRange(String range) {
        if (StringUtils.isTrivial(range)) {
            return null;
        }
        int[] ret = new int[2];
        if (range.endsWith("%")) {
            ret[0] = VegetationEntry.ABSOLUTE;
            ret[1] = Integer.parseInt(range.substring(0, range.length() - 1));
        } else {
            ret[0] = VegetationEntry.RELATIVE;
            ret[1] = Integer.parseInt(range);
        }
        return ret;
    }
}
