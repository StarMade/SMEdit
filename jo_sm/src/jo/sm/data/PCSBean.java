/**
 * Copyright 2014 SMEdit https://github.com/StarMade/SMEdit SMTools
 * https://github.com/StarMade/SMTools
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
 *
 */
package jo.sm.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jo.sm.logic.utils.BeanUtils;

public class PCSBean extends Bean {

    public static boolean debug = false;
    private static final Logger log = Logger.getLogger(PCSBean.class.getName());

    private final List<PropertyChangeListener> mGenericListeners;
    private Map<String, List<PropertyChangeListener>> mSpecificListerners;
    private PropertyChangeEvent queueEvent;

    // constructor
    public PCSBean() {
        mGenericListeners = new ArrayList<>();
        mSpecificListerners = new HashMap<>();
    }

    private String shortName(Object o) {
        Class<?> c;
        if (o instanceof Class) {
            c = (Class<?>) o;
        } else {
            c = o.getClass();
        }
        String ret = c.getName();
        int off = ret.lastIndexOf(".");
        return ret.substring(off + 1);
    }

    // listeners
    public void addPropertyChangeListener(String prop, PropertyChangeListener pcl) {
        if (pcl == null) {
            log.log(Level.WARNING, "addPropertyChangeListener - WTF???");
        }
        synchronized (mSpecificListerners) {
            List<PropertyChangeListener> listeners = mSpecificListerners.get(prop);
            if (listeners == null) {
                listeners = new ArrayList<>();
                mSpecificListerners.put(prop, listeners);
            }
            listeners.add(pcl);
        }
        if (debug) {
            System.out.println(shortName(pcl) + " hears change on " + shortName(this) + " for " + queueEvent.getPropertyName());
        }
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        if (pcl == null) {
            System.out.println("addPropertyChangeListener - wtf???");
        }
        synchronized (mGenericListeners) {
            mGenericListeners.add(pcl);
        }
        if (debug) {
            System.out.println(shortName(pcl) + " hears change on " + shortName(this) + " for " + queueEvent.getPropertyName());
        }
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        synchronized (mGenericListeners) {
            mGenericListeners.remove(pcl);
        }
        synchronized (mSpecificListerners) {
            for (List<PropertyChangeListener> listeners : mSpecificListerners.values()) {
                listeners.remove(pcl);
            }
        }
        if (debug) {
            System.out.println(shortName(pcl) + " hears change on " + shortName(this) + " for " + queueEvent.getPropertyName());
        }
    }

    protected void queuePropertyChange(String name, Object oldVal, Object newVal) {
        queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
    }

    protected void queuePropertyChange(String name, int oldVal, int newVal) {
        queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
    }

    protected void queuePropertyChange(String name, long oldVal, long newVal) {
        queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
    }

    protected void queuePropertyChange(String name, double oldVal, double newVal) {
        queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
    }

    protected void queuePropertyChange(String name, boolean oldVal, boolean newVal) {
        queueEvent = new PropertyChangeEvent(this, name, oldVal, newVal);
    }

    private PropertyChangeListener[] getPropertyChangeListeners(String prop) {
        Object[] list1;
        synchronized (mGenericListeners) {
            list1 = mGenericListeners.toArray();
        }
        Object[] list2;
        synchronized (mSpecificListerners) {
            List<PropertyChangeListener> listeners = mSpecificListerners.get(prop);
            if (listeners != null) {
                list2 = listeners.toArray();
            } else {
                list2 = new Object[0];
            }
        }
        PropertyChangeListener[] ret = new PropertyChangeListener[list1.length + list2.length];
        System.arraycopy(list1, 0, ret, 0, list1.length);
        System.arraycopy(list2, 0, ret, list1.length, list2.length);
        return ret;
    }

    protected void firePropertyChange() {
        PropertyChangeListener[] pcls = getPropertyChangeListeners(queueEvent.getPropertyName());
        PropertyChangeEvent ev = queueEvent;
        queueEvent = null;
        for (PropertyChangeListener pcl : pcls) {
            if (debug) {
                System.out.println(shortName(pcl) + " hears change on " + shortName(this) + " for " + queueEvent.getPropertyName());
            }
            pcl.propertyChange(ev);
        }
    }

    public void fireMonotonicPropertyChange(String name, Object val) {
        queuePropertyChange(name, null, val);
        firePropertyChange();
    }

    public void fireMonotonicPropertyChange(String name) {
        Object val = BeanUtils.get(this, name);
        fireMonotonicPropertyChange(name, val);
    }
}
