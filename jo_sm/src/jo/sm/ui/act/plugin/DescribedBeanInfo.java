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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.sm.logic.utils.StringUtils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class DescribedBeanInfo {

    private Object mBean;
    private final Class<?> mBeanClass;
    private BeanInfo mBeanInfo;
    private Map<String, PropertyDescriptor> mProps;
    private List<PropertyDescriptor> mOrderedProps;
    private Map<String, Description> mDescriptions;
    private Map<String, PropertyEditor> mEditors;

    public DescribedBeanInfo(Object bean) {
        mBean = bean;
        mBeanClass = mBean.getClass();
        init();
    }

    public DescribedBeanInfo(Class<?> beanClass) {
        mBeanClass = beanClass;
        init();
    }

    private void init() {
        try {
            mBeanInfo = Introspector.getBeanInfo(mBeanClass);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return;
        }
        setupProps();
        sortProps();
        setupEditors();
    }

    private void setupEditors() {
        mEditors = new HashMap<>();
        for (PropertyDescriptor prop : mOrderedProps) {
            PropertyEditor editor = prop.createPropertyEditor(mBean);
            if (editor == null) {
                editor = new GenericPropertyEditor(mBean, prop);
            }
            mEditors.put(prop.getName(), editor);
        }
    }

    private void sortProps() {
        Collections.sort(mOrderedProps, new Comparator<PropertyDescriptor>() {
            @Override
            public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
                int p1 = 50;
                if (mDescriptions.containsKey(o1.getName())) {
                    p1 = mDescriptions.get(o1.getName()).priority();
                }
                int p2 = 50;
                if (mDescriptions.containsKey(o2.getName())) {
                    p2 = mDescriptions.get(o2.getName()).priority();
                }
                if (p1 == p2) {
                    return o1.getName().compareTo(o2.getName());
                }
                return (int) Math.signum(p1 - p2);
            }
        });
    }

    private void setupProps() {
        mProps = new HashMap<>();
        mDescriptions = new HashMap<>();
        mOrderedProps = new ArrayList<>();
        for (PropertyDescriptor prop : mBeanInfo.getPropertyDescriptors()) {
            if (prop.getReadMethod() == null || prop.getWriteMethod() == null) {
            } else {
                try {
                    Description d = findDescription(mBeanClass, prop);
                    if (d != null) {
                        if (StringUtils.nonTrivial(d.displayName())) {
                            prop.setDisplayName(d.displayName());
                        }
                        if (StringUtils.nonTrivial(d.shortDescription())) {
                            prop.setShortDescription(d.shortDescription());
                        }
                    }
                    mDescriptions.put(prop.getName(), d);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mOrderedProps.add(prop);
                mProps.put(prop.getName(), prop);
            }
        }
    }

    private Description findDescription(Class<?> beanClass,
            PropertyDescriptor prop) {
        String propName = prop.getName();
        Field match = null;
        for (Field f : beanClass.getDeclaredFields()) {
            String fieldName = f.getName();
            if (propName.equalsIgnoreCase(fieldName)) {
                match = f;
                break;
            }
            if (fieldName.startsWith("m") && propName.equalsIgnoreCase(fieldName.substring(1))) {
                match = f;
                break;
            }
        }
        if (match == null) {
            return null;
        }
        return match.getAnnotation(Description.class);
    }

    public Object getValue(String propName) {
        PropertyEditor editor = mEditors.get(propName);
        if (editor == null) {
            return null;
        }
        return editor.getValue();
    }

    public String getAsText(String propName) {
        PropertyEditor editor = mEditors.get(propName);
        if (editor == null) {
            return null;
        }
        return editor.getAsText();
    }

    public void setAsText(String propName, String value) {
        PropertyEditor editor = mEditors.get(propName);
        if (editor == null) {
            return;
        }
        editor.setAsText(value);
    }

    public void setAsValue(String propName, Object value) {
        PropertyEditor editor = mEditors.get(propName);
        if (editor == null) {
            return;
        }
        editor.setValue(value);
    }

    public Object getBean() {
        return mBean;
    }

    public Class<?> getBeanClass() {
        return mBeanClass;
    }

    public BeanInfo getBeanInfo() {
        return mBeanInfo;
    }

    public Map<String, PropertyDescriptor> getProps() {
        return mProps;
    }

    public List<PropertyDescriptor> getOrderedProps() {
        return mOrderedProps;
    }

    public Map<String, Description> getDescriptions() {
        return mDescriptions;
    }

    public Map<String, PropertyEditor> getEditors() {
        return mEditors;
    }
}
