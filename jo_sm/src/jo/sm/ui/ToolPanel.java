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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.StatusPanel;
import jo.sm.ui.act.GoToFace;
import jo.sm.ui.act.GoToHome;
import jo.sm.ui.act.GoToProject;
import jo.sm.ui.act.GoToTwitter;
import jo.sm.ui.act.GoToWiki;
import jo.util.Paths;

public class ToolPanel extends JToolBar {

    private static final long serialVersionUID = 906510024455580931L;
    private static final Logger log = Logger.getLogger(ToolPanel.class.getName());
    final private StatusPanel frame;
    private JButton homeButton;
    private JButton faceButton;
    private JButton wikiButton;
    private JButton twitterButton;
    private JButton projectButton;

    /**
     * This is the default constructor
     * @param frame
     */
    public ToolPanel(StatusPanel frame) {
        super();
        this.wikiButton = new JButton();
        this.faceButton = new JButton();
        this.homeButton = new JButton();
        this.projectButton = new JButton();
        this.twitterButton = new JButton();
        this.frame = frame;
        init();
    }

    private void init() {
        this.setFloatable(false);
        //this.add(new JToolBar.Separator());
        this.add(getJBTo());
        this.add(getJBToWiki());
        this.add(getJBToFace());
        this.add(getJBToTwit());
        this.add(getJBToProject());
    }

    private JButton getJBTo() {
        final ImageIcon i = new ImageIcon(Paths.getIconDirectory() + "/home.png");
        homeButton = getDefaultButton(new GoToHome(), "", "Visit SMEdit.lazygamerz.org", i);
        return homeButton;
    }

    private JButton getJBToWiki() {
        final ImageIcon i = new ImageIcon(Paths.getIconDirectory() + "/wiki.png");
        wikiButton = getDefaultButton(new GoToWiki(), "", "Visit SMEdit Wiki", i);
        return wikiButton;
    }

    private JButton getJBToFace() {
        final ImageIcon i = new ImageIcon(Paths.getIconDirectory() + "/face.png");
        faceButton = getDefaultButton(new GoToFace(), "", "Visit SMEdit Facebook", i);
        return faceButton;
    }

    private JButton getJBToTwit() {
        final ImageIcon i = new ImageIcon(Paths.getIconDirectory() + "/twit.png");
        twitterButton = getDefaultButton(new GoToTwitter(), "", "Visit SMEdit Twitter", i);
        return twitterButton;
    }

    private JButton getJBToProject() {
        final ImageIcon i = new ImageIcon(Paths.getIconDirectory() + "/web.png");
        projectButton = getDefaultButton(new GoToProject(), "", "Visit SMEdit Project", i);
        return projectButton;
    }

    private JButton getDefaultButton(Action a, String txt, String tip, ImageIcon i) {
        JButton button = new JButton(a);
        button.setText(txt);
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(0, 3, 0, 3));
        button.setPreferredSize(new Dimension(20, 20));
        button.setMaximumSize(new Dimension(20, 20));
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.setFont(new Font("Tahoma", 0, 10));

        return button;
    }

    @Override
    public void setEnabled(boolean e) {
        super.setEnabled(e);
        homeButton.setEnabled(e);
        wikiButton.setEnabled(e);
        faceButton.setEnabled(e);
        twitterButton.setEnabled(e);
        projectButton.setEnabled(e);
    }
}
