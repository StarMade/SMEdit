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
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import jo.sm.logic.utils.ResourceUtils;

@SuppressWarnings("serial")
public class DlgAbout extends JDialog {

    private final JEditorPane mMessage;
    private JScrollPane mScroller;
    
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;

    public DlgAbout(JFrame base) {
        super(base);
        initComponents();
        setLocationRelativeTo(base);
        mMessage = new JEditorPane();
        mMessage.setContentType("text/html");
        mMessage.setEditable(false);
        try {
            mMessage.setText(ResourceUtils.loadSystemResourceString("about.htm", DlgAbout.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mScroller = new JScrollPane(mMessage);
        
        
        JButton ok = new JButton("Close");
        JButton audio = new JButton("Audiobook");
        JButton ebook = new JButton("E-book");
        JButton doc = new JButton("java docks");
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        
        JPanel buttonBar = new JPanel();
        getContentPane().add(buttonBar, BorderLayout.SOUTH);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(doc);
        buttonBar.add(audio);
        buttonBar.add(ebook);
        // link
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doOK();
            }
        });
        doc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(BegPanel.DOCUMENTATION);
            }
        });
        ebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(BegPanel.THE_RAIDERS_LAMENT);
            }
        });
        audio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(BegPanel.THE_RAIDERS_LAMENT_AUDIO);
            }
        });
        
        
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mScroller.getVerticalScrollBar().getModel().setValue(mScroller.getVerticalScrollBar().getModel().getMinimum());
            }
        };
        t.start();
    }
    
    private void initComponents() {
        jTabbedPane1 = new JTabbedPane();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jPanel3 = new JPanel();
        jLabel2 = new JLabel();

        setTitle("About SMEdit");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                doOK();
            }
        });

        jTabbedPane1.setPreferredSize(new Dimension(450, 500));

        jPanel1.setPreferredSize(new Dimension(300, 300));
        jPanel1.add(jLabel1);
        jLabel1.getAccessibleContext().setAccessibleName("jLabel1");

        jTabbedPane1.addTab("  About  ", jPanel1);

        jPanel2.setLayout(new BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, BorderLayout.CENTER);

        jTabbedPane1.addTab("  License  ", jPanel2);

        jPanel3.add(jLabel2);

        jTabbedPane1.addTab("  Authors  ", jPanel3);

        getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
        
        

        pack();
    }

    private void doOK() {
        setVisible(false);
        dispose();
    }

    private void doGoto(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE)) {
                try {
                    desktop.browse(URI.create(url));
                } catch (IOException e) {
                    // handled below
                }
            }
        }
    }
}
