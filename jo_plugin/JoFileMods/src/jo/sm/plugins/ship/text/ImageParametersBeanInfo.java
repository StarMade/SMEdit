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
package jo.sm.plugins.ship.text;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import jo.sm.ui.act.plugin.FilePropertyDescriptor;
import jo.sm.ui.act.plugin.FilePropertyInfo;


public class ImageParametersBeanInfo implements BeanInfo {
    private static final Logger log = Logger.getLogger(ImageParametersBeanInfo.class.getName());

    private final FilePropertyInfo mInfo;
    private final BeanInfo mRootBeanInfo;

    public ImageParametersBeanInfo() throws IntrospectionException {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(ImageParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
        mInfo = new FilePropertyInfo();
        mInfo.setDialogTitle("Image to paint with");
        String[] suffixes;
        suffixes = ImageIO.getReaderFileSuffixes();
        String[] names;
        names = ImageIO.getReaderFormatNames();
        String[][] filters;
        filters = new String[names.length][2];
        for (int i = 0; i < names.length; i++) {
            filters[i][0] = names[i];
            filters[i][1] = suffixes[i];
        }
        mInfo.setFilters(filters);
        mInfo.setDialogType(JFileChooser.OPEN_DIALOG);
        mInfo.setApproveButtonText("Open");
        mInfo.setApproveButtonTooltipText("Select image to paint with");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] props;
        props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            log.log(Level.INFO, "Name=" + props[i].getName());
            if (props[i].getName().equalsIgnoreCase("file")) {
                try {
                    props[i] = new FilePropertyDescriptor(props[i].getName(),
                            props[i].getReadMethod(), props[i].getWriteMethod(), mInfo);
                } catch (IntrospectionException e) {
                    log.log(Level.WARNING, "File Property Descriptor failed!", e);
                }
            }
        }
        return props;
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        return mRootBeanInfo.getAdditionalBeanInfo();
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return mRootBeanInfo.getBeanDescriptor();
    }

    @Override
    public int getDefaultEventIndex() {
        return mRootBeanInfo.getDefaultEventIndex();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return mRootBeanInfo.getDefaultPropertyIndex();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return mRootBeanInfo.getEventSetDescriptors();
    }

    @Override
    public Image getIcon(int flags) {
        return mRootBeanInfo.getIcon(flags);
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return mRootBeanInfo.getMethodDescriptors();
    }
}
