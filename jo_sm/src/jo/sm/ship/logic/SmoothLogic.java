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
package jo.sm.ship.logic;

import java.util.Iterator;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderTile;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class SmoothLogic {

    public static final int EXTERIOR = 1;
    public static final int INTERIOR = 2;
    public static final int EVERYWHERE = EXTERIOR + INTERIOR;

    public static final int WEDGES = 1;
    public static final int CORNERS = 2;
    public static final int EVERYTHING = WEDGES + CORNERS;

    private static final Point3i[] DELTAS = {
        new Point3i(1, 0, 0),
        new Point3i(-1, 0, 0),
        new Point3i(0, 1, 0),
        new Point3i(0, -1, 0),
        new Point3i(0, 0, 1),
        new Point3i(0, 0, -1),};

    public static void smooth(SparseMatrix<Block> grid, int scope, int type, StarMade sm, IPluginCallback cb) {
        Set<Point3i> exterior = HullLogic.findExterior(grid, cb);
        boolean[] edges = new boolean[6];
        cb.setStatus("Smoothing");
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null)) {
            lower.set(sm.getSelectedLower());
            upper.set(sm.getSelectedUpper());
        } else {
            grid.getBounds(lower, upper);
            lower.x--;
            lower.y--;
            lower.z--;
            upper.x++;
            upper.y++;
            upper.z++;
        }
        cb.startTask((upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z + 1));
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext();) {
            cb.workTask(1);
            Point3i p = i.next();
            if (grid.contains(p)) {
                continue;
            }
            if (exterior.contains(p)) {
                if ((scope & EXTERIOR) == 0) {
                    continue;
                }
            } else {
                if ((scope & INTERIOR) == 0) {
                    continue;
                }
            }
            int tot = 0;
            for (int j = 0; j < edges.length; j++) {
                edges[j] = isEdge(grid, p, DELTAS[j]);
                if (edges[j]) {
                    tot++;
                }
            }
            if ((tot == 2) && ((type & WEDGES) != 0)) {
                doWedge(grid, p, edges);
            }
            if ((tot == 3) && ((type & CORNERS) != 0)) {
                doCorner(grid, p, edges);
            }
        }
        cb.endTask();
    }

    private static void doCorner(SparseMatrix<Block> grid, Point3i p, boolean[] edges) {
        int ori = -1;
        if (edges[RenderTile.XM]) {
            if (edges[RenderTile.YM]) {
                if (edges[RenderTile.ZM]) {
                    ori = 1;
                } else if (edges[RenderTile.ZP]) {
                    ori = 0;
                }
            } else if (edges[RenderTile.YP]) {
                if (edges[RenderTile.ZM]) {
                    ori = 5;
                } else if (edges[RenderTile.ZP]) {
                    ori = 4;
                }
            }
        } else if (edges[RenderTile.XP]) {
            if (edges[RenderTile.YM]) {
                if (edges[RenderTile.ZM]) {
                    ori = 2;
                } else if (edges[RenderTile.ZP]) {
                    ori = 3;
                }
            } else if (edges[RenderTile.YP]) {
                if (edges[RenderTile.ZM]) {
                    ori = 6;
                } else if (edges[RenderTile.ZP]) {
                    ori = 7;
                }
            }
        }
        if (ori < 0) {
            return;
        }
        Block b = new Block();
        b.setActive(false);
        b.setBlockID(calculateCornerType(grid, p, edges));
        b.setOrientation((short) ori);
        grid.set(p, b);
    }

    private static void doWedge(SparseMatrix<Block> grid, Point3i p, boolean[] edges) {
        int ori = -1;
        if (edges[RenderTile.XM]) {
            if (edges[RenderTile.YM]) {
                ori = 3;
            } else if (edges[RenderTile.YP]) {
                ori = 5;
            } else if (edges[RenderTile.ZM]) {
                ori = 13;
            } else if (edges[RenderTile.ZP]) {
                ori = 8;
            }
        } else if (edges[RenderTile.XP]) {
            if (edges[RenderTile.YM]) {
                ori = 1;
            } else if (edges[RenderTile.YP]) {
                ori = 7;
            } else if (edges[RenderTile.ZM]) {
                ori = 11;
            } else if (edges[RenderTile.ZP]) {
                ori = 10;
            }
        } else if (edges[RenderTile.YM]) {
            if (edges[RenderTile.ZM]) {
                ori = 2;
            } else if (edges[RenderTile.ZP]) {
                ori = 0;
            }
        } else if (edges[RenderTile.YP]) {
            if (edges[RenderTile.ZM]) {
                ori = 6;
            } else if (edges[RenderTile.ZP]) {
                ori = 4;
            }
        }
        if (ori < 0) {
            return;
        }
        Block b = new Block();
        b.setActive(false);
        b.setBlockID(calculateWedgeType(grid, p, edges));
        b.setOrientation((short) ori);
        grid.set(p, b);

    }

    private static short calculateWedgeType(SparseMatrix<Block> grid, Point3i p,
            boolean[] edges) {
        short type1 = -1;
        short type2 = -1;
        for (int i = 0; i < edges.length; i++) {
            if (!edges[i]) {
                continue;
            }
            Point3i p2 = new Point3i();
            p2.add(p, DELTAS[i]);
            Block b = grid.get(p2);
            if (b == null) {
                continue;
            }
            if (type1 == -1) {
                type1 = b.getBlockID();
            } else if (type2 == -1) {
                type2 = b.getBlockID();
                break;
            }
        }
        if (type1 > type2) {
            type1 = type2;
        }
        if (type1 == BlockTypes.HULL_COLOR_GREY_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_GREY_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_PURPLE_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_PURPLE_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BROWN_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_BROWN_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BLACK_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_BLACK_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_RED_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_RED_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BLUE_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_BLUE_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_GREEN_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_GREEN_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_YELLOW_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_YELLOW_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_WHITE_ID) {
            return BlockTypes.HULL_COLOR_WEDGE_WHITE_ID;
        }
        if (type1 == BlockTypes.GLASS_ID) {
            return BlockTypes.GLASS_WEDGE_ID;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GREY) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_GREY;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_PURPLE) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_PURPLE;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BROWN) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_BROWN;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BLACK) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_BLACK;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_RED) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_RED;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BLUE) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_BLUE;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GREEN) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_GREEN;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GOLD) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_GOLD;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_WHITE) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_WHITE;
        }
        return type1;
    }

    private static short calculateCornerType(SparseMatrix<Block> grid, Point3i p,
            boolean[] edges) {
        short type1 = -1;
        short type2 = -1;
        short type3 = -1;
        for (int i = 0; i < edges.length; i++) {
            if (!edges[i]) {
                continue;
            }
            Point3i p2 = new Point3i();
            p2.add(p, DELTAS[i]);
            Block b = grid.get(p2);
            if (b == null) {
                continue;
            }
            if (type1 == -1) {
                type1 = b.getBlockID();
            } else if (type2 == -1) {
                type2 = b.getBlockID();
            } else if (type3 == -1) {
                type3 = b.getBlockID();
                break;
            }
        }
        if (type1 != type2) {
            if (type2 == type3) {
                type1 = type2;
            } else if (type1 != type3) {
                type1 = (short) Math.min(type1, Math.min(type2, type3));
            }
        }
        if (type1 == BlockTypes.HULL_COLOR_GREY_ID) {
            return BlockTypes.HULL_COLOR_CORNER_GREY_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_PURPLE_ID) {
            return BlockTypes.HULL_COLOR_CORNER_PURPLE_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BROWN_ID) {
            return BlockTypes.HULL_COLOR_CORNER_BROWN_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BLACK_ID) {
            return BlockTypes.HULL_COLOR_CORNER_BLACK_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_RED_ID) {
            return BlockTypes.HULL_COLOR_CORNER_RED_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_BLUE_ID) {
            return BlockTypes.HULL_COLOR_CORNER_BLUE_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_GREEN_ID) {
            return BlockTypes.HULL_COLOR_CORNER_GREEN_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_YELLOW_ID) {
            return BlockTypes.HULL_COLOR_CORNER_YELLOW_ID;
        }
        if (type1 == BlockTypes.HULL_COLOR_WHITE_ID) {
            return BlockTypes.HULL_COLOR_CORNER_WHITE_ID;
        }
        if (type1 == BlockTypes.GLASS_ID) {
            return BlockTypes.GLASS_CORNER_ID;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GREY) {
            return BlockTypes.POWERHULL_COLOR_CORNER_GREY;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_PURPLE) {
            return BlockTypes.POWERHULL_COLOR_WEDGE_PURPLE;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BROWN) {
            return BlockTypes.POWERHULL_COLOR_CORNER_BROWN;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BLACK) {
            return BlockTypes.POWERHULL_COLOR_CORNER_BLACK;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_RED) {
            return BlockTypes.POWERHULL_COLOR_CORNER_RED;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_BLUE) {
            return BlockTypes.POWERHULL_COLOR_CORNER_BLUE;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GREEN) {
            return BlockTypes.POWERHULL_COLOR_CORNER_GREEN;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_GOLD) {
            return BlockTypes.POWERHULL_COLOR_CORNER_GOLD;
        }
        if (type1 == BlockTypes.POWERHULL_COLOR_WHITE) {
            return BlockTypes.POWERHULL_COLOR_CORNER_WHITE;
        }
        return type1;
    }

    private static boolean isEdge(SparseMatrix<Block> grid, Point3i p, Point3i d) {
        Point3i p2 = new Point3i();
        p2.add(p, d);
        if (!grid.contains(p2)) {
            return false;
        }
        short type = grid.get(p2).getBlockID();
        if (BlockTypes.isWedge(type) || BlockTypes.isPowerWedge(type)) {
            return false;
        }
        return BlockTypes.isHull(type) || BlockTypes.isPowerHull(type) || (type == BlockTypes.GLASS_ID);
    }
}
