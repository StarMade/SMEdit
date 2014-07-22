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
import java.awt.Font;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jo.log.LabelLogHandler;



public class StatusPanel extends JPanel {
    
    private final static Logger log = Logger.getLogger(StatusPanel.class.getName());
    private final JPanel contentPanel;
    private final JPanel southPanel;
    private final JPanel midPanel;
    private final ToolPanel toolBar;
    private final LabelLogHandler handler;
    private final Font font;

    
    /** Creates a new instance of StatusBar */
    public StatusPanel() {
        contentPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        midPanel = new JPanel(new BorderLayout());
        toolBar = new ToolPanel(this);
        midPanel.add(toolBar);
        handler = new LabelLogHandler();
        font = handler.label.getFont();
        setLayout(new BorderLayout());
        Logger.getLogger("").addHandler(handler);
        
        southPanel.add(new JLabel(new TriangleSquareWindowsCornerIcon()), BorderLayout.EAST);
        
        handler.label.setBorder(javax.swing.BorderFactory.createTitledBorder(" App Events "));
        handler.label.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        handler.label.setPreferredSize(new Dimension(1000, 30 + 12));

        contentPanel.add(handler.label, BorderLayout.NORTH);
        contentPanel.add(midPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        
    }
    
}

