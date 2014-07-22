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

package jo.sm.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.logging.Logger;
import javax.swing.Icon;

/**
 *
 * @author Robert Barefoot
 */
public class TriangleSquareWindowsCornerIcon implements Icon {

    //RGB values discovered using ZoomIn
    private static final Color THREE_D_EFFECT_COLOR = new Color(255, 255, 255);
    private static final Color SQUARE_COLOR_LEFT = new Color(184, 180, 163);
    private static final Color SQUARE_COLOR_TOP_RIGHT = new Color(184, 180, 161);
    private static final Color SQUARE_COLOR_BOTTOM_RIGHT = new Color(184, 181, 161);

    //Dimensions
    private static final int WIDTH = 12;
    private static final int HEIGHT = 12;



    @Override
    public int getIconHeight() {
        return HEIGHT;
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        //Layout a row and column "grid"
        int firstRow = 0;
        int firstColumn = 0;
        int rowDiff = 4;
        int columnDiff = 4;

        int secondRow = firstRow + rowDiff;
        int secondColumn = firstColumn + columnDiff;
        int thirdRow = secondRow + rowDiff;
        int thirdColumn = secondColumn + columnDiff;


        //Draw the white squares first, so the gray squares will overlap
        draw3dSquare(g, firstColumn+1, thirdRow+1);

        draw3dSquare(g, secondColumn+1, secondRow+1);
        draw3dSquare(g, secondColumn+1, thirdRow+1);

        draw3dSquare(g, thirdColumn+1, firstRow+1);
        draw3dSquare(g, thirdColumn+1, secondRow+1);
        draw3dSquare(g, thirdColumn+1, thirdRow+1);

        //draw the gray squares overlapping the white background squares
        drawSquare(g, firstColumn, thirdRow);

        drawSquare(g, secondColumn, secondRow);
        drawSquare(g, secondColumn, thirdRow);

        drawSquare(g, thirdColumn, firstRow);
        drawSquare(g, thirdColumn, secondRow);
        drawSquare(g, thirdColumn, thirdRow);

    }

    private void draw3dSquare(Graphics g, int x, int y){
        Color oldColor = g.getColor(); //cache the old color
        g.setColor(THREE_D_EFFECT_COLOR); //set the white color
        g.fillRect(x,y,2,2); //draw the square
        g.setColor(oldColor); //reset the old color
    }


    private void drawSquare(Graphics g, int x, int y){
        Color oldColor = g.getColor();
        g.setColor(SQUARE_COLOR_LEFT);
        g.drawLine(x,y, x,y+1);
        g.setColor(SQUARE_COLOR_TOP_RIGHT);
        g.drawLine(x+1,y, x+1,y);
        g.setColor(SQUARE_COLOR_BOTTOM_RIGHT);
        g.drawLine(x+1,y+1, x+1,y+1);
        g.setColor(oldColor);
    }
    private static final Logger LOG = Logger.getLogger(TriangleSquareWindowsCornerIcon.class.getName());

}