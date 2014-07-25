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

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import jo.sm.data.Entity;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.BlueprintLogic;
import jo.sm.logic.EntityLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Blueprint;
import jo.sm.ship.logic.ShipLogic;
import jo.util.Paths;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ShipTreeLogic {

    public static DefaultMutableTreeNode getShipTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        addBlueprint(root, "Blueprints", false);
        addBlueprint(root, "Default Blueprints", true);
        addEntity(root, "Your Ships", "SHIP", "Player");
        addEntity(root, "Other Ships", "SHIP", "MOB_");
        addEntity(root, "Turrets", "SHIP", "AITURRET");
        addEntity(root, "Stations", "SPACESTATION", null);
        addEntity(root, "Shops", "SHOP", null);
        addEntity(root, "Planets", "PLANET", null);
        addEntity(root, "Rocks", "FLOATINGROCK", null);
        return root;
    }

    private static void addBlueprint(DefaultMutableTreeNode root,
            String title, boolean def) {
        DefaultMutableTreeNode group = new DefaultMutableTreeNode(title);
        String[] options;
        if (def) {
            options = BlueprintLogic.getDefaultBlueprintNames().toArray(new String[0]);
        } else {
            options = BlueprintLogic.getBlueprintNames().toArray(new String[0]);
        }
        if (options.length == 0) {
            return;
        }
        for (String name : options) {
            ShipSpec spec = getBlueprintSpec(name, def);
            DefaultMutableTreeNode option = new DefaultMutableTreeNode(spec);
            group.add(option);
        }
        root.add(group);
    }

    public static ShipSpec getBlueprintSpec(String name, boolean def) {
        ShipSpec spec = new ShipSpec();
        spec.setType(def ? ShipSpec.DEFAULT_BLUEPRINT : ShipSpec.BLUEPRINT);
        spec.setClassification(IBlocksPlugin.TYPE_SHIP);
        spec.setName(name);
        spec.setClassification(IBlocksPlugin.TYPE_SHIP);
        File bpDir = new File(StarMadeLogic.getInstance().getBaseDir(), def ? "blueprints-default" : "blueprints");
        File baseDir = new File(bpDir, name);
        spec.setFile(baseDir);
        return spec;
    }
    

    private static void addEntity(DefaultMutableTreeNode root,
            String title, String typeFilter, String nameFilter) {
        DefaultMutableTreeNode group = new DefaultMutableTreeNode(title);
        boolean addedAny = false;
        try {
            for (Entity e : EntityLogic.getEntities()) {
                if ((typeFilter != null) && !e.getType().equals(typeFilter)) {
                    continue;
                }
                if (nameFilter != null) {
                    if ("Player".equals(nameFilter)) {
                        if (!e.toString().startsWith("Ship ")) {
                            continue;
                        }
                    } else if (!e.getName().startsWith(nameFilter)) {
                        continue;
                    }
                }
                ShipSpec spec = new ShipSpec();
                spec.setType(ShipSpec.ENTITY);
                determineClassification(spec, e);
                spec.setName(e.toString());
                spec.setEntity(e);
                DefaultMutableTreeNode option = new DefaultMutableTreeNode(spec);
                group.add(option);
                addedAny = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!addedAny) {
            return;
        }
        root.add(group);
    }

    private static void determineClassification(ShipSpec spec, Entity e) {
        String fname = e.getFile().getName();
        if (fname.contains("_SHIP_")) {
            spec.setClassification(IBlocksPlugin.TYPE_SHIP);
        } else if (fname.contains("_FLOATINGROCK_")) {
            spec.setClassification(IBlocksPlugin.TYPE_FLOATINGROCK);
        } else if (fname.contains("_SHOP_")) {
            spec.setClassification(IBlocksPlugin.TYPE_SHOP);
        } else if (fname.contains("_SPACESTATION_")) {
            spec.setClassification(IBlocksPlugin.TYPE_STATION);
        } else if (fname.contains("_PLANET_")) {
            spec.setClassification(IBlocksPlugin.TYPE_PLANET);
        }
    }

    public static SparseMatrix<Block> loadShip(ShipSpec spec, IPluginCallback cb) {
        try {
            if (spec.getType() == ShipSpec.BLUEPRINT) {
                Blueprint blueprint = BlueprintLogic.readBlueprint(spec.getName(), cb);
                SparseMatrix<Block> grid = ShipLogic.getBlocks(blueprint.getData());
                //System.out.println("Original:");
                //HeaderLogic.dump(blueprint.getHeader());
                //LogicLogic.dump(blueprint.getLogic(), grid);
                //System.out.println("Loopback:");
                //HeaderLogic.dump(HeaderLogic.make(grid));
                //LogicLogic.dump(LogicLogic.make(grid), grid);
                return grid;
            } else if (spec.getType() == ShipSpec.DEFAULT_BLUEPRINT) {
                Blueprint blueprint = BlueprintLogic.readDefaultBlueprint(spec.getName(), cb);
                SparseMatrix<Block> grid = ShipLogic.getBlocks(blueprint.getData());
                //System.out.println("Original:");
                //HeaderLogic.dump(blueprint.getHeader());
                //LogicLogic.dump(blueprint.getLogic(), grid);
                //System.out.println("Loopback:");
                //HeaderLogic.dump(HeaderLogic.make(grid));
                //LogicLogic.dump(LogicLogic.make(grid), grid);
                return grid;
            } else if (spec.getType() == ShipSpec.ENTITY) {
                Entity e = spec.getEntity();
                EntityLogic.readEntityData(e, cb);
                //ShipLogic.dumpChunks(e.getData());
                SparseMatrix<Block> grid = ShipLogic.getBlocks(e.getData());
                e.setData(null); // conserve memory
                return grid;
            } else {
                throw new IllegalArgumentException("Unknown ship type " + spec.getType());
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
