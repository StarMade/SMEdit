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

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FilePropertyDescriptor extends PropertyDescriptor {

    private final FilePropertyInfo mInfo;

    public FilePropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName, FilePropertyInfo info)
            throws IntrospectionException {
        super(propertyName, beanClass, readMethodName, writeMethodName);
        mInfo = info;
    }

    public FilePropertyDescriptor(String propertyName, Class<?> beanClass, FilePropertyInfo info)
            throws IntrospectionException {
        super(propertyName, beanClass);
        mInfo = info;
    }

    public FilePropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod, FilePropertyInfo info) throws IntrospectionException {
        super(propertyName, readMethod, writeMethod);
        mInfo = info;
    }

    @Override
    public Class<?> getPropertyEditorClass() {
        return FilePropertyEditor.class;
    }

    @Override
    public PropertyEditor createPropertyEditor(final Object bean) {
        final PropertyEditor pe = new FilePropertyEditor(bean, mInfo);
        try {
            pe.setValue(getReadMethod().invoke(bean));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        pe.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent ev) {
                try {
                    getWriteMethod().invoke(bean, pe.getValue());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        return pe;
    }

}
