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
public class UndulatingPlugin implements IBlocksPlugin {

    private static final int GRID = 8;

    public static final String NAME = "Shape/Undulating";
    public static final String DESC = "Gently rolling surface";
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
        return new UndulatingParameters();
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
        UndulatingParameters params = (UndulatingParameters) p;
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
        return hParams.grid;
    }

    private void fillGrid(HexParams hparams, UndulatingParameters params, IPluginCallback cb) {
        cb.setStatus("Filling in terrain");
        cb.startTask(hparams.fieldWidth);
        for (int fieldX = 0; fieldX < hparams.fieldWidth - 1; fieldX++) {
            cb.workTask(1);
            for (int fieldY = 0; fieldY < hparams.fieldHeight - 1; fieldY++) {
                fillChunk(fieldX, fieldY, hparams, params);
            }
        }
        cb.endTask();
    }

    private void fillChunk(int fieldX, int fieldY, HexParams hparams, UndulatingParameters params) {
        int[] upperLeft = getFieldPosition(fieldX, fieldY, hparams, params);
        int upperLeftHeight = hparams.field[fieldX][fieldY];
        int upperRightHeight = hparams.field[fieldX + 1][fieldY];
        int lowerLeftHeight = hparams.field[fieldX][fieldY + 1];
        int[] lowerRight = getFieldPosition(fieldX + 1, fieldY + 1, hparams, params);
        int lowerRightHeight = hparams.field[fieldX + 1][fieldY + 1];
        for (int y = upperLeft[1]; y < lowerRight[1]; y++) {
            int leftHeight = (int) MathUtils.interpolate(y, upperLeft[1], lowerRight[1], upperLeftHeight, lowerLeftHeight);
            int rightHeight = (int) MathUtils.interpolate(y, upperLeft[1], lowerRight[1], upperRightHeight, lowerRightHeight);
            for (int x = upperLeft[0]; x < lowerRight[0]; x++) {
                int height = (int) MathUtils.interpolate(x, upperLeft[0], lowerRight[0], leftHeight, rightHeight);
                fillColumn(x, y, height, hparams, params);
            }
        }
    }

    private void fillColumn(int x, int y, int hexHeight, HexParams hparams, UndulatingParameters params) {
        double r = Math.sqrt(x * x + y * y);
        if (r > params.getPlanetRadius()) {
            return; // out of radius
        }
        int columnHeight = (int) MathUtils.interpolate(hexHeight, hparams.elevLow, hparams.elevHigh, 0, params.getPlanetHeight());
        PluginUtils.fill(hparams.grid, x, 0, y, x, columnHeight, y, params.getFillWith(), 0);
    }

    private int[] getFieldPosition(int fieldX, int fieldY, HexParams hparams, UndulatingParameters params) {
        int blockX = fieldX * GRID - params.getPlanetRadius();
        int blockY = fieldY * GRID - params.getPlanetRadius();
        return new int[]{blockX, blockY};
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

    private HexParams makeParams(UndulatingParameters params) {
        HexParams hparams = new HexParams();
        hparams.grid = new SparseMatrix<Block>();
        hparams.fieldWidth = params.getPlanetRadius() * 2 / GRID + 1;
        hparams.fieldHeight = params.getPlanetRadius() * 2 / GRID + 1;
        hparams.field = new int[hparams.fieldWidth][hparams.fieldHeight];
        hparams.fillTarget = hparams.fieldWidth * hparams.fieldHeight * 2;
        hparams.rnd = new Random();
        hparams.fillXY = new int[]{hparams.rnd.nextInt(hparams.fieldWidth), hparams.rnd.nextInt(hparams.fieldHeight)};
        return hparams;
    }

    class HexParams {

        SparseMatrix<Block> grid;
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
