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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.Entity;
import jo.sm.data.StarMade;
import jo.sm.ent.data.Tag;
import jo.sm.ent.logic.TagLogic;
import jo.sm.logic.utils.DebugLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.logic.DataLogic;
import jo.vecmath.Point3i;


public class EntityLogic {
    private static final Logger log = Logger.getLogger(EntityLogic.class.getName());

    public static List<Entity> getEntities() throws IOException {
        StarMade sm = StarMadeLogic.getInstance();
        if (sm.getEntities() == null) {
            sm.setEntities(new ArrayList<Entity>());
            File serverDatabase = new File(sm.getBaseDir(), "server-database");
            for (File entFile : serverDatabase.listFiles()) {
                if (entFile.getName().startsWith("ENTITY_")) {
                    try {
                        Entity entity = readEntity(entFile);
                        sm.getEntities().add(entity);
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Bad entity read!", e);
                        System.out.println("Bad entity read: " + e);
                        //e.printStackTrace();
                    }
                }
            }
        }
        return sm.getEntities();
    }

    public static Entity readEntity(File entFile) throws IOException {
        DebugLogic.debug("Reading entity " + entFile);
        Entity entity = new Entity();
        entity.setFile(entFile);
        parseName(entFile, entity);
        Tag t = TagLogic.readFile(new FileInputStream(entFile), true);
        entity.setTag(t);
        return entity;
    }

    public static void readEntityData(Entity entity, IPluginCallback cb) throws IOException {
        File dataDir = new File(entity.getFile().getParent(), "DATA");
        String name = entity.getFile().getName();
        name = name.substring(0, name.length() - 4); // strip .ent
        entity.setData(DataLogic.readFiles(dataDir, name, cb));
    }

    private static void parseName(File entFile, Entity entity) {
        String name = entFile.getName();
        name = name.substring(7); // strip ENTITY_
        name = name.substring(0, name.length() - 4); // strip .ent
        int o = name.indexOf("_");
        if (o < 0) {
            throw new IllegalArgumentException("Bad entity file name " + entFile.getName());
        }
        entity.setType(name.substring(0, o));
        name = name.substring(o + 1);
        if (null != entity.getType()) switch (entity.getType()) {
            case "FLOATINGROCK":
                o = name.lastIndexOf("_");
                entity.setUNID(name.substring(o + 1)); // valid if result is -1
                break;
            case "PLANET":
                o = name.lastIndexOf("_");
                entity.setUNID(name.substring(o + 1)); // valid if result is -1
                name = name.substring(0, o);
                String[] coords = name.split("_");
                Point3i pos = new Point3i(Integer.parseInt(coords[0]),
                        Integer.parseInt(coords[1]),
                        Integer.parseInt(coords[2]));
                entity.setLocation(pos);
                break;
            case "PLAYERCHARACTER":
                o = name.lastIndexOf("_");
                entity.setName(name.substring(o + 1)); // valid if result is -1
                break;
            case "PLAYERSTATE":
                o = name.lastIndexOf("_");
                entity.setName(name.substring(o + 1)); // valid if result is -1
                break;
            case "SHIP":
                if (name.startsWith("AITURRET_")) {
                    entity.setName("AITURRET");
                    entity.setUNID(name.substring(9));
                } else if (name.startsWith("MOB_SIM_")) {
                    o = name.indexOf("_", 8);
                    entity.setUNID(name.substring(o + 1)); // valid if result is -1
                    entity.setName(name.substring(0, o));
                } else if (name.startsWith("MOB_")) {
                    o = name.indexOf("_", 4);
                    entity.setUNID(name.substring(o + 1)); // valid if result is -1
                    entity.setName(name.substring(0, o));
                } else {
                    entity.setName(name);
                }   break;
            case "SHOP":
                o = name.lastIndexOf("_");
                entity.setUNID(name.substring(o + 1)); // valid if result is -1
                break;
            case "SPACESTATION":
                o = name.lastIndexOf("_");
                entity.setUNID(name.substring(o + 1)); // valid if result is -1
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type '" + entity.getType() + "'");
        }
    }
}
