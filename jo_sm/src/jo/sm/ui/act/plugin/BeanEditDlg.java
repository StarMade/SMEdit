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
package jo.sm.ui.act.plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.sm.logic.utils.StringUtils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class BeanEditDlg extends JDialog {

    private Object mBean;
    private final Class<?> mBeanClass;
    private BeanInfo mBeanInfo;
    private Customizer mCustomizer;

    public BeanEditDlg(JFrame base, Object bean) {
        super(base, "Edit Parameters", Dialog.ModalityType.DOCUMENT_MODAL);
        mBean = bean;
        mBeanClass = mBean.getClass();
        try {
            mBeanInfo = Introspector.getBeanInfo(mBeanClass);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return;
        }
        // instantiate
        Class<?> customizerClass = mBeanInfo.getBeanDescriptor().getCustomizerClass();
        if (customizerClass == null) {
            customizerClass = GenericBeanCustomizer.class;
        }
        try {
            mCustomizer = (Customizer) customizerClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return;
        }
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        // layout
        JPanel innerClient = new JPanel();
        innerClient.setLayout(new BorderLayout());
        innerClient.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        innerClient.add(BorderLayout.CENTER, new JScrollPane((Component) mCustomizer));
        Description d = mBeanClass.getAnnotation(Description.class);
        if (d != null) {
            if (StringUtils.nonTrivial(d.displayName())) {
                JLabel name = new JLabel(d.displayName());
                name.setFont(new Font("Dialog", Font.BOLD, 14));
                innerClient.add(BorderLayout.NORTH, name);
            }
            if (StringUtils.nonTrivial(d.shortDescription())) {
                JTextArea desc = new JTextArea(d.shortDescription());
                desc.setEditable(false);
                desc.setFont(new Font("Dialog", Font.ITALIC, 10));
                innerClient.add(BorderLayout.SOUTH, desc);
            }
        }

        JPanel outerClient = new JPanel();
        getContentPane().add(outerClient);
        outerClient.setLayout(new BorderLayout());
        outerClient.add(BorderLayout.CENTER, innerClient);
        JPanel buttonBar = new JPanel();
        outerClient.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(cancel);
        // link
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doOK();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doCancel();
            }
        });

        mCustomizer.setObject(mBean);
        //setSize(640, 480);
        pack();
        setLocationRelativeTo(base);
    }

    private void doOK() {
        setVisible(false);
        dispose();
    }

    private void doCancel() {
        mBean = null;
        setVisible(false);
        dispose();
    }

    public Object getBean() {
        return mBean;
    }
}
