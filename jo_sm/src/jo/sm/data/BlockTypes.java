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
package jo.sm.data;

import java.util.HashMap;
import java.util.Map;

import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class BlockTypes {

    public static final short SPECIAL = 8350;
    public static final short SPECIAL_SELECT_XP = SPECIAL + 1;
    public static final short SPECIAL_SELECT_XM = SPECIAL + 2;
    public static final short SPECIAL_SELECT_YP = SPECIAL + 3;
    public static final short SPECIAL_SELECT_YM = SPECIAL + 4;
    public static final short SPECIAL_SELECT_ZP = SPECIAL + 5;
    public static final short SPECIAL_SELECT_ZM = SPECIAL + 6;

    public static short WEAPON_CONTROLLER_ID = 6;
    public static short WEAPON_ID = 16;
    public static short CORE_ID = 1;
    public static short DEATHSTAR_CORE_ID = 65;
    public static short GLASS_ID = 63;
    public static short THRUSTER_ID = 8;
    public static short DOCK_ID = 7;
    public static short POWER_ID = 2;
    public static short SHIELD_ID = 3;
    public static short EXPLOSIVE_ID = 14;
    public static short RADAR_JAMMING_ID = 15;
    public static short CLOAKING_ID = 22;
    public static short SALVAGE_ID = 24;
    public static short MISSILE_DUMB_CONTROLLER_ID = 38;
    public static short MISSILE_DUMB_ID = 32;
    public static short MISSILE_HEAT_CONTROLLER_ID = 46;
    public static short MISSILE_HEAT_ID = 40;
    public static short MISSILE_FAFO_CONTROLLER_ID = 54;
    public static short MISSILE_FAFO_ID = 48;
    public static short SALVAGE_CONTROLLER_ID = 4;
    public static short GRAVITY_ID = 56;
    public static short REPAIR_ID = 30;
    public static short REPAIR_CONTROLLER_ID = 39;
    public static short COCKPIT_ID = 47;
    public static short LIGHT_ID = 55;
    public static short LIGHT_BEACON_ID = 62;
    public static short TERRAIN_ICE_ID = 64;
    public static short HULL_COLOR_GREY_ID = 5;
    public static short HULL_COLOR_PURPLE_ID = 69;
    public static short HULL_COLOR_BROWN_ID = 70;
    public static short HULL_COLOR_BLACK_ID = 75;
    public static short HULL_COLOR_RED_ID = 76;
    public static short HULL_COLOR_BLUE_ID = 77;
    public static short HULL_COLOR_GREEN_ID = 78;
    public static short HULL_COLOR_YELLOW_ID = 79;
    public static short HULL_COLOR_WHITE_ID = 81;
    public static short LANDING_ELEMENT = 112;
    public static short LIFT_ELEMENT = 113;
    public static short RECYCLER_ELEMENT = 114;
    public static short STASH_ELEMENT = 120;
    public static short AI_ELEMENT = 121;
    public static short DOOR_ELEMENT = 122;
    public static short BUILD_BLOCK_ID = 123;
    public static short TERRAIN_LAVA_ID = 80;
    public static short TERRAIN_EXOGEN_ID = 128;
    public static short TERRAIN_OCTOGEN_ID = 129;
    public static short TERRAIN_QUANTAGEN_ID = 130;
    public static short TERRAIN_QUANTANIUM_ID = 131;
    public static short TERRAIN_PLEXTANIUM_ID = 132;
    public static short TERRAIN_ORANGUTANIUM_ID = 133;
    public static short TERRAIN_SUCCUMITE_ID = 134;
    public static short TERRAIN_CENOMITE_ID = 135;
    public static short TERRAIN_AWESOMITE_ID = 136;
    public static short TERRAIN_VAPPECIDE_ID = 137;
    public static short TERRAIN_MARS_TOP = 138;
    public static short TERRAIN_MARS_ROCK = 139;
    public static short TERRAIN_MARS_DIRT = 140;
    public static short TERRAIN_MARS_TOP_ROCK = 141;
    public static short TERRAIN_EXTRANIUM_ID = 72;
    public static short TERRAIN_ROCK_ID = 73;
    public static short TERRAIN_SAND_ID = 74;
    public static short TERRAIN_EARTH_TOP_DIRT = 82;
    public static short TERRAIN_EARTH_TOP_ROCK = 83;
    public static short TERRAIN_TREE_TRUNK_ID = 84;
    public static short TERRAIN_TREE_LEAF_ID = 85;
    public static short TERRAIN_WATER = 86;
    public static short TERRAIN_DIRT_ID = 87;
    public static short DOCKING_ENHANCER_ID = 88;
    public static short TERRAIN_CACTUS_ID = 89;
    public static short TERRAIN_PURPLE_ALIEN_TOP = 90;
    public static short TERRAIN_PURPLE_ALIEN_ROCK = 91;
    public static short TERRAIN_PURPLE_ALIEN_VINE = 92;
    public static short TERRAIN_GRASS_SPRITE = 93;
    public static short PLAYER_SPAWN_MODULE = 94;
    public static short TERRAIN_BROWNWEED_SPRITE = 95;
    public static short TERRAIN_MARSTENTACLES_SPRITE = 96;
    public static short TERRAIN_ALIENVINE_SPRITE = 97;
    public static short TERRAIN_GRASSFLOWERS_SPRITE = 98;
    public static short TERRAIN_LONGWEED_SPRITE = 99;
    public static short TERRAIN_TALLSHROOM_SPRITE = 100;
    public static short TERRAIN_PURSPIRE_SPRITE = 101;
    public static short TERRAIN_TALLGRASSFLOWERS_SPRITE = 102;
    public static short TERRAIN_MINICACTUS_SPRITE = 103;
    public static short TERRAIN_REDSHROOM_SPRITE = 104;
    public static short TERRAIN_PURPTACLES_SPRITE = 105;
    public static short TERRAIN_TALLFLOWERS_SPRITE = 106;
    public static short TERRAIN_ROCK_SPRITE = 107;
    public static short TERRAIN_ALIENFLOWERS_SPRITE = 108;
    public static short TERRAIN_YHOLE_SPRITE = 109;
    public static short TERRAIN_M1L2_ID = 142;
    public static short TERRAIN_M1L3_ID = 143;
    public static short TERRAIN_M1L4_ID = 144;
    public static short TERRAIN_M1L5_ID = 145;
    public static short TERRAIN_M2L2_ID = 146;
    public static short TERRAIN_M2L3_ID = 147;
    public static short TERRAIN_M2L4_ID = 148;
    public static short TERRAIN_M2L5_ID = 149;
    public static short TERRAIN_M3L2_ID = 150;
    public static short TERRAIN_M3L3_ID = 151;
    public static short TERRAIN_M3L4_ID = 152;
    public static short TERRAIN_M3L5_ID = 153;
    public static short TERRAIN_M4L2_ID = 154;
    public static short TERRAIN_M4L3_ID = 155;
    public static short TERRAIN_M4L4_ID = 156;
    public static short TERRAIN_M4L5_ID = 157;
    public static short TERRAIN_M5L2_ID = 158;
    public static short TERRAIN_M5L3_ID = 159;
    public static short TERRAIN_M5L4_ID = 160;
    public static short TERRAIN_M5L5_ID = 161;
    public static short TERRAIN_M6L2_ID = 162;
    public static short TERRAIN_M6L3_ID = 163;
    public static short TERRAIN_M6L4_ID = 164;
    public static short TERRAIN_M6L5_ID = 165;
    public static short TERRAIN_M7L2_ID = 166;
    public static short TERRAIN_M7L3_ID = 167;
    public static short TERRAIN_M7L4_ID = 168;
    public static short TERRAIN_M7L5_ID = 169;
    public static short TERRAIN_M8L2_ID = 170;
    public static short TERRAIN_M8L3_ID = 171;
    public static short TERRAIN_M8L4_ID = 172;
    public static short TERRAIN_M8L5_ID = 173;
    public static short TERRAIN_M9L2_ID = 174;
    public static short TERRAIN_M9L3_ID = 175;
    public static short TERRAIN_M9L4_ID = 176;
    public static short TERRAIN_M9L5_ID = 177;
    public static short TERRAIN_M10L2_ID = 178;
    public static short TERRAIN_M10L3_ID = 179;
    public static short TERRAIN_M10L4_ID = 180;
    public static short TERRAIN_M10L5_ID = 181;
    public static short TERRAIN_M11L2_ID = 182;
    public static short TERRAIN_M11L3_ID = 183;
    public static short TERRAIN_M11L4_ID = 184;
    public static short TERRAIN_M11L5_ID = 185;
    public static short TERRAIN_M12L2_ID = 186;
    public static short TERRAIN_M12L3_ID = 187;
    public static short TERRAIN_M12L4_ID = 188;
    public static short TERRAIN_M12L5_ID = 189;
    public static short TERRAIN_M13L2_ID = 190;
    public static short TERRAIN_M13L3_ID = 191;
    public static short TERRAIN_M13L4_ID = 192;
    public static short TERRAIN_M13L5_ID = 193;
    public static short TERRAIN_M14L2_ID = 194;
    public static short TERRAIN_M14L3_ID = 195;
    public static short TERRAIN_M14L4_ID = 196;
    public static short TERRAIN_M14L5_ID = 197;
    public static short TERRAIN_M15L2_ID = 198;
    public static short TERRAIN_M15L3_ID = 199;
    public static short TERRAIN_M15L4_ID = 200;
    public static short TERRAIN_M15L5_ID = 201;
    public static short TERRAIN_M16L2_ID = 202;
    public static short TERRAIN_M16L3_ID = 203;
    public static short TERRAIN_M16L4_ID = 204;
    public static short TERRAIN_M16L5_ID = 205;
    public static short TERRAIN_NEGACIDE_ID = 206;
    public static short TERRAIN_QUANTACIDE_ID = 207;
    public static short TERRAIN_NEGAGATE_ID = 208;
    public static short TERRAIN_METATE_ID = 209;
    public static short TERRAIN_INSANIUM_ID = 210;
    public static short FACTORY_INPUT_ID = 211;
    public static short FACTORY_INPUT_ENH_ID = 212;
    public static short FACTORY_POWER_CELL_ID = 213;
    public static short FACTORY_POWER_CELL_ENH_ID = 214;
    public static short FACTORY_POWER_COIL_ID = 215;
    public static short FACTORY_POWER_COIL_ENH_ID = 216;
    public static short FACTORY_POWER_BLOCK_ID = 217;
    public static short FACTORY_POWER_BLOCK_ENH_ID = 218;
    public static short POWER_CELL_ID = 219;
    public static short POWER_COIL_ID = 220;
    public static short UNUSED_TEST = 221;
    public static short FACTORY_PARTICLE_PRESS = 222;
    public static short MAN_SD1000_CAP = 223;
    public static short MAN_SD2000_CAP = 224;
    public static short MAN_SD3000_CAP = 225;
    public static short MAN_SD1000_FLUX = 226;
    public static short MAN_SD2000_FLUX = 227;
    public static short MAN_SD3000_FLUX = 228;
    public static short MAN_SD1000_MICRO = 229;
    public static short MAN_SD2000_MICRO = 230;
    public static short MAN_SD3000_MICRO = 231;
    public static short MAN_SD1000_DELTA = 232;
    public static short MAN_SD2000_DELTA = 233;
    public static short MAN_SD3000_DELTA = 234;
    public static short MAN_SD1000_MEM = 235;
    public static short MAN_SD2000_MEM = 236;
    public static short MAN_SD3000_MEM = 237;
    public static short MAN_SDPROTON = 238;
    public static short MAN_RED = 239;
    public static short MAN_PURP = 240;
    public static short MAN_BROWN = 241;
    public static short MAN_GREEN = 242;
    public static short MAN_YELLOW = 243;
    public static short MAN_BLACK = 244;
    public static short MAN_WHITE = 245;
    public static short MAN_BLUE = 246;
    public static short MAN_P1000B = 247;
    public static short MAN_P2000B = 248;
    public static short MAN_P3000B = 249;
    public static short MAN_P10000A = 250;
    public static short MAN_P20000A = 251;
    public static short MAN_P30000A = 252;
    public static short MAN_P40000A = 253;
    public static short MAN_YHOLE_NUC = 254;
    public static short FACTORY_SD10000 = 255;
    public static short FACTORY_SD20000 = 256;
    public static short FACTORY_SD30000 = 257;
    public static short FACTORY_SDADV = 258;
    public static short FACTORY_SD1000 = 259;
    public static short FACTORY_SD2000 = 260;
    public static short FACTORY_SD3000 = 261;
    public static short FACTORY_MINERAL = 262;
    public static short POWERHULL_COLOR_GREY = 263;
    public static short POWERHULL_COLOR_BLACK = 264;
    public static short POWERHULL_COLOR_RED = 265;
    public static short POWERHULL_COLOR_PURPLE = 266;
    public static short POWERHULL_COLOR_BLUE = 267;
    public static short POWERHULL_COLOR_GREEN = 268;
    public static short POWERHULL_COLOR_BROWN = 269;
    public static short POWERHULL_COLOR_GOLD = 270;
    public static short POWERHULL_COLOR_WHITE = 271;
    public static short MAN_GLASS_BOTTLE = 272;
    public static short MAN_SCIENCE_BOTTLE = 273;
    public static short TERRAIN_ICEPLANET_SURFACE = 274;
    public static short TERRAIN_ICEPLANET_ROCK = 275;
    public static short TERRAIN_ICEPLANET_WOOD = 276;
    public static short TERRAIN_ICEPLANET_LEAVES = 277;
    public static short TERRAIN_ICEPLANET_SPIKE_SPRITE = 278;
    public static short TERRAIN_ICEPLANET_ICECRAG_SPRITE = 279;
    public static short TERRAIN_ICEPLANET_ICECORAL_SPRITE = 280;
    public static short TERRAIN_ICEPLANET_ICEGRASS_SPRITE = 281;
    public static short LIGHT_RED = 282;
    public static short LIGHT_BLUE = 283;
    public static short LIGHT_GREEN = 284;
    public static short LIGHT_YELLOW = 285;
    public static short TERRAIN_ICEPLANET_CRYSTAL = 286;
    public static short TERRAIN_REDWOOD = 287;
    public static short TERRAIN_REDWOOD_LEAVES = 288;
    public static short FIXED_DOCK_ID = 289;
    public static short FIXED_DOCK_ID_ENHANCER = 290;
    public static short FACTION_BLOCK = 291;
    public static short FACTION_HUB_BLOCK = 292;
    public static short HULL_COLOR_WEDGE_GREY_ID = 293;
    public static short HULL_COLOR_WEDGE_PURPLE_ID = 294;
    public static short HULL_COLOR_WEDGE_BROWN_ID = 295;
    public static short HULL_COLOR_WEDGE_BLACK_ID = 296;
    public static short HULL_COLOR_WEDGE_RED_ID = 297;
    public static short HULL_COLOR_WEDGE_BLUE_ID = 298;
    public static short HULL_COLOR_WEDGE_GREEN_ID = 299;
    public static short HULL_COLOR_WEDGE_YELLOW_ID = 300;
    public static short HULL_COLOR_WEDGE_WHITE_ID = 301;
    public static short HULL_COLOR_CORNER_GREY_ID = 302;
    public static short HULL_COLOR_CORNER_PURPLE_ID = 303;
    public static short HULL_COLOR_CORNER_BROWN_ID = 304;
    public static short HULL_COLOR_CORNER_BLACK_ID = 305;
    public static short HULL_COLOR_CORNER_RED_ID = 306;
    public static short HULL_COLOR_CORNER_BLUE_ID = 307;
    public static short HULL_COLOR_CORNER_GREEN_ID = 308;
    public static short HULL_COLOR_CORNER_YELLOW_ID = 309;
    public static short HULL_COLOR_CORNER_WHITE_ID = 310;
    // PENTA
    public static short HULL_COLOR_PENTA_GREY_ID = 357;
    public static short HULL_COLOR_PENTA_PURPLE_ID = 387;
    public static short HULL_COLOR_PENTA_BROWN_ID = 403;
    public static short HULL_COLOR_PENTA_BLACK_ID = 385;
    public static short HULL_COLOR_PENTA_RED_ID = 386;
    public static short HULL_COLOR_PENTA_BLUE_ID = 388;
    public static short HULL_COLOR_PENTA_GREEN_ID = 389;
    public static short HULL_COLOR_PENTA_YELLOW_ID = 391;
    public static short HULL_COLOR_PENTA_WHITE_ID = 392;
    // TETRA
    public static short HULL_COLOR_TETRA_GREY_ID = 348;
    public static short HULL_COLOR_TETRA_PURPLE_ID = 395;
    public static short HULL_COLOR_TETRA_BROWN_ID = 404;
    public static short HULL_COLOR_TETRA_BLACK_ID = 393;
    public static short HULL_COLOR_TETRA_RED_ID = 394;
    public static short HULL_COLOR_TETRA_BLUE_ID = 396;
    public static short HULL_COLOR_TETRA_GREEN_ID = 397;
    public static short HULL_COLOR_TETRA_YELLOW_ID = 398;
    public static short HULL_COLOR_TETRA_WHITE_ID = 400;

    public static short POWERHULL_COLOR_WEDGE_GREY = 311;
    public static short POWERHULL_COLOR_WEDGE_BLACK = 312;
    public static short POWERHULL_COLOR_WEDGE_RED = 313;
    public static short POWERHULL_COLOR_WEDGE_PURPLE = 314;
    public static short POWERHULL_COLOR_WEDGE_BLUE = 315;
    public static short POWERHULL_COLOR_WEDGE_GREEN = 316;
    public static short POWERHULL_COLOR_WEDGE_BROWN = 317;
    public static short POWERHULL_COLOR_WEDGE_GOLD = 318;
    public static short POWERHULL_COLOR_WEDGE_WHITE = 319;
    public static short POWERHULL_COLOR_CORNER_GREY = 320;
    public static short POWERHULL_COLOR_CORNER_BLACK = 321;
    public static short POWERHULL_COLOR_CORNER_RED = 322;
    public static short POWERHULL_COLOR_CORNER_PURPLE = 323;
    public static short POWERHULL_COLOR_CORNER_BLUE = 324;
    public static short POWERHULL_COLOR_CORNER_GREEN = 325;
    public static short POWERHULL_COLOR_CORNER_BROWN = 326;
    public static short POWERHULL_COLOR_CORNER_GOLD = 327;
    public static short POWERHULL_COLOR_CORNER_WHITE = 328;
    // HARD PENTA
    public static short POWERHULL_COLOR_PENTA_GREY = 401;
    public static short POWERHULL_COLOR_PENTA_BLACK = 369;
    public static short POWERHULL_COLOR_PENTA_RED = 370;
    public static short POWERHULL_COLOR_PENTA_PURPLE = 371;
    public static short POWERHULL_COLOR_PENTA_BLUE = 372;
    public static short POWERHULL_COLOR_PENTA_GREEN = 373;
    public static short POWERHULL_COLOR_PENTA_BROWN = 374;
    public static short POWERHULL_COLOR_PENTA_GOLD = 375;
    public static short POWERHULL_COLOR_PENTA_WHITE = 376;
    // HARD TETRA
    public static short POWERHULL_COLOR_TETRA_GREY = 402;
    public static short POWERHULL_COLOR_TETRA_BLACK = 377;
    public static short POWERHULL_COLOR_TETRA_RED = 378;
    public static short POWERHULL_COLOR_TETRA_PURPLE = 379;
    public static short POWERHULL_COLOR_TETRA_BLUE = 380;
    public static short POWERHULL_COLOR_TETRA_GREEN = 381;
    public static short POWERHULL_COLOR_TETRA_BROWN = 382;
    public static short POWERHULL_COLOR_TETRA_GOLD = 383;
    public static short POWERHULL_COLOR_TETRA_WHITE = 384;

    public static short GLASS_WEDGE_ID = 329;
    public static short GLASS_CORNER_ID = 330;

    public static short GLASS_PENTA_ID = 367;
    public static short GLASS_TETRA_ID = 368;

    public static short POWER_HOLDER_ID = 331;
    public static short POWER_DRAIN_BEAM_COMPUTER = 332;
    public static short POWER_DRAIN_BEAM_MODULE = 333;
    public static short POWER_SUPPLY_BEAM_COMPUTER = 334;
    public static short POWER_SUPPLY_BEAM_MODULE = 335;
    public static short DECORATIVE_PANEL_1 = 336;
    public static short DECORATIVE_PANEL_2 = 337;
    public static short DECORATIVE_PANEL_3 = 338;
    public static short DECORATIVE_PANEL_4 = 339;
    public static short LIGHT_BULB_YELLOW = 340;

    public static final Map<Short, String> BLOCK_NAMES = new HashMap<>();

    public static final int HULL_COLORS = 0;
    public static final int WEDGE_COLORS = 1;
    public static final int CORNER_COLORS = 2;
    public static final int PENTA_COLORS = 3;
    public static final int TETRA_COLORS = 4;
    public static final int POWERHULL_COLORS = 5;
    public static final int POWERWEDGE_COLORS = 6;
    public static final int POWERCORNER_COLORS = 7;
    public static final int POWERPENTA_COLORS = 8;
    public static final int POWERTETRA_COLORS = 9;

    public static short[][] HULL_COLOR_MAP = {
        { // hulls
            HULL_COLOR_GREY_ID,
            HULL_COLOR_PURPLE_ID,
            HULL_COLOR_BROWN_ID,
            HULL_COLOR_BLACK_ID,
            HULL_COLOR_RED_ID,
            HULL_COLOR_BLUE_ID,
            HULL_COLOR_GREEN_ID,
            HULL_COLOR_YELLOW_ID,
            HULL_COLOR_WHITE_ID,},
        { // wedges
            HULL_COLOR_WEDGE_GREY_ID,
            HULL_COLOR_WEDGE_PURPLE_ID,
            HULL_COLOR_WEDGE_BROWN_ID,
            HULL_COLOR_WEDGE_BLACK_ID,
            HULL_COLOR_WEDGE_RED_ID,
            HULL_COLOR_WEDGE_BLUE_ID,
            HULL_COLOR_WEDGE_GREEN_ID,
            HULL_COLOR_WEDGE_YELLOW_ID,
            HULL_COLOR_WEDGE_WHITE_ID,},
        { // corners
            HULL_COLOR_CORNER_GREY_ID,
            HULL_COLOR_CORNER_PURPLE_ID,
            HULL_COLOR_CORNER_BROWN_ID,
            HULL_COLOR_CORNER_BLACK_ID,
            HULL_COLOR_CORNER_RED_ID,
            HULL_COLOR_CORNER_BLUE_ID,
            HULL_COLOR_CORNER_GREEN_ID,
            HULL_COLOR_CORNER_YELLOW_ID,
            HULL_COLOR_CORNER_WHITE_ID,},
        { // PENTA
            HULL_COLOR_PENTA_GREY_ID,
            HULL_COLOR_PENTA_PURPLE_ID,
            HULL_COLOR_PENTA_BROWN_ID,
            HULL_COLOR_PENTA_BLACK_ID,
            HULL_COLOR_PENTA_RED_ID,
            HULL_COLOR_PENTA_BLUE_ID,
            HULL_COLOR_PENTA_GREEN_ID,
            HULL_COLOR_PENTA_YELLOW_ID,
            HULL_COLOR_PENTA_WHITE_ID,},
        { // TETRA
            HULL_COLOR_TETRA_GREY_ID,
            HULL_COLOR_TETRA_PURPLE_ID,
            HULL_COLOR_TETRA_BROWN_ID,
            HULL_COLOR_TETRA_BLACK_ID,
            HULL_COLOR_TETRA_RED_ID,
            HULL_COLOR_TETRA_BLUE_ID,
            HULL_COLOR_TETRA_GREEN_ID,
            HULL_COLOR_TETRA_YELLOW_ID,
            HULL_COLOR_TETRA_WHITE_ID,},
        { // power hulls
            POWERHULL_COLOR_GREY,
            POWERHULL_COLOR_PURPLE,
            POWERHULL_COLOR_BROWN,
            POWERHULL_COLOR_BLACK,
            POWERHULL_COLOR_RED,
            POWERHULL_COLOR_BLUE,
            POWERHULL_COLOR_GREEN,
            POWERHULL_COLOR_GOLD,
            POWERHULL_COLOR_WHITE,},
        { // power wedges
            POWERHULL_COLOR_WEDGE_GREY,
            POWERHULL_COLOR_WEDGE_PURPLE,
            POWERHULL_COLOR_WEDGE_BROWN,
            POWERHULL_COLOR_WEDGE_BLACK,
            POWERHULL_COLOR_WEDGE_RED,
            POWERHULL_COLOR_WEDGE_BLUE,
            POWERHULL_COLOR_WEDGE_GREEN,
            POWERHULL_COLOR_WEDGE_GOLD,
            POWERHULL_COLOR_WEDGE_WHITE,},
        { // power corners
            POWERHULL_COLOR_CORNER_GREY,
            POWERHULL_COLOR_CORNER_PURPLE,
            POWERHULL_COLOR_CORNER_BROWN,
            POWERHULL_COLOR_CORNER_BLACK,
            POWERHULL_COLOR_CORNER_RED,
            POWERHULL_COLOR_CORNER_BLUE,
            POWERHULL_COLOR_CORNER_GREEN,
            POWERHULL_COLOR_CORNER_GOLD,
            POWERHULL_COLOR_CORNER_WHITE,},
        { // power PENTA
            POWERHULL_COLOR_PENTA_GREY,
            POWERHULL_COLOR_PENTA_PURPLE,
            POWERHULL_COLOR_PENTA_BROWN,
            POWERHULL_COLOR_PENTA_BLACK,
            POWERHULL_COLOR_PENTA_RED,
            POWERHULL_COLOR_PENTA_BLUE,
            POWERHULL_COLOR_PENTA_GREEN,
            POWERHULL_COLOR_PENTA_GOLD,
            POWERHULL_COLOR_PENTA_WHITE,},
        { // power TETRA
            POWERHULL_COLOR_TETRA_GREY,
            POWERHULL_COLOR_TETRA_PURPLE,
            POWERHULL_COLOR_TETRA_BROWN,
            POWERHULL_COLOR_TETRA_BLACK,
            POWERHULL_COLOR_TETRA_RED,
            POWERHULL_COLOR_TETRA_BLUE,
            POWERHULL_COLOR_TETRA_GREEN,
            POWERHULL_COLOR_TETRA_GOLD,
            POWERHULL_COLOR_TETRA_WHITE,},};

    private static int indexOf(short[] array, short val) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isHull(short blockID) {
        return indexOf(HULL_COLOR_MAP[HULL_COLORS], blockID) >= 0;
    }

    public static boolean isWedge(short blockID) {
        return indexOf(HULL_COLOR_MAP[WEDGE_COLORS], blockID) >= 0;
    }

    public static boolean isCorner(short blockID) {
        return indexOf(HULL_COLOR_MAP[CORNER_COLORS], blockID) >= 0;
    }

    public static boolean isPenta(short blockID) {
        return indexOf(HULL_COLOR_MAP[PENTA_COLORS], blockID) >= 0;
    }

    public static boolean isTetra(short blockID) {
        return indexOf(HULL_COLOR_MAP[TETRA_COLORS], blockID) >= 0;
    }

    public static boolean isPowerHull(short blockID) {
        return indexOf(HULL_COLOR_MAP[POWERHULL_COLORS], blockID) >= 0;
    }

    public static boolean isPowerWedge(short blockID) {
        return indexOf(HULL_COLOR_MAP[POWERWEDGE_COLORS], blockID) >= 0;
    }

    public static boolean isPowerCorner(short blockID) {
        return indexOf(HULL_COLOR_MAP[POWERCORNER_COLORS], blockID) >= 0;
    }

    public static boolean isPowerPenta(short blockID) {
        return indexOf(HULL_COLOR_MAP[POWERPENTA_COLORS], blockID) >= 0;
    }

    public static boolean isPowerTetra(short blockID) {
        return indexOf(HULL_COLOR_MAP[POWERTETRA_COLORS], blockID) >= 0;
    }

    public static short getColoredBlock(short blockID, short colorID) {
        int idx = indexOf(HULL_COLOR_MAP[HULL_COLORS], colorID);
        if (idx < 0) {
            throw new IllegalArgumentException("Color must be a hull ID: " + colorID);
        }
        short newBlockID = -1;
        if (isHull(blockID)) {
            newBlockID = HULL_COLOR_MAP[HULL_COLORS][idx];
        } else if (isWedge(blockID)) {
            newBlockID = HULL_COLOR_MAP[WEDGE_COLORS][idx];
        } else if (isCorner(blockID)) {
            newBlockID = HULL_COLOR_MAP[CORNER_COLORS][idx];
        } else if (isPenta(blockID)) {
            newBlockID = HULL_COLOR_MAP[PENTA_COLORS][idx];
        } else if (isTetra(blockID)) {
            newBlockID = HULL_COLOR_MAP[TETRA_COLORS][idx];
        } else if (isPowerHull(blockID)) {
            newBlockID = HULL_COLOR_MAP[POWERHULL_COLORS][idx];
        } else if (isPowerWedge(blockID)) {
            newBlockID = HULL_COLOR_MAP[POWERWEDGE_COLORS][idx];
        } else if (isPowerCorner(blockID)) {
            newBlockID = HULL_COLOR_MAP[POWERCORNER_COLORS][idx];
        } else if (isPowerPenta(blockID)) {
            newBlockID = HULL_COLOR_MAP[POWERPENTA_COLORS][idx];
        } else if (isPowerTetra(blockID)) {
            newBlockID = HULL_COLOR_MAP[POWERTETRA_COLORS][idx];
        }
        return newBlockID;
    }

    public static short getColor(short blockID) {
        int idx = -1;
        if (isHull(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[HULL_COLORS], blockID);
        } else if (isWedge(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[WEDGE_COLORS], blockID);
        } else if (isCorner(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[CORNER_COLORS], blockID);
        } else if (isPenta(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[PENTA_COLORS], blockID);
        } else if (isTetra(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[TETRA_COLORS], blockID);
        } else if (isPowerHull(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[POWERHULL_COLORS], blockID);
        } else if (isPowerWedge(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[POWERWEDGE_COLORS], blockID);
        } else if (isPowerCorner(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[POWERCORNER_COLORS], blockID);
        } else if (isPowerPenta(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[POWERPENTA_COLORS], blockID);
        } else if (isPowerTetra(blockID)) {
            idx = indexOf(HULL_COLOR_MAP[POWERTETRA_COLORS], blockID);
        }
        if (idx < 0) {
            return -1;
        }
        return HULL_COLOR_MAP[HULL_COLORS][idx];
    }

    public static short getPoweredBlock(short blockID) {
        int idx = indexOf(HULL_COLOR_MAP[HULL_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[POWERHULL_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[WEDGE_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[POWERWEDGE_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[CORNER_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[POWERCORNER_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[PENTA_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[POWERPENTA_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[TETRA_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[POWERTETRA_COLORS][idx];
        }
        return -1;
    }

    public static short getUnPoweredBlock(short blockID) {
        int idx = indexOf(HULL_COLOR_MAP[POWERHULL_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[HULL_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[POWERWEDGE_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[WEDGE_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[POWERCORNER_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[CORNER_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[POWERPENTA_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[PENTA_COLORS][idx];
        }
        idx = indexOf(HULL_COLOR_MAP[POWERTETRA_COLORS], blockID);
        if (idx >= 0) {
            return HULL_COLOR_MAP[TETRA_COLORS][idx];
        }
        return -1;
    }

    public static boolean isAnyHull(short blockID) {
        if (isHull(blockID) || isCorner(blockID) || isWedge(blockID)
                || isPenta(blockID) || isTetra(blockID) || isPowerHull(blockID)
                || isPowerCorner(blockID) || isPowerWedge(blockID) || isPowerPenta(blockID)
                || isPowerTetra(blockID)) {
            return true;
        }
        return false;
    }

    public static Map<Short, Short> CONTROLLER_IDS = new HashMap<>();

    static {
        CONTROLLER_IDS.put(SALVAGE_CONTROLLER_ID, SALVAGE_ID);
        CONTROLLER_IDS.put(WEAPON_CONTROLLER_ID, WEAPON_ID);
        CONTROLLER_IDS.put(MISSILE_DUMB_CONTROLLER_ID, MISSILE_DUMB_ID);
        CONTROLLER_IDS.put(MISSILE_HEAT_CONTROLLER_ID, MISSILE_HEAT_ID);
        CONTROLLER_IDS.put(MISSILE_FAFO_CONTROLLER_ID, MISSILE_FAFO_ID);
        CONTROLLER_IDS.put(REPAIR_CONTROLLER_ID, REPAIR_ID);
        CONTROLLER_IDS.put(POWER_COIL_ID, POWER_ID);
    }

    public static boolean isController(short id) {
        return CONTROLLER_IDS.containsKey(id);
    }

    public static boolean isControllerForBlock(short controllerID, short blockID) {
        return CONTROLLER_IDS.containsKey(controllerID) && (blockID == CONTROLLER_IDS.get(controllerID));
    }

    public static boolean isAnyWedge(short id) {
        return isWedge(id) || isPowerWedge(id) || (id == GLASS_WEDGE_ID);
    }

    public static boolean isAnyCorner(short id) {
        return isCorner(id) || isPowerCorner(id) || (id == GLASS_CORNER_ID);
    }

    public static boolean isAnyPenta(short id) {
        return isPenta(id) || isPowerPenta(id) || (id == GLASS_PENTA_ID);
    }

    public static boolean isAnyTetra(short id) {
        return isTetra(id) || isPowerTetra(id) || (id == GLASS_TETRA_ID);
    }

    public static Block colorize(Block oldBlock, short color) {
        if (!isAnyHull(oldBlock.getBlockID())) {
            return oldBlock;
        }
        color = getColor(color);
        short newID = getColoredBlock(oldBlock.getBlockID(), color);
        Block newBlock = new Block(newID);
        newBlock.setOrientation(oldBlock.getOrientation());
        return newBlock;
    }
}
