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
package jo.sm.ui.logic;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ui.RenderPanel;
import jo.sm.ui.act.plugin.BlocksPluginAction;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MenuLogic {

    public static int addPlugins(RenderPanel panel, JMenu menu, int type, int subtype) {
        int count = 0;
        List<IBlocksPlugin> plugins = StarMadeLogic.getBlocksPlugins(type, subtype);
        if (plugins.isEmpty()) {
            return 0;
        }

        Map<Integer, List<IBlocksPlugin>> orderMap = getOrderedMap(plugins, type, subtype);
        Integer[] priorities = orderMap.keySet().toArray(new Integer[0]);
        Arrays.sort(priorities);
        Map<String, JMenu> cascades = new HashMap<>();
        int lastPriority = 0;
        int menuIdx = findStart(menu);
        for (int i = 0; i < priorities.length; i++) {
            if ((i > 0) && (priorities[i] / 10 > lastPriority / 10)) {
                JSeparator sep = new JSeparator();
                sep.setName("plugin");
                menu.add(sep, menuIdx++);
            }
            lastPriority = priorities[i];
            List<IBlocksPlugin> pluginsAtPriority = orderMap.get(priorities[i]);
            for (IBlocksPlugin plugin : pluginsAtPriority) {
                BlocksPluginAction action = new BlocksPluginAction(panel, plugin);
                String name = plugin.getName();
                int o = name.indexOf('/');
                if (o > 0) {
                    String cascadeName = name.substring(0, o);
                    JMenu submenu = cascades.get(cascadeName);
                    if (submenu == null) {
                        submenu = new JMenu(cascadeName);
                        submenu.setName("plugin");
                        cascades.put(cascadeName, submenu);
                        menu.add(submenu, menuIdx++);
                    }
                    JMenuItem m = new JMenuItem(action);
                    submenu.add(m);
                } else {
                    JMenuItem m = new JMenuItem(action);
                    menu.add(m, menuIdx++);
                }
                count++;
            }
        }
        return count;
    }

    private static int findStart(JMenu menu) {
        for (int idx = 0; idx < menu.getItemCount(); idx++) {
            String name = null;
            JMenuItem it = menu.getItem(idx);
            if (it == null) {
                Component c = menu.getPopupMenu().getComponent(idx);
                name = c.getName();
            } else {
                name = it.getName();
            }
            if ("pluginsStartHere".equals(name)) {
                return idx + 1;
            }
        }
        return menu.getItemCount();
    }

    private static Map<Integer, List<IBlocksPlugin>> getOrderedMap(
            List<IBlocksPlugin> plugins, int type, int subtype) {
        Map<Integer, List<IBlocksPlugin>> map = new HashMap<>();
        for (IBlocksPlugin plugin : plugins) {
            int[][] classifications = plugin.getClassifications();
            int priority = 50;
            for (int[] classification : classifications) {
                if (StarMadeLogic.isClassification(classification[0], type) && (classification[1] == subtype)) {
                    if (classification.length > 2) {
                        priority = classification[2];
                        break;
                    }
                }
            }
            List<IBlocksPlugin> pluginsAtPriority = map.get(priority);
            if (pluginsAtPriority == null) {
                pluginsAtPriority = new ArrayList<>();
                map.put(priority, pluginsAtPriority);
            }
            pluginsAtPriority.add(plugin);
        }
        return map;
    }

    public static void clearPluginMenus(JMenu menu) {
        for (int idx = menu.getItemCount() - 1; idx >= 0; idx--) {
            JMenuItem it = menu.getItem(idx);
            if (it == null) {
                continue;
            }
            Action a = it.getAction();
            if ((a != null) && (a instanceof BlocksPluginAction)) {
                menu.remove(idx);
            }
        }
        for (int idx = menu.getMenuComponentCount() - 1; idx >= 0; idx--) {
            Component comp = menu.getMenuComponent(idx);
            if ((comp instanceof JSeparator) && "plugin".equals(((JSeparator) comp).getName())) {
                menu.remove(idx);
            } else if ((comp instanceof JMenu) && "plugin".equals(((JMenu) comp).getName())) {
                menu.remove(idx);
            }
        }
    }
}
