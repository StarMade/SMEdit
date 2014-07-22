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

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MacroPropertyDescriptor extends PropertyDescriptor {
    private static final Logger log = Logger.getLogger(MacroPropertyDescriptor.class.getName());

    public MacroPropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName)
            throws IntrospectionException {
        super(propertyName, beanClass, readMethodName, writeMethodName);
    }

    public MacroPropertyDescriptor(String propertyName, Class<?> beanClass)
            throws IntrospectionException {
        super(propertyName, beanClass);
    }

    public MacroPropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod) throws IntrospectionException {
        super(propertyName, readMethod, writeMethod);
    }

    @Override
    public Class<?> getPropertyEditorClass() {
        return MacroPropertyEditor.class;
    }

    @Override
    public PropertyEditor createPropertyEditor(final Object bean) {
        final PropertyEditor pe = new MacroPropertyEditor(bean);
        try {
            pe.setValue(getReadMethod().invoke(bean));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
            log.log(Level.WARNING, "PropertyEditor failed!", e1);
        }
        pe.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent ev) {
                try {
                    getWriteMethod().invoke(bean, pe.getValue());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    log.log(Level.WARNING, "propertyChange failed!", e);
                }
            }
        });
        return pe;
    }

}
