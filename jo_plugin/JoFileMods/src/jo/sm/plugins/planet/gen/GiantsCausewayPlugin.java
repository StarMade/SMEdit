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
package jo.sm.plugins.planet.gen;

import java.util.Random;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.logic.MathUtils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class GiantsCausewayPlugin implements IBlocksPlugin {

    public static final String NAME = "Shape/Giant's Causeway";
    public static final String DESC = "Hexagonal slab surface";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_PLANET, SUBTYPE_GENERATE, 26},};

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getAuthor() {
        return AUTH;
    }

    @Override
    public Object newParameterBean() {
        return new GiantsCausewayParameters();
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        GiantsCausewayParameters params = (GiantsCausewayParameters) p;
        HexParams hParams = makeParams(params);
        cb.setStatus("Generating terrain");
        cb.startTask(hParams.fillTarget);
        while (hParams.fillTarget > 0) {
            fillAndMove(hParams);
            cb.workTask(1);
        }
        cb.endTask();
        findBounds(hParams);
        fillGrid(hParams, params, cb);
        //printGrid(hParams, params);
        return hParams.grid;
    }

    private void fillGrid(HexParams hparams, GiantsCausewayParameters params, IPluginCallback cb) {
        cb.setStatus("Filling in terrain");
        cb.startTask(params.getPlanetRadius() * 2);
        int[] xy = new int[2];
        for (xy[0] = -params.getPlanetRadius(); xy[0] <= params.getPlanetRadius(); xy[0]++) {
            cb.workTask(1);
            for (xy[1] = -params.getPlanetRadius(); xy[1] <= params.getPlanetRadius(); xy[1]++) {
                fillColumn(xy, hparams, params);
            }
        }
        cb.endTask();
    }

    private void fillColumn(int[] blockXY, HexParams hparams, GiantsCausewayParameters params) {
        double r = Math.sqrt(blockXY[0] * blockXY[0] + blockXY[1] * blockXY[1]);
        if (r > params.getPlanetRadius()) {
            return; // out of radius
        }
        int[] hexXY = findNearestHex(blockXY, hparams, params);
        int hexHeight = hparams.field[hexXY[0]][hexXY[1]];
        int columnHeight = (int) MathUtils.interpolate(hexHeight, hparams.elevLow, hparams.elevHigh, 0, params.getPlanetHeight());
        PluginUtils.fill(hparams.grid, blockXY[0], 0, blockXY[1], blockXY[0], columnHeight, blockXY[1], params.getFillWith(), 0);
    }

    private int[] findNearestHex(int[] blockXY, HexParams hparams, GiantsCausewayParameters params) {
        int approxX = (blockXY[0] + params.getPlanetRadius()) / hparams.hexWidth;
        int approxY = (blockXY[1] + params.getPlanetRadius()) / hparams.hexHeight;
        int[] bestXY = new int[]{-1, -1};
        int bestV = 0;
        int[] testXY = new int[2];
        int[] testBlockXY = new int[2];
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                testXY[0] = approxX + x;
                if ((testXY[0] < 0) || (testXY[0] >= hparams.fieldWidth)) {
                    continue;
                }
                testXY[1] = approxY + y;
                if ((testXY[1] < 0) || (testXY[1] >= hparams.fieldHeight)) {
                    continue;
                }
                getHexPosition(testXY, testBlockXY, hparams, params);
                int v = dist(testBlockXY, blockXY);
                if ((bestXY[0] < 0) || (v < bestV)) {
                    bestXY[0] = testXY[0];
                    bestXY[1] = testXY[1];
                    bestV = v;
                }
            }
        }
        return bestXY;
    }

    private void getHexPosition(int[] hexXY, int[] blockXY, HexParams hparams, GiantsCausewayParameters params) {
        blockXY[0] = hexXY[0] * hparams.hexWidth + params.getHexRadius();
        blockXY[1] = hexXY[1] * hparams.hexHeight + hparams.hexHeight / 2;
        if (hexXY[0] % 2 == 1) {
            blockXY[1] += hparams.hexHeight / 2;
        }
        blockXY[0] -= params.getPlanetRadius();
        blockXY[1] -= params.getPlanetRadius();
    }

    private int dist(int[] p1, int[] p2) {
        return (int) Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * (p2[1] - p1[1]));
    }

    private void findBounds(HexParams hparams) {
        hparams.elevHigh = hparams.field[0][0];
        hparams.elevLow = hparams.field[0][0];
        for (int y = 0; y < hparams.fieldHeight; y++) {
            for (int x = 0; x < hparams.fieldWidth; x++) {
                hparams.elevHigh = Math.max(hparams.elevHigh, hparams.field[x][y]);
                hparams.elevLow = Math.min(hparams.elevLow, hparams.field[x][y]);
            }
        }
    }

//    private void printGrid(HexParams hparams, GiantsCausewayParameters params)
//    {
//        int[] fieldXY = new int[2];
//        int[] blockXY = new int[2];
//        for (fieldXY[1] = 0; fieldXY[1] < hparams.fieldHeight; fieldXY[1]++)
//        {
//            for (fieldXY[0] = 0; fieldXY[0] < hparams.fieldWidth; fieldXY[0]++)
//            {
//                getHexPosition(fieldXY, blockXY, hparams, params);
//                System.out.print(blockXY[0]+","+blockXY[1]+"\t");
//            }
//            System.out.println();
//        }
//    }
    private void fillAndMove(HexParams hparams) {
        // fill
        fill(hparams, 0, 0, 8);
        fill(hparams, 0, -1, 4);
        fill(hparams, 0, 1, 4);
        fill(hparams, -1, 0, 4);
        fill(hparams, 1, 0, 4);
        if (hparams.fillXY[0] % 2 == 0) {
            fill(hparams, -1, -1, 4);
            fill(hparams, 1, -1, 4);
        } else {
            fill(hparams, -1, 1, 4);
            fill(hparams, 1, 1, 4);
        }
        fill(hparams, 0, -2, 2);
        fill(hparams, 0, 2, 2);
        fill(hparams, -2, 0, 2);
        fill(hparams, 2, 0, 2);
        fill(hparams, -2, -1, 2);
        fill(hparams, -2, 1, 2);
        fill(hparams, 2, -1, 2);
        fill(hparams, 2, 1, 2);
        if (hparams.fillXY[0] % 2 == 0) {
            fill(hparams, -1, -1, 2);
            fill(hparams, -1, 2, 2);
            fill(hparams, 1, -1, 2);
            fill(hparams, 1, 2, 2);
        } else {
            fill(hparams, -1, -2, 2);
            fill(hparams, -1, 1, 2);
            fill(hparams, 1, -2, 2);
            fill(hparams, 1, 1, 2);
        }
        // move
        switch (hparams.rnd.nextInt(6)) {
            case 0:
                hparams.fillXY[1]--;
                break;
            case 1:
                hparams.fillXY[1]++;
                break;
            case 2:
                hparams.fillXY[0]--;
                break;
            case 3:
                hparams.fillXY[0]++;
                break;
            case 4:
                hparams.fillXY[0]++;
                if (hparams.fillXY[0] % 2 == 0) {
                    hparams.fillXY[1]--;
                } else {
                    hparams.fillXY[1]++;
                }
                break;
            case 5:
                hparams.fillXY[0]--;
                if (hparams.fillXY[0] % 2 == 0) {
                    hparams.fillXY[1]--;
                } else {
                    hparams.fillXY[1]++;
                }
                break;

        }
        normalize(hparams, hparams.fillXY);
        hparams.fillTarget--;
    }

    private void fill(HexParams hparams, int dx, int dy, int amnt) {
        int[] xy = new int[]{hparams.fillXY[0] + dx, hparams.fillXY[1] + dy};
        normalize(hparams, xy);
        hparams.field[xy[0]][xy[1]] += amnt;
    }

    private void normalize(HexParams hparams, int[] xy) {
        if (xy[0] < 0) {
            xy[0] += hparams.fieldWidth;
        } else if (xy[0] >= hparams.fieldWidth) {
            xy[0] -= hparams.fieldWidth;
        }
        if (xy[1] < 0) {
            xy[1] += hparams.fieldHeight;
        } else if (xy[1] >= hparams.fieldHeight) {
            xy[1] -= hparams.fieldHeight;
        }
    }

    private HexParams makeParams(GiantsCausewayParameters params) {
        HexParams hparams = new HexParams();
        hparams.grid = new SparseMatrix<Block>();
        hparams.hexWidth = 3 * params.getHexRadius() / 2;
        hparams.hexHeight = (int) (Math.sqrt(3) * params.getHexRadius());
        hparams.fieldWidth = params.getPlanetRadius() * 2 / hparams.hexWidth + 1;
        hparams.fieldHeight = params.getPlanetRadius() * 2 / hparams.hexHeight + 1;
        hparams.field = new int[hparams.fieldWidth][hparams.fieldHeight];
        hparams.fillTarget = hparams.fieldWidth * hparams.fieldHeight * 2;
        hparams.rnd = new Random();
        hparams.fillXY = new int[]{hparams.rnd.nextInt(hparams.fieldWidth), hparams.rnd.nextInt(hparams.fieldHeight)};
        return hparams;
    }

    class HexParams {

        SparseMatrix<Block> grid;
        int hexWidth;
        int hexHeight;
        int fieldWidth;
        int fieldHeight;
        int[][] field;
        int fillTarget;
        Random rnd;
        int[] fillXY;
        int elevLow;
        int elevHigh;
    }
}
