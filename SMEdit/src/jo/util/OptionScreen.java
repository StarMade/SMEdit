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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import jo.sm.edit.SMEdit;

public class OptionScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private static String[] mArgs;
    //public final static String fileName = Paths.getOptsFile();
    private static final Logger log = Logger.getLogger(OptionScreen.class.getName());
    private Properties mProps;
    private File mStarMadeDir;
    private SMEdit screen;

    /* For tile properties dialog */
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JComboBox jComboBox2;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JSpinner jSpinner1;
    private JTextField jTextField1;
    
    @SuppressWarnings("unchecked")
    public OptionScreen(final String[] args) {

        loadJosmProps();

        mArgs = args;
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        setTitle(GlobalConfiguration.NAME + " version 1.0" + ((float) GlobalConfiguration.getVersion() / 100));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });

        initComponents();
        setVisible(true);
    }

    @SuppressWarnings({"unchecked", "unchecked"})
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel3 = new JLabel();
        jSpinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 32, 1));
        jLabel4 = new JLabel();
        jPanel3 = new JPanel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jPanel4 = new JPanel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new javax.swing.JButton();

        final ImageIcon icon = new ImageIcon();
        icon.setImage(GlobalConfiguration.getImage(Resources.SPLASH));
        jLabel1.setIcon(icon);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        jSpinner1.setBorder(createEtchedBorder());
        if (null != mProps.getProperty("memory", "")) {
            switch (mProps.getProperty("memory", "")) {
                case "0":
                    jSpinner1.setValue(1);
                    break;
                case "1":
                    jSpinner1.setValue(1);
                    break;
                case "2":
                    jSpinner1.setValue(2);
                    break;
                case "3":
                    jSpinner1.setValue(3);
                    break;
                case "4":
                    jSpinner1.setValue(4);
                    break;
                case "5":
                    jSpinner1.setValue(5);
                    break;
                case "6":
                    jSpinner1.setValue(6);
                    break;
                case "7":
                    jSpinner1.setValue(7);
                    break;
                case "8":
                    jSpinner1.setValue(8);
                    break;
                case "9":
                    jSpinner1.setValue(9);
                    break;
                case "10":
                    jSpinner1.setValue(10);
                    break;
                case "11":
                    jSpinner1.setValue(11);
                    break;
                case "12":
                    jSpinner1.setValue(12);
                    break;
                case "13":
                    jSpinner1.setValue(13);
                    break;
                case "14":
                    jSpinner1.setValue(14);
                    break;
                case "15":
                    jSpinner1.setValue(15);
                    break;
                case "16":
                    jSpinner1.setValue(16);
                    break;
                case "17":
                    jSpinner1.setValue(17);
                    break;
                case "18":
                    jSpinner1.setValue(18);
                    break;
                case "19":
                    jSpinner1.setValue(19);
                    break;
                case "20":
                    jSpinner1.setValue(20);
                    break;
                case "21":
                    jSpinner1.setValue(21);
                    break;
                case "22":
                    jSpinner1.setValue(22);
                    break;
                case "23":
                    jSpinner1.setValue(23);
                    break;
                case "24":
                    jSpinner1.setValue(24);
                    break;
                case "25":
                    jSpinner1.setValue(25);
                    break;
                case "26":
                    jSpinner1.setValue(26);
                    break;
                case "27":
                    jSpinner1.setValue(27);
                    break;
                case "28":
                    jSpinner1.setValue(28);
                    break;
                case "29":
                    jSpinner1.setValue(29);
                    break;
                case "30":
                    jSpinner1.setValue(30);
                    break;
                case "31":
                    jSpinner1.setValue(31);
                    break;
                case "32":
                    jSpinner1.setValue(32);
                    break;

            }
        }

        jPanel1.setBorder(createTitledBorder("Options"));

        javax.swing.GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, PREFERRED_SIZE, 400, PREFERRED_SIZE)
                        .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, PREFERRED_SIZE, 218, PREFERRED_SIZE)
                        .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(createTitledBorder("* System Settings *"));

        jLabel3.setText("Desired memory use in gigs?");

        javax.swing.GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, PREFERRED_SIZE, 217, PREFERRED_SIZE)
                        .addPreferredGap(UNRELATED)
                        .addComponent(jSpinner1)
                        .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(LEADING, false)
                                .addComponent(jSpinner1)
                                .addComponent(jLabel3, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new Color(255, 0, 0));
        jLabel4.setText("Be Sure to set the preload options before you begin using SMEdit Classic");

        jPanel3.setBorder(createTitledBorder("* Folder Settings *"));

        jLabel5.setText("Desired texture pack to use?");

        jLabel6.setText("StarMade game folder directory?");

        //jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Cartoon", "OldStyle", "Pixel", "Realistic", "Custom"}));
        jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Default", "Pixel", "Realistic"}));
        jComboBox2.setBorder(createEtchedBorder());
        if (null != mProps.getProperty("texture", "")) {
            switch (mProps.getProperty("texture", "")) {
                case "Default":
                    jComboBox2.setSelectedIndex(0);
                    break;
                case "Pixel":
                    jComboBox2.setSelectedIndex(2);
                    break;
                case "Realistic":
                    jComboBox2.setSelectedIndex(3);
                    break;
                case "Custom":
                    jComboBox2.setSelectedIndex(4);
                    break;
            }
        } else {
            jComboBox2.setSelectedIndex(1);
        }

        jTextField1 = new JTextField(mProps.getProperty("starmade.home", ""));
        mStarMadeDir = new File(jTextField1.getText());

        javax.swing.GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(LEADING)
                .addGroup(TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(TRAILING)
                                .addComponent(jLabel6, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(TRAILING, false)
                                .addComponent(jTextField1, DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                .addComponent(jComboBox2, 0, DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(LEADING, false)
                                .addComponent(jComboBox2)
                                .addComponent(jLabel5, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(LEADING, false)
                                .addComponent(jTextField1)
                                .addComponent(jLabel6, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("* Start Up Instructions *"));

        jLabel7.setText("Be sure to apply any changes before starting the main SMEdit app.");

        jLabel8.setText("Pressing \"Cancel\" will close the starter app without starting SMEdit.");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText("Apply");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProps();
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        jButton3.setText("Start SMEdit");
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screen = new SMEdit(mArgs);
            }
        });
        

        javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                .addComponent(jPanel2, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jPanel3, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        
                        .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(jLabel4, TRAILING, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(LEADING, false)
                            .addComponent(jButton3, TRAILING, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(RELATED)
                                .addComponent(jButton2)))))

                        .addContainerGap())
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel4)
                        .addPreferredGap(RELATED)
                        .addComponent(jPanel2, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addPreferredGap(RELATED, DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addPreferredGap(RELATED)
                        .addComponent(jPanel4, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .addPreferredGap(RELATED)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(12, 12, 12))
        );

        pack();

    }

    private void loadJosmProps() {
        File uHome = new File(System.getProperty("user.home"));
        File props = new File(uHome, ".josm");
        if (props.exists()) {
            mProps = new Properties();
            try {
                try (FileInputStream fis = new FileInputStream(props)) {
                    mProps.load(fis);
                }
            } catch (IOException e) {

            }
        } else {
            mProps = new Properties();
        }
    }

    private void saveProps() {
        if (mProps == null) {
            return;
        }
        if (mStarMadeDir != null) {
            mProps.put("memory", jSpinner1.getValue().toString());
            mProps.put("texture", jComboBox2.getSelectedItem().toString());
            mProps.put("starmade.home", jTextField1.getText());
        }
        File uHome = new File(System.getProperty("user.home"));
        File props = new File(uHome, ".josm");
        try {
            try (FileWriter fos = new FileWriter(props)) {
                mProps.store(fos, "StarMade Utils defaults");
            }
        } catch (IOException e) {

        }       
    }

    

}
