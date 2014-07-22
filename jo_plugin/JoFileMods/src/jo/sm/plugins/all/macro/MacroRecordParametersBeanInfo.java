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
package jo.sm.plugins.all.macro;

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


public class MacroRecordParametersBeanInfo implements BeanInfo {

    private static final Map<String, Object> PLACEMENT_MAP = new HashMap<>();

    private static final Map<String, Object> ENABLEMENT_MAP = new HashMap<>();
    private static final Logger log = Logger.getLogger(MacroRecordParametersBeanInfo.class.getName());

    static {
        PLACEMENT_MAP.put("File", "FILE");
        PLACEMENT_MAP.put("Edit", "EDIT");
        PLACEMENT_MAP.put("File", "VIEW");
        PLACEMENT_MAP.put("Generate", "GENERATE");
        PLACEMENT_MAP.put("Modify", "MODIFY");
        PLACEMENT_MAP.put("Paint", "PAINT");
    }
    static {
        ENABLEMENT_MAP.put("All", "ALL");
        ENABLEMENT_MAP.put("Ship", "SHIP");
        ENABLEMENT_MAP.put("Station", "STATION");
        ENABLEMENT_MAP.put("Shop", "SHOP");
        ENABLEMENT_MAP.put("Floating Rock", "FLOATINGROCK");
        ENABLEMENT_MAP.put("Planet", "PLANET");
    }
    private final BeanInfo mRootBeanInfo;

    public MacroRecordParametersBeanInfo() throws IntrospectionException {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(MacroRecordParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            if (props[i].getName().endsWith("acement")) {
                try {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(),
                            props[i].getReadMethod(), props[i].getWriteMethod(), PLACEMENT_MAP);
                } catch (IntrospectionException e) {
                    log.log(Level.WARNING, "ComboPropertyDescriptor failed!", e);
                }
            } else if (props[i].getName().endsWith("nablement")) {
                try {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(),
                            props[i].getReadMethod(), props[i].getWriteMethod(), ENABLEMENT_MAP);
                } catch (IntrospectionException e) {
                    log.log(Level.WARNING, "ComboPropertyDescriptor failed!", e);
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
