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
package jo.sm.plugins.ship.reflect;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.ui.act.plugin.ComboPropertyDescriptor;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class DuplicateParametersBeanInfo implements BeanInfo {

    private static final Map<String, Object> COMBO_MAP = new HashMap<>();
    private static final Logger log = Logger.getLogger(DuplicateParametersBeanInfo.class.getName());

    static {
        COMBO_MAP.put("Negative", -1);
        COMBO_MAP.put("No reflection", 0);
        COMBO_MAP.put("Positive", 1);

    }

    private final BeanInfo mRootBeanInfo;

    public DuplicateParametersBeanInfo() throws IntrospectionException {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(DuplicateParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] props;
        props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            if (props[i].getName().endsWith("eflect")) {
                try {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(),
                            props[i].getReadMethod(), props[i].getWriteMethod(), COMBO_MAP);
                } catch (IntrospectionException e) {
                    log.log(Level.WARNING, "Combo Property Descriptor failed!", e);
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
