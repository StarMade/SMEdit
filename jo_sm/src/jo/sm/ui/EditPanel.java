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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.JDialog;
import javax.swing.JFrame;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.util.Paths;
import jo.vecmath.Point3i;

@SuppressWarnings("serial")
public class EditPanel extends JPanel {

    private static final Logger log = Logger.getLogger(EditPanel.class.getName());
    private boolean mPainting;
    private final RenderPanel mRenderer;
    /* For main construct window */
    private JLabel mSymLabel;
    private JLabel mCheckLabel;
    private JLabel mColor;
    private JLabel mCurrent;
    private JLabel mChoice;
    private JButton mGrey;
    private JButton mBlack;
    private JButton mRed;
    private JButton mPurple;
    private JButton mBlue;
    private JButton mGreen;
    private JButton mBrown;
    private JButton mYellow;
    private JButton mWhite;
    private JButton mClear;
    private JSpinner mRadius;
    private JLabel mRaLabel;
    private JButton mAll;
    private JCheckBox mXSymmetry;
    private JCheckBox mYSymmetry;
    private JCheckBox mZSymmetry;
    /* For tile properties dialog */
    private JDialog propertiesDialog;

    private JLabel tileImg;

    public EditPanel(RenderPanel renderer, JFrame dialogOwner) {
        mRenderer = renderer;
        initComponents();
        createPropertiesDialog(dialogOwner);

        // link
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doMouseClick(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON3) {
                    mPainting = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON3) {
                    mPainting = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent ev) {
                if (mPainting) {
                    doMouseClick(ev.getX(), ev.getY());
                }
            }
        };
        mRenderer.addMouseListener(ma);
        mRenderer.addMouseMotionListener(ma);
        mClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorClick(null, (short) -1);
            }
        });
        mAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorAll();
            }
        });
    }

    private void initComponents() {

        mColor = new JLabel("Current Color:");
        mCurrent = new JLabel("None");
        mChoice = new JLabel("Color Choices:");
        mGrey = newButton(BlockTypes.HULL_COLOR_GREY_ID);
        mBlack = newButton(BlockTypes.HULL_COLOR_BLACK_ID);
        mRed = newButton(BlockTypes.HULL_COLOR_RED_ID);
        mPurple = newButton(BlockTypes.HULL_COLOR_PURPLE_ID);
        mBlue = newButton(BlockTypes.HULL_COLOR_BLUE_ID);
        mGreen = newButton(BlockTypes.HULL_COLOR_GREEN_ID);
        mBrown = newButton(BlockTypes.HULL_COLOR_BROWN_ID);
        mYellow = newButton(BlockTypes.HULL_COLOR_YELLOW_ID);
        mWhite = newButton(BlockTypes.HULL_COLOR_WHITE_ID);
        mRadius = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
        mSymLabel = new JLabel("Paint Symmetry:");
        mCheckLabel = new JLabel("  X       Y       Z");
        mRaLabel = new JLabel("Paint Radius:");
        mXSymmetry = new JCheckBox();
        mXSymmetry.setToolTipText("paint port/starboard");
        mYSymmetry = new JCheckBox();
        mYSymmetry.setToolTipText("paint dorsal/ventral");
        mZSymmetry = new JCheckBox();
        mZSymmetry.setToolTipText("paint fore/aft");
        mClear = getDefaultButton("Stop painting", new ImageIcon(Paths.getIconDirectory() + "/stop.png")); //clear
        mAll = getDefaultButton("Set all hulls to current color", new ImageIcon(Paths.getIconDirectory() + "/fill.png")); // paint all

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(mXSymmetry)
                                        .addComponent(mYSymmetry)
                                        .addComponent(mZSymmetry))
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(mColor)
                                        .addComponent(mCurrent)
                                        .addComponent(mChoice)
                                        .addGroup(layout.createSequentialGroup()
                        .addComponent(mGrey)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mBlack))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mRed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mPurple))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mGreen))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mBrown)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mYellow))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mWhite))
                                .addComponent(mRadius, PREFERRED_SIZE, 90, PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(mClear)
                                        .addPreferredGap(RELATED)
                                        .addComponent(mAll))
                                .addComponent(mSymLabel)
                                .addComponent(mRaLabel)
                                .addComponent(mCheckLabel)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(mColor)
                        .addComponent(mCurrent, PREFERRED_SIZE, 32, PREFERRED_SIZE)
                        .addPreferredGap(RELATED)
                        .addComponent(mChoice)
                        .addPreferredGap(RELATED)
                        
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mGrey)
                        .addComponent(mBlack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mRed)
                        .addComponent(mPurple))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mBlue)
                        .addComponent(mGreen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mBrown)
                        .addComponent(mYellow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mWhite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addPreferredGap(RELATED)
                        .addComponent(mRaLabel)
                        .addPreferredGap(RELATED)
                        .addComponent(mRadius, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addPreferredGap(RELATED)
                        .addComponent(mSymLabel)
                        .addPreferredGap(RELATED)
                        .addComponent(mCheckLabel)
                        .addPreferredGap(RELATED)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(mXSymmetry)
                                .addComponent(mYSymmetry)
                                .addComponent(mZSymmetry))
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                
                                .addComponent(mClear)
                                .addComponent(mAll)))
        );
    }

    private void doColorClick(Icon color, short blockID) {
        StarMadeLogic.getInstance().setSelectedBlockType(blockID);
        if (StarMadeLogic.getInstance().getSelectedBlockType() == -1) {
            mCurrent.setIcon(null);
            mCurrent.setText("None");
        } else {
            mCurrent.setIcon(color);
            mCurrent.setText("");
        }
    }

    private void doColorAll() {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0) {
            return;
        }
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Iterator<Point3i> i;
        if ((StarMadeLogic.getInstance().getSelectedLower() != null) && (StarMadeLogic.getInstance().getSelectedUpper() != null)) {
            i = new CubeIterator(StarMadeLogic.getInstance().getSelectedLower(), StarMadeLogic.getInstance().getSelectedUpper());
        } else {
            i = grid.iteratorNonNull();
        }
        colorByIterator(grid, i, false);
    }

    private void colorByIterator(SparseMatrix<Block> grid, Iterator<Point3i> i, boolean symmetric) {
        List<Point3i> coords = new ArrayList<>();
        while (i.hasNext()) {
            coords.clear();
            coords.add(i.next());
            if (symmetric) {
                if (mXSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.x != 8) {
                            coords.add(new Point3i(16 - p1.x, p1.y, p1.z));
                        }
                    }
                }
                if (mYSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.y != 8) {
                            coords.add(new Point3i(p1.x, 16 - p1.y, p1.z));
                        }
                    }
                }
                if (mZSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.z != 8) {
                            coords.add(new Point3i(p1.x, p1.y, 16 - p1.z));
                        }
                    }
                }
            }
            for (Point3i c : coords) {
                paintBlock(grid, c);
            }
        }
        mRenderer.repaint();
    }

    private void paintBlock(SparseMatrix<Block> grid, Point3i coords) {
        Block block = grid.get(coords);
        if (block == null) {
            return;
        }
        short newID = BlockTypes.getColoredBlock(block.getBlockID(), StarMadeLogic.getInstance().getSelectedBlockType());
        if (newID != -1) {
            block.setBlockID(newID);
        }
    }

    private void doMouseClick(int x, int y) {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0) {
            return;
        }
        RenderPoly b = mRenderer.getTileAt(x, y);
        if (b == null) {
            return;
        }
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Point3i p = b.getPosition();
        if (p == null) {
            return;
        }
        int r = (Integer) mRadius.getValue() - 1;
        Point3i lower = new Point3i(p.x - r, p.y - r, p.z - r);
        Point3i upper = new Point3i(p.x + r, p.y + r, p.z + r);
        colorByIterator(grid, new CubeIterator(lower, upper), true);
    }
    
    /**
     * Makes a JButton with the given icon and tooltop. If the icon cannot be
     * loaded, then the text will be used instead.
     *
     * Adds this RenderFame as an actionListener.
     *
     * @return a shiny new JButton
     *
     */
    private JButton getDefaultButton(final String tip, final ImageIcon i) {
        final JButton button = new JButton();
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(5, 5, 5, 5));
        button.setPreferredSize(new Dimension(32, 32));
        button.setMaximumSize(new Dimension(32, 32));

        return button;
    }

    private JButton newButton(final short blockID) {
        ImageIcon rawImage = BlockTypeColors.getBlockImage(blockID);
        final Image image = rawImage.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT);
        final JButton btn = new JButton(new ImageIcon(image));
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showProperties(blockID);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorClick(btn.getIcon(), blockID);
            }
        });
        return btn;
    }

    void createPropertiesDialog(JFrame dialogOwner) {

        /* Setup for the proeprties dialog */
        propertiesDialog = new JDialog(dialogOwner, "Block Properties");
        propertiesDialog.setSize(100, 100); //to set location better
        propertiesDialog.setLocationRelativeTo(null);

        tileImg = new JLabel();
        tileImg.setHorizontalAlignment(SwingConstants.CENTER);
        tileImg.setBorder(new TitledBorder("Image"));
        JPanel cp = (JPanel) propertiesDialog.getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel p2 = new JPanel(new BorderLayout());
        cp.add(p2, BorderLayout.CENTER);
        cp.add(tileImg, BorderLayout.NORTH);
        JPanel btns1 = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);

        c.gridx = 0;
        c.gridy = 0;

        btns1.add(new JLabel("ID"), c);
        c.gridx = 1;
        c.ipadx = 30;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        btns1.add(new JLabel("ID"), c);
        c.ipadx = 0;

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 1;
        btns1.add(new JLabel("Type"), c);
        c.gridx = 1;
        btns1.add(new JLabel("Type"), c);
        c.gridx = 0;
        c.gridy = 2;

        btns1.add(new JLabel("Name"), c);
        c.gridx = 1;
        btns1.add(new JLabel("Name"), c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridx = 1;

        p2.add(btns1, BorderLayout.NORTH);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;

        propertiesDialog.setSize(100, 100);
        propertiesDialog.setResizable(false);
    }

    void showProperties(final short blockID) {
        ImageIcon rawImage = BlockTypeColors.getBlockImage(blockID);
        final Image image = rawImage.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT);
        tileImg.setIcon(new ImageIcon(image));
        propertiesDialog.pack();
        propertiesDialog.setVisible(true);
    }
}
