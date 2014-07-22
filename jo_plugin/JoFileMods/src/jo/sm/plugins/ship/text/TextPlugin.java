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
 */

package jo.sm.plugins.ship.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3i;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 */
public class TextPlugin implements IBlocksPlugin {

    public static final String NAME = "Text";
    public static final String DESC = "Write some text on the hull.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_SHIP, SUBTYPE_PAINT},
                {TYPE_STATION, SUBTYPE_PAINT},
                {TYPE_SHOP, SUBTYPE_PAINT},};
    private static final Logger log = Logger.getLogger(TextPlugin.class.getName());

    private Point3i mStartingPoint;
    private Point3i mAdvanceVector;
    private int mAdvanceLength;
    private Point3i mHeightVector;
    private int mHeightLength;
    private Point3i mDepthVector;
    private int mDepthLength;

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
        return new TextParameters();
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
    @SuppressWarnings("empty-statement")
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        TextParameters params;
        params = (TextParameters) p;
        Point3i lower;
        lower = new Point3i();
        Point3i upper;
        upper = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);;
        if (cb != null) {
            cb.setStatus("Adding text");
        }
        setupExtent(lower, upper);
        BufferedImage text;
        text = setupImage(params);

        if (cb != null) {
            cb.startTask(mAdvanceLength);
        }
        SparseMatrix<Block> modified;
        modified = new SparseMatrix<>(original);
        Point3i advance;
        advance = new Point3i(mStartingPoint);
        for (int x = 0; x < mAdvanceLength; x++) {
            Point3i height;
            height = new Point3i(advance);
            for (int y = 0; y < mHeightLength; y++) {
                if ((text.getRGB(x, y) & 0xFFFFFF) != 0) {
                    log.log(Level.INFO, "X");
                    Point3i depth;
                    depth = new Point3i(height);
                    for (int z = 0; z < mDepthLength; z++) {
                        Block oldBlock;
                        oldBlock = modified.get(depth);
                        if (oldBlock != null) {
                            Block newBlock;
                            newBlock = BlockTypes.colorize(oldBlock, params.getInk());
                            modified.set(depth, newBlock);
                        }
                        depth.add(mDepthVector);
                    }
                } else {
                    log.log(Level.INFO, " ");
                }
                height.add(mHeightVector);
            }
            System.out.println();
            advance.add(mAdvanceVector);
            if (cb != null) {
                cb.workTask(1);
            }
        }
        if (cb != null) {
            cb.endTask();
        }
        ShipLogic.ensureCore(modified);
        return modified;
    }

    private BufferedImage setupImage(TextParameters params) {
        BufferedImage img;
        img = new BufferedImage(mAdvanceLength, mHeightLength, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g;
        g = img.getGraphics();
        int style;
        style = 0;
        if (params.isBold()) {
            style |= Font.BOLD;
        }
        if (params.isItalic()) {
            style |= Font.ITALIC;
        }
        Font font;
        font = new Font(params.getFont(), style, params.getSize());
        g.setFont(font);
        FontMetrics fm;
        fm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        int baseline;
        baseline = mHeightLength + ((0 + 1 - mHeightLength) / 2) - ((fm.getAscent() + fm.getDescent()) / 2) + fm.getAscent();
        g.drawString(params.getText(), 0, baseline);
        return img;
    }

    private void setupExtent(Point3i lower, Point3i upper) {
        int xSpan;
        xSpan = upper.x - lower.x + 1;
        int ySpan;
        ySpan = upper.y - lower.y + 1;
        int zSpan;
        zSpan = upper.z - lower.z + 1;
        mStartingPoint = lower;
        mAdvanceLength = Math.max(xSpan, Math.max(ySpan, zSpan));
        if (mAdvanceLength == xSpan) {
            mAdvanceVector = new Point3i(1, 0, 0);
            if (ySpan >= zSpan) {
                mHeightVector = new Point3i(0, 1, 0);
                mHeightLength = ySpan;
                mDepthVector = new Point3i(0, 0, 1);
                mDepthLength = zSpan;
            } else {
                mHeightVector = new Point3i(0, 0, 1);
                mHeightLength = zSpan;
                mDepthVector = new Point3i(0, 1, 0);
                mDepthLength = ySpan;
            }
        } else if (mAdvanceLength == ySpan) {
            mAdvanceVector = new Point3i(0, 1, 0);
            if (xSpan >= zSpan) {
                mHeightVector = new Point3i(1, 0, 0);
                mHeightLength = xSpan;
                mDepthVector = new Point3i(0, 0, 1);
                mDepthLength = zSpan;
            } else {
                mHeightVector = new Point3i(0, 0, 1);
                mHeightLength = zSpan;
                mDepthVector = new Point3i(1, 0, 0);
                mDepthLength = xSpan;
            }
        } else if (mAdvanceLength == zSpan) {
            mAdvanceVector = new Point3i(0, 0, 1);
            if (xSpan >= ySpan) {
                mHeightVector = new Point3i(1, 0, 0);
                mHeightLength = xSpan;
                mDepthVector = new Point3i(0, 1, 0);
                mDepthLength = ySpan;
            } else {
                mHeightVector = new Point3i(0, 1, 0);
                mHeightLength = ySpan;
                mDepthVector = new Point3i(1, 0, 0);
                mDepthLength = xSpan;
            }
        }
        log.log(Level.INFO, "Advance={0} x {1}", new Object[]{mAdvanceVector, mAdvanceLength});
        log.log(Level.INFO, "Height={0} x {1}", new Object[]{mHeightVector, mHeightLength});
        log.log(Level.INFO, "Depth={0} x {1}", new Object[]{mDepthVector, mDepthLength});
    }
}
