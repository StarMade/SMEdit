/**
 * Copyright 2014 SMEdit https://github.com/StarMade/SMEdit SMTools
 * https://github.com/StarMade/SMTools
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
 *
 */
package jo.sm.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import jo.sm.data.BlockTypes;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class BlockTypeColors {

    public static final Paint HULL_RED = Color.red;

    public static final Map<Short, Color> BLOCK_FILL = new HashMap<>();

    static {
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_XP, new Color(1.0f, 0.0f, 0.0f, 0.5f));
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_XM, new Color(0.5f, 0.0f, 0.0f, 0.5f));
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_YP, new Color(0.0f, 1.0f, 0.0f, 0.5f));
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_YM, new Color(0.0f, 0.5f, 0.0f, 0.5f));
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_ZP, new Color(0.0f, 0.0f, 1.0f, 0.5f));
        BLOCK_FILL.put(BlockTypes.SPECIAL_SELECT_ZM, new Color(0.0f, 0.0f, 0.5f, 0.5f));

        BLOCK_FILL.put(BlockTypes.HULL_COLOR_GREY_ID, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_GREY_ID, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_GREY_ID, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_GREY_ID, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_GREY_ID, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WHITE_ID, new Color(52, 72, 55));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_WHITE_ID, new Color(52, 72, 55));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_WHITE_ID, new Color(52, 72, 55));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_WHITE_ID, new Color(52, 72, 55));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_WHITE_ID, new Color(52, 72, 55));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_BROWN_ID, new Color(83, 79, 65));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_BROWN_ID, new Color(83, 79, 65));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_BROWN_ID, new Color(83, 79, 65));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_BROWN_ID, new Color(83, 79, 65));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_BROWN_ID, new Color(83, 79, 65));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_RED_ID, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_RED_ID, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_RED_ID, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_RED_ID, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_RED_ID, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PURPLE_ID, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_PURPLE_ID, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_PURPLE_ID, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_PURPLE_ID, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_PURPLE_ID, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_GREEN_ID, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_GREEN_ID, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_GREEN_ID, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_GREEN_ID, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_GREEN_ID, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_BLACK_ID, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_BLACK_ID, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_BLACK_ID, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_BLACK_ID, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_BLACK_ID, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_YELLOW_ID, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_YELLOW_ID, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_YELLOW_ID, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_YELLOW_ID, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_YELLOW_ID, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_BLUE_ID, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_WEDGE_BLUE_ID, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_CORNER_BLUE_ID, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_PENTA_BLUE_ID, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.HULL_COLOR_TETRA_BLUE_ID, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.GLASS_ID, new Color(162, 169, 170));
        BLOCK_FILL.put(BlockTypes.GLASS_WEDGE_ID, new Color(162, 169, 170));
        BLOCK_FILL.put(BlockTypes.GLASS_CORNER_ID, new Color(162, 169, 170));
        BLOCK_FILL.put(BlockTypes.GLASS_PENTA_ID, new Color(162, 169, 170));
        BLOCK_FILL.put(BlockTypes.GLASS_TETRA_ID, new Color(162, 169, 170));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_GREY, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_GREY, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_GREY, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_GREY, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_GREY, new Color(79, 73, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_RED, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_RED, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_RED, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_RED, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_RED, new Color(66, 68, 54));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PURPLE, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_PURPLE, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_PURPLE, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_PURPLE, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_PURPLE, new Color(80, 92, 97));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_GREEN, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_GREEN, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_GREEN, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_GREEN, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_GREEN, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_BLACK, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_BLACK, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_BLACK, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_BLACK, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_BLACK, new Color(95, 78, 61));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_GOLD, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_GOLD, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_GOLD, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_GOLD, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_GOLD, new Color(57, 59, 81));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_BLUE, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_BLUE, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_BLUE, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_BLUE, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_BLUE, new Color(59, 86, 66));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WHITE, new Color(60, 69, 73));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_WHITE, new Color(60, 69, 73));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_WHITE, new Color(60, 69, 73));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_WHITE, new Color(60, 69, 73));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_WHITE, new Color(60, 69, 73));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_BROWN, new Color(83, 67, 58));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_WEDGE_BROWN, new Color(83, 67, 58));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_CORNER_BROWN, new Color(83, 67, 58));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_PENTA_BROWN, new Color(83, 67, 58));
        BLOCK_FILL.put(BlockTypes.POWERHULL_COLOR_TETRA_BROWN, new Color(83, 67, 58));
        BLOCK_FILL.put(BlockTypes.FIXED_DOCK_ID, new Color(204, 204, 192));
        BLOCK_FILL.put(BlockTypes.FIXED_DOCK_ID_ENHANCER, new Color(58, 65, 59));
        BLOCK_FILL.put(BlockTypes.DOCK_ID, new Color(201, 205, 220));
        BLOCK_FILL.put(BlockTypes.DOCKING_ENHANCER_ID, new Color(53, 52, 51));
        BLOCK_FILL.put(BlockTypes.LIGHT_BEACON_ID, new Color(161, 172, 163));
        BLOCK_FILL.put(BlockTypes.LIGHT_GREEN, new Color(53, 64, 84));
        BLOCK_FILL.put(BlockTypes.LIGHT_YELLOW, new Color(61, 62, 75));
        BLOCK_FILL.put(BlockTypes.GRAVITY_ID, new Color(56, 56, 56));
        BLOCK_FILL.put(BlockTypes.BUILD_BLOCK_ID, new Color(255, 255, 255));
        BLOCK_FILL.put(BlockTypes.STASH_ELEMENT, new Color(35, 35, 69));
        BLOCK_FILL.put(BlockTypes.DOOR_ELEMENT, new Color(136, 141, 137));
        BLOCK_FILL.put(BlockTypes.POWER_ID, new Color(49, 50, 66));
        BLOCK_FILL.put(BlockTypes.FACTION_BLOCK, new Color(32, 31, 62));
        BLOCK_FILL.put(BlockTypes.FACTION_HUB_BLOCK, new Color(53, 51, 50));
        BLOCK_FILL.put(BlockTypes.POWER_HOLDER_ID, new Color(62, 61, 95));
        BLOCK_FILL.put(BlockTypes.DECORATIVE_PANEL_1, new Color(53, 51, 50));
        BLOCK_FILL.put(BlockTypes.DECORATIVE_PANEL_2, new Color(255, 255, 255));
        BLOCK_FILL.put(BlockTypes.DECORATIVE_PANEL_3, new Color(147, 141, 187));
        BLOCK_FILL.put(BlockTypes.DECORATIVE_PANEL_4, new Color(139, 161, 133));
        BLOCK_FILL.put(BlockTypes.LIGHT_BULB_YELLOW, new Color(144, 5, 70));
        BLOCK_FILL.put(BlockTypes.CORE_ID, new Color(255, 255, 255));
        BLOCK_FILL.put(BlockTypes.WEAPON_CONTROLLER_ID, new Color(53, 51, 54));
        BLOCK_FILL.put(BlockTypes.WEAPON_ID, new Color(192, 177, 139));
        BLOCK_FILL.put(BlockTypes.SALVAGE_CONTROLLER_ID, new Color(49, 55, 54));
        BLOCK_FILL.put(BlockTypes.SALVAGE_ID, new Color(4, 126, 238));
        BLOCK_FILL.put(BlockTypes.REPAIR_CONTROLLER_ID, new Color(67, 66, 66));
        BLOCK_FILL.put(BlockTypes.REPAIR_ID, new Color(102, 102, 102));
        BLOCK_FILL.put(BlockTypes.MISSILE_DUMB_CONTROLLER_ID, new Color(58, 53, 50));
        BLOCK_FILL.put(BlockTypes.MISSILE_DUMB_ID, new Color(53, 53, 55));
        BLOCK_FILL.put(BlockTypes.MISSILE_HEAT_CONTROLLER_ID, new Color(60, 51, 58));
        BLOCK_FILL.put(BlockTypes.MISSILE_HEAT_ID, new Color(59, 58, 52));
        BLOCK_FILL.put(BlockTypes.MISSILE_FAFO_CONTROLLER_ID, new Color(65, 54, 68));
        BLOCK_FILL.put(BlockTypes.MISSILE_FAFO_ID, new Color(51, 59, 59));
        BLOCK_FILL.put(BlockTypes.EXPLOSIVE_ID, new Color(179, 181, 180));
        BLOCK_FILL.put(BlockTypes.CLOAKING_ID, new Color(204, 207, 205));
        BLOCK_FILL.put(BlockTypes.RADAR_JAMMING_ID, new Color(59, 59, 67));
        BLOCK_FILL.put(BlockTypes.THRUSTER_ID, new Color(57, 70, 104));
        BLOCK_FILL.put(BlockTypes.SHIELD_ID, new Color(50, 55, 57));
        BLOCK_FILL.put(BlockTypes.COCKPIT_ID, new Color(55, 54, 53));
        BLOCK_FILL.put(BlockTypes.AI_ELEMENT, new Color(168, 176, 203));
        BLOCK_FILL.put(BlockTypes.POWER_SUPPLY_BEAM_COMPUTER, new Color(169, 214, 192));
        BLOCK_FILL.put(BlockTypes.POWER_DRAIN_BEAM_COMPUTER, new Color(38, 38, 38));
        BLOCK_FILL.put(BlockTypes.POWER_DRAIN_BEAM_MODULE, new Color(56, 55, 53));
        BLOCK_FILL.put(BlockTypes.FACTORY_INPUT_ID, new Color(61, 61, 61));
        BLOCK_FILL.put(BlockTypes.FACTORY_INPUT_ENH_ID, new Color(203, 194, 140));
        BLOCK_FILL.put(BlockTypes.FACTORY_PARTICLE_PRESS, new Color(57, 70, 104));
        BLOCK_FILL.put(BlockTypes.FACTORY_SD1000, new Color(61, 61, 61));
        BLOCK_FILL.put(BlockTypes.FACTORY_SD2000, new Color(40, 119, 117));
        BLOCK_FILL.put(BlockTypes.FACTORY_SD20000, new Color(203, 194, 140));
        BLOCK_FILL.put(BlockTypes.FACTORY_SD30000, new Color(57, 70, 104));
        BLOCK_FILL.put(BlockTypes.FACTORY_MINERAL, new Color(61, 61, 61));
        BLOCK_FILL.put(BlockTypes.MAN_SD1000_CAP, new Color(44, 64, 40));
        BLOCK_FILL.put(BlockTypes.MAN_SD2000_CAP, new Color(33, 62, 51));
        BLOCK_FILL.put(BlockTypes.MAN_SD3000_CAP, new Color(66, 66, 66));
        BLOCK_FILL.put(BlockTypes.MAN_SD1000_FLUX, new Color(41, 124, 123));
        BLOCK_FILL.put(BlockTypes.MAN_SD2000_FLUX, new Color(57, 111, 36));
        BLOCK_FILL.put(BlockTypes.MAN_GREEN, new Color(201, 201, 201));
        BLOCK_FILL.put(BlockTypes.MAN_YELLOW, new Color(54, 64, 96));
        BLOCK_FILL.put(BlockTypes.MAN_RED, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.MAN_BROWN, new Color(150, 7, 74));
        BLOCK_FILL.put(BlockTypes.MAN_PURP, new Color(206, 182, 162));
        BLOCK_FILL.put(BlockTypes.LANDING_ELEMENT, new Color(85, 64, 70));
        BLOCK_FILL.put(BlockTypes.LIFT_ELEMENT, new Color(217, 203, 217));
        BLOCK_FILL.put(BlockTypes.RECYCLER_ELEMENT, new Color(38, 38, 38));
        BLOCK_FILL.put(BlockTypes.TERRAIN_EXOGEN_ID, new Color(34, 31, 30));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M1L2_ID, new Color(32, 31, 62));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M1L3_ID, new Color(52, 65, 89));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M1L4_ID, new Color(81, 45, 65));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M1L5_ID, new Color(86, 63, 41));
        BLOCK_FILL.put(BlockTypes.TERRAIN_OCTOGEN_ID, new Color(217, 218, 217));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M2L2_ID, new Color(136, 141, 137));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M2L3_ID, new Color(176, 143, 120));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M2L4_ID, new Color(176, 143, 120));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M2L5_ID, new Color(88, 95, 98));
        BLOCK_FILL.put(BlockTypes.TERRAIN_QUANTAGEN_ID, new Color(33, 62, 51));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M3L2_ID, new Color(68, 88, 69));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M3L3_ID, new Color(68, 87, 70));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M3L4_ID, new Color(96, 79, 62));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M3L5_ID, new Color(72, 71, 68));
        BLOCK_FILL.put(BlockTypes.TERRAIN_QUANTANIUM_ID, new Color(57, 70, 104));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M4L2_ID, new Color(20, 20, 20));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M4L3_ID, new Color(38, 44, 116));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M4L4_ID, new Color(167, 69, 22));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M4L5_ID, new Color(59, 119, 39));
        BLOCK_FILL.put(BlockTypes.TERRAIN_PLEXTANIUM_ID, new Color(61, 61, 61));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M5L2_ID, new Color(18, 18, 18));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M5L3_ID, new Color(34, 40, 110));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M5L4_ID, new Color(144, 5, 70));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M5L5_ID, new Color(161, 64, 19));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ORANGUTANIUM_ID, new Color(72, 71, 68));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M6L2_ID, new Color(92, 105, 89));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M6L3_ID, new Color(78, 79, 89));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M6L4_ID, new Color(83, 78, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_M6L5_ID, new Color(247, 247, 247));
        BLOCK_FILL.put(BlockTypes.TERRAIN_LAVA_ID, new Color(51, 50, 62));
        BLOCK_FILL.put(BlockTypes.TERRAIN_MARS_TOP, new Color(38, 44, 116));
        BLOCK_FILL.put(BlockTypes.TERRAIN_CACTUS_ID, new Color(92, 59, 40));
        BLOCK_FILL.put(BlockTypes.TERRAIN_PURPLE_ALIEN_TOP, new Color(186, 193, 197));
        BLOCK_FILL.put(BlockTypes.TERRAIN_PURPLE_ALIEN_ROCK, new Color(186, 193, 197));
        BLOCK_FILL.put(BlockTypes.TERRAIN_GRASS_SPRITE, new Color(56, 51, 52));
        BLOCK_FILL.put(BlockTypes.TERRAIN_BROWNWEED_SPRITE, new Color(56, 48, 50));
        BLOCK_FILL.put(BlockTypes.TERRAIN_MARSTENTACLES_SPRITE, new Color(58, 48, 50));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ALIENVINE_SPRITE, new Color(59, 47, 51));
        BLOCK_FILL.put(BlockTypes.TERRAIN_GRASSFLOWERS_SPRITE, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_LONGWEED_SPRITE, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_TALLSHROOM_SPRITE, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_PURSPIRE_SPRITE, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_TALLGRASSFLOWERS_SPRITE, new Color(160, 172, 174));
        BLOCK_FILL.put(BlockTypes.TERRAIN_MINICACTUS_SPRITE, new Color(179, 184, 183));
        BLOCK_FILL.put(BlockTypes.TERRAIN_REDSHROOM_SPRITE, new Color(188, 186, 184));
        BLOCK_FILL.put(BlockTypes.TERRAIN_PURPTACLES_SPRITE, new Color(165, 166, 178));
        BLOCK_FILL.put(BlockTypes.TERRAIN_TALLFLOWERS_SPRITE, new Color(218, 212, 201));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ROCK_SPRITE, new Color(167, 160, 141));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ALIENFLOWERS_SPRITE, new Color(131, 155, 159));
        BLOCK_FILL.put(BlockTypes.TERRAIN_YHOLE_SPRITE, new Color(126, 134, 151));
        BLOCK_FILL.put(BlockTypes.TERRAIN_MARS_DIRT, new Color(38, 44, 116));
        BLOCK_FILL.put(BlockTypes.TERRAIN_TREE_LEAF_ID, new Color(62, 62, 60));
        BLOCK_FILL.put(BlockTypes.TERRAIN_WATER, new Color(158, 141, 139));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_LEAVES, new Color(62, 62, 60));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_SPIKE_SPRITE, new Color(54, 52, 52));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_ICECRAG_SPRITE, new Color(77, 75, 73));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_ICECORAL_SPRITE, new Color(145, 170, 179));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_ICEGRASS_SPRITE, new Color(157, 138, 151));
        BLOCK_FILL.put(BlockTypes.TERRAIN_ICEPLANET_CRYSTAL, new Color(131, 168, 149));
        BLOCK_FILL.put(BlockTypes.TERRAIN_REDWOOD_LEAVES, new Color(62, 62, 60));
        BLOCK_FILL.put(BlockTypes.DEATHSTAR_CORE_ID, new Color(204, 204, 192));
    }

    public static final Map<Short, Color> BLOCK_OUTLINE = new HashMap<>();

    static {
        BLOCK_OUTLINE.put(BlockTypes.WEAPON_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.WEAPON_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.CORE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DEATHSTAR_CORE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.GLASS_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.THRUSTER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DOCK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.SHIELD_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.EXPLOSIVE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.RADAR_JAMMING_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.CLOAKING_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.SALVAGE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_DUMB_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_DUMB_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_HEAT_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_HEAT_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_FAFO_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MISSILE_FAFO_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.SALVAGE_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.GRAVITY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.REPAIR_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.REPAIR_CONTROLLER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.COCKPIT_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_BEACON_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_GREY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PURPLE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_BROWN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_BLACK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_RED_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_BLUE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_GREEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_YELLOW_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WHITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LANDING_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIFT_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.RECYCLER_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.STASH_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.AI_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DOOR_ELEMENT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.BUILD_BLOCK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_LAVA_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_EXOGEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_OCTOGEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_QUANTAGEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_QUANTANIUM_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PLEXTANIUM_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ORANGUTANIUM_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_SUCCUMITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_CENOMITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_AWESOMITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_VAPPECIDE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MARS_TOP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MARS_ROCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MARS_DIRT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MARS_TOP_ROCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_EXTRANIUM_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ROCK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_SAND_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_EARTH_TOP_DIRT, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_EARTH_TOP_ROCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_TREE_TRUNK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_TREE_LEAF_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_WATER, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_DIRT_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DOCKING_ENHANCER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_CACTUS_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PURPLE_ALIEN_TOP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PURPLE_ALIEN_ROCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PURPLE_ALIEN_VINE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_GRASS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.PLAYER_SPAWN_MODULE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_BROWNWEED_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MARSTENTACLES_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ALIENVINE_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_GRASSFLOWERS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_LONGWEED_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_TALLSHROOM_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PURSPIRE_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_TALLGRASSFLOWERS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_MINICACTUS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_REDSHROOM_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_PURPTACLES_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_TALLFLOWERS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ROCK_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ALIENFLOWERS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_YHOLE_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M1L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M1L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M1L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M1L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M2L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M2L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M2L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M2L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M3L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M3L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M3L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M3L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M4L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M4L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M4L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M4L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M5L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M5L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M5L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M5L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M6L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M6L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M6L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M6L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M7L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M7L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M7L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M7L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M8L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M8L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M8L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M8L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M9L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M9L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M9L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M9L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M10L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M10L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M10L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M10L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M11L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M11L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M11L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M11L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M12L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M12L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M12L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M12L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M13L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M13L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M13L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M13L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M14L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M14L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M14L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M14L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M15L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M15L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M15L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M15L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M16L2_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M16L3_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M16L4_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_M16L5_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_NEGACIDE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_QUANTACIDE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_NEGAGATE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_METATE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_INSANIUM_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_INPUT_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_INPUT_ENH_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_CELL_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_CELL_ENH_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_COIL_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_COIL_ENH_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_BLOCK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_POWER_BLOCK_ENH_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_CELL_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_COIL_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.UNUSED_TEST, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_PARTICLE_PRESS, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD1000_CAP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD2000_CAP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD3000_CAP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD1000_FLUX, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD2000_FLUX, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD3000_FLUX, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD1000_MICRO, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD2000_MICRO, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD3000_MICRO, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD1000_DELTA, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD2000_DELTA, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD3000_DELTA, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD1000_MEM, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD2000_MEM, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SD3000_MEM, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SDPROTON, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_PURP, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_YELLOW, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P1000B, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P2000B, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P3000B, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P10000A, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P20000A, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P30000A, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_P40000A, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_YHOLE_NUC, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD10000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD20000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD30000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SDADV, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD1000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD2000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_SD3000, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTORY_MINERAL, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_GREY, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PURPLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_GOLD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_GLASS_BOTTLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.MAN_SCIENCE_BOTTLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_SURFACE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_ROCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_WOOD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_LEAVES, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_SPIKE_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_ICECRAG_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_ICECORAL_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_ICEGRASS_SPRITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_YELLOW, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_ICEPLANET_CRYSTAL, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_REDWOOD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.TERRAIN_REDWOOD_LEAVES, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FIXED_DOCK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FIXED_DOCK_ID_ENHANCER, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTION_BLOCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.FACTION_HUB_BLOCK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_GREY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_PURPLE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_BROWN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_BLACK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_RED_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_BLUE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_GREEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_YELLOW_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_WEDGE_WHITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_GREY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_PURPLE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_BROWN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_BLACK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_RED_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_BLUE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_GREEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_YELLOW_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_PENTA_WHITE_ID, Color.white);

        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_GREY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_PURPLE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_BROWN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_BLACK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_RED_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_BLUE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_GREEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_YELLOW_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_TETRA_WHITE_ID, Color.white);

        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_GREY_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_PURPLE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_BROWN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_BLACK_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_RED_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_BLUE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_GREEN_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_YELLOW_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.HULL_COLOR_CORNER_WHITE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_GREY, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_PURPLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_GOLD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_WEDGE_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_GREY, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_PURPLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_GOLD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_CORNER_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_GREY, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_PURPLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_GOLD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_PENTA_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_GREY, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_PURPLE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_BROWN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_BLACK, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_RED, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_BLUE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_GREEN, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_GOLD, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWERHULL_COLOR_TETRA_WHITE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.GLASS_WEDGE_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.GLASS_CORNER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_HOLDER_ID, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_DRAIN_BEAM_COMPUTER, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_DRAIN_BEAM_MODULE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_SUPPLY_BEAM_COMPUTER, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.POWER_SUPPLY_BEAM_MODULE, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DECORATIVE_PANEL_1, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DECORATIVE_PANEL_2, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DECORATIVE_PANEL_3, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.DECORATIVE_PANEL_4, Color.white);
        BLOCK_OUTLINE.put(BlockTypes.LIGHT_BULB_YELLOW, Color.white);
    }

    public static Color getOutlineColor(short blockType) {
        if (BLOCK_OUTLINE.containsKey(blockType)) {
            return BLOCK_OUTLINE.get(blockType);
        }
        return Color.white;
    }

    public static Color getFillColor(short blockType) {
        if (BLOCK_FILL.containsKey(blockType)) {
            return BLOCK_FILL.get(blockType);
        }
        return Color.gray;
    }

    public static final Map<Short, Short> BLOCK_HITPOINTS = new HashMap<>();
    public static final Map<Short, Integer> BLOCK_TEXTURE_IDS = new HashMap<>();

    private static boolean mBlockIconsLoaded = false;
    private static final Map<Short, ImageIcon> mBlockIcons = new HashMap<>();
    private static final List<BufferedImage> mTextureMaps = new ArrayList<>();
    public static int mAllTexturesImagesPerSide;
    public static int mAllTexturesPixelsPerImage;
    public static BufferedImage mAllTextures;
    public static Properties mBlockTypes;
    private static Properties mProps;

    public static void loadProps() {
        File home = new File(System.getProperty("user.home"));
        File props = new File(home, ".josm");
        if (props.exists()) {
            mProps = new Properties();
            try {
                try (FileInputStream fis = new FileInputStream(props)) {
                    mProps.load(fis);
                }
            } catch (IOException e) {

            }
        } else {
            mProps = new Properties();
        }
    }

    public static ImageIcon getBlockImage(short blockID) {
        if (blockID >= BlockTypes.SPECIAL) {
            return getSpecialBlockImage(blockID);
        }
        loadBlockIcons();
        if (!mBlockIcons.containsKey(blockID)) {
            if (!BLOCK_TEXTURE_IDS.containsKey(blockID)) {
                return null;
            }
            int textureID = BLOCK_TEXTURE_IDS.get(blockID);
            BufferedImage localBufferedImage = getTextureImage(textureID);
            mBlockIcons.put(blockID, new ImageIcon(localBufferedImage));
        }
        return mBlockIcons.get(blockID);
    }

    public static BufferedImage getTextureImage(int textureID) {
        int j = textureID % 256 % 16;
        int k = textureID % 256 / 16;
        BufferedImage map = mTextureMaps.get(textureID / 256);
        int hScale = map.getWidth() / 16;
        int vScale = map.getHeight() / 16;
        BufferedImage localBufferedImage = map.getSubimage(j * hScale, k * vScale, hScale, vScale);
        return localBufferedImage;
    }

    public static void loadBlockIcons() {
        if (mBlockIconsLoaded) {
            return;
        }
        try {
            loadTextureMaps();
            mBlockTypes = new Properties();
            File propsFile = new File(StarMadeLogic.getInstance().getBaseDir(), "data/config/BlockTypes.properties");
            try (InputStream is = new FileInputStream(propsFile)) {
                mBlockTypes.load(is);
            }
            File xmlFile = new File(StarMadeLogic.getInstance().getBaseDir(), "data/config/BlockConfig.xml");
            Document doc = XMLUtils.readFile(xmlFile);
            for (Node n : XMLUtils.findAllNodesRecursive(doc, "Block")) {
                String name = XMLUtils.getAttribute(n, "name");
                String type = XMLUtils.getAttribute(n, "type");
                if (!mBlockTypes.containsKey(type)) {
                    System.err.println("No ID found for '" + type + "', - " + name);
                    continue;
                }
                short id = ShortUtils.parseShort(mBlockTypes.get(type));
                //int icon = IntegerUtils.parseInt(XMLUtils.getAttribute(n, "icon"));
                int textureID = IntegerUtils.parseInt(XMLUtils.getAttribute(n, "textureId"));
                short hitPoints = ShortUtils.parseShort(XMLUtils.getTextTag(n, "Hitpoints"));

                BlockTypes.BLOCK_NAMES.put(id, name);
                BLOCK_HITPOINTS.put(id, hitPoints);
                BLOCK_TEXTURE_IDS.put(id, textureID);
                try {
                    Field f = BlockTypes.class.getField(type);
                    if (f != null) {
                        f.setShort(null, id);
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBlockIconsLoaded = true;
    }

    private static void loadTextureMaps() throws IOException {
        loadProps();
        for (int i = 0; i < 256; i++) {
            File f = new File(
                    /* Change the commented out lines to change the game version to loaf textures */
                    /* 0.094 and below */
                    StarMadeLogic.getInstance().getBaseDir(), "data/textures/block/t" + StringUtils.zeroPrefix(i, 3) + ".png");
                    /* 0.14 */
                    //StarMadeLogic.getInstance().getBaseDir(), "data/textures/block/" + mProps.getProperty("texture", "") + "/64/t" + StringUtils.zeroPrefix(i, 3) + ".png");

            if (!f.exists()) {
                break;
            }
            BufferedImage img = ImageIO.read(f);
            mTextureMaps.add(img);
        }
        int numTextures = 16 * 16 * mTextureMaps.size();
        mAllTexturesImagesPerSide = (int) Math.ceil(Math.sqrt(numTextures));
        mAllTexturesPixelsPerImage = 1024 / mAllTexturesImagesPerSide;
        mAllTextures = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mAllTextures.getGraphics();
        for (int i = 0; i < numTextures; i++) {
            BufferedImage texture = getTextureImage(i);
            Rectangle r = getAllTextureLocation(i);
            g.drawImage(texture, r.x, r.y, r.x + r.width, r.y + r.height, 0, 0, texture.getWidth(), texture.getHeight(), null);
        }
    }

    private static Rectangle getAllTextureLocation(int textureID) {
        int j = textureID % mAllTexturesImagesPerSide;
        int k = textureID / mAllTexturesImagesPerSide;
        return new Rectangle(j * mAllTexturesPixelsPerImage, 1024 - k * mAllTexturesPixelsPerImage - mAllTexturesPixelsPerImage,
                mAllTexturesPixelsPerImage, mAllTexturesPixelsPerImage);
    }

    public static Rectangle2D.Float getAllTextureLocation(short blockID) {
        int textureID = BLOCK_TEXTURE_IDS.get(blockID);
        int j = textureID % mAllTexturesImagesPerSide;
        int k = textureID / mAllTexturesImagesPerSide;
        float effectiveWidth = mAllTexturesImagesPerSide * mAllTexturesPixelsPerImage / 1024f;
        float chunk = effectiveWidth / mAllTexturesImagesPerSide;
        Rectangle2D.Float abs = new Rectangle2D.Float(j * chunk, k * chunk, chunk, chunk);
        return abs;
    }

    private static final Map<Short, ImageIcon> SPECIAL_ICONS = new HashMap<>();

    private static ImageIcon getSpecialBlockImage(short blockID) {
        if (SPECIAL_ICONS.containsKey(blockID)) {
            return SPECIAL_ICONS.get(blockID);
        }
        int color = 0;
        switch (blockID) {
            case BlockTypes.SPECIAL_SELECT_XP:
                color = 0x80FF0000;
                break;
            case BlockTypes.SPECIAL_SELECT_XM:
                color = 0x80800000;
                break;
            case BlockTypes.SPECIAL_SELECT_YP:
                color = 0x8000FF00;
                break;
            case BlockTypes.SPECIAL_SELECT_YM:
                color = 0x80008000;
                break;
            case BlockTypes.SPECIAL_SELECT_ZP:
                color = 0x800000FF;
                break;
            case BlockTypes.SPECIAL_SELECT_ZM:
                color = 0x80000080;
                break;
        }
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                img.setRGB(x, y, color);
            }
        }
        ImageIcon icon = new ImageIcon(img);
        SPECIAL_ICONS.put(blockID, icon);
        return icon;
    }
}
