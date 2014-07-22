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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.sm.logic.utils.StringUtils;

@SuppressWarnings("serial")
public class DlgError extends JDialog {

    private final JEditorPane mMessage;
    private JScrollPane mScroller;

    public DlgError(JFrame base, String title, String description, Throwable ex) {
        super(base, "SMEdit Error Message", Dialog.ModalityType.DOCUMENT_MODAL);
        // instantiate
        mMessage = new JEditorPane();
        mMessage.setContentType("text/html");
        mMessage.setEditable(false);
        mMessage.setText(composeError(title, description, ex));
        mScroller = new JScrollPane(mMessage);
        JButton ok = new JButton("Close");
        JButton doc = new JButton("Documentation");
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, new JLabel("About SMEdit"));
        client.add(BorderLayout.CENTER, mScroller);
        JPanel buttonBar = new JPanel();
        client.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(doc);
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
        setSize(640, 480);
        setLocationRelativeTo(base);
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

    private String composeError(String title, String description, Throwable ex) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");
        if (!StringUtils.isTrivial(title)) {
            html.append("<h1>");
            html.append(title);
            html.append("</h1>");
        }
        if (!StringUtils.isTrivial(description)) {
            html.append("<p>");
            html.append(description);
            html.append("</p>");
        }
        if (ex != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            html.append("<pre>");
            html.append(sw.toString());
            html.append("</pre>");
        }
        html.append("</body>");
        html.append("</html>");
        return html.toString();
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

    public static void showError(JFrame base, String title, String description, Throwable ex) {
        DlgError dlg = new DlgError(base, title, description, ex);
        dlg.setVisible(true);
    }
}
