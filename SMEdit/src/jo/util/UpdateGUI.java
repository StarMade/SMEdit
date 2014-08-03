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
package jo.util;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

/**
 * @Auther Robert Barefoot for SMEdit Classic and SMEdit2 - version 1.0
 */
public class UpdateGUI extends JFrame {

    private static final long serialVersionUID = 8646183995455154141L;
    private static final JLabel jLabel1 = new JLabel();
    private static final JLabel jLabel2 = new JLabel();
    public static JLabel jLabel3 = new JLabel();
    public static int percent = 0;

    public UpdateGUI() {
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setTitle("SMEdit Updater");

        JPanel panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createTitledBorder(" Downloading update... "));
        panel1.setMaximumSize(new Dimension(245, 510));
        panel1.setPreferredSize(new Dimension(210, 500));
        panel1.setRequestFocusEnabled(false);
        jLabel1.setIcon(new ImageIcon(GlobalConfiguration.getImage(Resources.DOWNLOAD)));
        jLabel2.setText("Please be patient wile your new App is being downloaded");
        jLabel3.setText("");

        GroupLayout jPanel1Layout = new GroupLayout(panel1);
        panel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap().addComponent(jLabel1)
                        .addPreferredGap(RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(LEADING)
                                .addComponent(jLabel3, DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                .addComponent(jLabel2, DEFAULT_SIZE, 289, Short.MAX_VALUE))
                        .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(LEADING, false)
                                .addComponent(jLabel1)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(RELATED, DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3)))
                        .addContainerGap(19, Short.MAX_VALUE)));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                .addGroup(TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel1, DEFAULT_SIZE, 379, Short.MAX_VALUE)
                        .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel1, PREFERRED_SIZE, 97, PREFERRED_SIZE)
                        .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
}
