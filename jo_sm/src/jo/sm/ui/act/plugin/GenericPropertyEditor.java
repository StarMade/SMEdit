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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;

import jo.sm.logic.utils.ConvertLogic;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class GenericPropertyEditor implements PropertyEditor {

    private final Object mBean;
    private final PropertyDescriptor mDescriptor;

    public GenericPropertyEditor(Object bean,
            PropertyDescriptor propertyDescriptor) {
        mBean = bean;
        mDescriptor = propertyDescriptor;
        if (!mDescriptor.getPropertyType().isPrimitive() && (mDescriptor.getPropertyType() != String.class)) {
            throw new IllegalArgumentException("Cannot handle editing type '" + mDescriptor.getPropertyType().getName() + "'");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getAsText() {
        Object oVal = getValue();
        if (oVal != null) {
            return oVal.toString();
        }
        return null;
    }

    @Override
    public Component getCustomEditor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getJavaInitializationString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getTags() {
        if (mDescriptor.getPropertyType() == boolean.class) {
            return new String[]{"false", "true"};
        }
        return null;
    }

    @Override
    public Object getValue() {
        try {
            if (mDescriptor.getReadMethod() != null) {
                return mDescriptor.getReadMethod().invoke(mBean);
            } else {
                return null;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics g, Rectangle rect) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setAsText(String sVal) throws IllegalArgumentException {
        Object oVal = ConvertLogic.toObject(sVal, mDescriptor.getPropertyType());
        setValue(oVal);
    }

    @Override
    public void setValue(Object val) {
        if (mDescriptor.getWriteMethod() != null) {
            try {
                mDescriptor.getWriteMethod().invoke(mBean, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

}
