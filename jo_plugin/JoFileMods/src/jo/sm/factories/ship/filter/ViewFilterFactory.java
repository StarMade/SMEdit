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
package jo.sm.factories.ship.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.sm.logic.StarMadeLogic;
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

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ViewFilterFactory implements IStarMadePluginFactory {

    private final List<FilterDefinition> mDefs;
    private final List<IStarMadePlugin> mPlugins;

    public ViewFilterFactory() {
        this.mPlugins = new ArrayList<>();
        this.mDefs = new ArrayList<>();
        loadDefinitions();
        loadPlugins();
    }

    @Override
    public IStarMadePlugin[] getPlugins() {
        return mPlugins.toArray(new IStarMadePlugin[0]);
    }

    private void loadPlugins() {
        for (FilterDefinition fd : mDefs) {
            mPlugins.add(new SelectFilterPlugin(fd));
        }
    }

    private void loadDefinitions() {
        File plugins = new File(Paths.getPluginsDirectory());
        File viewFilters = new File(plugins, "ViewFilters.xml");
        Document xml;
        if (viewFilters.exists()) {
            xml = XMLUtils.readFile(viewFilters);
        } else {
            xml = XMLUtils.readStream(ResourceUtils.loadSystemResourceStream("ViewFilters.xml", ViewFilterFactory.class));
        }
        loadDefinitions(xml);
    }

    private void loadDefinitions(Document xml) {
        BlockTypeColors.loadBlockIcons();
        Node fs = XMLUtils.findFirstNode(xml, "filters");
        String baseAuthor = XMLUtils.getAttribute(fs, "author");
        for (Node f : XMLUtils.findNodes(fs, "filter")) {
            String title = XMLUtils.getAttribute(f, "title");
            String desc = XMLUtils.getAttribute(f, "descriptions");
            String author = XMLUtils.getAttribute(f, "author");
            int priority = IntegerUtils.parseInt(XMLUtils.getAttribute(f, "priority"));
            if (StringUtils.isTrivial(author)) {
                author = baseAuthor;
            }
            Set<Short> ids = new HashSet<>();
            for (Node b : XMLUtils.findNodes(f, "block")) {
                String id = XMLUtils.getAttribute(b, "type");
                if (BlockTypeColors.mBlockTypes.containsKey(id)) {
                    ids.add(ShortUtils.parseShort(BlockTypeColors.mBlockTypes.get(id)));
                } else {
                    short val = ShortUtils.parseShort(id);
                    if (val > 0) {
                        ids.add(val);
                    }
                }
            }
            if (!StringUtils.isTrivial(title)) {
                FilterDefinition def = new FilterDefinition();
                def.setTitle(title);
                def.setDescription(desc);
                def.setAuthor(author);
                def.setPriority(priority);
                def.setBlocks(ids);
                mDefs.add(def);
            }
        }
    }
}
